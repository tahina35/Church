import { Location } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SignaturePad } from 'angular2-signaturepad';
import { ExpenseItem } from 'src/app/model/ExpenseItem';
import { Member } from 'src/app/model/Member';
import { PaymentRequest } from 'src/app/model/PaymentRequest';
import { PaymentRequestData } from 'src/app/model/PaymentRequestData';
import { Receipt } from 'src/app/model/Receipt';
import { Signature } from 'src/app/model/Signature';
import { MemberService } from 'src/app/services/member.service';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-sign-form',
  templateUrl: './sign-form.component.html',
  styleUrls: ['./sign-form.component.scss']
})
export class SignFormComponent implements OnInit {

  pr: PaymentRequest;
  items: Array<ExpenseItem>;
  receipts: Array<Receipt>;
  signatures: Array<Signature>;
  requestorName: string;
  directorSignature: Signature;
  pastorSignature: Signature;
  teamLeaderSignature: Signature;
  directorName: string = "fafafa";
  pastorName: string = "fafaf";
  teamLeaderName: string = "";
  signed: boolean = false;
  formErrors: string[] = [];
  member: Member;
  memberName: string;
  hasTeamLeader: boolean;

  @ViewChild(SignaturePad) signaturePad: SignaturePad;

  signaturePadOptions: Object = { // passed through to szimek/signature_pad constructor
      'minWidth': 1,
      'canvasWidth': 500,
      'canvasHeight': 175
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location, 
    private memberService: MemberService, 
    private prService: PaymentRequestService
    ) { 
   
    }

  ngOnInit(): void {
    this.route.params.subscribe(
      (params) => {
        const prId = +params['id'];
        this.prService.getPrById(prId).subscribe(
          (data: PaymentRequestData) => {
            console.log(data)
            this.pr = data.paymentRequest;
            this.items = data.items;
            this.receipts = data.receipts;
            this.signatures = data.signatures;
            console.log(this.signatures)
            this.requestorName = this.memberService.getName(data.paymentRequest.requestor);
            this.directorSignature = this.signatures.find(signature => signature.id.signerCode == 2);
            this.pastorSignature = this.signatures.find(signature => signature.id.signerCode == 3);
            this.directorName = this.memberService.getName(this.directorSignature.member);
            this.pastorName = this.memberService.getName(this.pastorSignature.member);
            if(this.signatures.length == 4) {
              this.teamLeaderSignature = this.signatures.find(signature => signature.id.signerCode == 4);
              this.teamLeaderName = this.memberService.getName(this.teamLeaderSignature.member);
              this.hasTeamLeader = true;
            }
            
            // logged in member information 
            let loggedMember = JSON.parse(localStorage.getItem("member"));
            this.memberService.findByUsername(loggedMember.username).subscribe(
              (res: Member) => {
                this.member = res;    
                this.memberName = this.memberService.getName(this.member);
                if(this.member.signature) {
                  this.signaturePad.fromDataURL(this.member.signature);
                  this.signed = true;
                }
              }
            )
          }, (error) => {
            this.location.back()
          }
        )
      }
    )
  }

  submit() {
    if(!this.signed) {
      this.formErrors.push("Requestor's signature");
    } else {
      this.member.signature = this.signaturePad.toDataURL();
      let signature : Signature;
      if(this.directorSignature.member.memberId == this.member.memberId) {
        signature = this.directorSignature;
      } else {
        signature = this.pastorSignature;
      }

      signature.hasSigned = true;
      signature.signedDate = new Date();
      signature.member = this.member;
      this.prService.signPR(signature).subscribe(
        (res) => {
          console.log(res);
          this.router.navigate(['/reimbursement/signer']);
        }
      )

    }
  }

  //signature
  drawComplete() {
    // will be notified of szimek/signature_pad's onEnd event
    console.log(this.signaturePad.toDataURL());
    this.signed = true;
  }

  drawStart() {
    // will be notified of szimek/signature_pad's onBegin event
    console.log('begin drawing');
  }

  clear() {
    this.signaturePad.clear();
    this.signed = false;
  }

}
