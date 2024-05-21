  <template>
    <div class="login-container">
      <form class="form-container" @submit.prevent="login">
        <h1>Login</h1>
        <input class="input-login" v-model="username" type="text" id="username" name="username" placeholder="Username">
        <button class="button-login" type="submit">Login</button>
      </form>
    </div>
  </template>

  <script>
  export default {
    name: 'LoginPage',
    data() {
      return {
        username: ''
      };
    },
    methods: {
      login() {
        if (this.username.trim()) {
          console.log('Username:', this.username);
          // Redirect to the chat page with the username as a route parameter
          this.$router.push({ name: 'ChatPage', params: { username: this.username } });
        } else {
          alert('Please enter a username');
        }
      }
    }
  };
  </script>

  <style scoped>
  .login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 50vh;
  }
  .form-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 1px solid #ccc;
    padding: 20px;
    width: 300px;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  }
  .label-login {
    margin-bottom: 10px;
  }
  .input-login {
    margin-bottom: 10px;
    padding: 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 16px;
  }
  .button-login {
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    font-size: 16px;
  }
  </style>
