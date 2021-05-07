<template>
   <div class="SubjectSettings">
      <div class="row">
         <div class="group-info card">
            <span class="card-title">Informations du groupe :</span>
            <w-alert info plain>Ce sont ces informations que vous devez transmettre pour vous connecter au groupe</w-alert>
            <span>Identifiant du groupe : <strong>{{ getGroup.groups_id }}</strong></span>
            <span>Clé de connection au groupe : <strong>{{ getGroup.token }}</strong></span>
         </div>

         <div class="users-list card">
            <span class="card-title" style="height: 12%; max-height: 12%;">Utilisateurs du groupes : (vous exclus)</span>
            <div style="height: 75%; width: 100%; max-height: 75%;">
               <w-table
                     class="users-table"
                     :headers="users.headers"
                     :items="users.list"
                     :selectable-rows="true"
                     @row-select="users.selection = $event.selectedRows"
               >
                  <template #no-data>
                     Il n'y a pas d'utilisateurs dans le groupe.
                  </template>
               </w-table>
            </div>
            <div style="display: flex; align-items: center; justify-content: space-between; width: 100%; height: 7%; max-height: 7%">
               <w-button class="ma1" bg-color="error" :disabled="users.selection.length === 0" @click="removeUsers">Supprimer la sélection</w-button>
               <w-button class="ma1" bg-color="error" :disabled="users.list.length === 0" @click="removeAll">Effacer tout</w-button>
            </div>
         </div>

      </div>

      <div class="row">
         <div class="card option-panel">
            <span class="card-title">Configuration :</span>

            <w-button bg-color="error" @click="addGroup.show = true" style="align-self: flex-start">Ajouter un sous-groupe</w-button>
            <w-dialog
                  v-model="addGroup.show"
                  :fullscreen="addGroup.fullscreen"
                  :width="addGroup.width"
                  :persistent="addGroup.persistent"
                  :persistent-no-animation="addGroup.persistentNoAnimation"
            >
               <div class="addGroup">
                  <span style="margin-bottom: 10px; font-size: 19px; font-weight: 600; color: #454150">Créer un groupe :</span>
                  <w-input autocomplete="off" color="grey-dark3" required style="margin-bottom: 10px; width: 90%" v-model="addGroup.data.name">
                     Nom du sous-groupe.
                  </w-input>
                  <w-input autocomplete="off" color="grey-dark3" required style="margin-bottom: 10px; width: 90%" v-model="addGroup.data.init">
                     Initiales du groupe
                  </w-input>

                  <span style="color: #565656">Type de groupe :</span>
                  <w-select color="grey-dark3" required style="margin-bottom: 10px; width: 90%; z-index: 500" v-model="addGroup.data.type"
                            :items="groupTypes.map(e => {
                         return {
                            label: typeTranslation[e.label]
                         }
                      })">
                  </w-select>
                  <w-button bg-color="error" @click="add">
                     Créer !
                  </w-button>
               </div>
            </w-dialog>

            <div class="uploadFile">
               <label for="fileUp">Ajouter une page d'accueil </label>
               <input id="fileUp" ref="uploadInput" type="file" accept=".md,.txt" @change="onSubjectUpload(); uploaded = true">
               <button class="validate" v-if="uploaded" @click="uploadFile(); uploaded = false">✔</button>
               <button class="cancel" v-if="uploaded" @click="uploaded = false">X</button>
            </div>
            <span v-if="uploaded" style="color: #F4F4F4">{{ $refs.uploadInput.files[0].name }}</span>
         </div>
      </div>

      <div class="row" style="height: 480px;">
         <div class="card role-edit">
            <span class="card-title">
               Rôles du groupe :
            </span>

            <div class="role-nav">
               <div class="roles-list">
                  <button :key="role.role_id"
                          v-for="role in getGroup.roles"
                          :style="role.name === roleToEdit.role.name ? { color: '#f65555', fontWeight: 600 } : {}"
                          @click="changeRoleShown(role)">
                     {{ role.name }}
                  </button>
               </div>
               <div class="rights" v-if="JSON.stringify({}) !== JSON.stringify(roleToEdit.role)">
                  <span style="font-size: 18px; font-weight: 600;">Rôle : {{ roleToEdit.role.name }}</span>

                  <div class="right">
                     <span>Permission d'envoyer des messages : </span>
                     <w-switch
                           class="ma2"
                           :value="true"
                           color="success"
                           v-model="roleToEdit.rights.canSendMessage"
                           @input="roleEdited = true"
                     >
                     </w-switch>
                  </div>

                  <div class="right">
                     <span>Permission de lire des messages : </span>
                     <w-switch
                           class="ma2"
                           :value="true"
                           color="success"
                           v-model="roleToEdit.rights.canReadMessage"
                           @input="roleEdited = true"
                     >
                     </w-switch>
                  </div>

                  <div class="right">
                     <span>Permission de modifier le groupe : </span>
                     <w-switch
                           class="ma2"
                           :value="true"
                           color="success"
                           v-model="roleToEdit.rights.canChangeGroup"
                           @input="roleEdited = true"
                     >
                     </w-switch>
                  </div>

                  <div class="right">
                     <span>Permission de modifier les salons de discussions : </span>
                     <w-switch
                           class="ma2"
                           :value="true"
                           color="success"
                           v-model="roleToEdit.rights.canModifyRoom"
                           @input="roleEdited = true"
                     >
                     </w-switch>
                  </div>

                  <div class="right">
                     <span>Permission de modifier les notes : </span>
                     <w-switch
                           class="ma2"
                           :value="true"
                           color="success"
                           v-model="roleToEdit.rights.canModifyNotes"
                           @input="roleEdited = true"
                     >
                     </w-switch>
                  </div>

                  <div class="right">
                     <span>Permission de lancer un stream : </span>
                     <w-switch
                           class="ma2"
                           :value="true"
                           color="success"
                           v-model="roleToEdit.rights.canStream"
                           @input="roleEdited = true"
                     >
                     </w-switch>
                  </div>

                  <w-alert class="alert" v-if="roleEdited" color="success">
                     Enregistrer les modifications ?
                     <w-button
                        bg-color="success"
                        @click="editRole"
                     >
                        Oui !
                     </w-button>
                  </w-alert>
               </div>
               <div v-else style="font-size: 18px; font-weight: 600;">
                  Sélectionner un rôle pour le modifier.
               </div>
            </div>
         </div>
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
   name: "SubjectSettings",
   data() {
      return {
         addGroup: {
            show: false,
            fullscreen: false,
            persistent: false,
            persistentNoAnimation: false,
            width: 300,
            data: {
               name: "",
               init: "",
               type: "",
            }
         },
         users: {
            list: [],
            headers: [
               { label: 'Nom', key: 'last_name' },
               { label: 'Prénom', key: 'name' },
               { label: 'Pseudo', key: 'username' },
               { label: 'Rôle', key: 'role' }
            ],
            selection: []
         },
         typeTranslation: {
            "ESTABLISHMENT": "Établissement",
            "FACULTY": "Université",
            "ADMINISTRATION": "Administration",
            "COURSE": "Filière/cursus",
            "GRADE": "Année",
            "SUBJECTS": "Matière",
            "OTHER": "Autres"
         },
         types: [
            {label: "ESTABLISHMENT", order: 1},
            {label: "FACULTY", order: 1},
            {label: "ADMINISTRATION", order: 2},
            {label: "SUBJECTS", order: 3},
            {label: "OTHER", order: 4},
         ],
         groupTypes: [],
         uploaded: false,
         formData: null,
         roleToEdit: {
            rights: {

            },
            role: {

            }
         },
         roleEdited: false
      }
   },
   methods: {
      add(event) {
         if (this.addGroup.data.init.length > 4)
            this.addGroup.data.init = this.addGroup.data.init.replaceAll(" ", "").substring(0, 4);
         if ((event.type === 'keydown' && event.keyCode === 13) || event.type === 'click') {
            axios.post("http://localhost:8080/api/groups/addNewGroup", {
               parent_id: this.getGroup.groups_id,
               user_id: this.getUser.id,
               depth: this.getGroup.depth + 1,
               description: this.addGroup.data.init,
               name: this.addGroup.data.name,
               type: Object.keys(this.typeTranslation).find(key => this.typeTranslation[key] === this.addGroup.data.type),
               text: ""
            }).then(response => {
               this.$emit('groupAdded', response.data);
            })
            this.addGroup.show = false;
         }
      },
      removeUsers() {
         this.users.selection.forEach(elt => {
            this.users.list.splice(this.users.list.indexOf(elt), 1);
            // TODO : remove du groupe, le user en question.
         })
      },
      removeAll() {
         this.users.list = [];
      },
      getOrder(type) {
         let res = -1;
         this.types.forEach(elt => {
            if (elt.label === type) {
               res = elt.order;
            }
         })
         return res;
      },
      onSubjectUpload() {
         let file = this.$refs.uploadInput.files[0];
         this.formData = new FormData();
         this.formData.append("file", file);
      },
      uploadFile() {
         axios({
            url: `http://localhost:8080/api/uploadfile/uploadSubject?group_id=${this.getGroup.groups_id}`,
            method: 'POST',
            data: this.formData,
            headers: {
               Accept: 'application/json',
               'Content-type': 'multipart/form-data'
            }
         })
      },
      changeRoleShown(role) {
         if (!this.roleEdited) {
            this.roleToEdit.role = role;
            if (role.rights === "*") {
               this.roleToEdit.rights = {
                  canSendMessage: true,
                  canReadMessage: true,
                  canChangeGroup: true,
                  canModifyRoom: true,
                  canModifyNotes: true,
                  canStream: true,
               }
            } else {
               const temp = role.rights;
               this.roleToEdit.rights = {
                  canSendMessage: temp.includes("s"),
                  canReadMessage: temp.includes("r"),
                  canChangeGroup: temp.includes("p"),
                  canModifyRoom: temp.includes("c"),
                  canModifyNotes: temp.includes("n"),
                  canStream: temp.includes("l"),
               }
            }
         }
      },
      editRole() {
         this.roleEdited = false
         console.log(this.roleToEdit);
         let right = "";
         if (this.roleToEdit.rights.canSendMessage)
            right += "s";
         if (this.roleToEdit.rights.canReadMessage)
            right += "r";
         if (this.roleToEdit.rights.canChangeGroup)
            right += "p";
         if (this.roleToEdit.rights.canModifyRoom)
            right += "c";
         if (this.roleToEdit.rights.canModifyNotes)
            right += "n";
         if (this.roleToEdit.rights.canStream)
            right += "l";
         if (right.length === 0)
            right = "-"
         axios.put(`http://localhost:8080/api/groups/modifyRightsForRole?role_id=${this.roleToEdit.role.role_id}&rights=${right}`)
      }
   },
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
   mounted() {
      this.groupTypes = this.types.filter(elt => elt.order > this.getOrder(this.getGroup.type));
      this.users.list = this.getGroup.users.filter(e => e.id !== this.getUser.id).sort((a, b) => {
         if (a.last_name < b.last_name)
            return -1;
         if (a.last_name > b.last_name)
            return 1;
         if (a.last_name === b.last_name)
            return 0;
      });
      console.log(this.getGroup);
   }
}
</script>

<style scoped>
.SubjectSettings {
   height: calc(100vh - 100px);
   width: 100%;
   padding: 40px;

   display: flex;
   flex-direction: column;
   justify-content: flex-start;
   align-items: center;

   overflow-y: auto;
   overflow-x: hidden;
}

.addGroup {
   margin-top: 30px;
   height: 220px;
   width: 100%;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}

.uploadFile {
   height: 24px;
   border-radius: 3px;
   display: flex;
   align-items: center;
   align-self: flex-start;
   justify-content: space-between;
}

#fileUp {
   height: 0;
   width: 0;
}

label[for="fileUp"] {
   color: #F4F4F4;
   background-color: #f65555;
   cursor: pointer;
   border-radius: 3px;
   height: 24px;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 14px;
   font-weight: 500;
   padding-left: 8px;
   padding-right: 8px;
   align-self: center;
}

.validate {
   height: 35px;
   width: 35px;
   color: #F4F4F4;
   border: none;
   outline: none;
   border-radius: 50%;
   background-color: #61e85c
}

.cancel {
   height: 35px;
   width: 35px;
   color: #F4F4F4;
   border: none;
   outline: none;
   border-radius: 50%;
   background-color: #fc3737;
}

.group-info {
   width: calc(40% - 10px);
}

.users-list {
   font-weight: 600;
   height: 460px !important;
   width: calc(60% - 10px);
}

.users-table {
   width: 100%;
}

.option-panel {
   width: calc(40% - 10px);
}

.row {
   width: 100%;
   height: 240px;
   padding: 10px 20px 10px 20px;
   border-radius: 5px;

   display: flex;
   align-items: flex-start;
   justify-content: space-between;
}

.card {
   height: 220px;
   background-color: #4f4b5a;
   color: #F4F4F4;
   padding: 15px;
   border-radius: 5px;
   font-size: 14px;
   display: flex;
   align-items: flex-start;
   justify-content: space-between;
   flex-direction: column;
}

.card-title {
   text-decoration: underline;
   font-size: 20px;
   font-weight: 600;
}

.role-edit {
   width: 50%;
   height: 460px;
}

.role-nav {
   display: flex;
   align-items: flex-start;
   justify-content: space-between;

   width: 100%;
   height: 90%;
}

.roles-list {
   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: flex-start;

   max-width: 20%;
   width: 20%;

   overflow-y: auto;
}

.roles-list > button {
   background-color: transparent;
   outline: none;
   border: none;
   color: #F4F4F4;

   width: 100%;
   height: 20px;
   display: flex;
   align-items: center;

   margin-bottom: 5px;
   font-size: 17px;

   cursor: pointer;
}

.rights {
   max-width: 80%;
   width: 80%;
   height: 100%;
   padding: 10px;

   display: flex;
   flex-direction: column;

   border: #2d2d2d 2px solid;
   border-radius: 3px;
}

.right {
   display: flex;
   align-items: center;
   justify-content: space-between;
}

.alert {
   display: flex;
   align-items: center;
   justify-content: space-between;
}
</style>