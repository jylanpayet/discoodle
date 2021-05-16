<template>
   <div class="AdminGroup">
      <div class="navbar">
         <div>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/discussion`"><div>Chat</div></router-link>
            <router-link class="navbar-links" :to="`/groupes/subject/${getGroup.groups_id}/parametres`" v-if="rights.canChangeGroup"><div>Paramètres</div></router-link>
         </div>
      </div>
      <div class="content">
         <router-view @groupAdded="$emit('groupAdded', $event);" />
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
   name: "AdminGroup",
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
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
         }
      }
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
}
</script>

<style scoped>
.AdminGroup {
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