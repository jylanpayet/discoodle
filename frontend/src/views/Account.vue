<template>
   <div class="Account" v-if="isAuthentificated">
      <SearchBar/>
   </div>
   <div class="Account" v-else>
      <Authentication />
   </div>
</template>

<script>
import SearchBar from "@/components/common/SearchBar";
import Authentication from "@/components/Authentification";

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
      this.isAuthentificated = document.cookie !== "";
   }
}
</script>

<style scoped>

</style>
