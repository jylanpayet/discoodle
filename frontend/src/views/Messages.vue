<template>
   <div class="Messages">
      <SearchBar/>
      <div class="messages-box-content">
         <div class="chat-list">

            <router-link to="/messages/new">
               <div :style="{ color: getColors.color5, fontSize: '15px' }">
                  <div class="conv-logo" :style="{ backgroundColor: getColors.color5, color: getColors.color1, fontSize: '40px' }">
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

export default {
   name: "Messages",
   components: {
      SearchBar
   },
   computed: {
      ...mapGetters(['getColors', 'getTheme'])
   },
   methods: {
      ...mapActions(['setConvUUID'])
   },
   data() {
      return {
         convList: [
            {
               uuid: "e485c6f4-72ca-11eb-9439-0242ac130002",
               name: "Bob"
            },
            {
               uuid: "e6e5c4c6-72ca-11eb-9439-0242ac130002",
               name: "Dylan"
            },
            {
               uuid: "b326cd8e-6468-4592-ba9c-24b5a87754b2",
               name: "fetty"
            },
            {
               uuid: "ac96ba0c-7313-11eb-9439-0242ac130002",
               name: "OG.Mimmo"
            }
         ]
      }
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

.chat-list::-webkit-scrollbar {
   width: 4px;
}


.chat-list::-webkit-scrollbar-thumb {
   background: #454150;
   border-radius: 15px;
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