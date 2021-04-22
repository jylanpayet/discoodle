<template>
   <div class="Account" v-if="isAuthentificated">
      <div class="content">
         <div class="nav-account">
            <div style="justify-content: flex-start; padding-left: 20px; width: calc(100% - 20px); font-size: 18px; color: #909090">Paramètres</div>

            <div>
               <router-link to="/compte/infos">
                  Informations
               </router-link>
            </div>
            <div>
               <router-link to="/compte/settings">
                  Préférences
               </router-link>
            </div>
            <div>
               <router-link to="/compte/home-set">
                  Paramètres de l'accueil
               </router-link>
            </div>
         </div>

         <div class="view-content">
            <router-view />
         </div>
      </div>
   </div>
   <div class="Account" v-else>
      <Authentication @logSuccess="isAuthentificated = true; $emit('logSuccess')" />
   </div>
</template>

<script>
import Authentication from "@/components/Authentification";
import { mapGetters } from 'vuex'

export default {
   name: "Account",
   components: {
         Authentication
   },
   computed: {
      ...mapGetters(['getUser'])
   },
   data() {
      return {
         isAuthentificated: {
            type: Boolean,
         }
      }
   },
   mounted() {
      this.isAuthentificated = (JSON.stringify(this.getUser) !== JSON.stringify({}));
   }
}
</script>

<style scoped>
.Account {
   width: 100%;
   height: 100%;
}

.content {
   position: relative;
   display: flex;
   width: 100%;
   height: calc(100% - 120px);
}

.nav-account {
   width: 25%;
   height: 100%;
   color: #f4f4f4;

   font-size: 25px;
   font-weight: 600;
}

.nav-account > div {
   width: 100%;
   height: 100px;
   border-bottom: 1px solid #909090;
   font-size: 25px;

   display: flex;
   align-items: center;
   justify-content: center;
}


.nav-account > div > a {
   width: 100%;
   height: 100%;

   display: flex;
   align-items: center;
   justify-content: center;
}

.nav-account > div:hover {
   background-position: -100%;
}

.view-content {
   height: calc(100% - 60px);
   width: calc(75% - 60px);

   padding: 30px;
}

.router-link-active {
   color: #E85C5C;
}

</style>
