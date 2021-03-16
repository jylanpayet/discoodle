<template>
   <div class="Home">
      <div @scroll="loadPosts">
         <SearchBar/>
         <div class="home-content">
            <div>
               <div class="welcome-discoodle">
                  Bienvenue sur Discoodle !
               </div>

               <!-- TODO : Intégrer les flux RSS et les posts -->

               <div class="posts-content">
                  <div v-if="posts.length === 0"
                       style="color: #F4F4F4; font-size: 20px; display: flex; align-items: center; justify-content: center">
                     Votre fil d'actualité est vide :( <br>
                     Vérifiez vos paramètres pour arranger ça !
                  </div>
                  <Post :key="post" v-for="post in posts.slice(fromPost, toPost)"
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
import { mapGetters } from "vuex"

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
         fullscreenPost: undefined,
         rssFeed: [
            "https://www.letudiant.fr/rss/metiers.html",
            "https://www.letudiant.fr/rss.html",
            "https://jobs-stages.letudiant.fr/stages-etudiants/rss.xml",
            "https://jobs-stages.letudiant.fr/jobs-etudiants/rss.xml"
         ],
         fromPost: 0,
         toPost: 20,
      }
   },
   mounted() {
      this.rssFeed.forEach(elt => {
         axios.get(elt).then(response => {
            const content = new DOMParser().parseFromString(response.data, "text/xml");
            const items = content.getElementsByTagName("item");
            items.forEach(elt => {
               const childs = elt.children;
               if (childs[0].tagName === "title" && childs[1].tagName === "link" && childs[2].tagName === "description") {
                  let temp = {
                     title: childs[0].innerHTML.replace("<![CDATA[", "").replace("]]>", ""),
                     link: childs[1].innerHTML.replace("<![CDATA[", "").replace("]]>", ""),
                     description: childs[2].innerHTML.replace("<![CDATA[", "").replace("]]>", ""),
                     type: "RSS",
                     user: content.querySelector("channel > title").innerHTML
                  }
                  this.posts.splice(this.rand(this.posts.length), 0, temp);
               }
            });
         });
      });
   },
   methods: {
      rand(max) {
         return Math.floor(Math.random() * Math.floor(max));
      },
      loadPosts() {
         const div = document.querySelector(".Home > div")
         if (div.scrollTop / div.scrollHeight > 3/4) {
            if (this.toPost + 10 > this.posts.length)
               return
            this.toPost += 10;
            this.fromPost += 10;
         }
      }
   },
   computed: {
      ...mapGetters(['getUser'])
   }
}
</script>

<style scoped>

.Home {
   height: 100%;
}


.Home > div {
   width: 100%;
   height: 100%;

   z-index: 2;

   overflow-y: auto;
   overflow-x: hidden;
}

.Home > div::before {
   content: "";
   width: calc(100% - 270px);
   height: 100%;
   position: absolute;
   background-image: url("../assets/home-image.png");
   background-size: cover;
   top: 0;
   right: 0;
   filter: opacity(0.2) grayscale(0.5);
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
