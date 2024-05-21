import { Client } from '@stomp/stompjs';

const brokerURL = 'ws://localhost:8080/stomp'; // Replace with your STOMP broker URL

const stompClient = new Client({
  brokerURL,
  reconnectDelay: 5000,
  debug: function (str) {
    console.log(str);
  }
});

export default stompClient;
