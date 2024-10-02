import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  constructor() {
    // this.connectWs();
  }

  connectWs() {
    const client = new Client({
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
    
    client.onConnect = function (frame: any) {
      // Do something, all subscribes must be done is this callback
      // This is needed because this will be executed after a (re)connect
      client.subscribe('/topic/newModel', function (message: Message) {
        console.log('Message: ' + message.body);
      });
    };
    
    client.onStompError = function (frame: { headers: { [x: string]: string; }; body: string; }) {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };
    
    client.activate();
  }
}