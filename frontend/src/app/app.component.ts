import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { WebSocketService } from './web-socket.service';
import { catchError, retry, throwError } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpModelService } from './http-model.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  constructor(private webSocketService: WebSocketService, private httpModelService: HttpModelService) {
    console.log('Connecting to websocket');
  }
}
