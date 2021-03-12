<template>
   <div class="ChatContent" :style="getTheme ? { backgroundColor: '#454150' } : { backgroundColor: '#909090' }"
        :key="$route.fullPath">
      <div class="user-info-content"
           :style="getTheme ? { backgroundColor: '#454150' } : { backgroundColor: '#909090' }">
         <span>{{ $route.query.name }}</span>
         <div class="room-icons">
            <button id="pin" @click="switchPinDisplay"><img src="../assets/pin.png" alt="pin"></button>
         </div>
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

      <div class="conversation-content">

         <Message v-for="message in messages" :key="message" :content="message.content"
                  :user-logo="message.sender.charAt(0).toUpperCase()"
                  :belong-to-myself="message.sender === user" :message-date="message.messageDate"/>

      </div>
      <div class="writing-content" v-if="writers.length > 0 && writers.length < 4">
         <span :key="items" v-for="items in writers">
            {{ items }} {{ writers.length > 1 ? "," : "" }}
         </span>
         {{ (writers.length > 1 ? "sont" : "est") + " en train d'écrire..." }}
      </div>
      <div class="writing-content" v-else-if="writers.length >= 4">
         Plusieurs personnes sont en train d'écrire.
      </div>
      <div class="input-content">
         <input type="text" :placeholder="`Envoyer un message à ${ $route.query.name }`"
                @keypress="actionInput">
         <div class="right-side-input">
            ☺
            <button @click="user = 'Dylan'">
               +
            </button>
         </div>
      </div>
   </div>
</template>

<script>
import Message from "@/components/common/Message";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {mapGetters} from "vuex";
import axios from 'axios';

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
         writers: [],
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
      actionInput(event) {
         if (event.keyCode === 13 && document.querySelector(".input-content > input").value !== "") {
            this.send();
            this.writers.pop(this.user);
         } else if (event.keyCode !== 13) {
            this.writing();
         }
      },
      writing() {
         if (!this.writers.includes(this.user)) {
            stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {  }, JSON.stringify({
               sender: this.user,
               type: "WRITING"
            }));
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
            if (message.type === "MESSAGE") {
               this.messages.unshift({
                  content: message.content,
                  sender: message.sender,
                  messageDate: message.messageDate,
                  pinned: message.pinned,
                  // messageReactions: message.messageReactions
               })
               this.writers.pop(message.sender)
            } else if (message.type === "WRITING") {
               if (!this.writers.includes(message.sender)) {
                  this.writers.push(message.sender)
                  setTimeout(() => {
                     this.writers.pop(message.sender)
                  }, 5000)
               }
            }
         }
      },
      send() {
         let messageContent = document.querySelector(".input-content > input");

         if (messageContent && stompClient) {
            let date = new Date();
            date = date.toLocaleString('fr-FR', {timeZone: 'UTC'}).substr(0, 17).replace(",", " -");
            date = date.substr(0, 13) + ((Number(date.substr(13, 2)) + 1) % 24) + date.substr(15, 3);

            let chatMessage = {
               ID: 10,
               content: messageContent.value,
               sender: this.user,
               messageDate: date,
               pinned: false,
               convUUID: this.getCurrentConv,
               // messageReactions: []

               type: "MESSAGE"
            };

            this.messages.unshift(chatMessage);
            stompClient.send(`/conversationListener/${this.getCurrentConv}/room.send`, { }, JSON.stringify(chatMessage));
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
      ...mapGetters(['getColors', 'getTheme', 'getCurrentConv'])
   }
}
</script>

<style scoped>
.ChatContent {
   width: calc(79% - 40px);
   height: calc(100% - 40px);
   border-radius: 12px;
   background-color: #454150;

   padding: 20px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;

   animation: appear-opacity ease-in-out 0.8s;
}

.user-info-content {
   height: 3%;
   width: 100%;

   display: flex;
   justify-content: space-between;
   align-items: center;

   color: #F4F4F4;
   font-size: 19px;
   font-weight: 600;

   background-color: #454150;
}

.input-content {
   width: 100%;
   height: 6%;
   border-radius: 100px;

   background-color: #F4F4F4;

   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
}

.right-side-input {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
   padding-right: 5px;

   font-size: 30px;
}

.conversation-content {
   position: relative;
   display: flex;
   flex-direction: column-reverse;

   padding-right: 6px;

   width: calc(100% + 4px);

   height: 77%;

   overflow-y: auto;
}

.input-content > input {
   height: 80%;
   width: 70%;
   background: none;

   padding-left: 20px;
   font-size: 14px;
   color: #454150;
   font-weight: 600;

   border: none;
   outline: none;
}

.input-content > input::placeholder {
   color: #909090;
}

.right-side-input > button {
   display: flex;
   align-items: center;
   justify-content: center;

   background-color: #909090;
   width: 35px;
   height: 35px;
   border-radius: 100px;

   cursor: pointer;

   color: #F4F4F4;
   font-size: 30px;
   font-weight: 600;

   border: none;
   outline: none;
}

.right-side-input > button:hover {
   background-color: #E85C5C;
   transform: rotate(90deg);
   transition: transform 0.7s;
}

.conversation-content::-webkit-scrollbar {
   width: 3px;
}


.conversation-content::-webkit-scrollbar-thumb {
   background: #909090;
   border-radius: 15px;
}

#pin > img {
   width: 20px;
   height: 20px;
}

#pin {
   outline: none;
   background: none;
   border: none;

   cursor: pointer;
}

#pin:hover {
   transform: scale(1.1) rotate(-10deg);
}

.pinned-message {
   animation: appear-opacity ease-in-out 0.5s;

   position: absolute;

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
