import { Component, OnInit } from '@angular/core';
import { Member } from 'src/app/model/Member';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  loggedMember;
  nbOfPRWaitingForSignature: number

  constructor(private paymentRequestService: PaymentRequestService) { }

  ngOnInit(): void {
    console.log(this.loggedMember);
    this.loggedMember = JSON.parse(localStorage.getItem("member"));
    this.getNbOfPRWaitingForSignature(this.loggedMember.id);
  }

  getNbOfPRWaitingForSignature(memberId: number) {
    this.paymentRequestService.getNbOfPRWaitingForSignature(memberId).subscribe(
      (res: number) => {
        this.nbOfPRWaitingForSignature = res;
        console.log(this.nbOfPRWaitingForSignature);
      }
    )
  }

}
