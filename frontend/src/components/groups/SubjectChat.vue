<template>
   <div class="SubjectChat">
      <div class="rooms-list">
         <div class="server-name">
            {{ getGroup.server.name }}
         </div>
         <router-link @click="setConvUUID(channel.room_id)" :to="`/groupes/subject/${getGroup.groups_id}/discussion/${channel.room_id}?name=${channel.room_name}`" class="room" :key="channel.room_id" v-for="channel in getGroup.server.rooms">
            # {{ channel.room_name }}
         </router-link>

         <button class="addRoom" @click="showAddRoom = true" v-if="rights.canModifyRoom">
            +
         </button>

         <w-dialog
               v-model="showAddRoom"
               :width="400"
         >
            <div class="addNewRoom">
               <span>Ajouter un salon :</span>
               <w-input autocomplete="off" v-model="newRoomName">Nom de la room :</w-input>
               <w-button class="ma1" bg-color="success" @click="createNewRoom">Ajouter la room</w-button>
            </div>
         </w-dialog>
      </div>
      <div class="separator"></div>
      <div class="chat">
         <router-view />
      </div>
   </div>
</template>

<script>

// TODO : recharger les rooms au mounted
import {mapActions, mapGetters} from "vuex";
import axios from "axios";

export default {
   name: "SubjectChat",
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
   methods: {
      ...mapActions(['setConvUUID']),
      createNewRoom() {
         if (this.newRoomName !== "") {
            axios.post(`http://localhost:8080/api/server/addRoom?server_id=${this.getGroup.server.server_id}&name=${this.newRoomName}`).then(response => {
               this.getGroup.server.rooms.push(response.data);
               this.newRoomName = "";
               this.showAddRoom = false;
            })
         }
      }
   },
   data() {
      return {
         rights: {
            canSendMessage: false,
            canReadMessage: false,
            canChangeGroup: false,
            canModifyRoom: false,
            canModifyNotes: false,
            canStream: false,
         },
         showAddRoom: false,
         newRoomName: ""
      }
   },
   beforeRouteUpdate() {
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
   mounted() {
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
   }
}
</script>

<style scoped>
.SubjectChat {
   height: 100%;
   width: 100%;

   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: center;
}

.rooms-list {
   height: 100%;
   width: 160px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
   margin-right: 5px;
}

.rooms-list > * {
   margin-bottom: 5px;
}

.chat {
   height: 100%;
   width: calc(100% - 167px);
   max-width: calc(100% - 167px);
}

.server-name {
   font-size: 100%;
   font-weight: 600;
   color: #F4F4F4;
   width: 100%;
   height: 50px;
   display: flex;
   align-items: center;
   justify-content: center;
   margin-bottom: 10px;
}

.room {
   height: 30px;
   width: 160px;
   font-size: 18px;
   font-weight: 600;
   display: flex;
   align-items: center;
   justify-content: flex-start;
   padding-left: 10px;
   color: #F4F4F4;

   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
}

.router-link-active {
   color: #E85C5C;
}

.separator {
   height: 80%;
   margin: auto;
   width: 2px;
   background-color: #8F8F8F;
   border-radius: 1px;
}

.addRoom {
   width: 30px;
   height: 30px;
   background-color: #4f4b5a;
   border-radius: 4px;
   border: none;
   outline: none;
   font-size: 20px;
   font-weight: 600;
   color: #F4F4F4;
   cursor: pointer;
}

.addRoom:hover {
   background-color: #E85C5C;
}

.addNewRoom {
   display: flex;
   align-items: center;
   justify-content: space-between;
   flex-direction: column;
}

.addNewRoom > div {
   width: 100%;
   margin-bottom: 10px;
}
</style>