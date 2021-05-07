<template>
   <div class="Message" :style="belong_to_myself ? { justifyContent: 'flex-end' } : { justifyContent: 'flex-start' }">
      <div @mouseover="showPin = true" @mouseleave="showPin = false" class="content"
           :style="!belong_to_myself ? { flexDirection: 'row-reverse' } : { flexDirection: 'row' }">
         <span v-if="edited"
               style="color: #7f7f7f; margin-left: 20px; margin-right: 20px; font-weight: 500; font-size: 11px;">
            (Modifié)
         </span>
         <w-tooltip :left="belong_to_myself" :right="!belong_to_myself" v-if="!messageEdit"
                    :class="mention ? 'message-content mention' : 'message-content'"
                    :style="belong_to_myself ? { marginRight: '10px', backgroundColor: '#e85c5c', color: '#F4F4F4', fontWeight: 500 } : { marginLeft: '10px', backgroundColor: '#C4C4C4' }">
            <template #activator="{ on }">
               <div v-on="on" v-html="displayMessage(content, true, true, true)">

               </div>
            </template>
            {{ sender }}
         </w-tooltip>
         <w-tooltip :right="!belong_to_myself" :left="belong_to_myself" v-else
                    :class="mention ? 'message-content mention' : 'message-content'"
                    :style="belong_to_myself ? { marginRight: '10px', backgroundColor: '#e85c5c', color: '#F4F4F4', fontWeight: 500 } : { marginLeft: '10px', backgroundColor: '#F4F4F4' }">
            <template #activator="{ on }">
               <div v-on="on">
                  <input type="text" :value="content" autocomplete="off" @keydown="actionInput">
               </div>
            </template>
            {{ sender }}
         </w-tooltip>

         <div class="buttons" v-if="showPin" :style="belong_to_myself ? { left: '-50px' } : { right: '50px' }">
            <button class="pin-message" @click="pinMessage" v-if="canPin">
               <img src="../../assets/pin.png" alt="">
            </button>
            <button class="edit-message" @click="messageEdit = true" v-if="belong_to_myself && canEdit">
               <img src="../../assets/pen.svg" alt="Pen">
            </button>
            <button class="delete-message" @click="deleteMessage" v-if="canRemove">
               X
            </button>
         </div>
         <div class="user-logo" :style="{ backgroundColor: '#F4F4F4' }">
            <span v-if="user.link_to_avatar === null">{{ sender.charAt(0).toUpperCase() }}</span>
            <img :src="user.link_to_avatar" alt="" v-else>
         </div>

      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import emojis from "@/assets/emojis_uncathegorized";
import marked from "marked";
import axios from "axios";

export default {
   name: "Message",
   props: {
      content: {
         type: String,
         required: true
      },
      sender: {
         required: true
      },
      belong_to_myself: {
         type: Boolean,
         required: true,
         default: true
      },
      message_date: {
         type: Number,
         required: true
      },
      message_id: {
         type: Number,
         required: true,
      },
      edited: {
         type: Boolean,
         required: true,
         default: false,
      },
      canRemove: {
         type: Boolean,
         default: true
      },
      canEdit: {
         type: Boolean,
         default: true
      },
      canPin: {
         type: Boolean,
         default: true,
      }
   },
   methods: {
      filterEmoji(content) {
         // Regex to match with the emoji string encode ( ':xxxxx_xxx_xxx_xxx:' where '_' is optionnal )
         const regex = ":[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*:";
         const emoji = [...content.matchAll(regex)];
         if (emoji && emoji.length > 0) {
            emoji.forEach(elt => {
               if (emojis[elt[0].replaceAll(":", "")])
                  content = content.replace(elt[0], emojis[elt[0].replaceAll(":", "")]);
            })
         }
         return content;
      },
      filterMarkdown(content) {
         return marked(content);
      },
      filterPing(content) {
         if (content.match(`(@${this.getUser.username})$|(@${this.getUser.username} )`)) {
            this.mention = true;
            return content.replaceAll(`@${this.getUser.username}`, `@${this.getUser.name}`);
         } else {
            this.mention = false;
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

         const body = new DOMParser().parseFromString(content, "text/html").querySelector("body");

         const p = body.children[0];
         if (p && p.children.length === 1) {
            if (p.children[0].tagName === "CODE") {
               p.classList.add("code");
            } else if (p.children[0].tagName === "A") {
               p.children[0].setAttribute("target", "_blank");
            }
         }

         return body.innerHTML;
      },
      pinMessage() {
         axios.put(`http://localhost:8080/api/messages/pinMessage?message_id=${this.message_id}`);
         this.$emit('pinnedMessage', this.message_id);
      },
      deleteMessage() {
         axios.delete(`http://localhost:8080/api/messages/deleteMessage?message_id=${this.message_id}`);
         this.$emit('deletedMessage', this.message_id);
      },
      editMessage(content) {
         if (content !== this.content) {
            axios.put(`http://localhost:8080/api/messages/editMessage`, {
               message_id: this.message_id,
               content: content
            })
            this.$emit('editedMessage', this.message_id, content);
         }
      },

      actionInput(event) {
         const input = document.querySelector(".message-content > div > input")
         if (event.keyCode === 13) {
            if (input.value === "")
               this.deleteMessage();
            else
               this.editMessage(input.value);
            this.messageEdit = false;
         } else {
            this.messageEdit = event.keyCode !== 27;
            input.value = this.displayMessage(input.value, false, false, false);
         }
      }
   },
   computed: {
      ...mapGetters(['getColors', 'getUser', 'getCurrentConv'])
   },
   data() {
      return {
         mention: false,
         showPin: false,
         messageEdit: false,
         user: {}
      }
   },
   mounted() {
      axios.get(`http://localhost:8080/api/users/findByUserName?username=${this.sender}`).then(response => {
         this.user = response.data;
      })
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
   max-width: calc(100% - 50px);

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
   min-width: 40px;
   min-height: 40px;
   width: 40px;
   height: 40px;

   background-color: #F4F4F4;
   color: #454150;
   font-size: 19px;
   font-weight: 600;

   display: flex;
   align-items: center;
   justify-content: center;

   border-radius: 100px;
}

.user-logo > img {
   width: 100%;
   height: 100%;
}

.mention {
   color: #2d2d2d !important;
   background-color: #5cb0e8 !important;
   font-size: 17px;
   font-weight: 700 !important;
}

.buttons {
   position: absolute;
   background-color: #454150;
   border-radius: 12px;
   height: 30px;
   bottom: 5px;

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
   border-radius: 12px;
   outline: none;
   background: none;
   cursor: pointer;
   display: flex;
   align-items: center;
   justify-content: center;
}

.buttons > button > img {
   height: 15px;
   width: 15px;
}

.buttons > button:focus {
   background-color: #E85C5C;
   color: #f4f4f4;
}

.delete-message {
   font-weight: 600;
   color: #f4f4f4;
}

.delete-message:hover {
   color: #E85C5C;
}

.delete-message:active {
   background-color: #E85C5C;
   color: #f4f4f4;
}

.message-content > div > input {
   border: none;
   outline: none;
   background-color: #ff8888;
   border-radius: 6px;
   color: #f4f4f4;
   height: 30px;
   padding-left: 4px;
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
