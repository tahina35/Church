import { Component, OnInit } from '@angular/core';
import { Member } from 'src/app/model/Member';
import { LoginService } from 'src/app/services/login.service';
import { MemberService } from 'src/app/services/member.service';

@Component({
  selector: 'app-reimbursement-main',
  templateUrl: './reimbursement-main.component.html',
  styleUrls: ['./reimbursement-main.component.scss']
})
export class ReimbursementMainComponent implements OnInit {

  username: string = '';

  constructor(
    private loginService: LoginService,
    private memberService: MemberService
    ) { }

  ngOnInit(): void {
    let member = JSON.parse(localStorage.getItem('member'));
    this.memberService.findByUsername(member.username).subscribe(
      (res: Member) => {
        this.username = this.memberService.getName(res);
      }
    )
    
  }

  logout() {
    this.loginService.logout();
  }

}
