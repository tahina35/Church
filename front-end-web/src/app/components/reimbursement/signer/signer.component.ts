import { Component, OnInit } from '@angular/core';
import { ExpenseItem } from 'src/app/model/ExpenseItem';
import { PaymentRequest } from 'src/app/model/PaymentRequest';
import { PaymentRequestData } from 'src/app/model/PaymentRequestData';
import { MemberService } from 'src/app/services/member.service';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-signer',
  templateUrl: './signer.component.html',
  styleUrls: ['./signer.component.scss']
})
export class SignerComponent implements OnInit {

  loggedMember;
  paymentRequestsData: PaymentRequestData[];
  prList = [];

  constructor(
    private paymentRequestService: PaymentRequestService,
    private memberService: MemberService) { }

  ngOnInit(): void {
    this.loggedMember = JSON.parse(localStorage.getItem("member"));
    this.getPRs(this.loggedMember.id);
  }

  getPRs(memberId: number) {
    this.paymentRequestService.getPRWaitingForSignature(memberId).subscribe(
      (prData: PaymentRequestData[]) => {
        this.paymentRequestsData = prData;
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

}
