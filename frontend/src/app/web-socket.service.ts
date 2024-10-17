import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import { HumanBeingService } from './human-being.service';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private hbService: HumanBeingService
  private client: Client = new Client({
    brokerURL: 'ws://localhost:8080/socket',
    connectHeaders: {
      'authorization': localStorage.getItem('token') || '',
    },
    debug: function (str: any) {
      console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });

  constructor(hbService: HumanBeingService) {
    this.hbService = hbService;

    const client = this.client;
    
    client.onConnect = function (frame: any) {
      // Do something, all subscribes must be done is this callback
      // This is needed because this will be executed after a (re)connect
      client.subscribe('/topic/newModel', function (message: Message) {
        hbService.update(JSON.parse(message.body));
      });

      client.subscribe('/topic/updatedModel', function (message: Message) {
        hbService.update(JSON.parse(message.body).modelDto);
      });

      client.subscribe('/topic/removeModel', function (message: Message) {
        hbService.delete(Number(message.body));
      });
    };
    
    client.onStompError = function (frame: { headers: { [x: string]: string; }; body: string; }) {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };
  }

  connectWs() {
    this.client.activate();
  }

  disconnectWs() {
    this.client.deactivate();
  }
}