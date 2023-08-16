import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor{
  

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = sessionStorage.getItem("token");
    if(request.url.endsWith("/auth/login") || request.url.endsWith("/auth/registerMerchant") || request.url.endsWith("/auth/registerUser")){
      return next.handle(request);
    }
    // Add your default headers here
    const modifiedRequest = request.clone({
      setHeaders: {
        'Content-Type': 'application/json', // Example: you can add any headers you need here
        'Authorization': `Bearer ${token}`, // Example: add an authorization header if needed
      },
    });

    return next.handle(modifiedRequest);
  }

}
