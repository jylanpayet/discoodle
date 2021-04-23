<template>
   <div class="Messages" v-if="!(JSON.stringify(getUser) === JSON.stringify({}))">
      <div class="left-pannel">
         <div class="settings-box">
            <div>
               <div class="add-conversation" @click="showPopUp = true">
                  +
               </div>
               <AddConversation v-if="showPopUp" @groupAdded="addNewRoom" @desactivatePopUp="showPopUp = false" />
               <a class="settings" :style="modifying ? { animation: '10s cubic-bezier(.11, 0, .09, 1) settingsRoll' } : {}" href="#/messages" @click="modifying = !modifying;"><img src="../assets/settings.png" alt="Settings"
                                          style="height: 100%; width: 100%;"></a>
            </div>
         </div>
         <div class="convs-list">
            <router-link :key="convs.room_id" v-for="convs in convList" :to="modifying ? `/messages` : `/messages/${convs.room_id}?name=${convs.room_name}`"
                         @click="!modifying ? setConvUUID(convs.room_id) : ''" :style="modifying ? { backgroundColor: '#15131c !important' } : {}">
               <div class="link-content">
                  <div class="user-logo">
                     <img v-if="convs.link_picture !== null" :src="convs.link_picture" alt="">
                     <img src="../assets/group.svg" alt="" v-else-if="convs.users.length > 2" style="width: 65%;">
                     <span v-else>{{ convs.room_name.charAt(0).toUpperCase() }}</span>
                  </div>
                  <div class="conv-name">
                     {{ convs.room_name }}
                  </div>
                  <button class="edit-button" v-if="modifying" @click="editRoom.show = !editRoom.show; setConvUUID(convs.room_id)">
                     <img src="../assets/pen.svg" alt="" style="width: 60%;">
                  </button>
                  <w-dialog
                        v-model="editRoom.show"
                        :fullscreen="editRoom.fullscreen"
                        :width="editRoom.width"
                        :persistent="editRoom.persistent"
                        :persistent-no-animation="editRoom.persistentNoAnimation"
                  >
                     <div>
                        <span style="margin-bottom: 10px; font-size: 19px; font-weight: 600; color: #454150">Modifier la discussion :</span>
                        <w-input :minlength="1" v-model="new_name">Modifier le nom de la room.</w-input>
                        <div style="display: flex; flex-direction: row; justify-content: space-between; margin-top: 10px;">
                           <button class="submit" @click="deleteRoom; editRoom.show = false;" style="background-color: #E85C5C !important;">
                              Supprimer
                           </button>
                           <button class="submit" @click="roomEdit">
                              Enregistrer
                           </button>
                        </div>
                     </div>
                  </w-dialog>
               </div>
            </router-link>
         </div>
      </div>
      <div class="right-pannel">
         <router-view @userAdded="getRoomsFromDB" />
      </div>
   </div>
   <Account @logSuccess="getRoomsFromDB" v-else />
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import axios from 'axios';
import Account from "@/views/Account";
import AddConversation from "@/components/AddConversation";

export default {
   name: "Messages",
   components: {AddConversation, Account},
   computed: {
      ...mapGetters(['getColors', 'getUser', 'getCurrentConv']),
   },
   methods: {
      ...mapActions(['setConvUUID']),
      getRoomsFromDB() {
         axios.get(`http://localhost:8080/api/users/seeAllRooms/${this.getUser.id}`).then(response => {
            this.convList = response.data
         });
      },
      addNewRoom(group) {
         this.convList.unshift({
            link_picture: group.link_picture,
            room_name: group.room_name,
            room_id: group.room_id,
            users: group.users
         })
      },
      roomEdit() {
         if (this.new_name === "")
            return
         const conv_uuid = this.getCurrentConv;
         axios.put(`http://localhost:8080/api/room/renameRoom/${conv_uuid}?new_name=${this.new_name}`).then(() => {
            this.convList.filter(elt => elt.room_id === conv_uuid).map(elt => elt.room_name = this.new_name);
            this.editRoom.show = false;
         })

      },
      deleteRoom() {

      }
   },
   data() {
      return {
         convList: [],
         displayConversationPopUp: false,
         showPopUp: false,
         modifying: false,
         editRoom: {
            show: false,
            fullscreen: false,
            persistent: false,
            persistentNoAnimation: false,
            width: 300
         },
         new_name: ""
      }
   },
   mounted() {
      if (!(JSON.stringify(this.getUser) === JSON.stringify({})))
         this.getRoomsFromDB()
   }
}
</script>

<style scoped>
.Messages {
   display: flex;
   flex-direction: row;
   justify-content: center;
   align-items: center;
   height: 100%;
   width: 100%;
   background-color: #18161F;
}

.left-pannel {
   height: 100%;
   width: 22%;
   background-color: #15131c;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
}

.settings-box {
   position: relative;
   min-height: 80px;
   width: 100%;

   display: flex;
   align-items: center;
   justify-content: center;
}

.settings-box:after {
   position: absolute;
   content: "";
   bottom: 0;
   height: 1px;
   width: 90%;
   opacity: 0.5;
   background-color: #909090;
}

.settings-box > div {
   width: 86%;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
}

.settings {
   cursor: pointer;
   height: 40px;
   width: 40px;
   border: none;
   outline: none;
   background: none;

   margin-right: 3px
}

.settings:hover {
   animation: 10s cubic-bezier(.11, 0, .09, 1) settingsRoll;
}

.add-conversation {
   height: 34px;
   width: 34px;

   background-color: #F4F4F4;
   border-radius: 50%;

   display: flex;
   align-items: center;
   justify-content: center;

   font-size: 38px;
   font-weight: 600;
   color: #454150;

   margin-left: 3px;

   cursor: pointer;
}

.add-conversation:hover {
   background-color: #E85C5C;
   color: #F4F4F4;
}

.convs-list {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;

   width: 100%;
   height: 100%;

   overflow-y: auto;
   overflow-x: hidden;
}

.convs-list > a {
   position: relative;
   width: 96%;
   height: 70px;

   display: flex;
   align-items: center;
   justify-content: center;
}

.convs-list > a:after {
   position: absolute;
   content: "";
   bottom: -1px;
   height: 1px;
   width: 90%;
   opacity: 0.5;
   background-color: #909090;
}

.router-link-active {
   background-color: #E85C5C;
   border-radius: 7px;
}

.router-link-active:after {
   content: none !important;
}

.link-content {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: flex-start;

   width: 90%;
   height: 100%;
}

.user-logo {
   min-height: 40px;
   min-width: 40px;
   height: 40px;
   width: 40px;
   background-color: #C4C4C4;
   color: #454150;
   font-size: 23px;
   font-weight: 600;

   margin-right: 20px;

   display: flex;
   align-items: center;
   justify-content: center;
   border-radius: 50%;
}

.conv-name {
   display: flex;
   align-items: center;
   width: calc(100% - 100px);
   height: 60px;
   font-size: 20px;
   font-weight: 600;

   overflow: hidden;
}

.right-pannel {
   height: 100%;
   width: 78%;
}

.edit-button {
   width: 40px;
   height: 40px;

   display: flex;
   align-items: center;
   justify-content: center;

   background: none;
   outline: none;
   border: none;
   cursor: pointer;
}

.edit-button > img {
   animation: shaking 4s ease-in-out infinite;
   transform-origin:50% 50%;
}

@keyframes settingsRoll {
   0% {
      transform: rotate(0);
   }
   85% {
      transform: rotate(2164deg);
   }
   93% {
      transform: rotate(2158deg);
   }
   100% {
      transform: rotate(2160deg);
   }
}

.submit {
   height: 35px;
   width: 40%;
   background-color: #28b140;
   font-size: 15px;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 999px;
   color: #F4F4F4;
   cursor: pointer;
}

@keyframes shaking {
   0% { -webkit-transform: translate(2px, 1px) rotate(0deg); }
   10% { -webkit-transform: translate(-1px, -2px) rotate(-1deg); }
   20% { -webkit-transform: translate(-3px, 0px) rotate(1deg); }
   30% { -webkit-transform: translate(0px, 2px) rotate(0deg); }
   40% { -webkit-transform: translate(1px, -1px) rotate(1deg); }
   50% { -webkit-transform: translate(-1px, 2px) rotate(-1deg); }
   60% { -webkit-transform: translate(-2px, 1px) rotate(0deg); }
   70% { -webkit-transform: translate(2px, 1px) rotate(-1deg); }
   80% { -webkit-transform: translate(-1px, -1px) rotate(1deg); }
   90% { -webkit-transform: translate(2px, 2px) rotate(0deg); }
   100% { -webkit-transform: translate(1px, -2px) rotate(-1deg); }
}
</style>
