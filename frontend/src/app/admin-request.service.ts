import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { AdminRequest } from './model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdminRequestService {
  constructor(private http: HttpClient) {}

  private _model = new BehaviorSubject<AdminRequest[]>([]);

  get model() {
    return this._model.asObservable();
  }

  updateAll() {
    this.http
      .get<AdminRequest[]>(environment.backendURL + '/api/v1/admin')
      .subscribe((data) => {
        this._model.next(data);
        console.log(data);
      });
  }

  update(adminRequest: AdminRequest) {
    let model = this._model.value;
    let index = model.findIndex((hb) => hb.id === adminRequest.id);
    if (index === -1) {
      model.push(adminRequest);
    } else {
      model[index] = adminRequest;
    }
    this._model.next(model);
  }

  delete(adminRequest: number) {
    let model = this._model.value;
    let index = model.findIndex((hb) => hb.id === adminRequest);
    if (index !== -1) {
      model.splice(index, 1);
      this._model.next(model);
    }
  }
}
