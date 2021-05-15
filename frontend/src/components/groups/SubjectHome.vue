<template>
   <div class="SubjectHome">
      <div class="content" v-if="showDefaultPage">
         <h1>{{ getGroup.name }}</h1>

         <h3>Bienvenue dans votre cours de {{ getGroup.name }} </h3>
         <p>
            Vous pourrez retrouver ici les documents mis en ligne par votre professeur(e), comme les devoirs, cours PDF, vidéos de cours, etc.
         </p>
         <p>
            Pour contacter le(s) professeur(s) :
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
   scroll-behavior: smooth;
}

.content {
   color: #F4F4F4;
   font-size: 15px;
   width: 100%;

   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: flex-start;
}

.content >>> h1,
.content >>> h2,
.content >>> h3,
.content >>> h4,
.content >>> h5,
.content >>> h6 {
   margin-top: 20px;
   text-decoration: underline;
}

.content >>> * {
   margin-top: 10px;
}

.content >>> pre {
   background-color: #4f4b5a;
   border-radius: 5px;
   padding: 10px;
}

.content >>> pre > code {
   font-family: 'Inconsolata', monospace;
}

.content >>> blockquote {
   padding: 10px 16px 10px 16px;
   border-radius: 2px;
   border-left: #E85C5C solid 2px;
   background-color: #4f4b5a;
   font-style: italic;
   display: flex;

}

.content >>> blockquote:before {
   content: "\"";
   margin-right: 4px;
}

.content >>> blockquote:after {
   content: "\"";
   margin-left: 4px;
}

.content >>> blockquote > p {
   display: flex;
   align-items: center;
   justify-content: center;
   margin-top: 0 !important;
}
</style>