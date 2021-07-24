import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  name: string;

  constructor(private loginService: LoginService) { 
    let member = JSON.parse(localStorage.getItem('member'));
    this.name = member.kfname + ' ' + member.klname;
  }

  ngOnInit(): void {
    
  }

  logout() {
    this.loginService.logout();
  }

}
