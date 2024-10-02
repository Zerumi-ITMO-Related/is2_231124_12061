import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { WebSocketService } from '../web-socket.service';
import { HttpModelService } from '../http-model.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {
  constructor(private router: Router, private webSocketService: WebSocketService, private httpModelService: HttpModelService, private http: HttpClient) {
    console.log('Connecting to websocket');
  }

  navigateToNewModel() {
    this.router.navigate(['/newModel']);
  }

  sendRequest() {
    this.http.get('http://localhost:8080/api/v1/model/1').subscribe((data) => {
      console.log(data);
    });
  }
}
