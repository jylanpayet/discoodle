<template>
   <div class="Home">
      <img src="../assets/home-image.png" alt="">
      <div>
         <SearchBar/>
         <div class="home-content">
            <div>
               <div class="welcome-discoodle">
                  Bienvenue sur Discoodle !
               </div>

               <!-- TODO : Intégrer les flux RSS et les posts -->

               <div class="posts-content">
                  <div v-if="posts.length === 0" style="color: #F4F4F4; font-size: 20px; display: flex; align-items: center; justify-content: center">
                     Votre fil d'actualité est vide :( <br>
                     Vérifiez vos paramètres pour arranger ça !
                  </div>
                  <Post :key="post" v-for="post in posts"
                        :title="post.title"
                        :content="post.description"
                        :link="post.link"
                        :post-type="post.type"
                        :user-name="post.user"
                  />
               </div>

            </div>
         </div>
      </div>
   </div>
</template>

<script>
import SearchBar from "@/components/common/SearchBar";
import axios from "axios";
import Post from "@/components/common/Post";

export default {
   name: 'Home',
   components: {
      SearchBar,
      Post
   },
   data() {
      return {
         posts: [
            // Posts to be display on the home page.
         ],
         fullscreenPost: undefined
      }
   },
   mounted() {
      axios.get("https://www.letudiant.fr/rss/metiers.html").then(response => {
         const content = new DOMParser().parseFromString(response.data, "text/xml");
         const items = content.getElementsByTagName("item");
         items.forEach(elt => {
            const childs = elt.children;

            let temp = {
               title: childs[0].innerHTML.replace("<![CDATA[", "").replace("]]>", ""),
               link: childs[1].innerHTML.replace("<![CDATA[", "").replace("]]>", ""),
               description: childs[2].innerHTML.replace("<![CDATA[", "").replace("]]>", ""),
               type: "RSS",
               user: "info l'étudiant"
            }
            this.posts.push(temp);
         });
      })
   }
}
</script>

<style scoped>

.Home {
   height: 100%;
}

.Home > img {
   position: absolute;
   width: calc(100% - 270px);


   z-index: 1;

   opacity: 0.1;

   --finalOpacity: 0.1;
   animation: appear-opacity 0.7s ease-in-out;
}

.Home > div {
   position: absolute;

   width: calc(100% - 270px);
   height: 100%;

   z-index: 2;

   overflow-y: auto;
   overflow-x: hidden;
}

.home-content {
   width: 100%;
   z-index: 500;
}

.home-content > div {
   position: relative;
   overflow-y: auto;
   margin: auto;
   height: 100%;
   width: 80%;


}

.welcome-discoodle {
   display: flex;
   align-items: center;
   justify-content: center;

   height: 300px;
   width: 100%;

   font-size: 45px;
   letter-spacing: 2px;
   color: #F4F4F4;
   font-weight: 600;

   margin-bottom: 60px;

   --finalOpacity: 1;
   animation: appear-opacity 1.5s ease-in-out;
}

.posts-content {
   width: calc(100% - 20px);
   padding: 10px;

   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: center;

   flex-wrap: wrap;
}


@keyframes appear-opacity {
   from {
      opacity: 0;
   }
   to {
      opacity: var(--finalOpacity);
   }
}
</style>
