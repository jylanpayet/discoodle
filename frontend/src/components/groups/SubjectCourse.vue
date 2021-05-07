<template>
   <div class="SubjectCourse">
      <div class="teacher-view">
         <div class="stream-start-stop">

         </div>
      </div>
   </div>
</template>

<script>
import axios from "axios";
import {mapGetters} from "vuex";

export default {
   name: "SubjectCourse",
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
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
.SubjectCourse {
   height: calc(100vh - 100px);
   width: 100%;
}


.teacher-view {
   display: flex;
   align-items: center;
   justify-content: space-between;

   min-height: 50px;
   background-color: #13111A;
}
</style>