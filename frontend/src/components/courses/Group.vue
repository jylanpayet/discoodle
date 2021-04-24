<template>
   <div class="Group">
      <div class="navbar">
         <div v-if="getGroup.type === 'SUBJECTS'">
            <router-link class="navbar-links" :to="`/cours/subject/${getGroup.groups_id}/accueil`"><div>Accueil</div></router-link>
            <router-link class="navbar-links" :to="`/cours/subject/${getGroup.groups_id}/cours`"><div>Cours</div></router-link>
            <router-link class="navbar-links" :to="`/cours/subject/${getGroup.groups_id}/chat`"><div>Chat</div></router-link>
            <router-link class="navbar-links" :to="`/cours/subject/${getGroup.groups_id}/notes`"><div>Notes</div></router-link>
            <router-link class="navbar-links" :to="`/cours/subject/${getGroup.groups_id}/parametres`" v-if="getUser.role === 'TEACHER'"><div>Paramètres</div></router-link>
         </div>
         <div v-else>

         </div>
      </div>
      <router-view />
   </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
   name: "Group",
   data() {
      return {

      }
   },
   methods: {

   },
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
   mounted() {
      console.log(this.getGroup);
   }
}
</script>

<style scoped>
.Group {
   width: 100%;
   min-height: 100vh;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
}

.navbar {
   width: 100%;
   height: 100px;
   display: flex;
   align-items: center;
   justify-content: center;
}

.navbar > div {
   position: relative;
   width: 80%;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: center;
}

.navbar-links {
   display: flex;
   align-items: center;
   justify-content: center;
   width: 120px;
   height: 40px;
}

.navbar-links > div {
   position: relative;
   color: #909090;
   font-weight: 600;
   font-size: 20px;
}

.router-link-active > div {
   color: #F4F4F4 !important;
}

.router-link-active > div:before {
   content: "";
   position: absolute;
   width: 100%;
   bottom: -2px;
   height: 2px;
   background-color: #F4F4F4;
   animation: width 0.4s ease-in-out;
}

@keyframes width {
   from {
      width: 0;
   }
   to {
      width: 100%;
   }
}
</style>