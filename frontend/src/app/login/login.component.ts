import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { CommonModule } from '@angular/common';
import { LoginData } from '../model';
import {
  FormControl,
  FormsModule,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css', '../app.component.css'],
})
export class LoginComponent {
  time = new Date();
  intervalId: any;

  loginForm = new FormGroup({
    login: new FormControl(''),
    password: new FormControl(''),
  });
  
  registerForm = new FormGroup({
    login: new FormControl(''),
    password: new FormControl(''),
    repeatedPassword: new FormControl(''),
  });

  loginModel: LoginData = {
    username: '',
    password: '',
  };

  sessionID = '';

  constructor(private router: Router, private http: HttpClient) {}
  ngOnInit() {
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);

    if (localStorage.getItem('token')) {
      this.router.navigate(['main']);
    }
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }

  login() {
    console.log('login');
    console.log(this.loginForm.value);
    const url = environment.backendURL + '/api/v1/login';
    /*this.http.post(url, this.loginModel).subscribe((response: any) => {
      if (response.status === 200) {
        localStorage.setItem('token', response.token);
        this.router.navigate(['main']);
      } else {
        alert('Login failed');
      }
    });*/
  }

  register() {
    const url = environment.backendURL + '/api/v1/register';
    if (this.registerForm.controls.password !== this.registerForm.controls.repeatedPassword) {
      alert('Passwords do not match');
      return;
    }
    this.http.post(url, this.loginModel).subscribe((response: any) => {
      if (response.status === 200) {
        localStorage.setItem('token', response.token);
        this.router.navigate(['main']);
      } else {
        alert('Register failed');
      }
    });
  }
}
