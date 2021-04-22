<template>
   <div class="SearchBar">
      <div class="content">
         <!-- TODO : Implement research -->
         <div class="bar">
            <img src="../../assets/search.png" alt="">
            <input type="search" placeholder="Rechercher">
         </div>

         <div class="notification-bell">
            <button @click="showNotification = !showNotification">
               <img src="../../assets/bell.png" alt="Bell">
            </button>
            <div class="red-alert" v-if="1 > 0"></div>
         </div>
      </div>

      <NotificationBox v-if="showNotification" @switchNotification="showNotification = !showNotification" />
   </div>
</template>

<script>
import { mapGetters } from "vuex";
import NotificationBox from "@/components/common/NotificationBox";

export default {
   name: "SearchBar",
   components: {
      NotificationBox
   },
   data() {
      return {
         showNotification: false
      }
   },
   computed: {
      ...mapGetters(['getColors'])
   }
}
</script>

<style scoped>
.SearchBar {
   width: 100%;
   height: 120px;

   display: flex;
   align-items: center;
   justify-content: center;

   z-index: 1;
}

.SearchBar > .content {
   display: flex;
   flex-direction: row;
   justify-content: space-between;
   align-items: center;

   width: 90%;
}

.bar {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: flex-start;

   width: 400px;
   height: 32px;
   background-color: #454150;
   border-radius: 12px;
}

.bar > img {
   width: 15px;
   height: 15px;

   margin-left: 15px;
   margin-right: 15px;
}

.bar > input {
   outline: none;
   user-select: none;
   border: none;

   width: calc(100% - 60px);

   background: none;
   color: #F4F4F4;
}

.bar > input::placeholder {
   color: #F4F4F4;
}

.notification-bell > button {
   background: none;
   border: none;
   outline: none;
   cursor: pointer;
}

.notification-bell > button:hover img {
   animation: rotating linear infinite 1.2s;
}

.notification-bell > button > img {
   width: 30px;
   height: 30px;
   transform: rotate(7.93deg);
}

.bar > input::-webkit-search-cancel-button {
   display: none;
}

.notification-bell {
   position: relative;

   z-index: 500;
}

.red-alert {
   position: absolute;
   top: -3px;
   right: -3px;
   width: 13px;
   height: 13px;
   background-color: #E85C5C;
   border-radius: 20px;


}



/*  KEYFRAMES  */
@keyframes rotating {
   0% {
      transform: rotate(7.93deg);
   }
   25% {
      transform: rotate(14.93deg);
   }
   75% {
      transform: rotate(0.93deg);
   }
   100% {
      transform: rotate(7.93deg);
   }
}
</style>
