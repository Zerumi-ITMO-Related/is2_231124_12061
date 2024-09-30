import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { environment } from '../../environments/environment';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css', '../app.component.css']
})
export class LoginComponent {
  time = new Date();
  intervalId : any;

  model : any = {};
  sessionID = '';

  constructor(private router: Router,
    private http: HttpClient) {
  }
  ngOnInit() {
    // Using Basic Interval
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);

    if (localStorage.getItem('token')) {
        this.router.navigate(['main'])
    }
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }

  login() {
    const url = environment.backendURL + '/app/login';
    this.proceedAuthRequest(url);
  }

  register() {
    const url = environment.backendURL + '/app/register';
    this.proceedAuthRequest(url);
  }

  proceedAuthRequest(url : string) {
    this.http.post<any>(url, {
      username: this.model.username,
      password: this.model.password
    })
      .subscribe(res => {
          if (res) {
            this.sessionID = res.sessionID;

            localStorage.setItem(
              'token',
              this.sessionID
            );

            this.router.navigate(['main']).then(r => {
              if (!r) {
                console.error("something went wrong...");
              }
            });
          } else {
            console.error("auth failed");
          }
        },
        err => {
          alert(/<body.*?>([\s\S]*)<\/body>/.exec(err.error)![1]);
        }
      )
  }
}
