import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { WebSocketService } from '../web-socket.service';
import { HttpModelService } from '../http-model.service';
import { Car, Coordinates, HumanBeing } from '../model';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';

const ELEMENT_DATA: HumanBeing[] = [
  {
    id: 1, name: 'Hydrogen',
    coordinates: new Coordinates,
    realHero: true,
    hasToothpick: true,
    car: new Car,
    mood: '123',
    impactSpeed: 0,
    minutesOfWaiting: 0,
    weaponType: '2233',
    creationDate: '3322'
  },
];

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [CommonModule, MatTableModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css',
})
export class MainComponent {
  constructor(
    private router: Router,
    private webSocketService: WebSocketService,
    private httpModelService: HttpModelService,
    private http: HttpClient
  ) {}

  time = new Date();
  intervalId: any;


  ngOnInit() {
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);
    this.webSocketService.connectWs();
  }

  displayedColumns: string[] = ['id', 'name', 'coordinates', 'realHero', 'hasToothpick', 'car', 'mood', 'impactSpeed', 'minutesOfWaiting', 'weaponType', 'creationDate'];
  dataSource = ELEMENT_DATA;

  newModel() {
    this.router.navigate(['/newModel']);
  }

  sendRequest() {
    this.http.get('http://localhost:8080/api/v1/model/1').subscribe((data) => {
      console.log(data);
    });
  }

  logout() {
    this.http.delete(environment.backendURL + "/api/v1/logout").subscribe(() => {
      localStorage.removeItem('token');
      this.router.navigate(['']);
    });
  }
}
