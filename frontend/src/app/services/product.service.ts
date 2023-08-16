import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, retry} from "rxjs"


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http:HttpClient) { }

  url:String = "http://localhost:8080/api/products";

  private productItemSubject : BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  
  setProductRefreshStatus(status:boolean):BehaviorSubject<boolean>{
    this.productItemSubject.next(status)
    return this.productItemSubject;
  }
  getProductRefreshStatus():BehaviorSubject<boolean>{
    return this.productItemSubject;
  }

  private filterProductsSubject : BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);

  setFilterProducts(products:any[]):BehaviorSubject<any[]>{
    this.filterProductsSubject.next(products)
    return this.filterProductsSubject;
  }

  getFilterProducts():BehaviorSubject<any[]>{
    return this.filterProductsSubject;
  }

  getAllProducts():Observable<any>{
    // const token = localStorage.getItem("token");
    // const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.url}`);
  }

  getProductById(id:Number):Observable<any>{
    return this.http.get(`${this.url}/${id}`)
  }

  addProduct(data:any):Observable<any>{
    return this.http.post(`${this.url}`,data);
  }

  likeProduct(id:number):Observable<any>{
    return this.http.get(`${this.url}/${id}/like`)
  }

  updateProduct(data:any):Observable<any>{
    return this.http.put(`${this.url}`,data)
  }

  getFilteredProducts(merchantIds?:any[],categoryIds?:any[],maxPrice?:any,minPrice?:any):Observable<any>{
    var separatedMerchantIds = merchantIds?.join(",");
    var separatedCategoryIds = categoryIds?.join(",");
    return this.http.get(`${this.url}/filter?categoryIds=${separatedCategoryIds}&merchantIds=${separatedMerchantIds}&minPrice=${minPrice}&maxPrice=${maxPrice}`)
  }

  getFilteredProductsBySingleParam(data:any):Observable<any>{
    var encodedData = encodeURIComponent(JSON.stringify(data));
    return this.http.get(`${this.url}/query?param=${encodedData}`)
  }

  
  
}
