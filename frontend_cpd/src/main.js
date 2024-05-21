import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import stompClient from './stomp';

const app = createApp(App);
app.use(router);
app.config.globalProperties.$stompClient = stompClient;
stompClient.activate();
app.mount('#app');
