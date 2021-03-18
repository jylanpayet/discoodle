<template>
   <div class="Messages">
      <SearchBar/>
      <div class="messages-box-content">
         <div class="chat-list">

            <router-link to="/messages/new">
               <div :style="{ color: getColors.color5, fontSize: '15px' }">
                  <div class="conv-logo" :style="{ backgroundColor: getColors.color5, color: getColors.color1, fontSize: '40px' }" @click="addConversation">
                     +
                  </div>
                  Ajouter une conversation
               </div>
            </router-link>

            <!--
               TODO : Afficher toutes les conversations d'une personne ici (groupes et MP)
            -->
            <router-link v-for="item in convList" :key="item.uuid" :to="`/messages/${item.uuid}?name=${item.name}`" @click="setConvUUID(item.uuid)">
               <div :style="{ color: getColors.color5 }">
                  <div class="conv-logo">
                     {{ item.name.charAt(0).toUpperCase() }}
                  </div>
                  {{ item.name }}
               </div>
            </router-link>


         </div>
         <router-view/>
      </div>
   </div>
</template>

<script>
import SearchBar from "@/components/common/SearchBar";
import {mapActions, mapGetters} from "vuex";
import axios from 'axios';

export default {
   name: "Messages",
   components: {
      SearchBar
   },
   computed: {
      ...mapGetters(['getColors', 'getTheme']),
      ...mapGetters(['getUser'])
   },
   methods: {
      ...mapActions(['setConvUUID']),
     getRoomsFromDB() {
       axios.get(`http://localhost:8080/api/users/seeAllRooms/${this.getUser.id}`).then(response => {
         this.convList = response.data
       });
     },
      addConversation() {
        axios.post(`http://localhost:8080/api/room/addNewRoom`, {
          name: "Discoodle",
          admin: this.getUser.id
        }).then(response => {
          console.log(response);
        }).catch(error => {
          console.log(error.response);
        });
      }
   },
   data() {
      return {
         convList: []
      }
   },
   mounted() {
      this.getRoomsFromDB();
   }
}
</script>

<style scoped>
.Messages {
   display: flex;
   flex-direction: column;
   justify-content: center;
   align-items: center;
   height: 100%;
}

.messages-box-content {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;

   padding: 40px;

   width: calc(100% - 80px);
   height: calc(100% - 120px - 80px);
}

.chat-list {
   width: 19%;
   height: 100%;

   display: flex;
   flex-direction: column;
   align-items: flex-start;

   overflow-y: auto;
}

.conv-logo {
   border-radius: 100px;
   background-color: #454150;
   width: 47px;
   height: 47px;

   margin-right: 17px;
   color: #F4F4F4;
   font-size: 19px;
   font-weight: 600;

   display: flex;
   align-items: center;
   justify-content: center;
}

.chat-list > a {
   width: 100%;
   max-width: 100%;
}

.chat-list > a > div {
   margin-bottom: 15px;

   cursor: pointer;

   color: #F4F4F4;
   font-size: 19px;
   font-weight: 600;

   display: flex;
   flex-direction: row;
   justify-content: flex-start;
   align-items: center;
}


a.router-link-exact-active div {
   background-color: #E85C5C;
   color: #F4F4F4;
   border-radius: 100px;
}

.chat-list > div {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: flex-start;
   width: 100%;
   cursor: pointer;
   color: #F4F4F4;
   font-size: 15px;
   font-weight: 600;
}
</style>
