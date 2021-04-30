<template>
   <div class="SubjectChat">
      <div class="rooms-list">
         <div class="server-name">
            {{ getGroup.server.name }}
         </div>
         <router-link @click="setConvUUID(channel.room_id)" :to="`/groupes/subject/${getGroup.groups_id}/discussion/${channel.room_id}?name=${channel.room_name}`" class="room" :key="channel.room_id" v-for="channel in getGroup.server.rooms">
            # {{ channel.room_name }}
         </router-link>
      </div>
      <div class="separator"></div>
      <div class="chat">
         <router-view />
      </div>
   </div>
</template>

<script>
import {mapActions, mapGetters} from "vuex";

export default {
   name: "SubjectChat",
   computed: {
      ...mapGetters(['getGroup', 'getUser'])
   },
   methods: {
      ...mapActions(['setConvUUID'])
   },
   mounted() {
      console.log(this.getGroup);
   },
   beforeRouteUpdate() {
      console.log(this.getGroup);
   }
}
</script>

<style scoped>
.SubjectChat {
   height: 100%;
   width: 100%;

   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: center;
}

.rooms-list {
   height: 100%;
   width: 160px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
   margin-right: 5px;
}

.rooms-list > * {
   margin-bottom: 5px;
}

.chat {
   height: 100%;
   width: calc(100% - 167px);
   max-width: calc(100% - 167px);
}

.server-name {
   font-size: 100%;
   font-weight: 600;
   color: #F4F4F4;
   width: 100%;
   height: 50px;
   display: flex;
   align-items: center;
   justify-content: center;
   margin-bottom: 10px;
}

.room {
   height: 30px;
   width: 160px;
   font-size: 18px;
   font-weight: 600;
   display: flex;
   align-items: center;
   justify-content: flex-start;
   padding-left: 10px;
   color: #F4F4F4;

   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
}

.router-link-active {
   color: #E85C5C;
}

.separator {
   height: 80%;
   margin: auto;
   width: 2px;
   background-color: #8F8F8F;
   border-radius: 1px;
}
</style>