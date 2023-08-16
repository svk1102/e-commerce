import { Component,OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { ProductService } from 'src/app/services/product.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-cart-items',
  templateUrl: './cart-items.component.html',
  styleUrls: ['./cart-items.component.css']
})
export class CartItemsComponent implements OnInit{

  cartProducts:any;
  cartData:any;
  userId!:number;
  totalPrice:number=0;

  constructor(private cart:CartService , private auth:AuthService,private product:ProductService , private order:OrderService , private router:Router){}

  getCart(){
    this.cartProducts=[]
    this.totalPrice = 0;
    this.cart.getCartData(this.userId).subscribe(res1 => {
      this.cartData = res1;
      Object.keys(this.cartData.cartItems).forEach(id => {
        this.product.getProductById(Number(id)).subscribe(res => {
          res.cartQuantity = this.cartData.cartItems[id];
          this.cartProducts.push(res)
          console.log(this.cartProducts)
          this.totalPrice = this.totalPrice + (res.cartQuantity*res.price)
        })
      })

    })    
  }

  addToCart(productId:number){
    this.cart.addToCart({"userId":this.userId , "productId" : productId})
    
  }

  removeFromCart(productId:number){
    this.cart.removeFromCart(productId)
  }

  increment(id:number){
    this.cartProducts.forEach((p: { productId: number; cartQuantity:number ;price:number}) => {
      if(p.productId === id){
        p.cartQuantity = p.cartQuantity + 1
        this.totalPrice = this.totalPrice + p.price;
      }
    })
    this.addToCart(id);
  }

  decrement(id:number){
    this.cartProducts.forEach((p: { productId: number; cartQuantity:number;price:number },index:any) => {
      if(p.productId === id){
        if(p.cartQuantity === 1){
          this.cartProducts.splice(index,1);
        }else{
          p.cartQuantity = p.cartQuantity - 1;
        }
        this.totalPrice = this.totalPrice - p.price
      }
    })
    this.removeFromCart(id);
  }

  placeOrder(){
    this.cart.getCartData(this.userId).subscribe(res => {
      this.cartData = res;
      // console.log(this.cartData.cartItems)
      this.order.placeOrder({
        "userId":this.userId,
        "price":this.totalPrice,
        "timeStamp":Math.floor(Date.now()/1000),
        "products":this.cartData.cartItems
      }).subscribe(res2 => {
        console.log(res2)
        this.cart.getCartItems();
        this.router.navigate(['/orders']);
      })
    })

  }

  ngOnInit(): void {
    this.userId = this.auth.isLoggedIn().Id;
    this.getCart()
    // this.calculatePrice();
  }
}
