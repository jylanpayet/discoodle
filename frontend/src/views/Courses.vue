<template>
   <div class="Courses" v-if="!(JSON.stringify(getUser) === JSON.stringify({}))">
      <div class="router">
         <router-link :key="group.groups_id" v-for="group in (getUser.role === 'student' ? subjects : groups)"
                      @click="setGroup(group)" :to="`/cours/subject/${group.groups_id}/accueil`">
            <div class="group">
               {{ group.name }}
            </div>
         </router-link>
         <button class="addGroup" @click="joinGroup.show = true">
            +
         </button>
      </div>
      <router-view/>
   </div>
   <Account @logSuccess="getRooms(getUser.role)" v-else/>

   <w-dialog
         v-model="joinGroup.show"
         :fullscreen="joinGroup.fullscreen"
         :width="joinGroup.width"
         :persistent="joinGroup.persistent"
         :persistent-no-animation="joinGroup.persistentNoAnimation"
   >
      <div class="joinGroup">
         <span style="margin-bottom: 10px; font-size: 19px; font-weight: 600; color: #454150">Rejoindre un groupe :</span>
         <w-input color="darkgray" :model-value="joinContentID" required style="margin-bottom: 10px; width: 90%" name="groupID" @keydown="join">
            Identifiant du groupe
         </w-input>
         <w-input color="darkgray" :model-value="joinContentKey" required style="margin-bottom: 10px; width: 90%" type="password" name="groupKey"
                  @keydown="join">Clé du groupe
         </w-input>
         <button class="submit" @click="join">
            Rejoindre !
         </button>
      </div>
   </w-dialog>
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
         groups: [],
         joinGroup: {
            show: false,
            fullscreen: false,
            persistent: false,
            persistentNoAnimation: false,
            width: 300
         },
         joinContentID: "",
         joinContentKey: ""
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
      ...mapActions(['setGroup']),
      join(event) {
         if ((event.type === 'keydown' && event.keyCode === 13) || event.type === 'click') {
            axios.post(`http://localhost:8080/api/groups/addNewMemberInGroup/${document.querySelector("input[name='groupID']")}
               ?user_id=${this.getUser.id}
               &token=${document.querySelector("input[name='groupKey']")}`
            ).then(response => {
               console.log(response);
            });
         }
      },
      create(event) {
         if ((event.type === 'keydown' && event.keyCode === 13) || event.type === 'click') {
            console.log(event);
         }
      }
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

.addGroup {
   background-color: #F4F4F4;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 40px;
   font-weight: 600;
   color: #454150;
   cursor: pointer;
   width: 50px;
   height: 50px;
   border: none;
   outline: none;
   border-radius: 50%;
}

.dialog > button {
   width: 230px;
   height: 300px;
   cursor: pointer;
   border: none;
   outline: none;
   border-radius: 6px;
   background-color: #8F8F8F;
}

.submit {
   height: 35px;
   width: 150px;
   background-color: #E85C5C;
   font-size: 15px;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 999px;
   color: #F4F4F4;
   cursor: pointer;
}

.joinGroup {
   height: 175px;
   width: 100%;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}
</style>