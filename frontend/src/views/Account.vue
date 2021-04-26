<template>
   <div class="Account" v-if="isAuthentificated">
      <div class="content">
         <div class="container">
            <span>Votre profil :</span>
            <div class="container-content">
               <div class="logo-and-text top-part">
                  <label class="logo">
                     <input ref="uploadImage" type="file" style="width: 0; height: 0;" @change="imageUpload()" v-if="getUser.link_to_avatar === null">
                     <span style="font-size: 55px; font-weight: 600; color: #F4F4F4" v-if="getUser.link_to_avatar === null">{{ getUser.name.charAt(0).toUpperCase() }}</span>
                     <img :src="getUser.link_to_avatar" alt="" v-else class="custom-image avatar">
                     <span class="upload-image" @click="removeLinkToAvatar" :style="getUser.link_to_avatar === null ? { backgroundColor: '#F4F4F4' } : { backgroundColor: 'rgba(232, 92, 92, 0.8)', color: '#F4F4F4 !important' }">
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
               <SelectGroup @selected="updateGroup" :groups="establishments.map(elt => {
                  return {
                     label: elt.name,
                     value: elt
                  }
               })" :placeholder="typeTranslation['ESTABLISHMENT']" :index="0"/>

               <SelectGroup @selected="updateGroup" :key="elt.id" v-for="elt in groups" :groups="elt.childs.map(e => {
                  return {
                     label: e.name,
                     value: e
                  }
               })" :placeholder="typeTranslation[elt.childs[0]?.type]" :index="elt.index+1"/>


            </div>
         </div>
      </div>
   </div>
   <div style="width: 100%; height: 100%;" v-else>
      <Authentication @logSuccess="isAuthentificated = true; $emit('logSuccess')" />
   </div>
</template>

<script>
import Authentication from "@/components/Authentification";
import {mapActions, mapGetters} from 'vuex'
import axios from "axios";
import SelectGroup from "@/components/common/SelectGroup";

export default {
   name: "Account",
   components: {
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
         temp.append("file",file);
         axios({
            url: `http://localhost:8080/api/uploadAvatar/${this.getUser.id}`,
            method: 'POST',
            data: temp,
            headers: {
               Accept: 'application/json',
               'Content-type': 'multipart/form-data'
            }
         }).then(response =>{
            this.setLinkToAvatar(response.data);
         })
      },
      removeLinkToAvatar() {
         if (this.getUser.link_to_avatar !== null) {
            axios.delete(`http://localhost:8080/api/deleteAvatar/${this.getUser.id}`).then(response => {
               if (response.data) {
                  this.setLinkToAvatar(null);
               }
            })
         }
      },
      getUnderGroups(parent_id) {
         return axios.get(`http://localhost:8080/api/groups/underGroup/${parent_id}`)
      },
      updateGroup(res) {
         this.getUnderGroups(res.group.groups_id).then(rep => {
            // TODO : si index < length, réindexer et supprimer les groups suivants
            if (res.index < this.groups.length - 1) {
               this.groups.push({
                  index: res.index,
                  group: res.group,
                  childs: rep.data
               })
            } else {
               this.groups.push({
                  index: res.index,
                  group: res.group,
                  childs: rep.data
               })
            }

         });
      },
      ...mapActions(['setLinkToAvatar'])
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
         }
      }
   },
   mounted() {
      this.isAuthentificated = (JSON.stringify(this.getUser) !== JSON.stringify({}));
      if (this.isAuthentificated) {
         axios.get("http://localhost:8080/api/groups/findIDOfDiscoodle").then(response => {
            axios.get(`http://localhost:8080/api/groups/underGroup/${response.data}`).then(rep => {
               this.establishments = rep.data;
            })
         })
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

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;

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

.cursus {

}

.cursus > div {
   justify-content: flex-start;
}
</style>
