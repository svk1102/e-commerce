import { Component, OnInit , inject} from '@angular/core';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { CartService } from '../services/cart.service';
import { MatDialog } from '@angular/material/dialog';
import { ProductFormComponent } from '../product-form/product-form.component';
import { ProductService } from '../services/product.service';
import { FormControl } from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {NgFor, AsyncPipe} from '@angular/common';
import { Router } from '@angular/router';
import { CategoryService } from '../services/category.service';
import { LiveAnnouncer } from '@angular/cdk/a11y';



@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  role!:String;
  currentUser:any;
  cartItems!:Number;
  panelOpenState = false;
  categories:any[] = [];
  merchants:any[] = [];
  loginStatus!:boolean;


  


  constructor(private auth:AuthService , private user:UserService , private cartService:CartService , private _dialog:MatDialog , private product:ProductService,private category:CategoryService ,private router:Router){}

  openProductForm(){
    const dialogRef = this._dialog.open(ProductFormComponent);
    
  }

  myControl = new FormControl('');
  options: any[] = [];
  filteredOptions!: Observable<any[]>;
  selectedValue!:String;
  

  clickedOption(value:any){
    console.log(value);
    let productId = -1;
    // console.log(this.options)
    this.options.forEach(o => {
      if(o.displayValue == value){
        productId = o.value
        this.selectedValue = o.value;
        // console.log(this.selectedValue + " test")
        // this.router.navigate([`/products/${this.selectedValue}`])
      }
    })
    console.log(productId);
    this.router.navigateByUrl(`/products/${productId}`)
    // setTimeout(() => {
    //   this.router.navigateByUrl(`/products/${productId}`)
    // },1)

  }

  getProductOptions(){
    this.product.getAllProducts().subscribe(res => {
      res.forEach((p: { productId: any,name:any }) => {
        this.options.push({
          value:p.productId,
          displayValue:p.name
        })
      })
    })
  }

  private _filter(value: any): any[] {
    const filterValue = value?.toLowerCase();

    return this.options.filter(option => option.displayValue.toLowerCase().includes(filterValue));
  }

  loginCheck(){
    if(this.auth.isLoggedIn()){
      var id = this.auth.isLoggedIn().Id;
        this.currentUser = this.user.getSingleUser(id).subscribe((res) => {
          console.log(res);
          this.currentUser = res;
          this.role = res.role;
          // this.cartItems = Object.keys(res.cart.cartItems).length;
        })
        this.cartService.getCartItems().subscribe((count) => {
          this.cartItems = count;
        })
    }
    
  }
  
  getCategories(){
    this.category.getCategory().subscribe(res => {
      this.categories = res;
    })
  }

  getMerchants(){
    this.user.getAllMerchants().subscribe(res =>{
      this.merchants = res;
    })
  }
  logout(){
    sessionStorage.clear();
    this.auth.setLoginCheck(false);
    this.router.navigateByUrl(`/auth/login`);
  }

  getLoginStatus(){
    this.auth.getLoginCheck().subscribe(res => {
      this.loginStatus = res;
    })
  }


  ngOnInit(): void {
    this.loginCheck();
    this.getCategories();
    this.getMerchants();
    
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith('temp'),
      map(value => this._filter(value || '')),
    );
    this.getProductOptions();
    this.getLoginStatus();

  }

  

}
