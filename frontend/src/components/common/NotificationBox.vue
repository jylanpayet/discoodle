<template>
   <div class="NotificationBox">
      <div class="top-part-notifications">
         Notification(s) :
         <button @click="disappear">X</button>
      </div>

      <div class="bar"></div>

      <div class="bottom-part-notifications">
         <span v-if="notifications.length === 0" style="align-self: center">
            C'est bien vide ici ...
         </span>
      </div>
   </div>
</template>

<script>
export default {
   name: "NotificationBox",
   data() {
      return {
         notifications: [

         ]
      }
   },
   methods: {
      disappear() {
         const notificationBox = document.querySelector(".NotificationBox");

         notificationBox.classList.add("disappear");

         setTimeout(() => {
            this.$emit("switchNotification");
         }, 700);
      }
   }
}
</script>

<style scoped>
.NotificationBox {
   position: fixed;
   right: 20px;
   bottom: 20px;

   width: 300px;

   z-index: 9999;

   animation: appear-right ease-in-out 0.8s;

   background-color: #454150;
   border-radius: 8px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;

   padding: 20px;
   color: #F4F4F4;

   box-shadow: #13111A 7px 7px 16px;
}

.bar {
   width: 60%;
   height: 1px;
   border-radius: 2px;

   background-color: #F4F4F4;

   margin-bottom: 15px;
}

.top-part-notifications {
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;


   width: 100%;
   margin-bottom: 15px;
}

.top-part-notifications > button {
   font-size: 20px;
   border: none;
   background: none;

   outline: none;
   cursor: pointer;

   color: #F4F4F4;
}

.bottom-part-notifications {
   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: space-between;


   width: 100%;
}

.disappear {
   animation: disappear 1s ease-in-out;
}

@keyframes disappear {
   from {
      right: 20px;
   }
   to {
      right: -400px;
   }
}


@keyframes appear-right {
   from {
      right: -400px;
   }
   to {
      right: 20px;
   }
}
</style>
