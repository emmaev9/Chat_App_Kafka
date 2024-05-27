<template>
  <div class="chat-page">
    <div class="sidebar">
      <div class="chat-users">
        <div class="titles"> Friends </div>
        <ul>
          <button class="group-button" @click="addFriend">Add friend</button>
          <li v-for="user in users" :key="user.name" @click="selectUser(user)">
            {{ user.name }}
          </li>
        </ul>
      </div>
    </div>
    <div class="chat-container">
      <div v-if="selectedUser">
        <h2>Chat with {{ selectedUser.name }}</h2>
        <div class="messages">
          <div v-for="message in currentMessages" :key="message.id">
            <strong>{{ message.sender }}:</strong> {{ message.content }}
          </div>
        </div>
        <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message..." />
      </div>

      <div v-else-if="selectedGroup">
        <h2>Chat with {{ selectedGroup.name }}</h2>
        <div class="messages">
          <div v-for="message in currentMessages" :key="message.id">
            <strong>{{ message.sender }}:</strong> {{ message.content }}
          </div>
        </div>
        <input v-model="newMessage" @keyup.enter="sendGroupMessage" placeholder="Type a message..." />
      </div>

      <div v-else>
        <h2>Select a user to start chatting</h2>
      </div>
    </div>
    <div class="sidebar">
      <div class="chat-groups">
        <div class="titles">Groups</div>
        <ul>
          <li v-for="group in groups" :key="group.name" @click="selectGroup(group)">
            {{ group.name }}
          </li>
        </ul>
        <button class="group-button" @click="createGroup">Create Group</button>
        <button class="group-button" @click="addPeopleToGroup">Add people to group</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { connect, sendMessageWS, sendMessageWSGroup } from '../service/websocketservice';

export default {
  name: 'ChatPage',
  props: ['user'],
  data() {
    return {
      users: [],
      groups: [],
      selectedUser: null,
      selectedGroup: null,
      currentMessages: [],
      newMessage: '',
    }
  },
  methods: {
    selectUser(user) {
      this.selectedUser = user;
      this.selectedGroup = null;
      this.currentMessages = user.messages;
    },
    selectGroup(group) {
      this.selectedGroup = group;
      this.selectedUser = null;
      this.currentMessages = group.messages;
    },
    addFriend() {
      const username = prompt('Enter friend username:');
      if (username) {
        this.users.push({ name: username, messages: [] });
      }
    },
    createGroup() {
      const groupName = prompt('Enter group name:');
      const userId = this.user;
      if (groupName) {
        this.groups.push({ name: groupName, messages: [] });
        axios.post(`http://localhost:8081/api/groups/addUser/${groupName}/${userId}`);
      }
    },
    addPeopleToGroup() {
      const groupName = prompt('Enter group name:');
      const userId = prompt('Enter friend username:');
      if (groupName && userId) {
        axios.post(`http://localhost:8081/api/groups/addUser/${groupName}/${userId}`);
      }
    },
    async sendMessage() {
      if (this.newMessage.trim() === '') return;
      
      const message = {
        id: Date.now(),
        type: 'private',
        receiver: this.selectedUser.name,
        sender: this.user,
        content: this.newMessage
      };
      if (!this.selectedUser.messages) {
        this.selectedUser.messages = [];
      }
      this.selectedUser.messages.push(message);

      sendMessageWS(`private-${this.user}-${this.selectedUser.name}-${this.newMessage}`);
      this.newMessage = '';
    },
    async sendGroupMessage() {
      if (this.newMessage.trim() === '') return;
      
      const message = {
        id: Date.now(),
        type: 'group',
        receiver: this.selectedGroup.name,
        sender: this.user,
        content: this.newMessage
      };

      if (!this.selectedGroup.messages) {
        this.selectedGroup.messages = [];
      }
      this.selectedGroup.messages.push(message);

      sendMessageWSGroup(`group-${this.user}-${this.selectedGroup.name}-${this.newMessage}`);
      this.newMessage = '';
    },
    handleMessage(message) {
      const splitedMessage = message.replace(/^"|"$/g,'').split('-');
      const sender = splitedMessage[1];
      const receiver = splitedMessage[2];
      const type = splitedMessage[0];
      const content = splitedMessage[3];
      
      const receivedMessage = {
        id: Date.now(),
        sender: sender,
        content: content,
        receiver: receiver,
        type: type
      };

      if (type === 'private') {
        const user = this.users.find(user => user.name === sender);
        if (!user) {
          const newUser = { name: sender, messages: [receivedMessage] };
          this.users.push(newUser);
          if (this.selectedUser && this.selectedUser.name === sender) {
            this.currentMessages.push(receivedMessage);
          }
        } else {
          if(this.selectedUser.name!=sender){
            user.messages.push(receivedMessage);
          }
          if (this.selectedUser && this.selectedUser.name == sender) {
            this.currentMessages.push(receivedMessage);
          }
        }
      } else if (type === 'group') {
        const group = this.groups.find(group => group.name === receiver);
        if (!group) {
          const newGroup = { name: receiver, messages: [receivedMessage] };
          this.groups.push(newGroup);
          if (this.selectedGroup && this.selectedGroup.name === receiver && sender !== this.user) {
            this.currentMessages.push(receivedMessage);
          }
        } else {
          if (this.selectedGroup && this.selectedGroup.name === receiver && sender !== this.user) {
            this.currentMessages.push(receivedMessage);
          }
        }
      }
    }
  },
  mounted() {
    connect(this.user, (message) => {
      this.handleMessage(message);
    });
  }
}
</script>

<style scoped>
/* (CSS remains unchanged) */
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
.titles {
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
