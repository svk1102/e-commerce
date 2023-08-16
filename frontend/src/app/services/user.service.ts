import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  userId!:Number;

  constructor(private http:HttpClient , private auth:AuthService) { }

  url = "http://localhost:8080/api/users"

  getSingleUser(id:Number):Observable<any>{
     this.userId= this.auth.isLoggedIn().Id 
    return this.http.get(`${this.url}/${this.userId}`)
  }

  getAllUsers():Observable<any>{
    return this.http.get(this.url);
  }

  getUserById(id:Number):Observable<any>{
    // this.userId= this.auth.isLoggedIn().Id 
   return this.http.get(`${this.url}/${id}`)
 }

 getAllMerchants():Observable<any>{
  return this.http.get(`${this.url}/merchants`)
 }

 getProductsByMerchant(id:number):Observable<any>{
  return this.http.get(`${this.url}/${id}/products`)
}


}
