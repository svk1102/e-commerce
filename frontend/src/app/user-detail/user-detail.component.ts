import { Component , OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit{

  userDetails:any;
  userId!:number;

  constructor(private auth:AuthService ,private user:UserService){}

  getUserDetails(){
    this.user.getSingleUser(0).subscribe(res => {
      this.userDetails = res;
      // console.log(res)
    })
  }
  ngOnInit(): void {
    this.getUserDetails();
  }
}
