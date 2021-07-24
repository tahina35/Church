import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaymentRequestData } from '../model/PaymentRequestData';
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

  createPR(data: PaymentRequestData) {
    return this.http.post(this.baseUrl + '/api/payment-request/new-form', data);
  }

}
