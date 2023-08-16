import { Component,OnInit } from '@angular/core';
import { ActivatedRoute, Router , NavigationEnd} from '@angular/router';
import { ProductService } from '../services/product.service';
import { UserService } from '../services/user.service';
import { filter } from 'rxjs/operators';


@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit{

  productId!:Number;
  productDetail:any;

constructor(private route:ActivatedRoute , private product:ProductService , private user:UserService , private router:Router){}

  getProductDetail(id:Number){
    this.product.getProductById(id).subscribe((res) => {
      this.productDetail = res;
      console.log(res);
      console.log(this.productDetail.userId)
      this.user.getUserById(this.productDetail.userId).subscribe(res2 => {
        console.log(res2)
        this.productDetail.merchantName = res2.firstName + " " + res2.lastName;
      })
    })
  }

  
  ngOnInit(): void {
    // console.log(this.route.snapshot.paramMap.get("id"));
    this.productId = Number(this.route.snapshot.paramMap.get("id"));  
    this.getProductDetail(this.productId);
    console.log("initalized")

    this.router.events.pipe(
      filter((event) => event instanceof NavigationEnd)
    ).subscribe((event) => {
      // Handle URI change here
      this.getProductDetail(Number(this.route.snapshot.paramMap.get("id")));
    });
  
  }
  
}
