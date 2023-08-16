import { Component,OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { ProductFormComponent } from 'src/app/product-form/product-form.component';
import { AuthService } from 'src/app/services/auth.service';
import { CartService } from 'src/app/services/cart.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit{
  merchantId!:number;
  
  filteredProducts:any;
  productData:any;
  upperValue:number=100000
  lowerValue:number=0
  userId!:number;
  currentUser:any;
  role:any;


  constructor(private route:ActivatedRoute , private products:ProductService , private auth:AuthService , private cart:CartService , private user:UserService ,private categoryS:CategoryService ,private _dialog:MatDialog){}


  formatLabel(value: number): string {
    if (value >= 1000) {
      return value / 1000 + 'k';
    }

    return `${value}`;
  }

  getAllProducts(){
    this.user.getProductsByMerchant(this.merchantId).subscribe(res => {
           this.productData=res;
      this.filteredProducts=res;
      console.log(res)
    })
  }

  productFilter():void{
    this.filteredProducts=this.productData.filter((p: { name: string , price:number , category:string}) => {
      // return (p.category.toLocaleLowerCase().includes(this.categoryFilter.toLocaleLowerCase()) && p.name.toLocaleLowerCase().includes(this.inputFilter.toLocaleLowerCase()) && (p.price >= this.lowerValue) && (p.price <= this.upperValue || p.price >this.upperValue))
      return (p.price >= this.lowerValue) && (p.price <= this.upperValue || p.price >this.upperValue)
    })
  }

  addToCart(productId:number){
    this.cart.addToCart({"userId":this.userId , "productId" : productId})
  }

  like(id:number){
    this.products.likeProduct(id).subscribe(res => {
      // console.log(res)
      document.getElementById(`${id}`)?.classList.toggle("unlike")
      this.products.getProductById(id).subscribe(res2 => {
        this.filteredProducts.forEach((p: { productId: number;likes:any }) => {
          if(p.productId == id){
            if(p.likes.includes(this.userId)){
              p.likes.pop(this.userId);
            }else{
              p.likes.push(this.userId);
            }
          }
        })
      })
    }) 
  }

  editProduct(data:any){
    const dialogRef = this._dialog.open(ProductFormComponent,{
      data
    });
  }

  ngOnInit(): void {
    this.merchantId = Number(this.route.snapshot.paramMap.get("id"));
    this.getAllProducts();
    this.userId = this.auth.isLoggedIn().Id;
    this.currentUser = this.user.getSingleUser(this.userId).subscribe((res) => {
      this.currentUser = res;
      this.role = res.role;
    })
    this.products.getProductRefreshStatus().subscribe(res => {
      if(res){
        this.getAllProducts();
        this.products.setProductRefreshStatus(false);
      }
    })
  }
}
