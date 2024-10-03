import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { WebSocketService } from '../web-socket.service';
import { HttpModelService } from '../http-model.service';
import { Car, Coordinates, HumanBeing, Thing } from '../model';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { Observable, map } from 'rxjs';
import { HumanBeingService } from '../human-being.service';
import { ThingService } from '../thing.service';

const ELEMENT_DATA: HumanBeing[] = [
  {
    id: 1,
    name: 'Hydrogen',
    coordinates: new Coordinates(),
    realHero: true,
    hasToothpick: true,
    car: new Car(),
    mood: '123',
    impactSpeed: 0,
    minutesOfWaiting: 0,
    weaponType: '2233',
    creationDate: '3322',
  },
];

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatSortModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css',
})
export class MainComponent {

  dataSource = new MatTableDataSource<HumanBeing>();
  thingDataSource = new MatTableDataSource<Thing>();

  hbMaterialDataSource$: Observable<MatTableDataSource<HumanBeing>>;

  constructor(
    private router: Router,
    private webSocketService: WebSocketService,
    private httpModelService: HttpModelService,
    private humanBeingService: HumanBeingService,
    private thingService: ThingService,
    private http: HttpClient
  ) {
    this.hbMaterialDataSource$ = this.humanBeingService.model.pipe(
      map((things) => {
        const dataSource = this.dataSource;
        dataSource.data = things;
        return dataSource;
      })
    );
    this.thingsAsMatTableDataSource$ = this.thingService.things.pipe(
      map((things) => {
        const dataSource = this.thingDataSource;
        this.thingDataSource.data = things
        return dataSource;
      })
    );

  }

  thingsAsMatTableDataSource$: Observable<MatTableDataSource<Thing>>;

  time = new Date();
  intervalId: any;

  ngOnInit() {
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);
    this.webSocketService.connectWs();
  }

  displayedColumns: string[] = [
    'id',
    'name',
    'coordinates',
    'realHero',
    'hasToothpick',
    'car',
    'mood',
    'impactSpeed',
    'minutesOfWaiting',
    'weaponType',
    'creationDate',
  ];

  newModel() {
    this.router.navigate(['/newModel']);
  }

  updateAll() {
    this.humanBeingService.updateAll();
  }

  sendRequest() {
    this.http.get('http://localhost:8080/api/v1/model/1').subscribe((data) => {
      console.log(data);
    });
  }

  logout() {
    this.http
      .delete(environment.backendURL + '/api/v1/logout')
      .subscribe(() => {
        localStorage.removeItem('token');
        this.router.navigate(['']);
      });
  }

  newComponent() {
    ELEMENT_DATA.push({
      id: 1,
      name: 'Hydrogen',
      coordinates: new Coordinates(),
      realHero: true,
      hasToothpick: true,
      car: new Car(),
      mood: '123',
      impactSpeed: 0,
      minutesOfWaiting: 0,
      weaponType: '2233',
      creationDate: '3322',
    });
    console.log(ELEMENT_DATA);
  }
}
