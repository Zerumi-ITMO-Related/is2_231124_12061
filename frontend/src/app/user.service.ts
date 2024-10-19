import { Injectable } from '@angular/core';
import { UserProfile } from './model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  authenticatedUserSubject = new BehaviorSubject<UserProfile>(new UserProfile());

  authenticatedUser: Observable<UserProfile> = this.authenticatedUserSubject.asObservable();

  constructor(private http: HttpClient) {
    
  }

  updateAuthenticatedUser() {
    this.http.get<UserProfile>(environment.backendURL + '/api/v1/whoami').subscribe(
      (user) => {
        this.authenticatedUserSubject.next(user);
      }
    );
  }
}
