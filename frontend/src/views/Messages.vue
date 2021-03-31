<template>
   <div class="Messages" v-if="!(JSON.stringify(getUser) === JSON.stringify({}))">
      <div class="left-pannel">
         <div class="settings-box">
            <div>
               <div class="add-conversation" @click="showPopUp = true">
                  +
               </div>
               <AddConversation v-if="showPopUp" @groupAdded="addNewGroup" @desactivatePopUp="showPopUp = false" />
               <div class="settings"><img src="../assets/settings.png" alt="Settings"
                                          style="height: 100%; width: 100%;"></div>
            </div>
         </div>
         <div class="convs-list">
            <router-link :key="convs.room_id" v-for="convs in convList" :to="`/messages/${convs.room_id}?name=${convs.room_name}`"
                         @click="setConvUUID(convs.room_id)">
               <div class="link-content">
                  <div class="user-logo">
                     {{ convs.room_name.charAt(0).toUpperCase() }}
                  </div>
                  <div class="conv-name">
                     {{ convs.room_name }}
                  </div>
               </div>
            </router-link>
         </div>
      </div>
      <div class="right-pannel">
         <router-view/>
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
      ...mapGetters(['getColors', 'getTheme', 'getUser']),
   },
   methods: {
      ...mapActions(['setConvUUID']),
      getRoomsFromDB() {
         axios.get(`http://localhost:8080/api/users/seeAllRooms/${this.getUser.id}`).then(response => {
            this.convList = response.data
         });
      },
      addNewGroup(group) {
         this.convList.unshift({
            link_picture: group.link_picture,
            room_name: group.room_name,
            room_id: group.room_id
         })
      }
   },
   data() {
      return {
         convList: [],
         displayConversationPopUp: false,
         showPopUp: false
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
   width: calc(100% - 70px);
   height: 60px;
   font-size: 20px;
   font-weight: 600;

   overflow: hidden;
}

.right-pannel {
   height: 100%;
   width: 78%;
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
</style>
