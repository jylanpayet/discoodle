<template>
   <div class="SubjectNotes">
      <w-table
            :headers="table.headers"
            :items="table.items"
            :selectable-rows="table.selectableRows"
            @row-select="table.noteSelection = $event"
            class="notes"
      >
         <template #no-data>
            Vous n'avez aucune note pour l'instant.
         </template>
      </w-table>

      <w-button class="ma1" bg-color="error" @click="addNote = true" v-if="rights.canModifyNotes">Ajouter une note</w-button>
      <w-button class="ma1" bg-color="error" @click="removeNote" v-if="rights.canModifyNotes">Supprimer la note</w-button>

      <w-dialog
            v-model="addNote"
            title="Ajouter une ou plusieurs notes"
            :width="550"
            persistent
      >
         <template #actions>
            <w-form class="note-content" no-blur-validation>
               <w-input color="grey-dark5" :value="inputNote.name" @input="inputNote.name = $event">Nom de la note :</w-input>

               <w-select color="grey-dark5"
                  :items="getGroup.users.map(e => {
                     return {
                        label: e.name + ' ' + e.last_name + ' (' + e.username + ')',
                        value: e
                     }
                  })"
                  label="Élève :"
                  @input="inputNote.user = $event"
               >
               </w-select>

               <w-input
                  color="grey-dark5"
                  type="number" min="0" :max="maxNote"
                  @input="inputNote.note = $event"
               >
                  Note{{ (maxNote && maxNote !== "") ? "" : ` (/ ${maxNote})` }}
               </w-input>

               <w-input
                  color="grey-dark5"
                  @input="inputNote.coef = $event"
                  type="number" min="0"
               >
                  Coefficient
               </w-input>

               <w-input
                  required
                  color="grey-dark5"
                  type="number" min="0" max="1000"
                  v-model="maxNote"
                  @input="changeMaxNote"
               >
                  Note maximale :
               </w-input>

               <div class="buttons">
                  <w-button class="ma1" bg-color="error" @click="cancel">Annuler</w-button>
                  <w-button class="ma1" type="reset" bg-color="warning">Effacer</w-button>
                  <w-button class="ma1" type="submit" bg-color="success" :disabled="!formWellFilled()" @click="addNoteForUser">Ajouter la note</w-button>
               </div>
            </w-form>
         </template>
      </w-dialog>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
   name: "SubjectNotes",
   data() {
      return {
         table: {
            headers: [
               { label: 'Nom', key: 'name' },
               { label: 'Coefficient', key: 'coef' },
               { label: 'Note (/20)', key: 'note' },
            ],
            selectableRows: 1,
            items: [],
            noteSelection: {}
         },
         addNote: false,
         inputNote: {
            name: "",
            user: {},
            note: "",
            coef: ""
         },
         maxNote: 20,
         validators: {
            required: value => !!value || 'Vous devez entrer une valeur ici.'
         },
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
   methods: {
      log(event) {
         console.log(event);
      },
      changeMaxNote(event) {
         this.maxNote = event;
      },
      cancel() {
         this.addNote = false;
         this.inputNote = {
            name: "",
            user: {},
            note: "",
            coef: ""
         };
         this.maxNote = 20;
      },
      formWellFilled() {
         return Boolean(this.inputNote.coef !== "" && this.inputNote.name !== "" && this.inputNote.note !== "" && this.inputNote.user);
      },
      addNoteForUser() {
         if (this.formWellFilled()) {
            axios.post("http://localhost:8080/api/note/addNewNote", {
               user_id: this.inputNote.user.id,
               group_id: this.getGroup.groups_id,
               note: (Number(this.inputNote.note) / this.maxNote) * 20,
               coef: Number(this.inputNote.coef),
               titre: this.inputNote.name
            }).then(response => {
               this.table.items.push({
                  id: response.data.note_id,
                  name: response.data.titre,
                  coef: response.data.coef,
                  note: response.data.note
               })
            });
         }
         this.addNote = false;
      },
      removeNote() {
         console.log(this.table.noteSelection);
         if (this.table.noteSelection.item?.id >= 0) {
            axios.delete(`http://localhost:8080/api/note/deleteNoteById?note_id=${this.table.noteSelection.item.id}`).then(() => {
               this.table.items = this.table.items.filter(e => {
                  return e.id !== this.table.noteSelection.item.id;
               });
            });
         }
      },
   },
   computed: {
      ...mapGetters(['getGroup', 'getUser']),
   },
   beforeRouteUpdate() {
      if (this.getUser.role === "STUDENT") {
         axios.get(`http://localhost:8080/api/note/getUserNoteByGroupId?group_id=${this.getGroup.groups_id}&user_id=${this.getUser.id}`).then(response => {
            this.table.items = response.data.map(e => {
               return {
                  id: e.note_id,
                  name: e.titre,
                  coef: e.coef,
                  note: e.note
               }
            });

            if (response.data.length > 0) {
               let moyenne = 0;
               let coefSumm = 0;
               this.table.items.forEach(elt => {
                  moyenne += (elt.note * elt.coef);
                  coefSumm += elt.coef;
               })
               moyenne = moyenne / coefSumm;
               this.table.items.push({
                  id: -1,
                  name: "Moyenne générale".toUpperCase(),
                  coef: "-",
                  note: moyenne.toFixed(2)
               })
            }
         })
      }

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

         if (this.rights.canModifyNotes) {
            axios.get(`http://localhost:8080/api/note/getAllNoteByGroupId?group_id=${this.getGroup.groups_id}`).then(response => {
               console.log(response);
               this.table.items = response.data.map(e => {
                  return {
                     id: e.id,
                     name: e.titre,
                     coef: e.coef,
                     note: e.note
                  }
               });

               if (response.data.length > 0) {
                  let moyenne = 0;
                  let coefSumm = 0;
                  this.table.items.forEach(elt => {
                     moyenne += (elt.note * elt.coef);
                     coefSumm += elt.coef;
                  })
                  moyenne = moyenne / coefSumm;
                  this.table.items.push({
                     id: -1,
                     name: "Moyenne de la classe :".toUpperCase(),
                     coef: "-",
                     note: moyenne.toFixed(2)
                  })
               }
            })
         }
      })
   },
   mounted() {
      if (this.getUser.role === "STUDENT") {
         axios.get(`http://localhost:8080/api/note/getUserNoteByGroupId?group_id=${this.getGroup.groups_id}&user_id=${this.getUser.id}`).then(response => {
            this.table.items = response.data.map(e => {
               return {
                  id: e.note_id,
                  name: e.titre,
                  coef: e.coef,
                  note: e.note
               }
            });

            if (response.data.length > 0) {
               let moyenne = 0;
               let coefSumm = 0;
               this.table.items.forEach(elt => {
                  moyenne += (elt.note * elt.coef);
                  coefSumm += elt.coef;
               })
               moyenne = moyenne / coefSumm;
               this.table.items.push({
                  id: -1,
                  name: "Moyenne générale".toUpperCase(),
                  coef: "-",
                  note: moyenne.toFixed(2)
               })
            }
         })
      }

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

         if (this.rights.canModifyNotes) {
            axios.get(`http://localhost:8080/api/note/getAllNoteByGroupId?group_id=${this.getGroup.groups_id}`).then(response => {
               this.table.items = response.data.map(e => {
                  return {
                     id: e.note_id,
                     name: e.titre,
                     coef: e.coef,
                     note: e.note
                  }
               });

               if (response.data.length > 0) {
                  let moyenne = 0;
                  let coefSumm = 0;
                  this.table.items.forEach(elt => {
                     moyenne += (elt.note * elt.coef);
                     coefSumm += elt.coef;
                  })
                  moyenne = moyenne / coefSumm;
                  this.table.items.push({
                     id: -1,
                     name: "Moyenne de la classe :".toUpperCase(),
                     coef: "-",
                     note: moyenne.toFixed(2)
                  })
               }
            })
         }
      })
   }
}
</script>

<style scoped>
.SubjectNotes {
   height: calc(100vh - 100px);
   width: 100%;

   padding: 50px;
}

.notes {
   color: #F4F4F4;
   font-weight: 600;

   background-color: #4f4b5a;

   margin-bottom: 30px;
}

.note-content {
   width: 100%;
}

.note-content > * {
   margin-bottom: 10px;
}

.buttons {
   margin-top: 20px;
   display: flex;
   justify-content: space-between;
}
</style>