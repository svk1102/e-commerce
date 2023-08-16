import { Component } from '@angular/core';
import { FormGroup,FormControl,FormControlName, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { passwordMatch } from './passwordMatch';
import { Router } from '@angular/router';




@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  constructor(private auth:AuthService , private router:Router){}

  title = "SignUp form";
  SignUpForm=new FormGroup({
    firstName:new FormControl('',Validators.required),
    lastName:new FormControl('',Validators.required),
    email:new FormControl('',[Validators.required,Validators.email]),
    password: new FormControl('',Validators.required),
    confirmPassword: new FormControl('',Validators.required)
  },[passwordMatch("password","confirmPassword")])

  SignUpUser(){

    delete this.SignUpForm.value.confirmPassword;
    console.log(this.SignUpForm.value);
    this.auth.signUpUser(this.SignUpForm.value).subscribe((res) => {
      console.log(res)
    })
    this.SignUpForm.reset();
    this.router.navigate(['/auth/login']);

  }

  SignUpMerchant(){
    delete this.SignUpForm.value.confirmPassword;
    console.log(this.SignUpForm.value)
    this.auth.signUpMerchant(this.SignUpForm.value).subscribe((res) => {
      console.log(res)
    })
    this.SignUpForm.reset();
    this.router.navigate(['/auth/login']);

  }

}
