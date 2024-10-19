import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { CommonModule } from '@angular/common';
import { AuthResponse, LoginData } from '../model';
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
    adminRequired: new FormControl(false),
  });

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
    this.http.post<AuthResponse>(url, this.loginForm.value).subscribe((response: AuthResponse) => {
      if (response.token) {
        localStorage.setItem('token', response.token);
        this.router.navigate(['main']);
      } else {
        alert('Login failed');
      }
    });
  }

  register() {
    const url = environment.backendURL + '/api/v1/register';
    if (this.registerForm.controls.password.value !== this.registerForm.controls.repeatedPassword.value) {
      alert('Passwords do not match');
      return;
    }
    const loginModel: LoginData = {
      login: this.registerForm.controls.login.value!!,
      password: this.registerForm.controls.password.value!!,
      adminRequired: this.registerForm.controls.adminRequired.value!!,
    };
    this.http.post<AuthResponse>(url, loginModel).subscribe((response: AuthResponse) => {
      if (response.token) {
        localStorage.setItem('token', response.token);
        this.router.navigate(['main']);
      } else {
        alert('Register failed');
      }
    });
  }
}
