import { checkDetails } from "./CheckDetails"
import { Dept } from "./Dept"
import { Member } from "./Member"

export class PaymentRequest {

    paymentRequestId: number
    creationDate: Date = new Date()
    requestorAddress: string
    requestor: Member
    status: string
    paymentDate: Date
    paidByCreditCard: boolean = false
    paidByCheck: boolean = false
    paidByCash: boolean = false
    department: Dept = new Dept()
    checkDetails: checkDetails
    category: number = 1
    payableTo: string

}