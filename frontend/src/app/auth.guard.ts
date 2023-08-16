import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './services/auth.service';

// export const authGuard: CanActivateFn = (route, state) => {
//   let guard = new AuthService(new HttpClient())  



//   return true;
// };
@Injectable()
export class AuthGuard implements CanActivate{
  constructor(private auth:AuthService , private router:Router) {}
  id:boolean = this.auth.isLoggedIn()
  
  canActivate():boolean{

    if(!this.id){
      this.router.navigate(['/auth/login'])
      return this.auth.isLoggedIn()
    }else{
      return this.auth.isLoggedIn()
    }
  } 

}
