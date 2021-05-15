<template>
   <div class="Discoodle">
      <div class="row">
         <div class="users">
            <h2 class="card-title" style="margin-bottom: 7px;">Utilisateurs de discoodle :</h2>

            <w-input
                  v-model="users.keyword"
                  placeholder="Rechercher"
                  autocomplete="off"
                  color="white"
            >

            </w-input>
            <w-table
                  :headers="users.headers"
                  :items="users.list"
                  :filter="users.keywordFilter(users.keyword)"
                  :loading="users.loading"
                  :selectable-rows="1"
                  @row-select="selectUser"

                  class="users-table"
            >
               <template #no-data>
                  Discoodle ne contient aucun utilisateur 😥
               </template>
            </w-table>
         </div>
      </div>

      <div class="row">
         <div class="user-selection">
            <div class="user-infos" v-if="users.selection">
               <div class="card-title">
                  <span>Informations de l'utilisateur :</span>
               </div>

               <div class="infos">
                  <div>
                     <span>ID : </span>
                     <span>{{ users.selection.id }}</span>
                  </div>
                  <div>
                     <span>Nom : </span>
                     <span>{{ users.selection.name }}</span>
                  </div>
                  <div>
                     <span>Prénom : </span>
                     <span>{{ users.selection.last_name }}</span>
                  </div>
                  <div>
                     <span>Mail : </span>
                     <span style="max-width: 80%">{{ users.selection.mail }}</span>
                  </div>
                  <div>
                     <span>Pseudo : </span>
                     <span>{{ users.selection.username }}</span>
                  </div>
                  <div>
                     <span>Rôle :</span>
                     <w-select
                           :items="[
                           { label: 'Étudiant', value: 'STUDENT' },
                           { label: 'Enseignant', value: 'TEACHER' },
                           { label: 'Administrateur', value: 'ADMIN' }
                        ]"
                           v-model="users.changingRole"
                           color="white"
                     >

                     </w-select>
                  </div>
               </div>

               <div class="buttons">
                  <w-button
                        @click="lockOrUnlockUser(users.selection.locked)"
                        bg-color="error"
                        style="margin-bottom: 10px;"
                  >
                     {{
                        users.selection.locked
                              ? "Débloquer l'utilisateur"
                              : "Bloquer l'utilisateur"
                     }}
                  </w-button>

                  <w-button
                        @click="changeUserRole"
                        bg-color="success"
                        :disabled="users.changingRole === users.selection.role"
                  >
                     Modifier les informations
                  </w-button>
               </div>

               <w-notification
                     v-model="users.roleChangedPopUp.show"
                     :timeout="3000"
                     plain
                     :error="users.roleChangedPopUp.error"
                     :success="!users.roleChangedPopUp.error"
                     absolute
                     bottom
               >
                  {{ users.roleChangedPopUp.message }}
               </w-notification>
            </div>
            <div v-else>
               <h4 style="color: #F4F4F4">
                  Sélectionnez un utilisateur pour voir ses informations ici.
               </h4>
            </div>
         </div>

         <div class="teacher-request">
            <div style="text-decoration: underline; color: #F4F4F4; font-weight: 600; height: 30px; font-size: 18px; display: flex; align-items: center; justify-content: center;">
               Demandes de statut enseignant :
            </div>
            <span style="color: #F4F4F4; height: 30px; font-size: 14px; display: flex; align-items: center; justify-content: center;"
                  v-if="requests.teacher.list.length === 0">
               Vous n'avez aucune demande de statut.
            </span>
            <div style="max-height: calc(100% - 40px); overflow-y: auto">
               <div class="request" :key="request.tr_id" v-for="request in requests.teacher.list">
                  <span>{{ request.user.username }}</span>

                  <div style="display: flex; align-items: center; justify-content: space-between; width: 160px;">
                     <w-button
                           bg-color="error"
                           @click="refuseTeacherRequest(request.user.id)"
                     >
                        Refuser
                     </w-button>
                     <w-button
                           bg-color="success"
                           @click="acceptTeacherRequest(request.user.id)"
                     >
                        Accepter
                     </w-button>
                  </div>
               </div>
            </div>
         </div>
      </div>

   </div>
</template>

<script>
import axios from "axios";
import {mapGetters} from "vuex";

export default {
   name: "Discoodle",
   computed: {
      ...mapGetters(['getUser'])
   },

   data() {
      return {
         users: {
            list: [],
            headers: [
               {label: 'ID', key: "id"},
               {label: 'Nom', key: "last_name"},
               {label: 'Prénom', key: "name"},
               {label: 'Pseudo', key: 'username'},
               {label: 'Rôle', key: "role"},
               {label: 'Mail', key: "mail"},
            ],
            keyword: "",
            keywordFilter: keyword => item => {
               const allTheColumns = `${item.id} ${item.last_name} ${item.name} ${item.mail} ${item.role}`
               return new RegExp(keyword, 'i').test(allTheColumns)
            },
            loading: true,
            selection: undefined,
            changingRole: "",
            roleChangedPopUp: {
               show: false,
               error: false,
               message: ""
            }
         },

         requests: {
            teacher: {
               list: [],
            }
         }
      }
   },

   methods: {
      selectUser(event) {
         if (event.selected) {
            this.users.selection = event.item;
            this.users.changingRole = event.item.role;
         } else {
            this.users.selection = undefined
            this.users.changingRole = undefined
         }
      },
      changeUserRole() {
         axios.put(`http://localhost:8080/api/users/changeRole?user_id=${this.users.selection.id}&role=${this.users.changingRole}`).then(response => {
            this.users.roleChangedPopUp.show = true;
            this.users.roleChangedPopUp.error = !!response.data.startsWith("ERR|");
            if (this.users.roleChangedPopUp.error)
               this.users.roleChangedPopUp.message = response.data.substring(5);
            else {
               this.users.roleChangedPopUp.message = response.data;
               this.users.selection.role = this.users.changingRole;
               this.users.list.forEach(e => {
                  if (e.id === this.users.selection.id)
                     e.role = this.users.selection.role;
               })
            }
         }).catch(() => {
            this.users.roleChangedPopUp.show = true;
            this.users.roleChangedPopUp.error = true;
            this.users.roleChangedPopUp.message = "Impossible de changer le rôle";
         });

      },
      lockOrUnlockUser(isLocked) {
         axios.put(`http://localhost:8080/api/users/lockOrUnlock?user_id=${this.users.selection.id}&lock=${!isLocked}`).then(response => {
            if (response.data?.id) {
               this.users.list.forEach(e => {
                  if (e.id === response.data.id)
                     e.locked = !isLocked;
               });
            }
         });
      },

      acceptTeacherRequest(user_id) {
         axios.put(`http://localhost:8080/api/TeacherRequest/acceptTeacherRequest?user_id=${user_id}`).then(() => {
            this.requests.teacher.list = this.requests.teacher.list.filter(elt => {
               return elt.user.id !== user_id;
            });
            this.users.list.forEach(e => {
               if (e.id === user_id)
                  e.role = "TEACHER";
            });
         });
      },
      refuseTeacherRequest(user_id) {
         axios.put(`http://localhost:8080/api/TeacherRequest/refuseTeacherRequest?user_id=${user_id}`).then(() => {
            this.requests.teacher.list = this.requests.teacher.list.filter(elt => {
               return elt.user.id !== user_id;
            });
         });
      }
   },

   async mounted() {
      await axios.get("http://localhost:8080/api/users").then(response => {
         this.users.list = response.data.filter(e => {
            return e.id !== this.getUser.id;
         });
         this.users.loading = false;
      });

      await axios.get("http://localhost:8080/api/TeacherRequest/getTeacherRequestBeingProcessed").then(response => {
         response.data.forEach(elt => {
            axios.get(`http://localhost:8080/api/users/infos?user_id=${elt.user_id}`).then(rep => {
               this.requests.teacher.list.push({
                  tr_id: elt.tr_id,
                  status: elt.status,
                  user: rep.data,
               });
            })
         })
      })

      console.log(this.requests.teacher.list);
   },

}
</script>

<style scoped>
.Discoodle {
   width: 100%;
   height: 100%;
   display: flex;
   flex-direction: column;
   align-content: flex-start;
   justify-content: flex-start;

   padding: 40px;
}

.users {
   background-color: #4f4b5a;
   width: 100%;
   min-height: 200px;
   max-height: 300px;
   border-radius: 5px;

   padding: 15px;

   overflow-y: auto;

   margin-bottom: 20px;
}

.users-table {
   color: #F4F4F4;
}

.users-table::-webkit-scrollbar {
   height: 4px !important;
}


.user-selection {
   width: calc(50% - 25px);
   min-height: 275px;
   background-color: #4f4b5a;
   border-radius: 5px;

   padding: 10px;
}

.user-infos {
   width: 100%;
   height: 100%;

   display: flex;
   flex-direction: column;
   align-content: flex-start;
   justify-content: space-between;
}

.infos {
   width: 100%;
   height: calc(80% - 30px);

   color: #F4F4F4;
   font-weight: 600;

   display: flex;
   flex-direction: column;
   align-content: flex-start;
   justify-content: space-between;

   padding: 10px;
}

.infos > div {
   display: flex;
   align-items: center;
   justify-content: space-between;
   height: 30px;
   width: 100%;
}

.infos > div > * {
   max-width: 50%;
}


.card-title {
   text-decoration: underline;
   max-width: 100% !important;
   color: #F4F4F4;
   font-weight: 600;
   height: 30px;
   font-size: 18px;
}

.buttons {
   display: flex;
   align-items: center;
   justify-content: center;
   flex-direction: column;
}

.buttons > button {
   width: 100%;
}

.row {
   display: flex;
   align-items: center;
   justify-content: space-between;
}

.teacher-request {
   width: calc(50% - 15px);
   height: 100%;
   max-height: 300px;
   padding: 10px;
   background-color: #4f4b5a;
   border-radius: 5px;
   color: #F4F4F4;
}

.request {
   width: 100%;
   background-color: #2d2d2d;
   height: 35px;
   border-radius: 3px;

   display: flex;
   align-items: center;
   justify-content: space-between;

   padding: 5px 5px 5px 10px;

   margin-bottom: 5px;
}

.teacher-request > * {
   margin-bottom: 5px;
}

</style>