import { Component,OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { OrderService } from 'src/app/services/order.service';
import { ProductService } from 'src/app/services/product.service';
import {AfterViewInit, ViewChild} from '@angular/core';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { DatePipe } from '@angular/common';
// import {MatInputModule} from '@angular/material/input';
// import {MatFormFieldModule} from '@angular/material/form-field';


@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit{
  userId!:number;
  detailOrders:any;
  filteredData:any;
  idFilter:string="";
  priceFilter:string="";
  startDate:any;
  endDate:any;

  constructor(private order:OrderService, private auth:AuthService, private product:ProductService){}

  getAllOrders(){
    this.order.getOrdersByUser(this.userId).subscribe(res => {
      this.detailOrders = res;
      this.detailOrders.forEach((order:any) => {
        order.totalProducts = Object.keys(order.products).length 
      })
      this.dataSource = new MatTableDataSource(this.detailOrders)
      this.dataSource.sort = this.sort
      this.dataSource.paginator = this.paginator
    })
  }
  displayedColumns: string[] = ['id', 'price', 'totalProducts', 'timeStamp'];
  dataSource!: MatTableDataSource<any>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;



  applyFilter(){
    this.dataSource.data=this.detailOrders.filter((o: { id: string , price:string , timeStamp:any }) => {
      if(this.startDate == null || this.endDate == null){
        return ((o?.id?.toString().toLocaleLowerCase().includes(this.idFilter?.toString().toLocaleLowerCase())) && (o.price?.toString().toLocaleLowerCase().includes(this.priceFilter?.toString().toLocaleLowerCase())))
      }
      else{
        return ((o?.id?.toString().toLocaleLowerCase().includes(this.idFilter?.toString().toLocaleLowerCase())) && (o.price?.toString().toLocaleLowerCase().includes(this.priceFilter?.toString().toLocaleLowerCase())) && (o.timeStamp > (new Date(this.startDate).getTime()/1000 )) && (o.timeStamp < (new Date(this.endDate).getTime()/1000 )) )
      }
    })
  }
  

  
  ngOnInit(): void {
    this.userId = this.auth.isLoggedIn().Id;
    this.getAllOrders();
  }
}
