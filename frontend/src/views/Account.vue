<template>
   <div class="Account" v-if="isAuthentificated">
      <div class="content">
         <div class="container">
            <span>Votre profil :</span>
            <div class="container-content">
               <div class="logo-and-text top-part">
                  <label class="logo">
                     <input ref="uploadImage" type="file" style="width: 0; height: 0;" @change="imageUpload()"
                            v-if="getUser.link_to_avatar === null">
                     <span style="font-size: 55px; font-weight: 600; color: #F4F4F4"
                           v-if="getUser.link_to_avatar === null">{{ getUser.name.charAt(0).toUpperCase() }}</span>
                     <img :src="getUser.link_to_avatar" alt="" v-else class="custom-image avatar">
                     <span class="upload-image" @click="removeLinkToAvatar"
                           :style="getUser.link_to_avatar === null ? { backgroundColor: '#F4F4F4' } : { backgroundColor: 'rgba(232, 92, 92, 0.8)', color: '#F4F4F4 !important' }">
                        {{ getUser.link_to_avatar === null ? "+" : "x" }}
                     </span>
                  </label>
                  <span style="font-size: 30px; font-weight: 600; color: #F4F4F4">
                     {{ getUser.name }} {{ getUser.last_name.toUpperCase() }}
                  </span>
               </div>
               <span class="separator"></span>
               <div class="account-info bottom-part">
                  <div>
                     <span>Mail :</span>
                     <span>Pseudo :</span>
                     <span>Rôle :</span>
                     <span>Mot de passe :</span>
                  </div>
                  <div style="align-items: flex-end;">
                     <span>{{ getUser.mail }}</span>
                     <span>{{ getUser.username }}</span>
                     <span>{{ getUser.role }}</span>
                     <span>∙∙∙∙∙∙∙∙∙∙∙∙</span>
                  </div>
               </div>
            </div>
         </div>

         <div class="container cursus">
            <span>Scolarité :</span>
            <div class="container-content">


               <div class="select-groups" v-if="!alreadyRegistered">
                  <SelectGroup @selected="showGroup" :groups="establishments.map(elt => {
                     return {
                        label: elt.name,
                        value: elt
                     }
                  })" :placeholder="typeTranslation['ESTABLISHMENT']" :index="0"/>

                  <SelectGroup @selected="showGroup" :key="elt.index" v-for="elt in groups" :groups="elt.childs.filter(item => (
                        allowedTypes.includes(item.type)
                  )).map(e => {
                     return {
                        label: e.name,
                        value: e
                     }
                  })" :placeholder="typeTranslation[elt.childs[0]?.type]" :index="elt.index+1"/>
               </div>

               <div class="select-groups" v-else>
                  <div :key="elt.index" v-for="elt in groups" class="selected">
                     <span>Votre {{ typeTranslation[elt.group.type].toLowerCase() }} :</span>
                     <div>
                        {{ elt.group.name }}
                     </div>
                  </div>
               </div>


               <div class="submit-delete">
                  <w-button class="ma1" bg-color="error" lg @click="confirmCancel.show = true">
                     Effacer
                  </w-button>
                  <w-button class="ma1" bg-color="success" lg @click="confirmRegister.show = true"
                            :disabled="alreadyRegistered">
                     Mettre à jour !
                  </w-button>
               </div>

               <JoinGroup v-if="showJoinGroup"
                          :name="groupSelected.group.name"
                          :id="groupSelected.group.groups_id"
                          :readonly="true"
                          @user-entry="checkEntry"
                          @close="disableGroup"
                          persistent
                          @cancel="removeLast"
               />
            </div>
         </div>

         <div class="container requests">
            <span>Mes demandes :</span>
            <div class="container-content" style="margin-bottom: 15px;">
               <span style="font-size: 18px; font-weight: 600; text-decoration: underline">Demande de rôle Enseignant :</span>
               <div style="width: 100%">
                  <w-button
                        @click="requestTeacher"
                        bg-color="error"
                        :disabled="requests.teacher.requestState || getUser.role === 'TEACHER'"
                  >
                     Demander le rôle Enseignant
                  </w-button>
               </div>

               <span>Etat de ma demande :</span>

               <div class="request">
                  <span v-if="getUser.role === 'TEACHER'">Vous êtes déjà professeur</span>
                  <span v-else-if="JSON.stringify(requests.teacher.request) === JSON.stringify({})">Aucune demande en cours.</span>
                  <w-card
                        v-else
                        success
                        :bg-color="{
                              'ACCEPTEE': 'success',
                              'COURS': 'warning',
                              'REFUSEE': 'error'
                           }[requests.teacher.request.status]"
                        style="width: 70%; height: 70px; display: flex; align-items: center; justify-content: center"
                  >
                     Votre demande {{
                        {
                           "ACCEPTEE": "a été accpetée",
                           "COURS": "est toujours en cours",
                           "REFUSEE": "a été refusée"
                        }[requests.teacher.request.status]
                     }}
                  </w-card>
               </div>
            </div>

            <div class="container-content" v-if="getUser.role === 'TEACHER'">
               <span style="font-size: 18px; font-weight: 600; text-decoration: underline">Demande de création d'établissements :</span>
               <w-button
                     bg-color="error"
                     plain
                     @click="requests.establishments.dialog.show = true;"
               >
                  Faire une demande
               </w-button>

               <div class="pending-requests">
                  <span v-if="requests.establishments.list.length === 0">Aucune requête en cours</span>
                  <div :key="req" v-for="req in requests.establishments.list" :class="req.status === 'ACCEPTEE' ? 'req green' : 'req'">
                     {{ req.name }} | {{ {
                              "ESTABLISHMENT": "Etablissement",
                              "FACULTY": "Université"
                           }[req.type] }}
                  </div>
               </div>

               <w-dialog
                     v-model="requests.establishments.dialog.show"
                     :width="500"
                     title="Demander un établissement"
                     color="grey-dark4"
               >
                  <div
                        style="width: 100%; min-height: 150px;"
                  >
                     <w-form
                           class="establishment-form"
                           @success="askEstablishment"
                     >
                        <w-input
                              :validators="[requests.establishments.dialog.form.required]"
                              v-model="requests.establishments.dialog.form.value.name"
                        >
                           Nom de l'établissement
                        </w-input>
                        <w-input
                              v-model="requests.establishments.dialog.form.value.init"
                              maxlength="4"
                        >
                           Initiales de l'établissement
                        </w-input>

                        <w-select
                         :items="[
                               { label: 'Etablissement', value: 'ESTABLISHMENT' },
                               { label: 'Université', value: 'FACULTY' }
                         ]"
                         v-model="requests.establishments.dialog.form.value.type"

                        >
                           Type d'établissement
                        </w-select>

                        <w-button
                              type="submit"
                              @click="requests.establishments.dialog.show = false;"
                              :disabled="!requests.establishments.dialog.form.value.name || requests.establishments.dialog.form.value.name === '' || !requests.establishments.dialog.form.value.type"
                        >
                           Demander
                        </w-button>
                     </w-form>
                  </div>
               </w-dialog>
            </div>
         </div>
      </div>
   </div>
   <div style="width: 100%; height: 100%;" v-else>
      <Authentication @logSuccess="isAuthentificated = true; updateEstablishment(); $emit('logSuccess');"/>
   </div>

   <w-dialog
         v-model="confirmRegister.show"
         title="Êtes-vous sûr(e) de vouloir vous inscrire dans ces groupes ?"
         persistent
         :width="550">
      Vous pourrez à tout moment changer cette inscription, mais il faudra vous munir des clés de connection fournies
      par votre établissement.

      <template #actions>
         <div class="spacer"/>
         <w-button
               class="mr2"
               @click="confirmRegister.show = false"
               bg-color="error">
            Je modifie ce que j'ai choisi.
         </w-button>
         <w-button
               @click="registerInGroups(false)"
               bg-color="success">
            Je m'inscris !
         </w-button>
      </template>
   </w-dialog>

   <w-dialog
         v-model="confirmCancel.show"
         title="Êtes-vous sûr(e) de vouloir vous effacer l'inscription ?"
         :width="550">
      <w-alert bg-color="warning" color="white">
         N'oubliez pas de cliquer sur mettre à jour si vous souhaitez effacer les groupes.
      </w-alert>

      Vous pourrez à tout moment refaire cette inscription, mais il faudra vous munir des clés de connection fournies
      par votre établissement.

      <template #actions>
         <div class="spacer"/>
         <w-button
               @click="resetSelects(true, true); confirmCancel.show = false;"
               bg-color="success">
            J'efface les groupes selectionnés.
         </w-button>
      </template>
   </w-dialog>

   <w-dialog
         v-if="!fullRegister"
         persistent
         :width="300"

         style="padding: 10px;"
   >
      De nouveaux groupes sont disponibles pour votre cursus.
      Cliquer sur le bouton suivant pour mettre à jour vos matières.

      <template #actions>
         <div class="spacer"/>
         <w-button
               @click="registerInGroups(true); fullRegister = true;"
               bg-color="success">
            Mettre à jour !
         </w-button>
      </template>
   </w-dialog>

   <w-notification error plain bottom v-model="showAlert" :timeout="5000">{{ message }}</w-notification>
   <w-notification success plain bottom v-model="showSuccess" :timeout="5000">{{ message }}</w-notification>
</template>

<script>
import Authentication from "@/components/Authentification";
import {mapActions, mapGetters} from 'vuex'
import axios from "axios";
import SelectGroup from "@/components/common/SelectGroup";
import JoinGroup from "@/components/groups/JoinGroup";

export default {
   name: "Account",
   components: {
      JoinGroup,
      SelectGroup,
      Authentication
   },
   computed: {
      ...mapGetters(['getUser'])
   },
   methods: {
      imageUpload() {
         let file = this.$refs.uploadImage.files[0];
         let temp = new FormData();
         temp.append("file", file);
         axios({
            url: `http://localhost:8080/api/uploadfile/uploadAvatar?user_id=${this.getUser.id}`,
            method: 'POST',
            data: temp,
            headers: {
               Accept: 'application/json',
               'Content-type': 'multipart/form-data'
            }
         }).then(response => {
            this.setLinkToAvatar(response.data);
         })
      },
      removeLinkToAvatar() {
         if (this.getUser.link_to_avatar !== null) {
            axios.delete(`http://localhost:8080/api/uploadfile/deleteAvatar?user_id=${this.getUser.id}`).then(response => {
               if (response.data) {
                  this.setLinkToAvatar(null);
               }
            })
         }
      },
      getUnderGroups(parent_id) {
         return axios.get(`http://localhost:8080/api/groups/underGroup?group_id=${parent_id}`)
      },
      updateGroup(res) {
         this.getUnderGroups(res.group.groups_id).then(rep => {
            if (res.index <= this.groups.length - 1) {
               // Si on change une valeur qui a déjà été entrée
               this.groups = this.groups.filter(elt => (
                     elt.index < res.index
               ))
               // Alors on met à jour les groupes après cette valeur
            }
            this.groups.push({
               index: res.index,
               group: res.group,
               childs: rep.data
            })
         });
      },

      updateEstablishment() {
         axios.get("http://localhost:8080/api/groups/findIDOfDiscoodle").then(response => {
            axios.get(`http://localhost:8080/api/groups/underGroup?group_id=${response.data}`).then(rep => {
               this.establishments = rep.data;
            })
         })
      },

      showGroup(res) {
         this.showJoinGroup = true;
         this.groupSelected = res;
      },
      disableGroup() {
         this.showJoinGroup = false;
         this.groupSelected = {}
      },
      checkEntry(res) {
         if (res.token === this.groupSelected.group.token) {
            if (this.groupSelected.group.type === "ESTABLISHMENT")
               this.updateEstablishment(this.groupSelected)
            else
               this.updateGroup(this.groupSelected);
         } else {
            this.showAlert = true;
            this.message = "Mauvaise clé de groupe."
            this.clearAfter(this.groupSelected.index);
         }
      },

      resetSelects(reset_est, reset_grp) {
         this.alreadyRegistered = false;
         if (reset_grp)
            this.groups = [];
         if (reset_est) {
            this.establishments = [];
            axios.get("http://localhost:8080/api/groups/findIDOfDiscoodle").then(response => {
               axios.get(`http://localhost:8080/api/groups/underGroup?group_id=${response.data}`).then(rep => {
                  this.establishments = rep.data;
               })
            })
         }
      },
      clearAfter(index) {
         if (index === 0) {
            this.resetSelects(true, true);
         } else {
            const temp = this.groups[index - 1];
            this.groups = this.groups.filter(elt => (
                  elt.index < index - 1
            ))
            setTimeout(() => {
               this.groups.push(temp);
            }, 50)
         }
      },
      removeLast() {
         this.showAlert = true;
         this.message = "Annulé"
         this.clearAfter(this.groupSelected.index);
      },

      registerInGroups(update) {
         let noErr = true;
         this.confirmRegister.show = false;
         this.groups.forEach(elt => {
            if (elt.group.type === "GRADE" || elt.group.type === "OTHER") {
               elt.childs.forEach(group => {
                  if (group.type === "SUBJECTS") {
                     axios.post(
                           `http://localhost:8080/api/groups/addNewMemberInGroup?group_id=${group.groups_id}&user_id=${this.getUser.id}&token=${group.token}`
                     ).catch(() => {
                        noErr = false;
                     })
                  }
               })
            }
            if (!update) {
               axios.post(
                     `http://localhost:8080/api/groups/addNewMemberInGroup?group_id=${elt.group.groups_id}&user_id=${this.getUser.id}&token=${elt.group.token}`
               ).catch(() => {
                  noErr = false;
               })
            }
         })

         if (noErr) {
            this.showSuccess = true;
            this.message = "Inscription effectuée avec succès !"
         } else {
            this.showAlert = true;
            this.message = "Erreur lors de l'inscription dans les groupes."
         }
      },

      requestTeacher() {
         axios.post(`http://localhost:8080/api/TeacherRequest/addTeacherRequest?user_id=${this.getUser.id}`).then(response => {
            if (response.data !== null) {
               this.requests.teacher.request = response.data;
               this.requests.teacher.requestState = true;
            } else {
               this.requests.teacher.request = {};
               this.requests.teacher.requestState = false;
            }
         });
      },

      async askEstablishment() {
         await axios.get("http://localhost:8080/api/groups/findIDOfDiscoodle").then(response => {
            axios.post(`http://localhost:8080/api/establishmentRequest/addEstablishmentRequest`, {
               parent_id: response.data,
               user_id: this.getUser.id,
               depth: 2,
               name: this.requests.establishments.dialog.form.value.name,
               description: this.requests.establishments.dialog.form.value.init ? this.requests.establishments.dialog.form.value.init : this.requests.establishments.dialog.form.value.name.substring(0, 4).toUpperCase(),
               type: this.requests.establishments.dialog.form.value.type,
               text: ""
            }).then(rep => {
               this.requests.establishments.list.push(rep.data);
            });
         });
         this.requests.establishments.dialog.form.value = {
            name: null,
            init: null,
            type: null
         }
      },

      ...mapActions(['setLinkToAvatar', 'setUser'])
   },
   data() {
      return {
         isAuthentificated: false,
         groups: [],
         establishments: [],
         typeTranslation: {
            "ESTABLISHMENT": "Établissement",
            "FACULTY": "Université",
            "ADMINISTRATION": "Administration",
            "COURSE": "Filière/cursus",
            "GRADE": "Année",
            "SUBJECTS": "Matière",
            "OTHER": "Autres"
         },
         allowedTypes: [
            "ESTABLISHMENT",
            "FACULTY",
            "ADMINISTRATION",
            "COURSE",
            "GRADE",
            "OTHER"
         ],
         showJoinGroup: false,
         groupSelected: {},
         showAlert: false,
         showSuccess: false,
         message: "",
         confirmRegister: {
            show: false
         },
         confirmCancel: {
            show: false,
         },
         alreadyRegistered: false,
         fullRegister: true,

         requests: {
            teacher: {
               requestState: false,
               request: {}
            },
            establishments: {
               dialog: {
                  show: false,
                  form: {
                     required: value => !!value || "Vous devez remplir ce champ",
                     value: {
                        name: null,
                        init: null,
                        type: null
                     }
                  }
               },
               list: [],
            }
         }
      }
   },
   mounted() {
      this.isAuthentificated = (JSON.stringify(this.getUser) !== JSON.stringify({}));
      if (this.isAuthentificated) {
         axios.get(`http://localhost:8080/api/users/findByUserName?username=${this.getUser.username}`).then(response => {
            this.setUser(response.data);
         });
         this.updateEstablishment();

         axios.get(`http://localhost:8080/api/users/seeAllGroups?user_id=${this.getUser.id}`).then(response => {
            const groups = response.data.sort((a, b) => (
                  a.depth - b.depth
            )).filter(elt => (
                  elt.type !== "SUBJECTS" && elt.type !== "DISCOODLE" && elt.type !== "OTHER"
            ))
            let c = 0;
            groups.forEach(elt => {
               this.getUnderGroups(elt.groups_id).then(rep => {
                  this.groups.push({
                     index: this.groups.length,
                     group: elt,
                     childs: rep.data
                  })
               })
               c++;
            })
            setTimeout(() => {
               this.groups = this.groups.sort((a, b) => (
                     a.group.depth - b.group.depth
               ))
            }, 200)
            this.alreadyRegistered = c > 0;


            // TODO : Améliorer cet algo.
            setTimeout(() => {
               this.groups.forEach(elt => {
                  if (elt.group.type === "GRADE" || elt.group.type === "OTHER") {
                     elt.childs.forEach(e => {
                        if (this.fullRegister) {
                           let temp = false;
                           response.data.forEach(item => {
                              if (JSON.stringify(item) === JSON.stringify(e))
                                 temp = true;
                           })
                           if (!temp)
                              this.fullRegister = false;
                        }
                     });
                  }
               });
            }, 500)
         });

         axios.get(`http://localhost:8080/api/TeacherRequest/getTeacherRequestOfUser?user_id=${this.getUser.id}`).then(response => {
            if (response.data?.status === 'ACCEPTEE')
               this.getUser.role = "TEACHER";
            if (response.data) {
               this.requests.teacher.request = response.data;
               this.requests.teacher.requestState = response.data.status === "COURS" || response.data.status === "ACCEPTEE";
            } else {
               this.requests.teacher.request = {};
               this.requests.teacher.requestState = false;
            }
         });

         axios.get(`http://localhost:8080/api/establishmentRequest/getEstablishmentRequestOfUser?user_id=${this.getUser.id}`).then(response => {
            this.requests.establishments.list = response.data.filter(e => {
               return e.status === 'ACCEPTEE' || e.status === 'COURS'
            });
         });
      }
   }
}
</script>

<style scoped>
.Account {
   position: relative;
   width: 100%;
   height: 100%;

   overflow-y: auto;
   overflow-x: hidden;
   padding: 40px;

   display: flex;
   align-items: flex-start;
   justify-content: flex-start;
}

.Account:before {
   content: "";
   position: absolute;
   width: 100%;
   height: 100%;
   background-image: url("../assets/compte.png");
   background-size: cover;
   top: 0;
   right: 0;
   filter: opacity(0.5) grayscale(0.5);
}

.content {
   position: relative;
   width: 60%;
   min-width: 500px;
   max-width: 700px;

   display: flex;
   flex-direction: column;
}

.container {
   width: 100%;
   min-height: 360px;

   display: flex;
   align-items: flex-start;
   justify-content: space-between;
   flex-direction: column;

   padding: 20px;
}

.container > span {
   font-weight: 600;
   font-size: 25px;
   color: #F4F4F4;
   margin-left: 10px;
}

.container-content {
   background-color: #454150;
   border-radius: 6px;
   padding: 20px;

   width: 100%;
   height: 85%;
   min-height: 280px;

   display: flex;
   align-items: center;
   justify-content: space-between;
   flex-direction: column;
}

.select-groups {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;

   margin-bottom: 10px;
   width: 100%;
   height: 85%;
   min-height: 280px;
}

.logo-and-text {
   padding: 10px;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
}

.logo {
   position: relative;

   min-width: 100px;
   min-height: 100px;
   width: 100px;
   height: 100px;
   background-color: #8F8F8F;
   border-radius: 50%;

   display: flex;
   align-items: center;
   justify-content: center;

   cursor: pointer;
}

.logo:hover {
   background-color: rgba(232, 92, 92, 0.8);
}

.custom-image {
   height: 100%;
   clip-path: circle();
}

.top-part {
   width: 100%;
   height: 52%;
}

.bottom-part {
   width: 100%;
   height: 48%;
}

.account-info {
   display: flex;
   align-items: center;
   justify-content: space-between;
   width: 90%;
}

.account-info > div {
   height: 80%;
   display: flex;
   align-items: flex-start;
   justify-content: space-between;
   flex-direction: column;
}

.account-info > div > span {
   font-size: 18px;
   font-weight: 500;
   color: #F4F4F4;
}


.separator {
   width: 80%;
   margin: auto;
   height: 2px;
   background-color: #8F8F8F;
   border-radius: 1px;
}

.upload-image {
   height: 30px;
   width: 30px;
   border-radius: 50%;
   color: #2d2d2d;
   position: absolute;
   right: 0;
   bottom: 0;
   border: none;
   outline: none;
   cursor: pointer;
   font-size: 20px;
   font-weight: 600;
   display: flex;
   align-items: center;
   justify-content: center;
}

.submit-delete {
   width: 100%;
   height: 30px;

   display: flex;
   align-items: center;
   justify-content: space-between;
}

.cursus > span {
   margin-bottom: 10px;
}

.selected {
   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: space-between;
   width: 100%;
   height: 55px;
   color: #F4F4F4;
   font-weight: 600;
   font-size: 18px;
   margin-bottom: 10px;
}

.selected > div {
   width: 100%;
   height: 28px;
   background-color: #c1c1c1;
   color: #797979;
   border-radius: 4px 4px 0 0;
   display: flex;
   align-items: center;
   justify-content: flex-start;
   padding-left: 10px;
   font-size: 13px;
   font-weight: 500;
   border-bottom: #8F8F8F solid 1px;
   cursor: not-allowed;
}

.requests > div {
   color: white;

   justify-content: space-between;
   align-items: flex-start;
}

.request {
   width: 100%;
   height: 120px;

   display: flex;
   align-items: center;
   justify-content: center;
}

.pending-requests {
   width: 100%;
   min-height: 150px;
   max-height: 150px;
   overflow-y: auto;
}

.establishment-form {
   width: 100%;
   min-height: 150px;
   display: flex;
   align-items: flex-start;
   justify-content: space-between;
   flex-direction: column
}

.establishment-form > * {
   width: 100%;
}

.req {
   margin-bottom: 10px;
   width: 100%;
   padding: 10px;
   height: 40px;
   background-color: #2d2d2d;
   border-radius: 3px;
   display: flex;
   align-items: center;
   justify-content: space-between;
}

.green {
   background-color: #5CE8AD !important;
   color: #F4F4F4 !important;
}
</style>
