import { AfterViewInit, Component, inject, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { WebSocketService } from '../web-socket.service';
import { HttpModelService } from '../http-model.service';
import {
  AdminRequest,
  Car,
  Coordinates,
  HumanBeing,
  UserProfile,
} from '../model';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { Observable, map } from 'rxjs';
import { HumanBeingService } from '../human-being.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { UserService } from '../user.service';
import { MatDialog } from '@angular/material/dialog';
import { NewModelComponent } from '../new-model/new-model.component';
import { FormsModule } from '@angular/forms';
import { AdminRequestService } from '../admin-request.service';
import { MatIcon, MatIconModule } from '@angular/material/icon';

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
    MatIconModule,
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent {
  dataSource = new MatTableDataSource<AdminRequest>();

  adminRqMaterialDataSource$: Observable<MatTableDataSource<AdminRequest>>;

  currentUser: UserProfile;

  constructor(
    private router: Router,
    private webSocketService: WebSocketService,
    private httpModelService: HttpModelService,
    private adminRqService: AdminRequestService,
    private userService: UserService,
    private http: HttpClient
  ) {
    this.currentUser = userService.authenticatedUserSubject.value;
    this.adminRqMaterialDataSource$ = this.adminRqService.model.pipe(
      map((things) => {
        const dataSource = this.dataSource;
        dataSource.data = things;
        return dataSource;
      })
    );
    this.userService.authenticatedUser.subscribe((user) => {
      this.currentUser = user;
    });
  }

  time = new Date();
  intervalId: any;

  displayedColumns = [
    'id',
    'userId',
    'username',
    'requestDate',
    'status',
    'comment',
    'accept',
    'reject',
  ];

  ngOnInit() {
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);
  }

  openMainPanel() {
    this.router.navigate(['main']);
  }

  updateAll() {
    this.adminRqService.updateAll();
  }

  applyFilter($event: Event) {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  acceptRequest(request: any) {
    this.http.put(environment.backendURL + '/api/v1/admin/approve', request).subscribe();
  }

  rejectRequest(request: AdminRequest) {
    this.http.put(environment.backendURL + '/api/v1/admin/decline', request).subscribe();
  }

  logout() {
    localStorage.removeItem('token');
    this.http.delete(environment.backendURL + '/api/v1/logout');
    this.router.navigate(['']);
    this.webSocketService.disconnectWs();
    window.location.reload();
  }
}
