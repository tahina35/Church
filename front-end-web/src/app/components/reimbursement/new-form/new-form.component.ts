import { ViewportScroller } from '@angular/common';
import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SignaturePad } from 'angular2-signaturepad';
import { Select2OptionData } from 'ng-select2';
import { BudgetCode } from 'src/app/model/BudgetCode';
import { Dept } from 'src/app/model/Dept';
import { ExpenseItem } from 'src/app/model/ExpenseItem';
import { Member } from 'src/app/model/Member';
import { PaymentRequest } from 'src/app/model/PaymentRequest';
import { PaymentRequestData } from 'src/app/model/PaymentRequestData';
import { Receipt } from 'src/app/model/Receipt';
import { Signature } from 'src/app/model/Signature';
import { DepartmentService } from 'src/app/services/department.service';
import { LoginService } from 'src/app/services/login.service';
import { MemberService } from 'src/app/services/member.service';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-new-form',
  templateUrl: './new-form.component.html',
  styleUrls: ['./new-form.component.scss']
})
export class NewFormComponent implements OnInit {

  date: NgbDateStruct;
  member: Member;
  teamLeader: Member;
  teamLeaderName: string;
  requestorName: string;
  requestorAddress: string;
  items:ExpenseItem[]= [];
  expenseItem: ExpenseItem = new ExpenseItem();
  deptLeader: Member;
  allDepts: Dept[];
  deptLeaderName: string;
  pastors:Array<Select2OptionData>;
  selectedPastor: string;
  selectedBudgetCode: BudgetCode;
  budgetCodes: BudgetCode[];
  purchaseDate: NgbDateStruct;
  signatureImg: string;
  formErrors: string[] = [];
  signed: boolean = false;
  paymentRequest: PaymentRequest = new PaymentRequest();
  receipts: Array<Receipt>;
  submitting: boolean = false;

  @ViewChild(SignaturePad) signaturePad: SignaturePad;

  signaturePadOptions: Object = { // passed through to szimek/signature_pad constructor
    'canvasWidth': 500,
    'canvasHeight': 175,
    'minWidth': 0.5
  };    

  //file upload
  shortLink: string = "";
  loading: boolean = false; // Flag variable
  files: FormData = new FormData();// Variable to store file

  pageYoffset;

  @HostListener('window:scroll', ['$event']) onScroll(event){
    this.pageYoffset = window.pageYOffset;
  }
  

  constructor(private loginService: LoginService, 
    private memberService: MemberService, 
    private modalService: NgbModal,
    private paymentRequestService: PaymentRequestService,
    private deptService: DepartmentService,
    private scroll: ViewportScroller,
    private router: Router) {
  }

  ngOnInit(): void {
    
    this.getDepts();
    this.getPastors();
    this.getBudgetCodes();
    let loggedMember = JSON.parse(localStorage.getItem("member"));
    this.memberService.findByUsername(loggedMember.username).subscribe(
      (res: Member) => {
        this.member = res;
        this.requestorName = this.memberService.getName(this.member);
        this.requestorAddress = this.memberService.getAddress(this.member);
        this.paymentRequest.payableTo = this.requestorName;
        if(this.member.signature) {
          this.signaturePad.fromDataURL(this.member.signature);
          this.signed = true;
        }
      }
    )
  }

  ngAfterViewInit() {
    // this.signaturePad is now available
    console.log(this.signaturePad)
    this.signaturePad.set('minWidth', 5); // set szimek/signature_pad options at runtime
    this.signaturePad.clear(); // invoke functions from szimek/signature_pad API
  }

  logout() {
    this.loginService.logout();
  }

  open(content) {
    this.modalService.open(content).result.then(
      () => {
        
      },
      (reason) => {
       this.resetModal();
      }
    )
  }

  resetModal() {
    this.expenseItem = new ExpenseItem();
    this.selectedBudgetCode = null;
  }

  getDepts() {
    this.deptService.findAll().subscribe(
      (depts: Dept[]) => {
        this.allDepts = depts;
      }
    )
  }

  getPastors() {
    this.memberService.getPastors().subscribe(
      (pastors: Member[]) => {
        this.selectedPastor = pastors[0].memberId.toString();
        this.pastors = pastors.map(
          (pastor: Member) => {
            return ({id: pastor.memberId.toString(), text: this.memberService.getName(pastor)});
          }
        );
        
        console.log(this.selectedPastor);
      }
    )
  }

  deptChange($event) {
    console.log(this.paymentRequest);
    if(this.paymentRequest.department.teamLeader) {
      this.deptLeader = this.paymentRequest.department.parentDept.leader;
      this.deptLeaderName = this.memberService.getName(this.deptLeader);
      this.teamLeader = this.paymentRequest.department.teamLeader;
      this.teamLeaderName = this.memberService.getName(this.teamLeader);
    } else {  
      this.teamLeader = null;
      this.teamLeaderName = "";
      this.deptLeader = this.paymentRequest.department.leader;
      this.deptLeaderName = this.memberService.getName(this.deptLeader);
    }
   
    
  }

  addItem() {
    this.expenseItem.budgetCode = this.selectedBudgetCode;
    console.log(this.purchaseDate)
    const date = this.purchaseDate.year + '-' + this.purchaseDate.month + '-' + this.purchaseDate.day;
    this.expenseItem.purchaseDate = new Date(date);
    this.items.push(this.expenseItem);
    this.modalService.dismissAll();
    console.log(this.items);
  }

  getBudgetCodes() {
    this.paymentRequestService.getBudgetCode().subscribe(
      (codes: BudgetCode[]) => {
        this.budgetCodes = codes;
      }
    )
  }

  removeItem(item) {
    const index = this.items.indexOf(item);
    if (index > -1) {
      this.items.splice(index, 1);
    }
  }

  submit() {
    this.formErrors = [];
    if(this.validateForm()) {
      this.submitting = true;
      this.member.signature = this.signaturePad.toDataURL();
      let date: string = this.date.year + '-' + this.date.month + '-' + this.date.day;
      let director: Member = this.deptLeader;
      let pastor: Member = new Member();
      pastor.memberId = +this.selectedPastor;
      this.paymentRequest.creationDate = new Date(date);
      this.paymentRequest.requestor = this.member;
      this.paymentRequest.requestorAddress = this.requestorAddress;

      console.log(this.paymentRequest)

      let prdata : PaymentRequestData = new PaymentRequestData();
      prdata.paymentRequest = this.paymentRequest;
      prdata.items = this.items;
      prdata.signatures = this.createSignatures(director, pastor, this.teamLeader);
      prdata.receipts = this.receipts;

      console.log(prdata.signatures);

      this.paymentRequestService.uploadDocuments(this.files).subscribe(
        (res) => {
          console.log(res);
          this.paymentRequestService.createPR(prdata).subscribe(
            (res) => {
              this.router.navigate(['/reimbursement']);
            }
          )
        }
      )  
      
    } else {
      this.scroll.scrollToPosition([0,0]);
    }
  }

  createSignatures(director: Member, pastor: Member, teamLeader: Member) {
    let directorSignature = new Signature();
    let pastorSignature = new Signature();
    let teamLeaderSignature = new Signature();
    directorSignature.member = director;
    pastorSignature.member = pastor;
    teamLeaderSignature.member = teamLeader
    return [directorSignature, pastorSignature, teamLeaderSignature];
  }

  validateForm() {
    let valid : boolean = true;
    if(!this.date) {
      this.formErrors.push("Date");
      valid = false;
    } if(!this.paymentRequest.department.deptId) {
      this.formErrors.push("Ministry Team");
      valid = false;
    } if(!this.selectedPastor) {
      this.formErrors.push("Pastor of Department");
      valid = false;
    } if(this.items.length == 0) {
      this.formErrors.push("Expense Items");
      valid = false;
    } if(!this.files.has('files')) {
      this.formErrors.push("Receipts and Documents");
      valid = false;
    } if(!this.paymentRequest.payableTo) {
      this.formErrors.push("Payble to");
      valid = false;
    } if(!this.requestorAddress) {
      this.formErrors.push("Requestor's address");
      valid = false;
    } if(!this.signed) {
      this.formErrors.push("Requestor's signature");
      valid = false;
    }
    console.log(this.formErrors);
    return valid;
  }



  //signature
  drawComplete() {
    // will be notified of szimek/signature_pad's onEnd event
    console.log(this.signaturePad.toDataURL());
    this.signatureImg = this.signaturePad.toDataURL();
    this.signed = true;
  }

  drawStart() {
    // will be notified of szimek/signature_pad's onBegin event
    console.log('begin drawing');
  }

  clear() {
    this.signaturePad.clear();
    this.signatureImg  = "";
    this.signed = false;
  }

  // On file Select
  onChange(event) {
    this.files.delete('files');
    this.receipts = [];
    for(var i = 0; i<event.target.files.length; i++) {
      this.files.append('files', event.target.files[i]);
      let receipt = new Receipt();
      receipt.name = event.target.files[i].name;
      this.receipts.push(receipt);
    }
  }


}
