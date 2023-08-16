import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import { UserService } from './user.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http:HttpClient , private user:UserService , private auth:AuthService) { }

  url="http://localhost:8080/api/carts"
  

  private cartItemsSubject : BehaviorSubject<number> = new BehaviorSubject<number>(0);

  getCartItems():BehaviorSubject<number>{
    this.user.getSingleUser(this.auth.isLoggedIn().Id).subscribe((res) => {
      // console.log(Object.keys(res.cart.cartItems).length)
      this.cartItemsSubject.next(Object.keys(res.cart.cartItems).length);
    })
    return this.cartItemsSubject;
  }

  addToCart(data:any){
    return this.http.post(this.url,data).subscribe((res) => {
      // console.log(res);
      this.getCartItems();
    })
  }

  removeFromCart(id:number){
    return this.http.delete(`${this.url}/${id}`).subscribe(res => {
      // console.log(res);
      this.getCartItems();
    })
  }

  getCartData(id:number){
    return this.http.get(`${this.url}/${id}`)
  }
  

}
