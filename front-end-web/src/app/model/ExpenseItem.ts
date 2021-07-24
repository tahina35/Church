import { BudgetCode } from "./BudgetCode"

export class ExpenseItem {

    expenseItemID:number
    itemName:string
    detailedPurpose:string
    amount:number
    remarks:string
    purchaseDate:Date
    budgetCode:BudgetCode = new BudgetCode()

}