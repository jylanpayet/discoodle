<template>
   <div class="Message" :style="belongToMyself ? { alignSelf: 'flex-end' } : { alignSelf: 'flex-start' }">
      <div :style="!belongToMyself ? { flexDirection: 'row-reverse' } : { flexDirection: 'row' }">
         <div class="date" :style="{ color: getTheme ? '#C4C4C4' : '#F4F4F4' }">
            {{ printDate(messageDate) }}
         </div>
         <div class="message-content" v-html="filterMarkdown(filterEmoji(content))" :style="belongToMyself ? { marginRight: '10px', backgroundColor: '#E85C5C', color: '#F4F4F4', fontWeight: 500 } : { marginLeft: '10px', backgroundColor: getTheme ? '#C4C4C4' : '#F4F4F4' }">

         </div>
         <div class="user-logo" :style="{ backgroundColor: '#F4F4F4' }">
            {{ userLogo }}
         </div>
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import emojis from "@/assets/emojis_uncathegorized";
import marked from "marked";

export default {
   name: "Message",
   props: {
      content: {
         type: String,
         required: true
      },
      userLogo: {
         required: true
      },
      belongToMyself: {
         type: Boolean,
         required: true,
         default: true
      },
      messageDate: {
         type: String,
         required: true
      },
      messageID: {
         type: Number,
         required: true,
      }
   },
   methods: {
      printDate(messageDate) {
         return new Date().toLocaleString().substr(0, 10) === messageDate.substr(0, 10) ? messageDate.substr(13) : messageDate.substr(0, 10)
      },

      filterEmoji(content){
         // Regex to match with the emoji string encode ( ':xxxxx_xxx_xxx_xxx:' where '_' is optionnal )
         const regex = ":[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*:";
         const emoji = [...content.matchAll(regex)];
         if(emoji && emoji.length > 0) {
            emoji.forEach(elt => {
               console.log(elt[0]);
               if (emojis[elt[0].replaceAll(":", "")])
                  content = content.replace(elt[0], emojis[elt[0].replaceAll(":", "")]);
            })
         }
         return content;
      },
      filterMarkdown(content){
         return marked(content);
      }

   },
   computed: {
      ...mapGetters(['getColors', 'getTheme'])
   }
}
</script>

<style scoped>
.Message {
   position: relative;
   max-width: calc(50% + 42px + 10px + 30px);
   width: 60%;
}

.Message > div {
   position: relative;
   display: flex;
   align-items: flex-end;
   justify-content: flex-end;
   max-width: 100%;


   margin-top: 10px;
}

.message-content {
   padding: 15px;
   max-width: calc(80% - 42px - 10px - 30px);

   hyphens: manual;
   word-wrap: break-word;

   background-color: #C4C4C4;
   color: #454150;
   border-radius: 12px;

   word-break: break-all;

   font-size: 14px;
   font-weight: 600;
}

.user-logo {
   min-width: 42px;
   min-height: 42px;

   background-color: #F4F4F4;
   color: #454150;
   font-size: 19px;
   font-weight: 600;

   display: flex;
   align-items: center;
   justify-content: center;

   border-radius: 100px;
}

.date {
   font-size: 13px;
   margin-left: 7px;
   margin-right: 7px;
   font-weight: 500;
}

@keyframes appear-right {
   from {
      transform: translateX(100%);
   }
   to {
      transform: translateX(0);
   }
}

@keyframes appear-left {
   from {
      transform: translateX(-100%);
   }
   to {
      transform: translateX(0);
   }
}
</style>
