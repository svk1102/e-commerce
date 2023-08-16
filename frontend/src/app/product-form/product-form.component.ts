import { Component , Inject, OnInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';
import { MatLabel } from '@angular/material/form-field';
import { ProductService } from '../services/product.service';
import { AuthService } from '../services/auth.service';
import { CategoryService } from '../services/category.service';
import { DialogRef } from '@angular/cdk/dialog';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit{
  ProductRequest:any;

  selectedValue!:string;
  categories: any = []

  constructor(private product:ProductService,private auth:AuthService, private category:CategoryService, private _dialogRef:MatDialogRef<ProductFormComponent> , @Inject(MAT_DIALOG_DATA) public data:any){}

ProductForm=new FormGroup({
    name:new FormControl('',Validators.required),
    category:new FormControl('',Validators.required),
    price:new FormControl('',Validators.required),
    quantity: new FormControl('',Validators.required),
    imageUrl: new FormControl('',Validators.required)
  })

  submitProduct(){
    this.ProductRequest = this.ProductForm.value;
    this.ProductRequest.userId = this.auth.isLoggedIn().Id;
    console.log(this.ProductRequest)
    if(this.data){
      this.ProductRequest.productId = this.data.productId;
      this.product.updateProduct(this.ProductRequest).subscribe((res) => {
        console.log(res);
        this._dialogRef.close();
        this.product.setProductRefreshStatus(true);
      })
    }else{
      this.product.addProduct(this.ProductRequest).subscribe((res) => {
      console.log(res);
      this._dialogRef.close();
      this.product.setProductRefreshStatus(true);
    })
    }
    
    
    
  }

  getCategory(){
    this.category.getCategory().subscribe(res => {
      console.log(res)
      this.categories = res;
    })
  }

  ngOnInit(): void {
    this.getCategory();
    this.ProductForm.patchValue(this.data);
  }
  
}
