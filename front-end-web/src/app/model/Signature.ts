import { SignatureKey } from "./Key/SignatureKey";
import { Member } from "./Member";
import { PaymentRequest } from "./PaymentRequest";

export class Signature {

    paymentRequest: PaymentRequest = new PaymentRequest();
    member:Member = new Member();
    hasSigned: boolean
    signedDate: Date
    id: SignatureKey

}