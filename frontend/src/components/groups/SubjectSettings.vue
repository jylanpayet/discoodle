<template>
   <div class="SubjectSettings">
      <button class="submit" @click="addGroup.show = true">Ajouter un sous-groupe</button>
      <w-dialog
            v-model="addGroup.show"
            :fullscreen="addGroup.fullscreen"
            :width="addGroup.width"
            :persistent="addGroup.persistent"
            :persistent-no-animation="addGroup.persistentNoAnimation"
      >
         <div class="addGroup">
            <span style="margin-bottom: 10px; font-size: 19px; font-weight: 600; color: #454150">Créer un groupe :</span>
            <w-input color="darkgray" required style="margin-bottom: 10px; width: 90%" name="groupName">
               Nom du sous-groupe.
            </w-input>
            <w-input color="darkgray" required style="margin-bottom: 10px; width: 90%" name="groupDescription">
               Initiales du groupe
            </w-input>

            <span>Type de groupe :</span>
            <w-select color="darkgray" required style="margin-bottom: 10px; width: 90%; z-index: 500" name="groupKey"
                      :items="groupTypes.map(e => {
                         return {
                            label: typeTranslation[e.label]
                         }
                      })">
            </w-select>
            <button class="submit" @click="add">
               Créer !
            </button>
         </div>
      </w-dialog>


      <div class="uploadFile">
         <label for="fileUp">Ajouter un fichier markdown</label>
         <input id="fileUp" ref="uploadInput" type="file" accept=".md" @change="onSubjectUpload(); uploaded = true">
         <button class="validate" v-if="uploaded" @click="uploadFile(); uploaded = false">✔</button>
         <button class="cancel" v-if="uploaded" @click="uploaded = false">X</button>
      </div>
      <span v-if="uploaded" style="color: #F4F4F4">{{ $refs.uploadInput.files[0].name }}</span>
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
            width: 300
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
         formData: null
      }
   },
   methods: {
      add(event) {
         if ((event.type === 'keydown' && event.keyCode === 13) || event.type === 'click') {
            axios.post("http://localhost:8080/api/groups/addNewGroup", {
               parent_id: this.getGroup.groups_id,
               user_id: this.getUser.id,
               depth: this.getGroup.depth + 1,
               description: document.querySelector("input[name='groupDescription']").value,
               name: document.querySelector("input[name='groupName']").value,
               type: document.querySelector("input[name='groupKey']").value,
               text: ""
            }).then(response => {
               console.log(response);
            })
            this.addGroup.show = false;
         }
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
      }
   },
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
   mounted() {
      this.groupTypes = this.types.filter(elt => elt.order > this.getOrder(this.getGroup.type));

      console.log(this.groupTypes);
      console.log(this.groupTypes.map(e => this.typeTranslation[e.label]));
   }
}
</script>

<style scoped>
.SubjectSettings {
   height: calc(100vh - 100px);
   width: 100%;
   padding: 40px;
}

.submit {
   height: 35px;
   background-color: #E85C5C;
   padding-left: 10px;
   padding-right: 10px;
   font-size: 15px;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 999px;
   color: #F4F4F4;
   cursor: pointer;
}

.addGroup {
   height: 220px;
   width: 100%;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}

.uploadFile {
   height: 70px;
   width: 340px;
   border: #454150;
   border-radius: 12px;
   display: flex;
   align-items: center;
   justify-content: space-between;
}

#fileUp {
   height: 0;
   width: 0;
}

label[for="fileUp"] {
   color: #F4F4F4;
   background-color: #E85C5C;
   cursor: pointer;
   border-radius: 5000px;
   padding-left: 10px;
   padding-right: 10px;
   height: 35px;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 15px;
   font-weight: 600;
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
</style>