<template>
   <div class="Planning">
      <div class="webcams">
         <video src="" id="webcamVideo" autoplay playsinline>

         </video>

         <video src="" id="remoteVideo" autoplay playsinline>

         </video>
      </div>

      <button class="webcamButton" @click="startWebcam">
         Webcam
      </button>
      <button class="screenshareButton" @click="startScreenshare">
         Partage d'écran
      </button>

      <br><br>

      <button class="callButton" @click="createCall" disabled>
         Démarrer le stream
      </button>

      <br><br>

      <input type="text" id="callInput" v-model="model.callInput">
      <button class="answerButton" disabled @click="answerCall">
         Rejoindre le stream
      </button>

      <br><br>

      <button @click="logInfo">
         Afficher les informations
      </button>
   </div>
</template>

<script>
import firebase from 'firebase/app';
import 'firebase/firestore';

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


export default {
   name: "Planning",
   components: {},
   methods: {
      async startWebcam() {
         this.localStream = await navigator.mediaDevices.getUserMedia({
            video: true,
            audio: true
         })

         this.remoteStream = new MediaStream();

         this.localStream.getTracks().forEach((track) => {
            this.pc.addTrack(track, this.localStream);
         });

         this.pc.ontrack = event => {
            event.streams[0].getTracks().forEach(track => {
               this.remoteStream.addTrack(track);
            })
         }

         document.querySelector("#webcamVideo").srcObject = this.localStream;
         document.querySelector("#remoteVideo").srcObject = this.remoteStream;

         document.querySelector(".callButton").disabled = false;
         document.querySelector(".answerButton").disabled = false;
      },

      async startScreenshare() {
         this.localStream = await navigator.mediaDevices.getDisplayMedia({
            video: true,
            audio: true
         });

         this.remoteStream = new MediaStream();

         this.localStream.getTracks().forEach((track) => {
            this.pc.addTrack(track, this.localStream);
         });

         this.pc.ontrack = event => {
            event.streams[0].getTracks().forEach(track => {
               this.remoteStream.addTrack(track);
            })
         }

         document.querySelector("#webcamVideo").srcObject = this.localStream;
         document.querySelector("#remoteVideo").srcObject = this.remoteStream;

         document.querySelector(".callButton").disabled = false;
         document.querySelector(".answerButton").disabled = false;
      },

      async createCall() {
         const callDoc = firestore.collection('calls').doc();
         const offerCandidates = callDoc.collection('offerCandidates');
         const answerCandidates = callDoc.collection('answerCandidates');

         this.model.callInput = callDoc.id;

         this.pc.onicecandidate = (event) => {
            event.candidate && offerCandidates.add(event.candidate.toJSON());
         };

         const offerDescription = await this.pc.createOffer();
         await this.pc.setLocalDescription(offerDescription);

         const offer = {
            sdp: offerDescription.sdp,
            type: offerDescription.type,
         };

         await callDoc.set({ offer });

         callDoc.onSnapshot((snapshot) => {
            const data = snapshot.data();
            if (!this.pc.currentRemoteDescription && data?.answer) {
               const answerDescription = new RTCSessionDescription(data.answer);
               this.pc.setRemoteDescription(answerDescription);
            }
         });

         answerCandidates.onSnapshot((snapshot) => {
            snapshot.docChanges().forEach((change) => {
               if (change.type === 'added') {
                  const candidate = new RTCIceCandidate(change.doc.data());
                  this.pc.addIceCandidate(candidate);
               }
            });
         });

      },

      async answerCall() {
         const callId = this.model.callInput;
         console.log(callId);

         const callDoc = firestore.collection('calls').doc(callId);
         const answerCandidates = callDoc.collection('answerCandidates');
         const offerCandidates = callDoc.collection('offerCandidates');

         this.pc.onicecandidate = (event) => {
            event.candidate && answerCandidates.add(event.candidate.toJSON());
         };

         const callData = (await callDoc.get()).data();

         const offerDescription = callData.offer;
         await this.pc.setRemoteDescription(new RTCSessionDescription(offerDescription));

         const answerDescription = await this.pc.createAnswer();
         await this.pc.setLocalDescription(answerDescription);

         const answer = {
            type: answerDescription.type,
            sdp: answerDescription.sdp,
         };

         await callDoc.update({ answer });

         offerCandidates.onSnapshot((snapshot) => {
            snapshot.docChanges().forEach((change) => {
               if (change.type === 'added') {
                  let data = change.doc.data();
                  this.pc.addIceCandidate(new RTCIceCandidate(data));
               }
            });
         });
      },

      logInfo() {
         navigator.mediaDevices.enumerateDevices().then(response => {
            console.log(response);
         });
         console.log(this.localStream, this.localStream.getTracks());
         console.log(this.remoteStream, this.remoteStream.getTracks());
      }
   },
   data() {
      return {
         pc: null,
         localStream: null,
         remoteStream: null,
         servers: {
            iceServers: [
               {
                  urls: ['stun:stun1.l.google.com:19302', 'stun:stun2.l.google.com:19302'],
               },
            ],
            iceCandidatePoolSize: 10,
         },

         model: {
            callInput: "",
         }
      }
   },
   mounted() {
      this.pc = new RTCPeerConnection(this.servers)
   },
   unmounted() {
      this.localStream?.getTracks().forEach(function(track) {
         if (track.readyState === 'live') {
            track.stop();
         }
      });

      this.remoteStream?.getTracks().forEach(function(track) {
         if (track.readyState === 'live') {
            track.stop();
         }
      });
   }
}
</script>

<style scoped>
.webcams {
   display: flex;
   align-items: center;
   justify-content: space-between;
   width: 100%;
   height: 400px;
   background-color: #4f4b5a;
}

button {
   height: 30px;
   width: 100px;
   background-color: #F4F4F4;
   color: #2d2d2d;
}

video {
   max-height: 100%;
   max-width: 50%;
}

button {
   background-color: #F4F4F4;
   color: #4f4b5a;
   border: none;
   outline: none;

   cursor: pointer;

   height: 30px;
}

button:hover {
   background-color: #E85C5C;
   color: #F4F4F4;
}

button[disabled] {
   background-color: #454545;
   color: #999999;
   cursor: not-allowed;
}
</style>
