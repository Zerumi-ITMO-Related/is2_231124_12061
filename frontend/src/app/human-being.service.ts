import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { HumanBeing } from './model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class HumanBeingService {
  constructor(private http: HttpClient) {}

  private _model = new BehaviorSubject<HumanBeing[]>([]);

  get model() {
    return this._model.asObservable();
  }

  updateAll() {
    this.http
      .get<HumanBeing[]>(environment.backendURL + '/api/v1/model')
      .subscribe((data) => {
        this._model.next(data);
        console.log(data);
      });
  }

  update(humanBeing: HumanBeing) {
    let model = this._model.value;
    let index = model.findIndex((hb) => hb.id === humanBeing.id);
    if (index === -1) {
      model.push(humanBeing);
    } else {
      model[index] = humanBeing;
    }
    this._model.next(model);
  }

  delete(humanBeing: number) {
    let model = this._model.value;
    let index = model.findIndex((hb) => hb.id === humanBeing);
    if (index !== -1) {
      model.splice(index, 1);
      this._model.next(model);
    }
  }
}
