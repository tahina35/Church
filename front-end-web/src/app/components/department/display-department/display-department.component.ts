import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Dept } from 'src/app/model/Dept';
import { Page } from 'src/app/model/Page';
import { DepartmentService } from 'src/app/services/department.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteDeptModal } from './delete-department.modal';
import { Member } from 'src/app/model/Member';
import { MemberService } from 'src/app/services/member.service';
import { Select2OptionData } from 'ng-select2';
import { Role } from 'src/app/model/Role';
import { Position } from 'src/app/model/Position';

@Component({
  selector: 'app-display-department',
  templateUrl: './display-department.component.html',
  styleUrls: ['./display-department.component.scss']
})
export class DisplayDepartmentComponent implements OnInit {

  isParentDept: boolean = true;
  totalPages:number;
  departments: Dept[];
  parentDepts: Dept[];
  error?: string;
  currentPage: number = 1;
  searchValue:string = '';
  dept: Dept = new Dept();
  totalItems:number = 0;
  selectedDept:number = 0;
  departmentMembers:Array<Select2OptionData> = []; 

  constructor(private deptService: DepartmentService, private memberService: MemberService, private route: ActivatedRoute, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.findAllDepts();
    this.findParentDepts();
  }

  findAllDepts() {
    this.deptService.findPaginated(this.currentPage).subscribe(
      (data: Page<Dept>) => {
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.departments = data.content;
        console.log(data)
      },
      (err) => {
        this.error = err;
      }
    )
  }

  findParentDepts() {
    this.deptService.findParentDepts().subscribe(
      (data: Dept[]) => {
        this.parentDepts = data;
      },
      (err) => {
        this.error = err;
      }
    )  
  }

  findAllDeptsPaginated() {
    this.deptService.findPaginated(this.currentPage).subscribe(
      (data: Page<Dept>) => {
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.departments = data.content;
      },
      (err) => {
        this.error = err;
      }
    )
  }

  onSubmit() {
    this.currentPage = 1;
    this.search();
  }

  search() {
    this.deptService.search(this.searchValue, this.currentPage).subscribe(
      (data: Page<Dept>) => {
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.departments = data.content;
      },
      (err) => {
        this.error = err;
      }
    )
  }

  onPageChange() {
    if(this.searchValue === '') {
      this.findAllDeptsPaginated();
    } else {
      this.search();
    }
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {},
      (reason) => {
        this.resetModal();
      }
    )
  }

  resetModal() {
    this.selectedDept = 0;
    this.dept = new Dept();
    this.isParentDept = true;
  }

  addDept() {
    if(this.selectedDept) {
      this.dept.parentDept = { deptId: this.selectedDept} as Dept;
    } else {
      this.dept.parentDept = null;
    }

    if(!this.dept.creationDate) {
      this.dept.creationDate = new Date();
    }

    if(!this.dept.leader.memberId) {
      this.dept.leader = null;
    }

    this.dept.parent = this.isParentDept;

    this.deptService.add(this.dept).subscribe(
      (data) => {   
        this.currentPage = 1;
        this.findAllDepts();
        this.resetModal
        this.modalService.dismissAll();
      },
      (err) => {
        this.error = err;
      }
    )
  }

  updateDepartment(content, department: Dept) {
    this.dept.leader = new Member();
    if(department.parentDept) {
      this.selectedDept = department.parentDept.deptId;
    } else {
      this.selectedDept = 0;
    }

    this.isParentDept = department.parent;
    this.dept.deptId = department.deptId;
    this.dept.creationDate = department.creationDate;
    this.dept.name = department.name;
    this.dept.deptCode = department.deptCode;
    if(department.leader) {
      this.dept.leader.memberId = department.leader.memberId;
    }
  
    this.memberService.getByDepartment(department.deptId).subscribe(
      (data: Member[]) => {
        const members = data.map(
          (member: Member) => {
            return ({id: member.memberId.toString(), text: member.kfname + ' ' + member.klname});
          }
        );

        this.departmentMembers = members;

        this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
          () => {},
          (reason) => {
            this.resetModal();
          }
        )

      },
      (err) => {
        this.error = err;
      }
    )

  }

  deleteDepartment(dept: Dept) {
    const modalRef  = this.modalService.open(DeleteDeptModal, {ariaLabelledBy: 'modal-basic-title'});
    modalRef.componentInstance.dept = dept;
    modalRef.result.then(
      () => {
        this.deptService.delete(dept.deptId).subscribe(
          () => {
            this.currentPage = 1;
            this.findAllDepts();
          },
          (err) => {
            this.error = err;
            setTimeout(()=>{                           //<<<---using ()=> syntax
              this.error = '';
            }, 8000);
          }
        )
      }
    )
  }

}
