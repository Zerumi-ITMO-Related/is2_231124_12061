import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class HttpModelService {
  constructor() {
    // this.newModel(null);
  }

  newModel(model: any) {
    fetch('http://localhost:8080/api/v1/model', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        name: 'John Doe',
        coordinates: { x: 10, y: 20 },
        realHero: true,
        hasToothpick: false,
        car: { name: 'Toyota' },
        mood: 'SADNESS',
        impactSpeed: 120,
        minutesOfWaiting: 15,
        weaponType: 'PISTOL',
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log('Success:', data);
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }
}
