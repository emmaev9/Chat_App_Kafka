import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

let stompClient = null;
const WEBSOCKET_URL = 'http://localhost:8081/ws';

export function connect(userID, callback) {
    const socket = new SockJS(WEBSOCKET_URL);
    stompClient = Stomp.over(socket);
    
    // Add custom headers to the connection request
    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/chat', message => {
            callback(message.body);
        });
        stompClient.publish({ destination: '/app/subscribe', body: userID })
    });
}

export function sendMessageWS(message) {
    stompClient.send("/app/sendPrivateMessage", {}, JSON.stringify(message));
}

export function sendMessageWSGroup(message) {
    stompClient.send("/app/sendGroupMessage", {}, JSON.stringify(message));
}

export function addUser(user) {
    stompClient.send("/app/chat.addUser", {}, JSON.stringify(user));
}
