import { Member } from "./Member"

export class Dept {

    deptId:number
    name:string
    creationDate:Date
    deptCode:number
    parentDept:Dept
    leader:Member = new Member()
    parent:boolean = true;

}