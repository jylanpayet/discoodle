<template>
   <div class="Messages">
      <div class="left-pannel">
         <div class="settings-box">
            <div>
               <div class="add-conversation">
                  +
               </div>
               <div class="settings"><img src="../assets/settings.png" alt="Settings" style="height: 100%; width: 100%;"></div>
            </div>
         </div>
         <div class="convs-list">
            <router-link :key="convs.uuid" v-for="convs in convList" :to="`/messages/${convs.uuid}?name=${convs.name}`" @click="setConvUUID(convs.uuid)">
               <div class="link-content">
                  <div class="user-logo">
                     {{ convs.name.charAt(0).toUpperCase() }}
                  </div>
                  <div class="conv-name">
                     {{ convs.name }}
                  </div>
               </div>
            </router-link>
         </div>
      </div>
      <div class="right-pannel">
         <router-view />
      </div>
   </div>
</template>

<script>
import {mapActions, mapGetters} from "vuex";

export default {
   name: "Messages",
   components: {
   },
   computed: {
      ...mapGetters(['getColors', 'getTheme'])
   },
   methods: {
      ...mapActions(['setConvUUID'])
   },
   data() {
      return {
         convList: [
            {
               uuid: "e485c6f4-72ca-11eb-9439-0242ac130002",
               name: "Bob"
            },
            {
               uuid: "e6e5c4c6-72ca-11eb-9439-0242ac130002",
               name: "Dylan"
            },
            {
               uuid: "b326cd8e-6468-4592-ba9c-24b5a87754b2",
               name: "fetty"
            },
            {
               uuid: "ac96ba0c-7313-11eb-9439-0242ac130002",
               name: "OG.Mimmo"
            }
         ]
      }
   },
   beforeMount() {
      // TODO : Charger les conversations ici
   }
}
</script>

<style scoped>
.Messages {
   display: flex;
   flex-direction: row;
   justify-content: center;
   align-items: center;
   height: 100%;
   width: 100%;
   background-color: #18161F;

}

.left-pannel {
   height: 100%;
   width: 22%;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;
}

.settings-box {
   min-height: 80px;
   width: 100%;

   display: flex;
   align-items: center;
   justify-content: center;
}

.settings-box > div {
   width: 86%;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
}

.settings {
   cursor: pointer;
   height: 40px;
   width: 40px;

   margin-right: 3px
}

.settings:hover {
   animation: 10s cubic-bezier(.11, 0, .09, 1) settingsRoll;
}

.add-conversation {
   height: 34px;
   width: 34px;

   background-color: #F4F4F4;
   border-radius: 50%;

   display: flex;
   align-items: center;
   justify-content: center;

   font-size: 38px;
   font-weight: 600;
   color: #454150;

   margin-left: 3px;

   cursor: pointer;
}

.add-conversation:hover {
   background-color: #E85C5C;
   color: #F4F4F4;
}

.convs-list {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: flex-start;

   width: 100%;

   overflow-y: auto;
   overflow-x: hidden;
}

.convs-list > a {
   position: relative;
   width: 96%;
   height: 70px;

   display: flex;
   align-items: center;
   justify-content: center;
}

.convs-list > a:before {
   position: absolute;
   content: "";
   bottom: 0;
   height: 1px;
   width: 90%;
   opacity: 0.5;
   background-color: #909090;
}

.router-link-active {
   background-color: #E85C5C;
   border-radius: 7px;
   width: 95%;
}

.router-link-active:before {
   height: 0;
}

.link-content {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: flex-start;

   width: 90%;
   height: 100%;
}

.user-logo {
   height: 40px;
   width: 40px;
   background-color: #C4C4C4;
   color: #454150;
   font-size: 23px;
   font-weight: 600;

   margin-right: 20px;

   display: flex;
   align-items: center;
   justify-content: center;
   border-radius: 50%;
}

.conv-name {
   display: flex;
   align-items: center;
   width: calc(100% - 70px);
   height: 60px;
   font-size: 20px;
   font-weight: 600;

   overflow: hidden;
}

.right-pannel {
   height: 100%;
   width: 78%;
}

@keyframes settingsRoll {
   0% {
      transform: rotate(0);
   }
   85% {
      transform: rotate(2164deg);
   }
   93% {
      transform: rotate(2158deg);
   }
   100% {
      transform: rotate(2160deg);
   }
}
</style>
