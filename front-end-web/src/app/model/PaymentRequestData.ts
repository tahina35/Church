import { ExpenseItem } from "./ExpenseItem";
import { Member } from "./Member";
import { PaymentRequest } from "./PaymentRequest";
import { Receipt } from "./Receipt";

export class PaymentRequestData {

    paymentRequest: PaymentRequest
    signers: Array<Member>
    items: Array<ExpenseItem>
    receipts: Array<Receipt>
}