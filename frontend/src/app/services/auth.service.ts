import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, timestamp} from "rxjs"
import jwtDecode from "jwt-decode";



@Injectable({
  providedIn: 'root'
})
export class AuthService {

  token:any;
  decoded:any;

  constructor(private http:HttpClient) { }
  
  private loginCheckSubject : BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  setLoginCheck(status:boolean):BehaviorSubject<boolean>{
    this.loginCheckSubject.next(status)
    return this.loginCheckSubject;
  }
  getLoginCheck():BehaviorSubject<boolean>{
    return this.loginCheckSubject;
  }

  url:String = "http://localhost:8080/auth";

  signUpUser(data:any):Observable<any>{
    return this.http.post(`${this.url}/registerUser`,data);
  }

  signUpMerchant(data:any):Observable<any>{
    return this.http.post(`${this.url}/registerMerchant`,data);
  }
  
  loginUser(data:any):Observable<any>{
    return this.http.post(`${this.url}/login`,data);
  }

  isLoggedIn(){
      this.token = sessionStorage.getItem("token");
      if(this.token !== null){
        this.decoded = jwtDecode(this.token);
        if(this.decoded.exp > Math.floor(Date.now()/1000 )){
          this.setLoginCheck(true); 
          return (this.decoded);
        }else{
          return (false);
        }
      }else{
        return (false);
      }

  }
}
