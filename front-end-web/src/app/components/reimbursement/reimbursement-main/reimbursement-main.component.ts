import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-reimbursement-main',
  templateUrl: './reimbursement-main.component.html',
  styleUrls: ['./reimbursement-main.component.scss']
})
export class ReimbursementMainComponent implements OnInit {

  username: string = '';

  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
    let member = JSON.parse(localStorage.getItem('member'));
    this.username = member.fname + ' ' + member.lname;
  }

  logout() {
    this.loginService.logout();
  }

}
