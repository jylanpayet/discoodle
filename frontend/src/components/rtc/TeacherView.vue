<template>
   <div class="TeacherView">
      <div class="stream">
         <video class="video-content" v-if="stream" :srcObject.prop="stream" autoplay muted/>
         <div class="video-content no-stream" v-else>
            Aucune source vidéo
         </div>

         <div class="controls">
            <div class="left-controls">
               <w-tooltip top>
                  <template #activator="{ on }">
                     <button
                           :class="sources.webcam.active ? 'active' : ''"
                           @click="webcam(sources.webcam.active)"
                           v-on="on"
                     >
                        <svg width="22" height="22" viewBox="0 0 29 29" fill="none" xmlns="http://www.w3.org/2000/svg">
                           <path d="M24.6443 20.7341C22.1725 23.5599 18.5504 25.3542 14.5001 25.3542C10.4497 25.3542 6.82656 23.5599 4.35469 20.7341C2.27987 22.222 1.01318 24.163 1.01318 26.294C1.01318 30.9626 7.05098 28.1724 14.5001 28.1724C21.947 28.1724 27.9869 30.9626 27.9869 26.294C27.9869 24.163 26.7202 22.222 24.6443 20.7341Z"
                                 fill="#F4F4F4"/>
                           <path d="M14.5 23.7369C21.0536 23.7369 26.3685 18.422 26.3685 11.8684C26.3685 5.31383 21.0536 0 14.5 0C7.94542 0 2.63159 5.31383 2.63159 11.8684C2.63159 18.422 7.94542 23.7369 14.5 23.7369ZM14.5 5.93422C17.7768 5.93422 20.4343 8.59383 20.4343 11.8684C20.4343 15.1431 17.7768 17.8027 14.5 17.8027C11.2222 17.8027 8.56581 15.1431 8.56581 11.8684C8.56581 8.59383 11.2222 5.93422 14.5 5.93422Z"
                                 fill="#F4F4F4"/>
                           <path d="M14.5001 16.1843C16.8824 16.1843 18.8159 14.2498 18.8159 11.8685C18.8159 9.48729 16.8824 7.55273 14.5001 7.55273C12.1167 7.55273 10.1843 9.48729 10.1843 11.8685C10.1843 14.2498 12.1167 16.1843 14.5001 16.1843ZM15.578 9.70416C16.1779 9.70416 16.6645 10.1908 16.6645 10.7907C16.6645 11.3906 16.1779 11.8782 15.578 11.8782C14.977 11.8782 14.4904 11.3906 14.4904 10.7907C14.4904 10.1908 14.977 9.70416 15.578 9.70416Z"
                                 fill="#F4F4F4"/>
                        </svg>
                     </button>
                  </template>
                  {{ sources.webcam.active ? "Arrêter la webcam" : "Démarrer la webcam" }}
               </w-tooltip>
               <w-tooltip top>
                  <template #activator="{ on }">
                     <button
                           :class="sources.screenshare.active ? 'active' : ''"
                           @click="screenshare(sources.screenshare.active)"
                           v-on="on"
                     >
                        <svg width="22" height="22" viewBox="0 0 31 31" fill="none" xmlns="http://www.w3.org/2000/svg">
                           <g clip-path="url(#clip0)">
                              <path d="M30.266 2.99048H0.732329C0.327893 2.99048 0 3.3183 0 3.72274V24.1039C0 24.508 0.327893 24.8354 0.732329 24.8354H11.8381V26.5918C11.8381 26.6178 11.8432 26.6413 11.8458 26.6664H10.2521C9.88161 26.6664 9.58086 26.9672 9.58086 27.3376C9.58086 27.7082 9.88161 28.0092 10.2521 28.0092H21.1138C21.4842 28.0092 21.785 27.7085 21.785 27.3376C21.785 26.9672 21.4842 26.6664 21.1138 26.6664H19.5202C19.5224 26.6421 19.5281 26.6178 19.5281 26.5918V24.8354H30.2676C30.6718 24.8354 31 24.5077 31 24.1039V3.72274C30.9988 3.3183 30.6711 2.99048 30.266 2.99048ZM28.9238 20.9091C28.9238 21.3136 28.5959 21.6415 28.1917 21.6415H2.80679C2.40263 21.6415 2.07467 21.3137 2.07467 20.9091V5.45547C2.07467 5.05131 2.40263 4.72334 2.80679 4.72334H28.1917C28.5959 4.72334 28.9238 5.05124 28.9238 5.45547V20.9091Z"
                                    fill="#F4F4F4"/>
                           </g>
                           <defs>
                              <clipPath id="clip0">
                                 <rect width="31" height="31" fill="white"/>
                              </clipPath>
                           </defs>
                        </svg>

                     </button>
                  </template>
                  {{ sources.screenshare.active ? "Arrêter le partage" : "Partager mon écran" }}
               </w-tooltip>
               <w-tooltip top>
                  <template #activator="{ on }">
                     <button
                           :class="sources.audio.active ? '' : 'active'"
                           @click="mute(sources.audio.active)"
                           :disabled="!(sources.webcam.active || sources.screenshare.active)"
                           v-on="on"
                     >
                        <svg width="22" height="22" viewBox="0 0 27 27" fill="none" xmlns="http://www.w3.org/2000/svg">
                           <path d="M25.65 0L18.5625 7.0875V5.0625C18.5625 2.19375 16.3687 0 13.5 0C10.6313 0 8.4375 2.19375 8.4375 5.0625V11.8125C8.4375 13.3313 9.1125 14.6812 10.125 15.525L8.775 16.875C7.59375 15.8625 6.75 14.3438 6.75 12.6562V8.4375C5.7375 8.4375 5.0625 9.1125 5.0625 10.125V12.6562C5.0625 14.85 6.075 16.7062 7.59375 18.0562L0 25.8188V27H1.18125L27 1.0125V0H25.65V0Z"
                                 fill="#F4F4F4"/>
                           <path d="M21.0938 8.6062L20.25 9.44995V12.6562C20.25 15.8625 17.2125 18.5625 13.8375 18.5625H13.1625C12.6563 18.5625 12.15 18.3937 11.6438 18.3937L10.125 19.575C10.6312 19.7437 11.1375 19.9125 11.8125 20.0812V23.625C6.75 23.625 7.59375 27 7.59375 27H19.4062C19.4062 27 20.25 23.625 15.1875 23.625V20.0812C18.9 19.4062 21.9375 16.3687 21.9375 12.6562V10.125C21.9375 9.44995 21.6 8.9437 21.0938 8.6062Z"
                                 fill="#F4F4F4"/>
                           <path d="M18.5626 11.8125V11.1375L12.9939 16.875C13.1626 16.875 13.3314 16.875 13.5001 16.875C16.3689 16.875 18.5626 14.5125 18.5626 11.8125Z"
                                 fill="#F4F4F4"/>
                        </svg>

                     </button>
                  </template>
                  {{ sources.audio.active ? "Couper le micro" : "Activer le micro" }}
               </w-tooltip>
            </div>
            <div class="right-controls">
               <w-tooltip top>
                  <template #activator="{ on }">
                     <button
                           :class="sources.stream.active ? 'active': ''"
                           @click="startStream(sources.stream.active)"
                           v-on="on"
                     >
                        <svg width="28" height="28" viewBox="0 0 34 31" fill="none" xmlns="http://www.w3.org/2000/svg">
                           <path d="M1.76175 18.3828C1.26204 13.4786 3.5593 9.03635 7.36956 6.21407C8.8548 5.11301 11.0757 6.05587 11.0757 7.79607C11.0757 8.43519 10.7426 9.0237 10.2012 9.42236C7.50143 11.4283 5.87739 14.5923 6.27299 18.0854C6.78657 22.6352 10.7842 26.3181 15.7674 26.8243C22.2705 27.4887 27.795 22.844 27.795 17.0476C27.795 13.9722 26.2335 11.2195 23.7974 9.41603C23.2561 9.01737 22.9299 8.42887 22.9299 7.79607C22.9299 6.07486 25.1161 5.10035 26.5874 6.17611C30.0715 8.73894 32.3063 12.656 32.3063 17.0476C32.3063 25.0778 24.8246 31.5513 15.8784 30.9628C8.48002 30.4882 2.44885 25.1157 1.76175 18.3828V18.3828ZM16.9958 0C15.7466 0 14.7402 0.923885 14.7402 2.05659V11.5296C14.7402 12.6686 15.7535 13.5862 16.9958 13.5862C18.2382 13.5862 19.2515 12.6623 19.2515 11.5296V2.05659C19.2515 0.923885 18.2451 0 16.9958 0V0Z"
                                 fill="#F4F4F4"/>
                        </svg>
                     </button>
                  </template>
                  {{ sources.stream.active ? "Arrêter le stream" : "Démarrer le stream" }}
               </w-tooltip>
               <w-tooltip top>
                  <template #activator="{ on }">
                     <button
                           v-on="on"
                           @click="quizPopUp.show = true"
                           :disabled="!stream_id"
                     >
                        <svg width="32" height="32" viewBox="0 0 35 35" fill="none" xmlns="http://www.w3.org/2000/svg">
                           <path d="M26.2158 2.40625L25.4691 9.29688L21.5811 10.4248C20.6787 9.8916 19.6533 9.54297 18.587 9.39941L17.5 7.24609L16.413 9.39941C15.3467 9.54297 14.3213 9.8916 13.4189 10.4248L8.93819 8.27148L8.78418 2.40625C11.3477 0.909111 14.3213 0 17.5 0C20.6787 0 23.6523 0.909111 26.2158 2.40625Z"
                                 fill="#FF641A"/>
                           <path d="M18.587 9.39941C19.6533 9.54297 20.6787 9.8916 21.5811 10.4248L25.4692 9.29688L26.2158 2.40625C23.6523 0.909111 20.6787 0 17.5 0V7.24609L18.587 9.39941Z"
                                 fill="#F03800"/>
                           <path d="M13.3984 24.5956L13.729 29.6282L8.78418 32.5937C3.57526 29.5791 0 23.9395 0 17.5L6.74365 15.4492L9.29688 17.5C9.29688 20.5351 10.9375 23.1806 13.3984 24.5956Z"
                                 fill="#FF641A"/>
                           <path d="M35.0001 17.5C35.0001 23.9395 31.4248 29.5791 26.2159 32.5937L20.8428 28.8858L21.6016 24.5956C24.0626 23.1806 25.7032 20.5352 25.7032 17.5L28.2667 15.4492L35.0001 17.5Z"
                                 fill="#F03800"/>
                           <path d="M26.2158 32.5938C23.6523 34.0908 20.6787 35.0001 17.5 35.0001C14.3213 35.0001 11.3477 34.0908 8.78418 32.5938L13.3984 24.5957C14.6085 25.293 16.003 25.7032 17.5 25.7032C18.997 25.7032 20.3915 25.293 21.6016 24.5957L26.2158 32.5938Z"
                                 fill="#474F54"/>
                           <path d="M26.2158 32.5938L21.6016 24.5957C20.3915 25.293 18.997 25.7032 17.5 25.7032V35.0001C20.6787 35.0001 23.6523 34.0908 26.2158 32.5938Z"
                                 fill="#32393F"/>
                           <path d="M13.4189 10.4248C10.958 11.8193 9.29688 14.4648 9.29688 17.5H0C0 11.0605 3.57526 5.4209 8.78418 2.40625L13.4189 10.4248Z"
                                 fill="#474F54"/>
                           <path d="M35 17.5H25.7031C25.7031 14.4648 24.042 11.8193 21.5811 10.4248L26.2158 2.40625C31.4247 5.4209 35 11.0605 35 17.5Z"
                                 fill="#32393F"/>
                           <path d="M17.5 26.7285C12.4114 26.7285 8.27148 22.5886 8.27148 17.5C8.27148 15.2545 9.0946 13.0933 10.5891 11.4144C11.941 9.89591 13.7526 8.86683 15.7369 8.47964L16.5829 6.78754C16.7565 6.44014 17.1116 6.2207 17.5 6.2207C17.8884 6.2207 18.2435 6.44014 18.4172 6.78754L19.2632 8.47964C21.2475 8.86683 23.0591 9.89591 24.4109 11.4144C25.9054 13.0933 26.7285 15.2545 26.7285 17.5C26.7285 22.5886 22.5886 26.7285 17.5 26.7285Z"
                                 fill="#FED843"/>
                           <path d="M26.7285 17.5C26.7285 15.2545 25.9054 13.0933 24.4109 11.4144C23.059 9.89591 21.2474 8.86683 19.2631 8.47964L18.4171 6.78754C18.2435 6.44014 17.8884 6.2207 17.5 6.2207V26.7285C22.5886 26.7285 26.7285 22.5886 26.7285 17.5V17.5Z"
                                 fill="#FABE2C"/>
                           <path d="M20.5762 15.4492C20.5762 16.7412 19.7558 17.9102 18.5459 18.3408C18.5459 18.915 18.0743 19.4687 17.5 19.4687C16.9462 19.4687 16.4746 19.0995 16.4746 18.5254C16.4746 17.664 16.8643 16.9462 17.5 16.5771C17.6025 16.5156 17.7256 16.4541 17.8486 16.413C18.2587 16.2695 18.5254 15.8798 18.5254 15.4492C18.5254 14.8749 18.0743 14.4238 17.5 14.4238C16.9257 14.4238 16.4746 14.8749 16.4746 15.4492C16.4746 16.0234 16.0235 16.4746 15.4492 16.4746C14.8749 16.4746 14.4238 16.0234 14.4238 15.4492C14.4238 13.747 15.7978 12.373 17.5 12.373C19.2022 12.373 20.5762 13.747 20.5762 15.4492Z"
                                 fill="#474F54"/>
                           <path d="M17.5 22.627C18.0663 22.627 18.5254 22.1679 18.5254 21.6016C18.5254 21.0353 18.0663 20.5762 17.5 20.5762C16.9337 20.5762 16.4746 21.0353 16.4746 21.6016C16.4746 22.1679 16.9337 22.627 17.5 22.627Z"
                                 fill="#474F54"/>
                           <path d="M18.5254 21.6016C18.5254 21.0351 18.0663 20.5762 17.5 20.5762V22.627C18.0663 22.627 18.5254 22.1679 18.5254 21.6016Z"
                                 fill="#32393F"/>
                           <path d="M20.5762 15.4492C20.5762 16.7412 19.7558 17.9102 18.5459 18.3408C18.5459 18.915 18.0743 19.4687 17.5 19.4687V16.5771C17.6025 16.5156 17.7256 16.4541 17.8486 16.413C18.2587 16.2695 18.5254 15.8798 18.5254 15.4492C18.5254 14.8749 18.0743 14.4238 17.5 14.4238V12.373C19.2022 12.373 20.5762 13.747 20.5762 15.4492V15.4492Z"
                                 fill="#32393F"/>
                        </svg>
                     </button>
                  </template>
                  Lancer un quiz
               </w-tooltip>
            </div>
         </div>
      </div>

      <div class="mini-chat">
         <MiniChat ref="mini-chat" :room_id="stream_id ? stream_id : 'DEFAULT'" @showUser="showDrawer = true"/>
      </div>
   </div>

   <w-drawer
         bg-color="grey-dark3"
         v-model="showDrawer"
         right
         @close="showDrawer = false"
         class="user-connected"
   >
      <div :key="user.id" v-for="user in userWatching">
         {{ user.name }} {{ user.last_name }}
      </div>
   </w-drawer>

   <w-dialog
         v-model="quizPopUp.show"
         title="Poser une question"
         persistent
         :width="500"
   >
      <template #actions>
         <div class="question">
            <w-form
                  class="question-form"
                  @submit="sendQuestion"
            >
               <div class="question-input">
                  <w-input
                        :validators="[quizForm.validators]"
                        v-model="quizPopUp.question"
                  >
                     Question
                  </w-input>
                  <div style="width: 100%; display: flex; align-items: center; justify-content: space-between; height: 50px; ">
                     Choix multiples ?
                     <w-switch v-model="quizPopUp.multiple" @input="selectMultiple"/>
                  </div>
               </div>

               <div class="odd-input">
                  <div class="possible-choice" :key="quizPopUp.answer.indexOf(q)" v-for="q in quizPopUp.answer">
                     <w-input
                           v-model="q.prop"
                     >
                        Proposition {{ q.id }} :
                     </w-input>
                     <w-switch
                           @input="addReponse($event, q.id)"
                           v-model="q.need"
                           class="need-response"
                     />
                  </div>

                  <w-button
                        @click="addQuestion"
                        bg-color="info"
                        :disabled="quizPopUp.answer.length >= 4"
                  >
                     Ajouter une question
                  </w-button>
               </div>

               <div class="buttons">
                  <w-button
                        @click="quizPopUp = {
                           show: false,
                           question: '',
                           answer: [
                               { id: 1, prop: '', need: true }
                           ],
                           needResponse: true
                        }"
                        bg-color="error"
                  >
                     Annuler
                  </w-button>
                  <w-button
                        bg-color="success"
                        type="submit"
                        :disabled="!quizPopUp.answer.filter(e => e.need).length > 0"
                  >
                     Envoyer la question
                  </w-button>
               </div>
            </w-form>
         </div>
      </template>
   </w-dialog>

   <w-notification
         info
         plain
         absolute
         top
         v-if="sources.stream.active"
   >
      Stream en cours
   </w-notification>

</template>

<script>
import firebase from 'firebase/app';
import 'firebase/firestore';
import MiniChat from "@/components/rtc/MiniChat";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {mapGetters} from "vuex";
import axios from "axios";

let stompClient = null;


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
   name: "TeacherView",
   components: {MiniChat},

   props: {
      rights: null,
   },

   methods: {
      async webcam(isActive) {
         // isActive tells if the cam is already active.

         // So if it is, we need to remove cam
         if (isActive) {
            this.stream?.getTracks().forEach((track) => {
               if (track.readyState === 'live') {
                  track.stop();
               }
            });
            this.stream = null;
            this.sources.webcam.active = false;
            // Else we're starting the cam
         } else {
            // Cancel screenshare if it's active
            if (this.sources.screenshare.active) {
               this.stream?.getTracks().forEach((track) => {
                  if (track.readyState === 'live') {
                     track.stop();
                  }
               });
               this.stream = null;
               this.sources.screenshare.active = false;
            }
            // Send stream to peer_connection and set the webcam as the stream.
            this.stream = await navigator.mediaDevices.getUserMedia({
               video: true,
               audio: true
            });
            this.stream.getTracks().forEach((track) => {
               this.peer_connection.addTrack(track, this.stream);
            });
            this.sources.webcam.active = true;
         }
      },

      async screenshare(isActive) {
         if (isActive) {
            this.stream?.getTracks().forEach((track) => {
               if (track.readyState === 'live') {
                  track.stop();
               }
            });
            this.stream = null;
            this.sources.screenshare.active = false;
         } else {
            if (this.sources.webcam.active) {
               this.stream?.getTracks().forEach((track) => {
                  if (track.readyState === 'live') {
                     track.stop();
                  }
               });
               this.stream = null;
               this.sources.webcam.active = false;
            }
            this.stream = await navigator.mediaDevices.getDisplayMedia({
               video: true,
               audio: true
            });
            this.stream.getTracks().forEach((track) => {
               this.peer_connection.addTrack(track, this.stream);
            });

            this.sources.screenshare.active = true;
         }
      },

      mute(isActive) {
         // todo : mute
         this.sources.audio.active = !isActive;
      },

      async startStream(isActive) {
         if (isActive) {
            this.send({
               type: "STREAM_STOPPED",
               callID: this.stream_id
            })
         } else {
            const callDoc = firestore.collection('calls').doc();
            const offerCandidates = callDoc.collection('offerCandidates');
            const answerCandidates = callDoc.collection('answerCandidates');

            this.send({
               type: "STREAM_STARTED",
               callID: callDoc.id,
               user_id: -1
            });
            this.stream_id = callDoc.id;

            this.peer_connection.onicecandidate = (event) => {
               event.candidate && offerCandidates.add(event.candidate.toJSON());
            };

            const offerDescription = await this.peer_connection.createOffer();
            await this.peer_connection.setLocalDescription(offerDescription);

            const offer = {
               sdp: offerDescription.sdp,
               type: offerDescription.type,
            };

            await callDoc.set({offer});

            callDoc.onSnapshot((snapshot) => {
               const data = snapshot.data();
               if (!this.peer_connection.currentRemoteDescription && data?.answer) {
                  const answerDescription = new RTCSessionDescription(data.answer);
                  this.peer_connection.setRemoteDescription(answerDescription);
               }
            });

            answerCandidates.onSnapshot((snapshot) => {
               snapshot.docChanges().forEach((change) => {
                  if (change.type === 'added') {
                     const candidate = new RTCIceCandidate(change.doc.data());
                     this.peer_connection.addIceCandidate(candidate);
                  }
               });
            });
         }

         this.sources.stream.active = !this.sources.stream.active;
      },


      addQuestion() {
         this.quizPopUp.answer.push({
            id: this.quizPopUp.answer.length + 1,
            prop: '',
            need: false
         })
      },
      addReponse(event, id) {
         if (!this.quizPopUp.multiple) {
            this.quizPopUp.answer.forEach(elt => {
               if (elt.id !== id)
                  elt.need = false;
            })
         }
      },
      selectMultiple() {
         this.quizPopUp.answer.forEach(elt => {
            elt.need = false;
         })
      },
      sendQuestion() {
         this.$refs['mini-chat'].addQuestion(this.quizPopUp);
         this.quizPopUp = {
            show: false,
            question: "",
            answer: [
               {id: 1, prop: '', need: true}
            ],
            multiple: true
         };
      },


      connect() {
         let ws = new SockJS("http://localhost:8080/ws");
         stompClient = Stomp.over(ws);
         // Comment the next line if you want to show websocket's logs
         stompClient.debug = null

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
         this.$refs["mini-chat"]?.disconnect();
      },
      onMessageReceived(payload) {
         const obj = JSON.parse(payload.body);
         if (obj.info === "IS_STREAMING") {
            if (this.sources.stream.active) {
               this.send({
                  type: "STREAM_STARTED",
                  callID: this.stream_id,
                  user_id: obj.user_id
               });

               axios.get(`http://localhost:8080/api/users/infos?user_id=${obj.user_id}`).then(response => {
                  if (this.userWatching.indexOf(response.data) !== -1)
                     this.userWatching.push(response.data);
               });
            }
         }
      },
      send(info) {
         stompClient.send(`/conversations/stream_chat/${this.getGroup.groups_id}`, {}, JSON.stringify({
            info: info.type,
            callID: info.callID,
         }))
      },

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

         stream_id: null,
         stream: null,

         sources: {
            webcam: {
               active: false,
            },
            screenshare: {
               active: false,
            },
            audio: {
               active: true,
            },
            stream: {
               active: false,
            }
         },

         userWatching: [],
         showDrawer: false,

         quizPopUp: {
            show: false,
            question: "",
            answer: [
               {id: 1, prop: '', need: true}
            ],
            multiple: true
         },
         quizForm: {
            validators: {
               required: value => !!value || 'Vous devez remplir ce champ'
            }
         },
      }
   },

   computed: {
      ...mapGetters(['getUser', 'getGroup'])
   },

   mounted() {
      if (this.rights.canStream) {
         this.peer_connection = new RTCPeerConnection(this.servers);
         this.connect();
      }
   },
   unmounted() {
      this.stream?.getTracks().forEach(function (track) {
         if (track.readyState === 'live') {
            track.stop();
         }
      });
      this.disconnect();
   }
}
</script>

<style scoped>
.TeacherView {
   width: 100%;
   height: 100%;

   display: flex;
   align-items: center;
   justify-content: space-between;
   flex-direction: row;
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

.controls {
   height: 50px;
   min-width: 300px;
   background-color: #4f4b5a;
   border-radius: 9999px;

   display: flex;
   justify-content: space-between;
   align-items: center;

   font-size: 12px;
}

.controls > div {
   display: flex;
   align-items: center;
   justify-content: space-between;
   height: 100%;
   max-width: 50%;
}

.controls button {
   width: 42px;
   height: 42px;
   background-color: #909090;
   border-radius: 50%;

   margin: 4px;
   border: none;
   outline: none;
   cursor: pointer;
   color: #F4F4F4;

   display: flex;
   align-items: center;
   justify-content: center;
}

.controls button:not([disabled]):hover {
   transform: scale(1.03);
}

.controls button:not([disabled]):active {
   transform: scale(1.05);
}

.controls .active {
   background-color: #E85C5C;
}

button[disabled] {
   cursor: not-allowed;
}


.question * {
   overflow-x: hidden;
   overflow-y: hidden;
}

.question {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
   width: 100%;
   min-height: 200px;
}

.question > * {
   margin-bottom: 5px;
}

.question-form {
   width: 100%;
   height: 100%;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;
}

.question-form > * {
   width: 100%;
}

.buttons {
   width: 100%;
   display: flex;
   align-items: center;
   justify-content: space-between;
}

.question-input {
   border-bottom: solid 2px #2d2d2d;
   padding-bottom: 10px;
   width: 100%;
}

.odd-input {
   margin-top: 10px;
   min-height: 80px;
   display: flex;
   flex-direction: column;
   justify-content: flex-start;
}

.possible-choice {
   width: 100%;
   margin-bottom: 5px;
   display: flex;
   align-items: center;
   justify-content: space-between;
}

.need-response {
   width: 40px;
   max-width: 50px;
   margin-left: 30px;
}

.user-connected {
   padding: 15px;
   display: flex;
   align-items: center;
   justify-content: flex-start;
}

.user-connected > div {
   width: 100%;
   height: 40px;
   border-radius: 3px;
   background-color: #909090;
   display: flex;
   align-items: center;
   justify-content: flex-start;
   padding: 10px;
   margin-bottom: 10px;
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