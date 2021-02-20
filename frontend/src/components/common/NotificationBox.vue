<template>
   <div class="NotificationBox">
      Notification(s) :
      <div>

      </div>
   </div>
</template>

<script>
export default {
   name: "NotificationBox",
}
</script>

<style scoped>
.NotificationBox {
   position: absolute;
   right: 20px;
   bottom: 20px;

   width: 300px;

   animation: appear-right ease-in-out 0.8s;

   background-color: #454150;
   border-radius: 8px;

   padding: 20px;
   color: #F4F4F4;

   box-shadow: #13111A 7px 7px 16px;
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