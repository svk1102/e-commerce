import { Component } from '@angular/core';
import { FormGroup,FormControl,FormControlName, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';




@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  hide:boolean = true;


  constructor(private auth:AuthService , private router:Router){ }

  title = "Login form";
  loginForm=new FormGroup({
    email:new FormControl('',[Validators.required,Validators.email]),
    password: new FormControl('',Validators.required)
  })

  loginUser(){
    console.log(this.loginForm.value)
    this.auth.loginUser(this.loginForm.value).subscribe((res) => {
      sessionStorage.setItem("token",res.token);
      console.log(res);
      this.router.navigate(['/products']);
    })
    this.auth.setLoginCheck(true);
    this.loginForm.reset();
  }
}
