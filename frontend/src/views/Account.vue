<template>
   <div class="Account" v-if="isAuthentificated">
      <SearchBar/>
   </div>
   <div class="Account" v-else>
      <Authentication @logSuccess="isAuthentificated = true" />
   </div>
</template>

<script>
import SearchBar from "@/components/common/SearchBar";
import Authentication from "@/components/Authentification";

import vueCookie from "vue-cookie"

export default {
   name: "Account",
   components: {
      SearchBar,
      Authentication
   },
   data() {
      return {
         isAuthentificated: {
            type: Boolean,
         }
      }
   },
   beforeMount() {
      this.isAuthentificated = vueCookie.get("username") !== null && vueCookie.get("username") !== "";
   }
}
</script>

<style scoped>

</style>
