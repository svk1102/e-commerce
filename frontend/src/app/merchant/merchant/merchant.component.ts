import { Component , OnInit} from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-merchant',
  templateUrl: './merchant.component.html',
  styleUrls: ['./merchant.component.css']
})
export class MerchantComponent implements OnInit{
  merchants:any[]=[];

  constructor(private user:UserService){}
  
  getAllMerchants(){
    this.user.getAllMerchants().subscribe(res => {
      this.merchants = res
    })
  }

  ngOnInit(): void {
    this.getAllMerchants();
  }
}
