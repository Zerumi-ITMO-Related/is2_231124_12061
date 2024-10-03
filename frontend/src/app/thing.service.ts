import { Injectable } from '@angular/core';
import { BehaviorSubject, of } from 'rxjs';
import { Thing } from './model';

@Injectable({
  providedIn: 'root'
})
export class ThingService {

  private _things = new BehaviorSubject<Thing[]>([])

  get things() {
    return this._things.asObservable()
  }

  updateThings() {
    this._things.next([
      { a: this.getRandom(), b: this.getRandom(), c: this.getRandom() },
      { a: this.getRandom(), b: this.getRandom(), c: this.getRandom() },
      { a: this.getRandom(), b: this.getRandom(), c: this.getRandom() },
      { a: this.getRandom(), b: this.getRandom(), c: this.getRandom() },
      { a: this.getRandom(), b: this.getRandom(), c: this.getRandom() },
    ])
  }

  private getRandom() {
    return Math.floor(Math.random() * 100)
  }
}
