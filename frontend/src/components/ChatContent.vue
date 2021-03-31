<template>
   <div class="ChatContent"
        :key="$route.fullPath">
      <div class="conv-info">
         <div>
            <span style="font-size: 23px; font-weight: 500; color: #F4F4F4">{{ $route.query.name }}</span>
            <div class="top-right-buttons">
               <button :style="pinAdd ? { animation: '' } : { animation: 'none' }" style="height: 100%;" @click="switchPinDisplay">
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
                     <button class="unpin-message" @click="unpinMessage(messages.id)">
                        X
                     </button>
                  </div>
               </div>
            </div>

         </div>
      </div>
      <div class="conv-messages">
         <span v-if="messages.length === 0"
               style="align-self: center; font-size: 15px; font-weight: 500; color: #F4F4F4"> Soyez le premier à envoyer un message à {{
               $route.query.name
            }}</span>
         <Message :key="message" v-for="message in messages"
                  :content="message.content"
                  :user-logo="message.sender.charAt(0).toUpperCase()"
                  :belong-to-myself="message.sender === user"
                  :message-date="message.messageDate"
                  :message-i-d="message.id"
                  :is-edited="message.edited"
                  @pinnedMessage="pinnedMessage"
                  @deletedMessage="deletedMessage"
                  @editedMessage="editedMessage"
         />
      </div>
      <div class="conv-input">
         <span>
            {{ getWritersAsText() }}
         </span>
         <div>
            <input type="text" autocomplete="off" :placeholder="`Envoyer un message à ${ $route.query.name }`"
                   @keypress="actionInput">
            <div class="right-side-input">
               <button style="height: 32px; width: 32px;">
                  <img src="../assets/happy.svg" alt="Smiley">
               </button>
               <button class="submit-file">
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
import marked from "marked";
import Message from "@/components/common/Message";
import emojis from "@/assets/emojis_uncathegorized";

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
         user: "",
         writers: [],
         showPinned: false,
         pinAdd: false,
      }
   },
   mounted() {
      this.user = this.getUser.username;
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
      getWritersAsText() {
         if (this.writers.length === 0)
            return "";
         if (this.writers.length === 1)
            return `${this.writers[0]} est en train d'écrire...`
         if (this.writers.length > 4)
            return "Plusieurs personnes sont en train d'écrire";
         let res = "";
         this.writers.forEach(elt => {
            res += (elt === this.writers[this.writers.length - 1] ? `et ${elt}` : `${elt}${elt === this.writers[this.writers.length - 2] ? "" : ","} `)
         });
         return `${res} sont en train d'écrire...`;
      },
      actionInput(event) {
         if (event.keyCode === 13 && document.querySelector(".conv-input > div > input").value !== "") {
            this.send();
            this.writers.pop(this.user);
         } else if (event.keyCode !== 13) {
            this.writing();
            const inputContent = document.querySelector(".conv-input > div > input");
            inputContent.value = this.displayMessage(inputContent.value, false, true, false);
         }
      },
      writing() {
         if (!this.writers.includes(this.user)) {
            stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
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
                  convUUID: message.convUUID,
                  id: message.id,
                  content: message.content,
                  sender: message.sender,
                  messageDate: message.messageDate,
                  pinned: message.pinned,
                  edited: false,
                  // TODO : Add messageReactions implementation.
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
            } else if (message.type === "PINNED") {
               this.messages.forEach(elt => {
                  if (elt.id === message.id && !this.pinned.includes(elt))
                     this.pinned.push(elt);
               });
            } else if (message.type === "UNPINNED") {
               this.pinned = this.pinned.filter(elt => elt.id !== message.id);
            } else if (message.type === "DELETED") {
               this.messages = this.messages.filter(elt => elt.id !== message.id);
               this.pinned = this.pinned.filter(elt => elt.id !== message.id);
            } else if (message.type === "EDITED") {
               this.messages.forEach(elt => {
                  if (elt.id === message.id) {
                     elt.content = message.content;
                     elt.edited = true;
                  }
               });
               this.pinned.forEach(elt => {
                  if (elt.id === message.id) {
                     elt.content = message.content;
                     elt.edited = true;
                  }
               });
            }
         }
      },
      send() {
         let messageContent = document.querySelector(".conv-input > div > input");

         if (messageContent && stompClient) {
            let date = new Date();
            date = date.toLocaleString('fr-FR', {timeZone: 'UTC'}).substr(0, 17).replace(",", " -");
            date = date.substr(0, 13) + ((Number(date.substr(13, 2)) + 1) % 24) + date.substr(15, 3);

            let chatMessage = {
               id: this.messages.length,
               content: messageContent.value,
               sender: this.user,
               messageDate: date,
               pinned: false,
               edited: false,
               convUUID: this.getCurrentConv,
               // messageReactions: []

               type: "MESSAGE"
            };

            this.messages.unshift(chatMessage);
            stompClient.send(`/conversationListener/${this.getCurrentConv}/room.send`, {}, JSON.stringify(chatMessage));
            messageContent.value = '';
         }
      },
      getMessagesFromJSON() {
         axios.get(`http://localhost:8080/api/messages?uuid=${this.getCurrentConv}`).then(response => {
            this.messages = response.data.messages;
            this.pinned = this.messages.filter(elt => (elt.pinned === true));
         });

      },
      switchPinDisplay() {
         this.showPinned = !this.showPinned;
      },
      unpinMessage(messageID) {
         axios.put(`http://localhost:8080/api/room/unpinMessage/${this.getCurrentConv}?messageID=${messageID}`).then(() => {
            let c = 0;
            this.pinned.forEach(elt => {
               if (elt.id === messageID)
                  this.pinned.splice(c, 1);
               c++;
            })
            stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
               id: messageID,
               type: "UNPINNED"
            }));
         })
      },
      pinnedMessage(messageID) {
         this.messages.forEach(elt => {
            if (elt.id === messageID && !this.pinned.includes(elt)) {
               stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
                  id: messageID,
                  type: "PINNED"
               }));
               this.pinned.push(elt);
            }
         })
      },
      deletedMessage(messageID) {
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            id: messageID,
            type: "DELETED"
         }));
      },
      editedMessage(messageID, content) {
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            id: messageID,
            content: content,
            type: "EDITED"
         }));
      },

      filterEmoji(content){
         // Regex to match with the emoji string encode ( ':xxxxx_xxx_xxx_xxx:' where '_' is optionnal )
         const regex = ":[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*:";
         const emoji = [...content.matchAll(regex)];
         if(emoji && emoji.length > 0) {
            emoji.forEach(elt => {
               if (emojis[elt[0].replaceAll(":", "")])
                  content = content.replace(elt[0], emojis[elt[0].replaceAll(":", "")]);
            })
         }
         return content;
      },
      filterMarkdown(content){
         return marked(content);
      },
      filterPing(content) {
         if (content.includes(`@${this.getUser.username}`)) {
            this.mention = true;
            return content.replaceAll(`@${this.getUser.username}`, `@${this.getUser.name}`);
         }
         return content;
      },
      displayMessage(content, mardkdown, emojis, ping) {
         if (ping)
            content = this.filterPing(content);
         if (emojis)
            content = this.filterEmoji(content);
         if (mardkdown)
            content = this.filterMarkdown(content);
         return content;
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
   justify-content: space-between;
   align-items: center;
}

.conv-info > div {
   position: relative;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
   margin: auto;

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
   flex-direction: column;
   align-items: center;
   justify-content: center;
}

.conv-input > div {
   width: 90%;
   height: 46px;
   background-color: #F4F4F4;
   border-radius: 100px;

   display: flex;
   align-items: center;
   justify-content: space-between;

   padding-left: 15px;
   padding-right: 4px;
}

.conv-input > span {
   color: #F4F4F4;
   font-size: 12px;
   width: 90%;
   padding-left: 15px;
   height: 20px;
   margin-bottom: 5px;

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
   height: 38px;
   width: 38px;
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

   padding: 10px 10px 0 10px;
   border-radius: 12px;
   right: 36px;
   top: 70px;
   min-width: 350px;
   background-color: #909090;
}

.pinned-message > div {
   position: relative;
   width: 100%;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
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
   position: relative;
   padding: 10px;
   margin-bottom: 10px;
   width: 100%;
   background-color: #C4C4C4;
   color: #454150;
   border-radius: 12px;
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

.unpin-message {
   position: absolute;
   right: 5px;
   top: 5px;
   height: 20px;
   width: 20px;
   border-radius: 29px;
   background-color: #454150;
   color: #f4f4f4;
   cursor: pointer;
}

.unpin-message:hover {
   transform: scale(1.1);
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
