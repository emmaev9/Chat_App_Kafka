<template>
  <div class="chat-page">
    <div class="sidebar">
      <div class="chat-users">
       <div class="titles"> Users </div>
        <ul>
          <li v-for="user in users" :key="user.username" @click="selectUser(user)">
            {{ user.username }}
          </li>
        </ul>
      </div>
    </div>
    <div class="chat-container">
      <div v-if="selectedUser">
        <h2>Chat with {{ selectedUser.username }}</h2>
        <div class="messages">
          <div v-for="message in messages" :key="message.id">
            <strong>{{ message.sender }}:</strong> {{ message.content }}
          </div>
        </div>
        <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message..." />
      </div>
      <div v-else>
        <h2>Select a user to start chatting</h2>
        <button @click="sendMessageWebsocket">Send message</button>
      </div>
    </div>
    <div class="sidebar">
      <div class="chat-groups">
        <div class="titles">Groups</div>
        <ul>
          <li v-for="group in groups" :key="group.name">
            {{ group.name }}
          </li>
        </ul>
        <button class="group-button" @click="createGroup">Create Group</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { connect, sendMessageWS } from '../service/websocketservice';

export default {
  name: 'ChatPage',
  props: ['username'],
  data() {
    return {
      users: [
        { username: 'Alice' },
        { username: 'Bob' },
        // Add more users as needed
      ],
      groups: [
        { name: 'Group 1' },
        { name: 'Group 2' },
        // Add more groups as needed
      ],
      selectedUser: null,
      messages: [],
      newMessage: '',
      user: this.username
    }
  },
  methods: {
    selectUser(user) {
      this.selectedUser = user;
      this.fetchMessages();
    },
    createGroup() {
      const groupName = prompt('Enter group name:');
      if (groupName) {
        const newGroup = {
          name: groupName
        };
        this.groups.push(newGroup);
      }
    },
    async fetchMessages() {
      const response = await axios.get(`/api/messages?username=${this.selectedUser.username}`);
      this.messages = response.data;
    },
    async sendMessage() {
      if (this.newMessage.trim() === '') return;
      
      await axios.post('/api/messages', {
        sender: this.user,
        recipient: this.selectedUser.username,
        content: this.newMessage
      });

      this.newMessage = '';
      this.fetchMessages();
    },
    sendMessageWebsocket() {
      sendMessageWS("private-aurel-aurel-Penis")
    }
  },
  mounted() {
    connect('aurel', (message) => {
      console.log("received: " + message)
    });
  }
}
</script>

<style scoped>

.chat-page {
  display: flex;
  flex-direction: row;
  margin: 100px;
  height: 100vh;
  border: #c5d8fd 1px solid;
}
.chat-title {
  margin: 0;
  padding: 20px;
  background-color: #007bff;
  color: white;
  width: 100%;
  text-align: center;
}
.titles{
  font-size: 20px;
  font-weight: bold;
  padding: 10px;

}
.sidebar {
  width: 25%;
  padding: 20px;
  background-color: #e8fafe;
}

.chat-container {
  width: 75%;
  padding: 20px;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #ccc;
}

li:hover {
  background-color: #d5f7ff;
}

button {
  margin-top: 20px;
  padding: 10px;
}

.messages {
  max-height: 500px;
  overflow-y: auto;
}

.group-button {
  padding: 10px;
  background-color: #54cce2;
  color: rgb(0, 0, 0);
  border: none;
  border-radius: 5px;
  font-size: 16px;
}

input {
  width: 70%;
  padding: 10px;
  margin-top: 10px;
}
</style>
