<template>
   <div class="Message" :style="belongToMyself ? { alignSelf: 'flex-end' } : { alignSelf: 'flex-start' }">
      <div :style="!belongToMyself ? { flexDirection: 'row-reverse' } : { flexDirection: 'row' }">
         <div class="date" :style="{ color: getTheme ? '#C4C4C4' : '#F4F4F4' }">
            {{ printDate(messageDate) }}
         </div>
         <div class="message-content" :style="belongToMyself ? { marginRight: '10px', backgroundColor: '#E85C5C', color: '#F4F4F4', fontWeight: 500 } : { marginLeft: '10px', backgroundColor: getTheme ? '#C4C4C4' : '#F4F4F4' }">
            {{ filterEmoji(content)}}
         </div>
         <div class="user-logo" :style="{ backgroundColor: '#F4F4F4' }">
            {{ userLogo }}
         </div>
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import emojis from "@/assets/emojis";

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
      }
   },
   methods: {
      printDate(messageDate) {
         return new Date().toLocaleString().substr(0, 10) === messageDate.substr(0, 10) ? messageDate.substr(13) : messageDate.substr(0, 10)
      },

      filterEmoji(content){
         const regex = ":[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*:";
         const emoji = [...content.matchAll(regex)];
         emoji.forEach(elt => emoji[0]= elt[0]);
         if(emoji){
            emoji.forEach(elt => {
               elt = elt.replaceAll(":", "");
               if (emojis.People[elt])
                  content = content.replace(`:${elt}:`, emojis.People[elt.replaceAll(":","")]);
               if (emojis.Nature[elt])
                  content = content.replace(`:${elt}:`, emojis.Nature[elt.replaceAll(":","")]);
               if (emojis.Objects[elt])
                  content = content.replace(`:${elt}:`, emojis.Objects[elt.replaceAll(":","")]);
               if (emojis.Places[elt])
                  content = content.replace(`:${elt}:`, emojis.Places[elt.replaceAll(":","")]);
               if (emojis.Symbols[elt])
                  content = content.replace(`:${elt}:`, emojis.Symbols[elt.replaceAll(":","")]);

            })
         }
         return content;
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
