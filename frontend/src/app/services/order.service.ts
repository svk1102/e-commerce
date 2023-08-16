import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private http:HttpClient) { }

  url = "http://localhost:8080/api";

  private orderFilterSubject : BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);

  setFilteredOrders(orders:any[]):BehaviorSubject<any[]>{
    this.orderFilterSubject.next(orders)
    return this.orderFilterSubject;
  }
  getFiteredOrders():BehaviorSubject<any[]>{
    return this.orderFilterSubject;
  }

  getOrdersByUser(id:number):Observable<any>{
    return this.http.get(`${this.url}/users/${id}/orders`);
  }
  
  placeOrder(data:any):Observable<any>{
    return this.http.post(`${this.url}/orders`,data);
  }

  getOrderById(id:number):Observable<any>{
    return this.http.get(`${this.url}/orders/${id}`)
  }

  getAllOrders():Observable<any>{
    return this.http.get(`${this.url}/orders`)
  }

  getOrdersByFilter(data:any):Observable<any>{
    var encodedData = encodeURIComponent(JSON.stringify(data));
    return this.http.get(`${this.url}/orders/filter?param=${encodedData}`)
  }
}
