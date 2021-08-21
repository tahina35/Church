import { Component, OnInit } from '@angular/core';
import { UsernameAndPassword } from '../../../model/UsernameAndPassword';
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login.service';
import { HttpResponse } from '@angular/common/http';
import { MemberService } from 'src/app/services/member.service';
import { Member } from 'src/app/model/Member';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  credentials: UsernameAndPassword = {} as UsernameAndPassword;
  error?: string;
  success?: string;

  constructor(private router: Router, private loginservice: LoginService, private memberService: MemberService) { 
    let message = this.router.getCurrentNavigation().extras.state;
    if(message) {
      if(message.status === "success") {
        this.success = message.message;
      } else {
        this.error = message.message;
      }  
    }
  }

  ngOnInit(): void {

  }

  onSubmit() {
    this.resetErrors();
    this.loginservice.login(this.credentials).subscribe(
      (res: HttpResponse<any>) => {
        let token = res.headers.get('Authorization');
        this.loginservice.storeToken(token);
        this.memberService.findByUsername(this.credentials.username).subscribe(
          (member: Member) => {
            console.log(member);
            let memberDetails = {
              id: member.memberId,
              username: member.homeemail,
              fname: member.kfname,
              lname: member.klname,
              efname: member.efname,
              elname: member.elname,
              admin: member.admin
            }
            localStorage.setItem('member', JSON.stringify(memberDetails));
            if(!member.admin) {
              this.router.navigate(['/reimbursement']);
            } else {
              this.router.navigate(['/']);
            }
          },
          (err) => {
            this.error = err;
          }
        )
      },
      (err) => {
        this.error = err;
      }
    );
  }

  resetErrors() {
    this.error = "";
    this.success = "";
  }

}
