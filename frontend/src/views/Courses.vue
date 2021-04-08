<template>
   <div class="Courses" v-if="!(JSON.stringify(getUser) === JSON.stringify({}))">
      <div class="router">
         <router-link :key="group.groups_id" v-for="group in (getUser.role === 'student' ? subjects : groups)" @click="setGroup(group)" :to="`/cours/subject/${group.groups_id}/accueil`">
            <div class="group">
               {{ group.name }}
            </div>
         </router-link>
      </div>
      <router-view />
   </div>
   <Account @logSuccess="getRooms(getUser.role)" v-else/>
</template>

<script>

import Account from "@/views/Account";
import {mapActions, mapGetters} from "vuex";
import axios from "axios";
export default {
   name: "Courses",
   components: {Account},
   computed: {
      ...mapGetters(['getUser'])
   },
   data() {
      return {
         subjects: [],
         groups: []
      }
   },
   methods: {
      getRooms(role) {
         if (role === "STUDENT") {
            axios.get(`http://localhost:8080/api/users/seeAllSubjects/${this.getUser.id}`).then(response => {
               this.groups = response.data;
            })
         } else {
            axios.get(`http://localhost:8080/api/users/seeAllGroups/${this.getUser.id}`).then(response => {
               this.groups = response.data;
            })
         }
      },
      ...mapActions(['setGroup'])
   },
   mounted() {
      if (!(JSON.stringify(this.getUser) === JSON.stringify({}))) {
         this.getRooms(this.getUser.role);
      }
   }
}
</script>

<style scoped>
.Courses {
   position: relative;
   width: 100%;
   height: 100%;

   display: flex;
   flex-direction: row;
   align-items: flex-start;
   justify-content: flex-start;
}

.router {
   position: relative;
   width: 150px;
   padding: 50px;
   
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
}

.group {
   width: 50px;
   height: 50px;
   margin-bottom: 20px;
   border-radius: 9px;
   background-color: #454150;

   display: flex;
   align-items: center;
   justify-content: center;

   color: #F4F4F4;
   font-size: 17px;
   font-weight: 600;
   z-index: 500;
}

.group:hover {
   border-radius: 8px;
}

.router-link-active > .group {
   position: relative;
   background-color: #E85C5C !important;
}
</style>