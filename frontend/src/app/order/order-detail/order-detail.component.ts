import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from 'src/app/services/order.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.css']
})
export class OrderDetailComponent implements OnInit{

  orderId!:number;
  orderDetails:any;
  productDetails:any;

  constructor(private route:ActivatedRoute , private order:OrderService, private product:ProductService){}

  getOrderDetails(){
    this.productDetails = [];
    this.order.getOrderById(this.orderId).subscribe(res => {
      console.log(res)
      this.orderDetails = res;
      Object.keys(this.orderDetails.products).forEach(pid => {
        this.getProductDetails(pid,this.orderDetails.products[pid]);
      })
    })
  }

  getProductDetails(pid:any,quantity:any){
    this.product.getProductById(pid).subscribe(res => {
      console.log(res)
      res.quantity = quantity;
      this.productDetails.push(res)
      console.log(this.productDetails)
    })
  }


    ngOnInit(): void {
      this.orderId = Number(this.route.snapshot.paramMap.get("id"))
      this.getOrderDetails();

    }
}
