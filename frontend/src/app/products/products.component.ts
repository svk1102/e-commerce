import { Component,OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { AuthService } from '../services/auth.service';
import { CartService } from '../services/cart.service';
import { UserService } from '../services/user.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ProductFormComponent } from '../product-form/product-form.component';
import { CategoryService } from '../services/category.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit{

  filteredProducts:any;
  productData:any;
  // inputFilter:string=""
  upperValue:number=100000
  lowerValue:number=0
  userId!:number;
  // categoryFilter!:string
  currentUser:any;
  role:any;

  selectedValue!:string;


  constructor(private products:ProductService , private auth:AuthService , private cart:CartService , private user:UserService , private _dialog:MatDialog){}


  

  getAllProducts(){
    this.products.getAllProducts().subscribe((res) => {
      console.log(res)
      this.productData=res;
      this.products.setFilterProducts(res);
      // this.filteredProducts=res;
      this.filteredProducts.reverse();
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

  getFilteredProducts(){
    this.products.getFilterProducts().subscribe(res => {
      this.filteredProducts = res;
    })
  }

  ngOnInit(): void {
    this.getAllProducts();
    this.getFilteredProducts();
    this.userId = this.auth.isLoggedIn().Id;
    this.currentUser = this.user.getSingleUser(this.userId).subscribe((res) => {
      this.currentUser = res;
      this.role = res.role;
      // this.cartItems = Object.keys(res.cart.cartItems).length;
    })
    this.products.getProductRefreshStatus().subscribe(res => {
      if(res){
        this.getAllProducts();
        this.products.setProductRefreshStatus(false);
      }
    })
  }

}
