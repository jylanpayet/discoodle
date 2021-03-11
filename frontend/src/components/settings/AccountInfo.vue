<template>
   <div class="AccountInfo">
      <div style="height: 80px;">
         Informations personnelles
      </div>
   </div>
</template>

<script>
export default {
   name: "AccountInfo"
}
</script>

<style scoped>
.AccountInfo {
   display: flex;
   flex-direction: column;
   align-items: flex-start;
}

.AccountInfo > div {
   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: center;
   position: relative;
   width: 100%;

   color: #f4f4f4;
   font-size: 22px;
}

.AccountInfo > div::after {
   align-self: center;
   position: absolute;
   content: "";
   bottom: 0;
   width: 80%;
   background-color: #909090;
   height: 1px;
}
</style>
