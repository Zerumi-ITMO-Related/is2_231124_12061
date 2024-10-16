import { AfterViewInit, Component, inject, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { WebSocketService } from '../web-socket.service';
import { HttpModelService } from '../http-model.service';
import { Car, Coordinates, HumanBeing, UserProfile } from '../model';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';
import {
  MatPaginator,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { Observable, map } from 'rxjs';
import { HumanBeingService } from '../human-being.service';
import {
  MatFormFieldModule,
} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { UserService } from '../user.service';
import { MatDialog } from '@angular/material/dialog';
import { NewModelComponent } from '../new-model/new-model.component';
import { FormsModule } from '@angular/forms';

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
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatMenuModule,
    MatButtonModule,
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css',
})
export class MainComponent implements AfterViewInit {
  dataSource = new MatTableDataSource<HumanBeing>();

  hbMaterialDataSource$: Observable<MatTableDataSource<HumanBeing>>;
  currentUser: UserProfile = new UserProfile();

  constructor(
    private router: Router,
    private webSocketService: WebSocketService,
    private httpModelService: HttpModelService,
    private humanBeingService: HumanBeingService,
    private userService: UserService,
    private http: HttpClient
  ) {
    this.hbMaterialDataSource$ = this.humanBeingService.model.pipe(
      map((things) => {
        const dataSource = this.dataSource;
        dataSource.data = things;
        return dataSource;
      })
    );
    this.userService.authenticatedUser.subscribe((user) => {
      this.currentUser = user;
    })
  }

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  @ViewChild(MatSort)
  sort!: MatSort;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  time = new Date();
  intervalId: any;

  ngOnInit() {
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);
    this.webSocketService.connectWs();
    this.userService.updateAuthenticatedUser();
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

  @ViewChild(MatMenuTrigger)
  contextMenu!: MatMenuTrigger;

  contextMenuPosition = { x: '0px', y: '0px' };

  onContextMenu(event: MouseEvent, item: any) {
    event.preventDefault();
    this.contextMenuPosition.x = event.clientX + 'px';
    this.contextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menuData = { 'item': item };
    this.contextMenu.openMenu();
  }

  applyFilter($event: Event) {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }


  readonly dialog = inject(MatDialog);
  
  newModel() {
    const dialogRef = this.dialog.open(NewModelComponent, {
      data: {name: "123", animal: "123"},
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        // todo: send the new model to the server
        console.log(result);
      }
    });
  }

  updateAll() {
    this.humanBeingService.updateAll();
  }

  onTableContextDelete(item: any) {
    console.log(item);
  }

  onTableContextEdit(item: any) {
    console.log(item);
  }

  logout() {
    localStorage.removeItem('token');
    this.http.delete(environment.backendURL + '/api/v1/logout');
    this.router.navigate(['']);
    this.webSocketService.disconnectWs();
  }
}
