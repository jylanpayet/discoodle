<template>
   <div class="ChatContent"
        :key="$route.fullPath">
      <div class="conv-info">
         <div>
            <span style="font-size: 23px; font-weight: 500; color: #F4F4F4">{{ $route.query.name }}</span>
            <div class="top-right-buttons">
               <button style="height: 100%;" @click="switchPinDisplay">
                  <img src="../assets/pin.png" alt="" style="height: 60%;">
               </button>
               <button style="color: #F4F4F4; font-size: 33px; font-weight: 500; height: 100%">
                  +
               </button>
            </div>

            <div class="pinned-message" v-if="showPinned">
               <div>
                  {{ pinned.length === 0 ? "Aucun message épinglé" : "" }}
                  <div class="pinned-message-content" :key="messages" v-for="messages in pinned">
                     {{ messages.sender }} : <br>
                     {{ messages.content }} <br>
                     {{ messages.messageDate }}
                  </div>
               </div>
            </div>

         </div>
      </div>
      <div class="conv-messages">
         <span v-if="messages.length === 0" style="align-self: center; font-size: 15px; font-weight: 500; color: #F4F4F4"> Soyez le premier à envoyer un message à {{ $route.query.name }}</span>
         <Message :key="message" v-for="message in messages"
                  :content="message.content"
                  :user-logo="message.sender.charAt(0).toUpperCase()"
                  :belong-to-myself="message.sender === user"
                  :message-date="message.messageDate"
                  />
      </div>
      <div class="conv-input">
         <div>
            <input type="text" :placeholder="`Envoyer un message à ${ $route.query.name }`" @keypress="isEnter">
            <div class="right-side-input">
               <button style="height: 32px; width: 32px;">
                  <img src="../assets/happy.svg" alt="Smiley">
               </button>
               <button class="submit-file" @click="user = 'Dylan'">
                  +
               </button>
            </div>
         </div>
      </div>
   </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {mapGetters} from "vuex";
import axios from 'axios';
import Message from "@/components/common/Message";

let stompClient = null;

export default {
   name: "ChatContent",
   components: {
      Message
   },
   data() {
      return {
         messages: [],
         pinned: [],
         user: "Bob",
         showPinned: false
      }
   },
   mounted() {
      this.getMessagesFromJSON();
      this.connect();
   },
   beforeRouteUpdate() {
      this.disconnect();
      this.getMessagesFromJSON();
      this.connect();
   },
   unmounted() {
      this.disconnect();
   },
   methods: {
      isEnter(event) {
         if (event.keyCode === 13 && document.querySelector(".conv-input > div > input").value !== "") {
            this.send();
         }
      },
      connect() {
         let ws = new SockJS("http://localhost:8080/ws");
         stompClient = Stomp.over(ws);
         // Comment the next line if you want to show websocket's logs
         stompClient.debug = null

         stompClient.connect({}, () => {
            stompClient.subscribe(`/conversations/rooms/${this.getCurrentConv}`, (sdkEvent) => {
               this.onMessageReceived(sdkEvent);
            })
         }, this.errorCallBack);
      },
      disconnect() {
         if (stompClient !== null) {
            stompClient.disconnect();
         }
      },
      errorCallBack() {
         setTimeout(() => {
            this.connect();
         }, 5000);
      },
      onMessageReceived(payload) {
         let message = JSON.parse(payload.body);

         if (message.sender !== this.user) {
            this.messages.unshift({
               content: message.content,
               sender: message.sender,
               messageDate: message.messageDate,
               pinned: message.pinned,
               // messageReactions: message.messageReactions
            })
         }
      },
      send() {
         let messageContent = document.querySelector(".conv-input > div > input");

         if (messageContent && stompClient) {
            let date = new Date();
            date = date.toLocaleString('fr-FR', {timeZone: 'UTC'}).substr(0, 17).replace(",", " -");
            date = date.substr(0, 13) + ((Number(date.substr(13, 2)) + 1) % 24) + date.substr(15, 3);

            let chatMessage = {
               content: messageContent.value,
               sender: this.user,
               messageDate: date,
               pinned: false,
               convUUID: this.getCurrentConv,
               // messageReactions: []
            };

            this.messages.unshift(chatMessage);
            stompClient.send(`/conversationListener/${this.getCurrentConv}/room.send`, {}, JSON.stringify(chatMessage));
            messageContent.value = '';
         }
      },
      getMessagesFromJSON() {
         axios.get(`http://localhost:8080/api/messages?uuid=${this.getCurrentConv}`).then(response => {
            this.messages = response.data.chatMessages;
            this.pinned = this.messages.filter(elt => (elt.pinned === true));
         });
      },
      switchPinDisplay() {
         this.showPinned = !this.showPinned;
      },

   },
   computed: {
      ...mapGetters(['getColors', 'getTheme', 'getCurrentConv', 'getUser'])
   }
}
</script>

<style scoped>
button {
   background: none;
   border: none;
   outline: none;
   cursor: pointer;
}

.ChatContent {
   width: 100%;
   height: 100%;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}

.conv-info {
   width: 100%;
   height: 80px;
   background-color: #18161F;

   display: flex;
   align-items: center;
   justify-content: center;
}

.conv-info > div {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;

   width: 95%;
   height: 60px;
}

.top-right-buttons {
   display: flex;
   align-items: center;
   justify-content: space-between;

   height: 40px;
   width: 65px;
}

.conv-input {
   height: 100px;
   width: 100%;

   display: flex;
   align-items: center;
   justify-content: center;
}

.conv-input > div {
   width: 90%;
   height: 50px;
   background-color: #F4F4F4;
   border-radius: 100px;

   display: flex;
   align-items: center;
   justify-content: space-between;

   padding-left: 15px;
   padding-right: 5px;
}

.conv-messages {
   height: calc(100% - 180px);
   width: 95%;
   align-self: center;

   display: flex;
   flex-direction: column-reverse;

   overflow-y: auto;
   overflow-x: hidden;
   padding-right: 8px;
}

.right-side-input {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;

   height: 44px;
   width: 90px;
}

.submit-file {
   background-color: #454150;
   height: 40px;
   width: 40px;
   color: #F4F4F4;
   font-size: 30px;
   font-weight: 600;

   display: flex;
   align-items: center;
   justify-content: center;

   border-radius: 50%;
}

.submit-file:hover {
   background-color: #E85C5C;
   color: #F4F4F4;
}

.conv-input > div > input {
   outline: none;
   border: none;
   background: none;

   max-width: calc(100% - 100px);
   width: 1000px;
   height: 35px;
   font-size: 15px;
   color: #454150;
}

.conv-input > div > input::placeholder {
   font-size: 15px;
   color: #909090;
}


.pinned-message {
   animation: appear-opacity ease-in-out 0.5s;

   position: absolute;

   z-index: 500;

   border-radius: 12px;
   right: 50px;
   max-width: 350px;
   min-width: 350px;
   transform: translateY(35px);

   min-height: 50px;
   background-color: #909090;
}

.pinned-message > div {
   position: relative;
   width: calc(100% - 20px);

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;

   margin: 10px 10px 0 10px;

}

.pinned-message > div::before {
   content: "";
   position: absolute;
   right: 0;
   top: 0;
   width: 13px;
   height: 13px;
   background-color: #909090;
   transform: rotate(45deg) translateY(-9px) translateX(-15px);
}

.pinned-message-content {
   padding: 10px;
   margin-bottom: 10px;
   width: calc(100% - 20px);
   background-color: #C4C4C4;
   color: #454150;
   border-radius: 12px;

   cursor: pointer;
}

@keyframes appear-opacity {
   from {
      /* transform: translateX(100%); */
      opacity: 0;
   }
   to {
      /* transform: translateX(0); */
      opacity: 1;
   }
}

</style>
