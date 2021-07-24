import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Member } from '../model/Member';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private cookieService: CookieService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (!this.cookieService.check('access-token')) {
            this.router.navigate(['/login']);
            return false;
        } 

        let member = JSON.parse(localStorage.getItem('member'));
        if (!member.admin) {
            this.router.navigate(['/reimbursement']);
            return false;
        }
        
        return true;
    }
}