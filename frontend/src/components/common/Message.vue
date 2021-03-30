<template>
   <div class="Message" :style="belongToMyself ? { justifyContent: 'flex-end' } : { justifyContent: 'flex-start' }">
      <div @mouseover="showPin = true" @mouseleave="showPin = false" class="content" :style="!belongToMyself ? { flexDirection: 'row-reverse' } : { flexDirection: 'row' }">
         <div :class="mention ? 'message-content mention' : 'message-content'" v-html="displayMessage(content, true, true, true)" :style="belongToMyself ? { marginRight: '10px', backgroundColor: '#e85c5c', color: '#F4F4F4', fontWeight: 500 } : { marginLeft: '10px', backgroundColor: getTheme ? '#C4C4C4' : '#F4F4F4' }">

         </div>
         <div class="buttons" v-if="showPin" :style="belongToMyself ? { left: 0 } : { right: 0 }">
            <button class="pin-message" @click="pinMessage">
               <img src="../../assets/pin.png" alt="">
            </button>
            <button class="delete-message" @click="deleteMessage" v-if="belongToMyself">
               X
            </button>
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
import axios from "axios";

marked.setOptions({
   renderer: new marked.Renderer(),
   highlight: function(code, language) {
      const hljs = require('highlight.js');
      const validLanguage = hljs.getLanguage(language) ? language : 'plaintext';
      return hljs.highlight(validLanguage, code).value;
   },
   pedantic: false,
   gfm: true,
   breaks: false,
   sanitize: false,
   smartLists: true,
   smartypants: false,
   xhtml: false
});

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
               if (emojis[elt[0].replaceAll(":", "")])
                  content = content.replace(elt[0], emojis[elt[0].replaceAll(":", "")]);
            })
         }
         return content;
      },
      filterMarkdown(content){
         return marked(content);
      },
      filterPing(content) {
         if (content.includes(`@${this.getUser.username}`)) {
            this.mention = true;
            return content.replaceAll(`@${this.getUser.username}`, `@${this.getUser.name}`);
         }
         return content;
      },
      displayMessage(content, mardkdown, emojis, ping) {
         if (ping)
            content = this.filterPing(content);
         if (emojis)
            content = this.filterEmoji(content);
         if (mardkdown)
            content = this.filterMarkdown(content);
         return content;
      },
      pinMessage() {
         axios.put(`http://localhost:8080/api/room/pinMessage/${this.getCurrentConv}?messageID=${this.messageID}`);
         this.$emit('pinnedMessage', this.messageID);
      },
      deleteMessage() {
         axios.put(`http://localhost:8080/api/room/deleteMessage/${this.getCurrentConv}?messageID=${this.messageID}`);
         this.$emit('deletedMessage', this.messageID);
      }
   },
   computed: {
      ...mapGetters(['getColors', 'getTheme', 'getUser', 'getCurrentConv'])
   },
   data() {
      return {
         mention: false,
         showPin: false
      }
   }
}
</script>

<style scoped>
.Message {
   position: relative;
   width: 100%;

   display: flex;
   flex-direction: row;
}

.content {
   position: relative;
   display: flex;
   align-items: flex-end;
   justify-content: flex-end;
   width: 60%;

   margin-right: 0;
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

.mention {
   color: #ffffff !important;
   background-color: #dea80a !important;
   font-size: 17px;
   font-weight: 700 !important;
}

.buttons {
   position: absolute;
   background-color: #454150;
   border-radius: 12px;

   display: flex;
   flex-direction: row;
   align-self: center;
   align-items: center;
   justify-content: center;

   visibility: visible;
}

.buttons > button {
   height: 30px;
   width: 30px;
   border: none;
   outline: none;
   background: none;
   cursor: pointer;
   display: flex;
   align-items: center;
   justify-content: center;
}

.pin-message:hover {
   transform: scale(1.1);
}

.buttons > button > img {
   height: 15px;
   width: 15px;
}

.buttons:active {
   background-color: #E85C5C;
   color: #f4f4f4;
}
.delete-message {
   font-weight: 600;
   color: #f4f4f4;
   border-radius: 12px;
}
.delete-message:hover {
   color: #E85C5C;
}
.delete-message:active {
   background-color: #E85C5C;
   color: #f4f4f4;
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
