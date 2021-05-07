<template>
   <div class="SubjectHome">
      <div class="content" v-if="showDefaultPage">
         <h1>{{ getGroup.name }}</h1>

         <h3>Bienvenue dans votre cours de {{ getGroup.name }} </h3>
         <p>
            Vous pourrez retrouver ici les documents mis en ligne par votre professeur, comme les devoirs, cours PDF, vidéo de cours, etc.
         </p>
         <p>
            Vous pouvez contacter votre ou vos enseignant(s) à/aux l'adresse(s) mail(s) suivante(s) :
         </p>
         <ul>
            <li :key="mails.id" v-for="mails in getGroup.roles[0].users">
               <em><strong>
                  {{ mails.mail }}
               </strong></em>
            </li>
         </ul>
      </div>

      <div class="content" v-html="content" v-else>
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";
import marked from 'marked';

export default {
   name: "SubjectHome",
   data() {
      return {
         content: "",
         showDefaultPage: false,
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
   beforeRouteUpdate() {
      axios.get(`http://localhost:8080/common/groups/${this.getGroup.groups_id}/cours.md`).then(rep => {
         this.content = marked(rep.data);
      }).catch(() => {
         console.log("IMPOSSIBLE DE CHARGER LE MARKDOWN DU COURS");
         this.showDefaultPage = true;
      });

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
      axios.get(`http://localhost:8080/common/groups/${this.getGroup.groups_id}/cours.md`).then(rep => {
         this.content = marked(rep.data);
      }).catch(() => {
         console.log("IMPOSSIBLE DE CHARGER LE MARKDOWN DU COURS");
         this.showDefaultPage = true;
      });

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
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
}
</script>

<style scoped>
.SubjectHome {
   height: 100%;
   width: 100%;
   padding: 40px;
   overflow-y: auto;
   overflow-x: hidden;
}

.content {
   color: #F4F4F4;
   font-size: 15px;
   width: 100%;
   height: 100%;
}

.content > * {
   margin-bottom: 30px;
}
</style>