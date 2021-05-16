<template>
   <div class="Channel">
      <div class="infos">
         <div>
            <h2 class="name" style="color: #F4F4F4;">{{ room.room_name }}</h2>
            <div class="buttons">
               <button :style="pinAdd ? { animation: '' } : { animation: 'none' }" style="height: 100%;"
                       @click="showPinned = !showPinned;">
                  <img src="../../../../assets/pin.png" alt="">
               </button>
               <button style="cursor: pointer;" @click="showUserList = true;">
                  <img src="../../../../assets/user.svg" alt="">
               </button>
               <w-drawer
                     v-model="showUserList"
                     right
                     width="350px"
                     bg-color="grey-dark5"

                     style="z-index: 550"
               >
                  <div class="user-list">
                     <span style="font-size: 18px; color: #F4F4F4; font-weight: 600; margin-bottom: 30px;">Utilisateur(s) de la room :</span>
                     <div class="role" :key="role.id" v-for="role in getGroup.roles">
                        <span>{{ role.name }} - {{ role.users.length }}</span>
                        <div :key="user.id" class="user" v-for="user in role.users">
                           <img src="../../../../assets/crown.svg" style="height: 80%; margin-right: 10px;" alt=""
                                v-if="user.id === room.room_admin">
                           {{ user.name }} {{ user.last_name.toUpperCase() }}
                        </div>
                     </div>
                  </div>
               </w-drawer>
            </div>

            <w-dialog
                  :width="500"
                  v-model="showPinned"
                  style="overflow-y: auto;"
            >
               <div style="max-height: 70vh;">
                  <h3 style="margin-bottom: 10px;">
                     {{ pinned.length === 0 ? "Aucun message épinglé" : "Liste des messages épinglés :" }}</h3>
                  <w-divider style="margin-bottom: 10px;"></w-divider>
                  <div :key="messages" v-for="messages in pinned" class="message-pinned">
                     <div>
                        <strong style="text-decoration: underline">{{ messages.sender }}</strong> : <br>
                        {{ messages.content }} - {{ new Date(messages.message_date).toLocaleDateString() }} <br>
                     </div>
                     <button class="unpin-message" v-if="rights.canModifyRoom"
                             @click="unpinMessage(messages.message_id)">
                        X
                     </button>
                  </div>
               </div>
            </w-dialog>
         </div>
      </div>

      <div class="conv-messages" v-if="rights.canReadMessage">
         <span v-if="messages.length === 0"
               style="align-self: center; font-size: 15px; font-weight: 500; color: #F4F4F4"> Soyez le premier à envoyer un message à {{
               $route.query.name
            }}</span>
         <Message :key="message" v-for="message in messages"
                  :content="message.content"
                  :sender="message.sender"
                  :belong_to_myself="message.sender === getUser.username"
                  :message_date="message.message_date"
                  :message_id="message.message_id"
                  :edited="message.edited"
                  :can-pin="rights.canModifyRoom"
                  :can-remove="rights.canModifyRoom"
                  @pinnedMessage="pinnedMessage"
                  @deletedMessage="deletedMessage"
                  @editedMessage="editedMessage"
         />
      </div>

      <div class="conv-messages" v-else>
         <span style="align-self: center; font-size: 17px; font-weight: 600; color: #F4F4F4">Vous n'avez pas la permission de lire les messages.</span>
      </div>

      <div class="conv-input">
         <span>
            {{ getWritersAsText() }}
         </span>
         <div v-if="rights.canSendMessage">
            <input type="text" autocomplete="off" :placeholder="`Envoyer un message à ${ $route.query.name }`"
                   @keydown="actionInput">
            <div class="right-side-input">
               <EmojiPicker @selected-emoji="insertEmoji" @closeEmoji="showEmoji = false" v-if="showEmoji"
                            class="emojiPicker"/>
               <button style="height: 28px; width: 28px;" @click="showEmoji = !showEmoji">
                  <img src="../../../../assets/happy.svg" alt="Smiley" style="width: 100%;">
               </button>
               <label class="submit-file" style="cursor: pointer;">
                  +
                  <input type="file" ref="uploadImage" @change="uploadImage()" style="height: 0; width: 0;">
               </label>
            </div>
         </div>
         <span v-else style="font-size: 17px; font-weight: 600">
            Vous n'avez pas la permission d'envoyer des messages.
         </span>
      </div>
   </div>
</template>

<script>
import axios from "axios";
import {mapGetters} from "vuex";
import marked from "marked";

import SockJS from "sockjs-client";
import Stomp from "stompjs";
import emojis from "@/assets/emojis_uncathegorized";
import Message from "@/components/common/Message";
import EmojiPicker from "@/components/common/EmojiPicker";

let stompClient = null;

export default {
   name: "Channel",
   components: {
      EmojiPicker,
      Message,
   },
   computed: {
      ...mapGetters(['getCurrentConv', 'getUser', 'getFriends', 'getGroup'])
   },

   data() {
      return {
         room: {},
         writers: [],
         messages: [],
         pinned: [],

         showUserList: false,
         pinAdd: false,
         showPinned: false,
         showEmoji: false,

         rights: {
            canSendMessage: false,
            canReadMessage: false,
            canChangeGroup: false,
            canModifyRoom: false,
            canModifyNotes: false,
            canStream: false,
         }
      }
   },

   methods: {
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

         if (message.sender !== this.getUser.username) {
            if (message.type === "MESSAGE") {

               this.messages.unshift({
                  convUUID: message.convUUID,
                  message_id: message.message_id,
                  content: message.content,
                  sender: message.sender,
                  message_date: message.message_date,
                  pinned: message.pinned,
                  edited: false,
               })
            } else if (message.type === "WRITING") {
               if (!this.writers.includes(message.sender)) {
                  this.writers.push(message.sender)
                  setTimeout(() => {
                     this.writers.pop(message.sender)
                  }, 5000)
               }
            } else if (message.type === "PINNED") {
               this.messages.forEach(elt => {
                  if (elt.message_id === message.message_id && !this.pinned.includes(elt))
                     this.pinned.push(elt);
               });
            } else if (message.type === "UNPINNED") {
               this.pinned = this.pinned.filter(elt => elt.message_id !== message.message_id);
            } else if (message.type === "DELETED") {
               this.messages = this.messages.filter(elt => elt.message_id !== message.message_id);
               this.pinned = this.pinned.filter(elt => elt.message_id !== message.message_id);
            } else if (message.type === "EDITED") {
               this.messages.forEach(elt => {
                  if (elt.message_id === message.message_id) {
                     elt.content = message.content;
                     elt.edited = true;
                  }
               });
               this.pinned.forEach(elt => {
                  if (elt.message_id === message.message_id) {
                     elt.content = message.content;
                     elt.edited = true;
                  }
               });
            } else if (message.type === "USERS_ADDED") {
               message.users.forEach(user => {
                  if (this.containUsername(user)) {
                     axios.get(`http://localhost:8080/api/users/findByUserName?username=${user}`).then(response => {
                        this.users.push(response.data)
                     })
                  }
               })
               setTimeout(() => {
                  this.getUserOfRoom()
               }, 500)
            } else if (message.type === "USER_REMOVED") {
               this.users = this.users.filter(elt => elt.id !== message.user_id);
               setTimeout(() => {
                  this.getUserOfRoom()
               }, 500)
               if (message.user_id === this.getUser.id)
                  location.replace("/messages")
            } else if (message.type === "CHANGE_ADMIN") {
               this.roomAdminID = message.user_id;
            }
         }
      },
      send() {
         let messageContent = document.querySelector(".conv-input > div > input");

         if (messageContent && stompClient) {
            axios.post(`http://localhost:8080/api/messages/sendMessage?room_uuid=${this.getCurrentConv}`, {
               conv_uuid: this.getCurrentConv,
               content: messageContent.value,
               sender: this.getUser.username,
               message_date: Date.now()
            }).then(response => {
               this.messages.unshift(response.data);
               stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify(response.data))
               messageContent.value = "";
            });
         }
      },
      writing() {
         if (!this.writers.includes(this.getUser.username)) {
            stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
               sender: this.getUser.username,
               type: "WRITING"
            }));
         }
      },


      promoteAdmin(user_id) {
         axios.put(`http://localhost:8080/api/rooms/changeAdmin?room_id=${this.getCurrentConv}&admin=${user_id}`)
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            user_id: user_id,
            type: "CHANGE_ADMIN"
         }));
      },
      unpinMessage(messageID) {
         axios.put(`http://localhost:8080/api/messages/unpinMessage?message_id=${messageID}`).then(() => {
            let c = 0;
            this.pinned.forEach(elt => {
               if (elt.message_id === messageID)
                  this.pinned.splice(c, 1);
               c++;
            })
            stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
               message_id: messageID,
               type: "UNPINNED"
            }));
         })
      },
      pinnedMessage(messageID) {
         this.messages.forEach(elt => {
            if (elt.message_id === messageID && !this.pinned.includes(elt)) {
               stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
                  message_id: messageID,
                  type: "PINNED"
               }));
               this.pinned.push(elt);
            }
         })
      },
      deletedMessage(messageID) {
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            message_id: messageID,
            type: "DELETED"
         }));
      },
      editedMessage(messageID, content) {
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            message_id: messageID,
            content: content,
            type: "EDITED"
         }));
      },
      async addUsers(users) {
         for (const user of users) {
            if (this.containUsername(user)) {
               await axios.get(`http://localhost:8080/api/users/findByUserName?username=${user}`).then(response => {
                  axios.post(`http://localhost:8080/api/rooms/addNewMember?room_id=${this.getCurrentConv}&user_id=${response.data.id}`)
               })
            }
         }
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            users: users,
            type: "USERS_ADDED"
         }));
      },
      removerUser(user_id) {
         axios.delete(`http://localhost:8080/api/rooms/removeMember?room_id=${this.getCurrentConv}&user_id=${user_id}`)
         stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify({
            user_id: user_id,
            type: "USER_REMOVED"
         }));
      },
      uploadImage() {
         let file = this.$refs.uploadImage.files[0];
         let temp = new FormData();
         temp.append("file", file);
         axios({
            url: `http://localhost:8080/api/uploadfile/uploadImageInChat?room_id=${this.getCurrentConv}`,
            method: 'POST',
            data: temp,
            headers: {
               Accept: 'application/json',
               'Content-type': 'multipart/form-data'
            }
         }).then(response => {
            if (response.data !== "L'extension n'est pas un fichier jpg ou png, il ne peut donc pas être upload" && response.data !== "Erreur lors du téléchargement de l'image !") {
               axios.post(`http://localhost:8080/api/messages/sendMessage?room_uuid=${this.getCurrentConv}`, {
                  conv_uuid: this.getCurrentConv,
                  content: `![${"Image de " + this.getUser.username}](${response.data})`,
                  sender: this.getUser.username,
                  message_date: Date.now()
               }).then(response => {
                  this.messages.unshift(response.data);
                  stompClient.send(`/conversations/rooms/${this.getCurrentConv}`, {}, JSON.stringify(response.data))
               });
            }
         })
      },
      insertEmoji(emoji) {
         document.querySelector(".conv-input > div > input").value += emoji;
      },

      filterEmoji(content) {
         // Regex to match with the emoji string encode ( ':xxxxx_xxx_xxx_xxx:' where '_' is optionnal )
         const regex = ":[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*:";
         const emoji = [...content.matchAll(regex)];
         if (emoji && emoji.length > 0) {
            emoji.forEach(elt => {
               if (emojis[elt[0].replaceAll(":", "")])
                  content = content.replace(elt[0], emojis[elt[0].replaceAll(":", "")]);
            })
         }
         return content;
      },
      filterMarkdown(content) {
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


      getUserOfRoom() {
         axios.get(`http://localhost:8080/api/rooms/findUserOfRoom?room_id=${this.getCurrentConv}`).then(response => {
            this.room.users = response.data;
         });
      },
      getMessagesFromJSON() {
         axios.get(`http://localhost:8080/api/messages?room_uuid=${this.getCurrentConv}`).then(response => {
            this.messages = response.data.sort((a, b) => b.message_date - a.message_date);
            this.pinned = this.messages.filter(elt => (elt.pinned === true));
         });
      },


      actionInput(event) {
         if (event.keyCode === 13 && document.querySelector(".conv-input > div > input").value !== "") {
            this.send();
            this.writers.pop(this.getUser.username);
         } else if (event.keyCode !== 13) {
            this.writing();
            const inputContent = document.querySelector(".conv-input > div > input");
            inputContent.value = this.displayMessage(inputContent.value, false, true, false);
         }
      },
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
      containUsername(username) {
         let bool = false;
         this.getFriends.forEach(elt => {
            if (elt.username === username)
               bool = true;
         })
         return bool;
      },
   },

   mounted() {
      axios.get(`http://localhost:8080/api/rooms/findRoomByID?room_uuid=${this.getCurrentConv}`).then(response => {
         this.room = response.data;
         this.getMessagesFromJSON();
         this.connect();
      })

      // Get rights of user in this group.
      axios.get(`http://localhost:8080/api/groups/getRoleByGroupAndUser?user_id=${this.getUser.id}&group_id=${this.getGroup.groups_id}`).then(response => {
         let fullRights = false;
         response.data.forEach(elt => {
            if (elt.rights === "*")
               fullRights = true;
         });

         if (fullRights) {
            this.rights = {
               canSendMessage: true,
               canReadMessage: true,
               canChangeGroup: true,
               canModifyRoom: true,
               canModifyNotes: true,
               canStream: true,
            }
         } else {
            response.data.sort((a, b) => {
               return a.rights.length - b.rights.length;
            });

            const temp = response.data[response.data.length - 1].rights;
            this.rights = {
               canSendMessage: temp.includes("s"),
               canReadMessage: temp.includes("r"),
               canChangeGroup: temp.includes("p"),
               canModifyRoom: temp.includes("c"),
               canModifyNotes: temp.includes("n"),
               canStream: temp.includes("l"),
            }
         }
      })
   },
   beforeRouteUpdate() {
      this.disconnect();
      axios.get(`http://localhost:8080/api/rooms/findRoomByID?room_uuid=${this.getCurrentConv}`).then(response => {
         this.room = response.data;
         this.getMessagesFromJSON();
      })
      this.connect();

      // Get rights of user in this group.
      axios.get(`http://localhost:8080/api/groups/getRoleByGroupAndUser?user_id=${this.getUser.id}&group_id=${this.getGroup.groups_id}`).then(response => {
         let fullRights = false;
         response.data.forEach(elt => {
            if (elt.rights === "*")
               fullRights = true;
         });

         if (fullRights) {
            this.rights = {
               canSendMessage: true,
               canReadMessage: true,
               canChangeGroup: true,
               canModifyRoom: true,
               canModifyNotes: true,
               canStream: true,
            }
         } else {
            response.data.sort((a, b) => {
               return a.rights.length - b.rights.length;
            });

            const temp = response.data[response.data.length - 1].rights;
            this.rights = {
               canSendMessage: temp.includes("s"),
               canReadMessage: temp.includes("r"),
               canChangeGroup: temp.includes("p"),
               canModifyRoom: temp.includes("c"),
               canModifyNotes: temp.includes("n"),
               canStream: temp.includes("l"),
            }
         }
      })
   },
   unmounted() {
      this.disconnect();
   },
}
</script>

<style scoped>
.Channel {
   width: 100%;
   height: 100%;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;
}

.infos {
   display: flex;
   align-items: center;
   justify-content: center;

   height: 50px;
   margin-bottom: 5px;
   width: 100%;
}

.infos > div {
   display: flex;
   align-items: center;
   justify-content: space-between;
   width: 90%;
   height: 100%;
}

.buttons {
   position: relative;
   display: flex;
   align-items: center;
   justify-content: space-between;

   height: 40px;
   width: 80px;
}

button {
   cursor: pointer;
   background: none;
   outline: none;
   border: none;

   height: 40px;
   width: 40px;
}

button > img {
   width: 60%;
}

button {
   background: none;
   border: none;
   outline: none;
   cursor: pointer;
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
   height: 38px;
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
   position: relative;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;

   height: 38px;
   width: 70px;
}

.message-pinned {
   display: flex;
   align-items: center;
   justify-content: space-between;
   margin-top: 10px;
}

.message-pinned > div {
   background-color: #4f4b5a;
   color: #F4F4F4;
   padding: 10px;
   width: 90%;

   border-radius: 4px;
}

.submit-file {
   background-color: #454150;
   height: 32px;
   width: 32px;
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
   height: 30px;
   font-size: 15px;
   color: #454150;
}

.conv-input > div > input::placeholder {
   font-size: 15px;
   color: #909090;
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
   height: 30px;
   width: 30px;
   font-weight: 600;
   font-size: 17px;
   border-radius: 29px;
   background-color: #454150;
   color: #f4f4f4;
   cursor: pointer;
}

.unpin-message:hover {
   transform: scale(1.1);
   background-color: #E85C5C;
}

.emojiPicker {
   position: absolute;
   top: -10px;
   left: 32px;
   transform: translateX(-100%) translateY(-100%);
}

.user-list {
   padding: 20px;
   width: 100%;
   height: 100%;

   display: flex;
   align-items: flex-start;
   justify-content: flex-start;
   flex-direction: column;
}

.user {
   position: relative;
   width: 100%;
   height: 40px;
   background-color: #8F8F8F;
   border-radius: 12px;

   display: flex;
   align-items: center;
   justify-content: flex-start;
   padding-left: 15px;

   color: #F4F4F4;
   font-size: 18px;
   font-weight: 600;
   margin-bottom: 10px;
}

.user > div {
   position: absolute;
   right: 20px;

   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
}

.user > div > button {
   font-size: 18px;
   width: 22px;
   height: 22px;
   font-weight: 600;
   color: #F4F4F4;
}

.role {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;

   width: 100%;
   color: #F4F4F4;
   font-size: 17px;
   font-weight: 600;
   margin-bottom: 10px;
}

.role > span {
   margin-bottom: 10px;
   width: 100%;
   height: 20px;
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