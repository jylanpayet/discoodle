<template>
   <div class="FriendsList">
      <ul>
         <li :key="items" v-for="items in friends">
            {{ items }}
         </li>
      </ul>
   </div>
</template>

<script>
export default {
   name: "FriendsList",
   data() {
      return {
         friends: [

         ]
      }
   },
   mounted() {
      // TODO : Chercher les amis du user
   }
}
</script>

<style scoped>
.FriendsList {
   width: 100%;
   height: 100%;
}
</style>
