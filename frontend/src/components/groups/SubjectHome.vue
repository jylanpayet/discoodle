<template>
   <div class="SubjectHome">
      <div class="content" v-html="content">
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
         content: ""
      }
   },
   mounted() {
      axios.get(`http://localhost:8080/common/groups/${this.getGroup.groups_id}/cours.md`).then(rep => {
         this.content = marked(rep.data);
      }).catch(err => {
         console.log(err);
      });
   },
   computed: {
      ...mapGetters(['getGroup'])
   }
}
</script>

<style scoped>
.SubjectHome {
   height: calc(100vh - 100px);
   width: 100%;
   padding: 40px;
   overflow-y: auto;
   overflow-x: hidden;
}

.content {
   color: #F4F4F4;
   font-size: 15px;
   width: 80%;
}
</style>