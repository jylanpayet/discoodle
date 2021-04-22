<template>
  <div class="global">
    <form ref="uploadForm" @submit.prevent="submit">
      <input type="file" ref="upload" @change="onFileUpload()" class="form-control" required>
      <input type="button" @click="startUpload" name="Upload" value=" Upload ">
    </form>

    <form ref="uploadSubjectForm" @submit.prevent="submit">
      <input type="file" ref="uploadSubject" @change="onSubjectUpload()" class="form-control" required>
      <input type="button" @click="startSubjectUpload" name="Upload" value=" Upload ">
    </form>
    <form ref="uploadImageForm" @submit.prevent="submit">
      <input type="file" ref="uploadImage" @change="onImageUpload()" class="form-control" required>
      <input type="button" @click="startImageUpload" name="Upload" value=" Upload ">
    </form>

  </div>
</template>

<script>

import axios from "axios";
export default {
  name:"UploadFile",
  data: () =>({
    formData: null
  }),
  methods:{
    onFileUpload(){
      let file=this.$refs.upload.files[0];
      this.formData=new FormData();
      this.formData.append("file",file);
    },
    onSubjectUpload(){
      let file=this.$refs.uploadSubject.files[0];
      this.formData=new FormData();
      this.formData.append("file",file);
    },
    onImageUpload(){
      let file=this.$refs.uploadImage.files[0];
      this.formData=new FormData();
      this.formData.append("file",file);
    },
    startUpload() {
      axios({
        url: `http://localhost:8080/api/upload/1007`,
        method: 'POST',
        data: this.formData,
        headers: {
          Accept: 'application/json',
          'Content-type': 'multipart/form-data'
        }
      }).then(response =>{
        console.log(JSON.stringify(response.data))
      })
    },
    startImageUpload() {
      axios({
        url: `http://localhost:8080/api/uploadAvatar/1`,
        method: 'POST',
        data: this.formData,
        headers: {
          Accept: 'application/json',
          'Content-type': 'multipart/form-data'
        }
      }).then(response =>{
        console.log(JSON.stringify(response.data))
      })
    },
    startSubjectUpload() {
      axios({
        url: `http://localhost:8080/api/uploadSubject/1007`,
        method: 'POST',
        data: this.formData,
        headers: {
          Accept: 'application/json',
          'Content-type': 'multipart/form-data'
        }
      }).then(response =>{
        console.log(JSON.stringify(response.data))
      });
    }
  }
}
</script>