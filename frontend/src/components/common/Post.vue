<template>
   <a class="Post" v-if="postType === 'RSS'" :href="link" target="_blank">
      <span :style="{ color: colors[rand(colors.length)] }" style="font-size: 25px; width: 100%; font-weight: 600; text-transform: uppercase">{{ userName }}</span>
      <span style="font-size: 20px; width: 100%; font-weight: 600; text-overflow: ellipsis">{{ title }}</span>
      <div style="word-break: break-word;">
         {{ content }}
      </div>
   </a>

</template>

<script>
export default {
   name: "Post",
   props: {
      title: {
         type: String,
      },
      link: {
         type: String,
      },
      content: {
         type: String,
      },
      userName: {
         type: String,
      },
      userPseudo: {
         type: String,
      },
      postType: {
         type: String,
         required: true
      }
   },
   data() {
      return {
         colors: [
               "#5CE8AD",
               "#785CE8",
               "#E85C5C",
               "#5CCFE8",
               "#e8b55c",
               "#E85CC9"
         ]
      }
   },
   methods: {
      rand(max) {
         return Math.floor(Math.random() * Math.floor(max));
      }
   }
}
</script>

<style scoped>
.Post {
   width: 570px;
   height: 320px;
   background-color: #454150;
   border-radius: 12px;

   margin: 15px;
   padding: 15px;

   display: flex;
   flex-direction: column;
   align-items: flex-start;

   color: #F4F4F4;
   font-size: 15px;

   overflow-y: auto;
}

.Post > * {
   margin-bottom: 10px;
}

.Post:hover {
   transform: scale(1.02);
   animation: background-change infinite 2s ease-in-out;
}

@keyframes background-change {
   0% {
      background-color: #454150;
   }
   50% {
      background-color: #4f4b5a;
   }
   100% {
      background-color: #454150;
   }
}
</style>
