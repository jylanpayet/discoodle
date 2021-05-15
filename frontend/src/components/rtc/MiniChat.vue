<template>
   <div class="MiniChat">
      <div class="infos">
         Chat
         <w-button
            v-if="getUser.role === 'TEACHER'"
            bg-color="info"
            plain
            @click="$emit('showUser')"
         >
            Utilisateurs
         </w-button>
      </div>
      <div class="messages">
         <div class="message" :key="message" v-for="message in messages">
            <span :style="{ color: message.color, fontWeight: '600' }">
               {{ message.user }}
               <i style="color: #909090; font-size: 10px; font-weight: 500">{{ message.date }}</i>
            </span>
            <p style="font-size: 14px;">
               {{ message.content }}
            </p>
         </div>
      </div>
      <div class="question" v-if="question.show">
         <span style="color: #F4F4F4; font-size: 14px">Question : {{ question.form.question }}</span>
         <div style="min-height: calc(100% - 15px); width: 100%;">
            <w-checkboxes
               v-if="canAnswer"
               v-model="quizReponse"
               :items="question.form.answer.map(e => {
                  return {
                     label: e.prop,
                     value: e,
                  }
               })"
            >

            </w-checkboxes>
            <w-alert style="margin: 5px auto 5px auto; width: 90%;" :warning="quizAlert.warning" :success="quizAlert.success" plain v-if="question.form.multiple">{{ quizAlert.message }}</w-alert>
         </div>
      </div>
      <w-form class="input" @success="send">
         <w-input
            bg-color="white"
            color="grey-dark5"
            v-model="input"
            autocomplete="off"
            :static-label="true"
            :disabled="room_id === 'DEFAULT'"
         >
            Envoyer un message
         </w-input>
      </w-form>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

let stompClient = null;

export default {
   name: "MiniChat",
   props: {
      room_id: {
         type: String,
         required: true
      }
   },

   data() {
      return {
         messages: [],
         input: "",
         color: "",
         question: {
            show: false,
            form: null
         },
         quizReponse: [],
         quizAlert: {
            warning: true,
            success: false,
            message: "Choix multiples"
         },
         canAnswer: true,
      }
   },

   methods: {

      randomColor() {
         return ["#5CE8AD", "#E85CC5", "#5C8BE8", "#E8C05C", "#E85C5C"][Math.floor(Math.random() * 5)];
      },

      connect() {
         let ws = new SockJS("http://localhost:8080/ws");
         stompClient = Stomp.over(ws);
         // Comment the next line if you want to show websocket's logs
         stompClient.debug = null

         stompClient.connect({}, () => {
            stompClient.subscribe(`/conversations/rooms/${this.room_id}`, (sdkEvent) => {
               this.onMessageReceived(sdkEvent);
            })
         }, this.errorCallBack);
      },
      errorCallBack() {
         setTimeout(() => {
            this.connect();
         }, 5000);
      },
      disconnect() {
         if (stompClient !== null) {
            stompClient.disconnect();
         }
      },
      onMessageReceived(payload) {
         const message = JSON.parse(payload.body);
         if (message.type === "MESSAGE") {
            this.messages.unshift({
               content: message.content,
               date: message.date,
               user: message.user,
               color: message.color
            });
         } else {
            console.log(message);
            this.question.show = true;
            this.question.form = message.form;
            setTimeout(() => {
               // Time to answer
               this.canAnswer = false;
               if (this.arrayEquals(this.quizReponse.filter(e => e.need).map(e => e.id).sort((a, b) => a - b), this.question.form.answer.filter(e => e.need).map(e => e.id).sort((a, b) => a - b))) {
                  this.quizAlert.success = true;
                  this.quizAlert.message = "Bonne(s) réponse(s) !";
               } else {
                  this.quizAlert.warning = true;
                  this.quizAlert.message = "Mauvaise(s) réponse(s)"
               }
               setTimeout(() => {
                  this.question.show = false;
                  this.quizAlert.warning = true;
                  this.quizAlert.success = false;
                  this.quizAlert.message = "Choix multiples";
                  this.canAnswer = true;
               }, 5000);
            }, 10000);
         }
      },
      send() {
         if (this.input !== "") {
            stompClient.send(`/conversations/rooms/${this.room_id}`, {}, JSON.stringify({
               content: this.input,
               date: new Date().toLocaleTimeString().substring(0, 5),
               user: this.getUser.name || "Invité",
               color: this.color,
               type: "MESSAGE"
            }));
            this.input = "";
         }
      },
      addQuestion(question) {
         stompClient.send(`/conversations/rooms/${this.room_id}`, {}, JSON.stringify({
            form: question,
            type: "QUESTION"
         }));
      },

      arrayEquals(a, b) {
         return a.length === b.length &&
               a.every((val, index) => val === b[index]);
      }
   },

   computed: {
      ...mapGetters(['getUser'])
   },

   updated() {
      this.connect();
   },
   mounted() {
      this.color = this.randomColor();
      this.connect();
   },
   unmounted() {
      console.log("CHAT UNMOUTNED");
      this.disconnect();
   }
}
</script>

<style scoped>
.MiniChat {
   max-height: 600px;
   max-width: 300px;
   height: 100%;
   width: 100%;
   background-color: #4f4b5a;
   border-radius: 5px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;

   padding: 10px;
}

.infos {
   min-height: 40px;
   height: 8%;
   width: 90%;
   font-size: 20px;
   font-weight: 600;
   color: #F4F4F4;

   display: flex;
   align-items: center;
   justify-content: space-around;
   border-bottom: solid 2px #F4F4F4;
}

.messages {
   height: 410px;
   overflow-y: auto;
   width: 100%;
   display: flex;
   flex-direction: column-reverse;
   align-items: flex-start;
   justify-content: flex-start;
}

.message {
   margin-top: 10px;
   width: 100%;

   color: #F4F4F4;
}

.input {
   max-height: 40px;

   color: #F4F4F4;

   width: 100%;
   border-radius: 2px;
}

.question {
   width: 100%;
   background-color: #5d5d5d;
   border-radius: 2px;
   padding: 5px;
   margin-top: 10px;

   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: flex-start;
}

.question * {
   font-size: 12px;
   color: #F4F4F4;
}
</style>