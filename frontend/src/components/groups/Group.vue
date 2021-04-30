<template>
   <div class="Group" v-if="getGroup.type === 'SUBJECTS'"><Subject /></div>
   <div class="Group" v-else-if="getGroup.type === 'DISCOODLE'"><Discoodle /></div>
   <div class="Group" v-else>

   </div>
</template>

<script>
import {mapGetters} from "vuex";
import Subject from "@/components/groups/Subject";
import Discoodle from "@/components/groups/Discoodle";

export default {
   name: "Group",
   components: {Discoodle, Subject},
   data() {
      return {}
   },
   methods: {},
   computed: {
      ...mapGetters(['getGroup'])
   }
}
</script>

<style scoped>
.Group {
   width: calc(100% - 150px);
   height: 100%;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
}


</style>