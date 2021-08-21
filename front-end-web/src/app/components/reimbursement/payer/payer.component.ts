import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExpenseItem } from 'src/app/model/ExpenseItem';
import { PaymentRequestData } from 'src/app/model/PaymentRequestData';
import { MemberService } from 'src/app/services/member.service';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-payer',
  templateUrl: './payer.component.html',
  styleUrls: ['./payer.component.scss']
})
export class PayerComponent implements OnInit {

  loggedMember;
  paymentRequestData: PaymentRequestData[];
  prList = [];

  constructor(
    private paymentRequestService: PaymentRequestService,
    private router: Router,
    private memberService: MemberService) { }

  ngOnInit(): void {
    this.loggedMember = JSON.parse(localStorage.getItem("member"));
    this.getPRs();
  }

  getPRs() {
    this.paymentRequestService.getPRWaitingForPayment().subscribe(
      (prData: PaymentRequestData[]) => {
        this.paymentRequestData = prData;
        for (let i=0; i < prData.length; i++) {
          const data = {
            prId: prData[i].paymentRequest.paymentRequestId,
            requestor: this.memberService.getName(prData[i].paymentRequest.requestor),
            dateCreated: prData[i].paymentRequest.creationDate,
            amount: this.getPRAmount(prData[i].items),
            status: prData[i].paymentRequest.status
          };
          this.prList.push(data);
        }
      }
    )
  }

  getPRAmount(items: Array<ExpenseItem>) {
    let total: number = 0;
    for (let i=0; i < items.length; i++) {
      total+= items[i].amount;
    }
    return total;
  }

  navigateToFormSignaturePage(prID: number) {
    this.router.navigate(['/reimbursement/pay-form/' + prID]);
  }

}
