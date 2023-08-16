import { Component,OnInit, ViewChild } from '@angular/core';
import { CategoryService } from '../services/category.service';
import { OrderService } from '../services/order.service';
import { UserService } from '../services/user.service';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { ProductService } from '../services/product.service';
import { MatAccordion } from '@angular/material/expansion';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),]

})
export class AdminComponent implements OnInit{

  ELEMENT_DATA:any =[];
  ELEMENT_DATA2:any=[];
  ELEMENT_DATA3:any=[];

  displayedColumns1: string[] = ['id', 'name','image'];
  displayedColumns2: string[] = ['id','price','quantity','timeStamp','userId']
  displayedColumns3: string[] = ['id','firstName','lastName','role']
  columnsToDisplayWithExpand = [...this.displayedColumns2, 'expand'];
  expandedElement: any | null;

  dataSource1 = [...this.ELEMENT_DATA];
  dataSource2 = this.ELEMENT_DATA2
  dataSource3 = this.ELEMENT_DATA3
  value:any;
  role:any;
  url:any;
  orders:any;


  constructor(private category:CategoryService , private order:OrderService , private user:UserService , private product:ProductService){}

  @ViewChild(MatTable) table!: MatTable<any>;
  @ViewChild(MatAccordion) accordion!: MatAccordion;


  getCategories(){
    this.category.getCategory().subscribe(res => {
      console.log(res)
      this.ELEMENT_DATA = res
      console.log(this.ELEMENT_DATA)
      this.dataSource1=[...this.ELEMENT_DATA]
    })
  }

  getAllOrders(){
    this.order.getAllOrders().subscribe(res => {
      this.orders = res;
      this.orders.forEach((o: { quantity: number; products: any; productDetails:any}) => {
        o.quantity = Object.keys(o.products).length;
        var products:any = [];
        Object.keys(o.products).forEach((pId:any) => {
          this.product.getProductById(Number(pId)).subscribe((res:any) => {
            res.quantity = o.products[pId];
            products.push(res);
          })
        })
        o.productDetails = products;
      })
      console.log(this.orders);
      this.ELEMENT_DATA2 = this.orders;
      this.dataSource2 = this.ELEMENT_DATA2;
      this.order.setFilteredOrders(this.orders);
    })
  }

  getFilteredOrders(){
    this.order.getFiteredOrders().subscribe(res => {
      this.orders = res;
      this.orders.forEach((o: { quantity: number; products: any; productDetails:any}) => {
        o.quantity = Object.keys(o.products).length;
        var products:any = [];
        Object.keys(o.products).forEach((pId:any) => {
          this.product.getProductById(Number(pId)).subscribe((res:any) => {
            res.quantity = o.products[pId];
            products.push(res);
          })
        })
        o.productDetails = products;
      })
      console.log(this.orders);
      this.ELEMENT_DATA2 = this.orders;
      this.dataSource2 = this.ELEMENT_DATA2;
    })
  }
  
  getCurrentUser(){

    this.user.getSingleUser(0).subscribe(res => {
      console.log(res)
      this.role = res.role;
    })
  }

  getAllUsers(){
    this.user.getAllUsers().subscribe(res => {
      console.log(res)
      this.ELEMENT_DATA3 = res
      this.dataSource3 = res
    })
  }

  addData(name:string , imageUrl:string){
   this.category.addCategory({"name":name , "imageUrl":imageUrl}).subscribe(res => {
    this.ELEMENT_DATA.push(res)
    this.value='';
    this.url=''
    this.dataSource1=[...this.ELEMENT_DATA]
   }) 
  }




  ngOnInit(): void {
    this.getCategories();
    this.getFilteredOrders();
    this.getAllOrders();
    this.getAllUsers();
    this.getCurrentUser();
  }
}
