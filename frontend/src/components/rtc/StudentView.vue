<template>
   <div class="StudentView">
      <div class="stream">
         <video class="video-content" v-if="stream" :srcObject.prop="stream" autoplay muted/>
         <div class="video-content no-stream" v-else>
            Le stream n'as pas commencé
         </div>
      </div>

      <div class="mini-chat">
         <MiniChat ref="mini-chat" :room_id="stream_id ? stream_id : 'DEFAULT'"/>
      </div>
   </div>
</template>

<script>
import firebase from 'firebase/app';
import 'firebase/firestore';
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {mapGetters} from "vuex";
import MiniChat from "@/components/rtc/MiniChat";


const firebaseConfig = {
   apiKey: "AIzaSyB1nMvRom2_rglfpi02kcZ6TIgGDkmKgBU",
   authDomain: "discoodle-rtc.firebaseapp.com",
   projectId: "discoodle-rtc",
   storageBucket: "discoodle-rtc.appspot.com",
   messagingSenderId: "5609697082",
   appId: "1:5609697082:web:e53778a4261f5a531e2a03",
   measurementId: "G-DCH8KRF07S"
};

if (!firebase.apps.length) {
   firebase.initializeApp(firebaseConfig);
}

const firestore = firebase.firestore();

let stompClient = null;

export default {
   name: "StudentView",
   components: {MiniChat},

   props: {
      rights: null
   },


   computed: {
      ...mapGetters(['getUser', 'getGroup'])
   },

   data() {
      return {
         peer_connection: null,
         servers: {
            iceServers: [
               {
                  urls: ['stun:stun1.l.google.com:19302', 'stun:stun2.l.google.com:19302'],
               },
            ],
            iceCandidatePoolSize: 10,
         },

         stream: null,
         joinedStream: false,
         stream_id: null
      }
   },

   methods: {
      connect() {
         let ws = new SockJS("http://localhost:8080/ws");
         stompClient = Stomp.over(ws);
         // Comment the next line if you want to show websocket's logs
         // stompClient.debug = null

         stompClient.connect({}, () => {
            stompClient.subscribe(`/conversations/stream_chat/${this.getGroup.groups_id}`, (sdkEvent) => {
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
         const obj = JSON.parse(payload.body);
         if (obj.info === "STREAM_STARTED" && !this.joinedStream) {
            console.log(obj);

            // TODO rejoidnre le stream
            this.joinStream(obj.callID);

            this.joinedStream = true;
         } else if (obj.info === "STREAM_STOPPED") {
            console.log(obj);
            this.stream = null;
            this.joinedStream = false;
         }
      },
      send(info) {
         stompClient.send(`/conversations/stream_chat/${this.getGroup.groups_id}`, {}, JSON.stringify({
            info: info.type,
            user_id: info.user_id
         }))
      },


      async joinStream(id) {
         const callId = id;
         this.stream_id = id;

         this.stream = new MediaStream();

         const callDoc = firestore.collection('calls').doc(callId);
         const answerCandidates = callDoc.collection('answerCandidates');
         const offerCandidates = callDoc.collection('offerCandidates');

         this.peer_connection.onicecandidate = (event) => {
            event.candidate && answerCandidates.add(event.candidate.toJSON());
         };

         console.log((await callDoc.get()));
         const callData = (await callDoc.get()).data();

         if (!callData.offer) {
            setTimeout(() => {
               this.joinStream(id);
            }, 5000);
         } else {
            const offerDescription = callData.offer;
            await this.peer_connection.setRemoteDescription(new RTCSessionDescription(offerDescription));

            const answerDescription = await this.peer_connection.createAnswer();
            await this.peer_connection.setLocalDescription(answerDescription);

            const answer = {
               type: answerDescription.type,
               sdp: answerDescription.sdp,
            };

            await callDoc.update({answer});

            offerCandidates.onSnapshot((snapshot) => {
               snapshot.docChanges().forEach((change) => {
                  if (change.type === 'added') {
                     let data = change.doc.data();
                     this.peer_connection.addIceCandidate(new RTCIceCandidate(data));
                  }
               });
            });
         }
      }
   },

   mounted() {
      console.log("MOUNTED");
      this.peer_connection = new RTCPeerConnection(this.servers);
      this.peer_connection.ontrack = event => {
         console.log(event);
         event.streams[0].getTracks().forEach(track => {
            this.stream.addTrack(track);
         })
      }
      this.connect();
      const user_id = this.getUser.id;
      setTimeout(() => {
         this.send({
            type: "IS_STREAMING",
            user_id: user_id
         });
      }, 1000);
   },
   unmounted() {
      this.disconnect();
   }
}
</script>

<style scoped>
.StudentView {
   width: 100%;
   height: 100%;

   display: flex;
   align-items: center;
   justify-content: space-between;
}

.stream {
   height: 100%;
   width: 65%;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-evenly;
}

.no-stream {
   --prim: #2d2d2d;
   --second: #212432;
   animation: 4s ease-in-out 1s infinite running changeColor;
   background-color: #2d2d2d;
   color: #F4F4F4;
   font-weight: 600;
   font-size: 18px;
   height: 400px;

   display: flex;
   align-items: center;
   justify-content: center;
}

.video-content {
   width: 100%;
   height: 350px;
   max-height: 350px;
   border-radius: 5px;
}

.mini-chat {
   height: 100%;
   width: 30%;
}


@keyframes changeColor {
   0% {
      background-color: var(--prim);
   }
   50% {
      background-color: var(--second);
   }
   100% {
      background-color: var(--prim);
   }
}
</style>