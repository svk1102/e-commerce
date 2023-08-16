import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MerchantComponent } from './merchant/merchant.component';
import { ProductsComponent } from './products/products.component';

const routes: Routes = [
  {
    path:"",
    component:MerchantComponent
  },{
    path:":id/products",
    component:ProductsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MerchantRoutingModule { }
