<template>
   <div class="Subject">
      <div class="navbar">
         <div>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/accueil`"><div>Accueil</div></router-link>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/cours`"><div>Cours</div></router-link>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/discussion`"><div>Chat</div></router-link>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/notes`"><div>Notes</div></router-link>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/parametres`" v-if="getUser.role === 'TEACHER'"><div>Paramètres</div></router-link>
         </div>
      </div>
      <div class="content">
         <router-view />
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
   name: "Subject",
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
   mounted() {
      axios.get(`http://localhost:8080/api/groups/getRoleByGroupAndUser?group_id=${this.getGroup.groups_id}&user_id=${this.getUser.id}`).then(response => {
         console.log(response);
      })
   },
   beforeRouteUpdate() {
      axios.get(`http://localhost:8080/api/groups/getRoleByGroupAndUser?group_id=${this.getGroup.groups_id}&user_id=${this.getUser.id}`).then(response => {
         console.log(response);
      })
   },
}
</script>

<style scoped>

.Subject {
   width: 100%;
   height: 100%;
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

.content {
   height: calc(100% - 100px);
   width: 100%;
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