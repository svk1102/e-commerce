import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http:HttpClient) { }

  url = "http://localhost:8080/api/category"

  getCategory():Observable<any>{
    return this.http.get(this.url);
  }

  addCategory(data:any):Observable<any>{
    return this.http.post(this.url,data);
  }

  getCategoryByName(name:string):Observable<any>{
    return this.http.get(`${this.url}/${name}/products`)
  }
}
