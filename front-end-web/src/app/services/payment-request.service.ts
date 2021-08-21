import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaymentRequestData } from '../model/PaymentRequestData';
import { Signature } from '../model/Signature';
import { BaseService } from './base.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentRequestService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getBudgetCode() {
    return this.http.get(this.baseUrl + '/api/payment-request/budget-code');
  }

  createPR(data) {
    return this.http.post(this.baseUrl + '/api/payment-request/new-form', data);
  }

  getNbOfPRWaitingForSignature(memberId: number) {
    return this.http.get(this.baseUrl + '/api/payment-request/number-of-pr-waiting-for-signature/member/' + memberId);
  }

  getNbOfPRWaitingForPayment() {
    return this.http.get(this.baseUrl + '/api/payment-request/number-of-pr-waiting-for-payment');
  }

  getPRWaitingForSignature(memberId: number) {
    return this.http.get(this.baseUrl + '/api/payment-request/pr-waiting-for-signature/member/' + memberId);
  }

  getPRWaitingForPayment() {
    return this.http.get(this.baseUrl + '/api/payment-request/pr-waiting-for-payment');
  }

  uploadDocuments(files) {
    return this.http.post(this.baseUrl + '/api/payment-request/upload-documents', files); 
  }

  getPrById(prId: number) {
    return this.http.get(this.baseUrl + '/api/payment-request/pr-' + prId);
  }

  signPR(signature: Signature) {
    return this.http.post(this.baseUrl + '/api/payment-request/sign', signature); 
  }

}
