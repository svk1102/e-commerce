import { Component } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-admin-filters',
  templateUrl: './admin-filters.component.html',
  styleUrls: ['./admin-filters.component.css']
})
export class AdminFiltersComponent {

  idFilter!:Number;
  userFilter!:Number;
  startDate:any;
  endDate:any;

  constructor(private order:OrderService){}

  applyFilter(){
    
    this.order.getOrdersByFilter({id:this.idFilter,user:this.userFilter,startDate:new Date(this.startDate).getTime()/1000 , endDate:new Date(this.endDate).getTime()/1000}).subscribe(res => {
      this.order.setFilteredOrders(res);
    })
  }
}
