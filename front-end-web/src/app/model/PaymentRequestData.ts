import { ExpenseItem } from "./ExpenseItem";
import { Member } from "./Member";
import { PaymentRequest } from "./PaymentRequest";
import { Receipt } from "./Receipt";
import { Signature } from "./Signature";

export class PaymentRequestData {

    paymentRequest: PaymentRequest
    signatures: Array<Signature>
    items: Array<ExpenseItem>
    receipts: Array<Receipt>
}