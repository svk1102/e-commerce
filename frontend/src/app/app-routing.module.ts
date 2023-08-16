import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AuthGuard } from './auth.guard';
import { AdminComponent } from './admin/admin.component';
import { UserDetailComponent } from './user-detail/user-detail.component';

const routes: Routes = [
  {
    path:"auth",
    loadChildren:()=>import("./auth/auth.module")
    .then(mod => mod.AuthModule)
  },
  {
    path:"merchant",
    loadChildren:()=>import("./merchant/merchant.module")
    .then(mod => mod.MerchantModule),
    canActivate:[AuthGuard]
  },
  {
    path:"category",
    loadChildren:()=>import("./category/category.module")
    .then(mod => mod.CategoryModule),
    canActivate:[AuthGuard]
  },
  {
    path:"cart",
    loadChildren:()=>import("./cart/cart.module")
    .then(mod => mod.CartModule),
    canActivate:[AuthGuard]
  },{
    path:"orders",
    loadChildren:()=>import("./order/order.module")
    .then(mod => mod.OrderModule),
    canActivate:[AuthGuard]
  },
  {
    component:ProductsComponent,
    path:"products",
    canActivate:[AuthGuard]
  },
  {
    component:ProductDetailComponent,
    path:"products/:id",
    canActivate:[AuthGuard]
  },
  {
    component:AdminComponent,
    path:"admin",
    canActivate:[AuthGuard]
  },
  {
    component:UserDetailComponent,
    path:"profile",
    canActivate:[AuthGuard]
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
