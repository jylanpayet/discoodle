<template>
   <div class="EmojiPicker">
      <div class="search-emojis">
         <input type="text" spellcheck="false" placeholder="Recherchez un emoji ..." autocomplete="false">
         <button style="margin-left: 10px; outline: none; border: none; background-color: #E85C5C; color: #f4f4f4; font-weight: 500; height: 20px; width: 20px; border-radius: 50px; cursor: pointer;" @click="$emit('closeEmoji')">X</button>
      </div>
      <div class="category-select">
         <span style="color: #f4f4f4">Catégorie :</span>
         <button @click="changeCategory(0)" :style="category.indexOf(true) === 0 ? { backgroundColor: '#E85C5C' } : {}">
            😄
         </button>
         <button @click="changeCategory(1)" :style="category.indexOf(true) === 1 ? { backgroundColor: '#E85C5C' } : {}">
            🐱
         </button>
         <button @click="changeCategory(2)" :style="category.indexOf(true) === 2 ? { backgroundColor: '#E85C5C' } : {}">
            📌
         </button>
         <button @click="changeCategory(3)" :style="category.indexOf(true) === 3 ? { backgroundColor: '#E85C5C' } : {}">
            🏠
         </button>
         <button @click="changeCategory(4)" :style="category.indexOf(true) === 4 ? { backgroundColor: '#E85C5C' } : {}">
            💯
         </button>
      </div>
      <div class="emojiList">
         <div class="People" v-if="category[0]">
            <span :key="emoji" v-for="emoji of categorizedEmojis['People']" @click="$emit('selected-emoji', emoji)" >{{ emoji }}</span>
         </div>
         <div class="Nature" v-if="category[1]">
            <span :key="emoji" v-for="emoji of categorizedEmojis['Nature']" @click="$emit('selected-emoji', emoji)" >{{ emoji }}</span>
         </div>
         <div class="Objects" v-if="category[2]">
            <span :key="emoji" v-for="emoji of categorizedEmojis['Objects']" @click="$emit('selected-emoji', emoji)" >{{ emoji }}</span>
         </div>
         <div class="Places" v-if="category[3]">
            <span :key="emoji" v-for="emoji of categorizedEmojis['Places']" @click="$emit('selected-emoji', emoji)" >{{ emoji }}</span>
         </div>
         <div class="Symbols" v-if="category[4]">
            <span :key="emoji" v-for="emoji of categorizedEmojis['Symbols']" @click="$emit('selected-emoji', emoji)" >{{ emoji }}</span>
         </div>
      </div>
   </div>
</template>

<script>
import emojis from "@/assets/emojis";
import emojis_uncathegorized from "@/assets/emojis_uncathegorized";

export default {
   name: "EmojiPicker",
   data() {
      return {
         categorizedEmojis: emojis,
         allEmojis: emojis_uncathegorized,
         category: [true, false, false, false, false]
      }
   },
   methods: {
      changeCategory(index) {
         for (let i = 0; i < 5; i++) {
            this.category[i] = i === index;
         }
      },
   }
}
</script>

<style scoped>
.EmojiPicker {
   padding: 10px;
   position: relative;
   width: 270px;
   height: 290px;
   background-color: #454150;
   border-radius: 12px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: space-between;

   animation: appear-opacity ease-in-out 0.4s;
}

.EmojiPicker::after {
   position: absolute;
   content: "";
   bottom: -2px;
   right: 6px;
   height: 20px;
   width: 20px;
   border-radius: 3px;
   background-color: #454150;
   transform: rotate(45deg);
}

.EmojiPicker > div::-webkit-scrollbar {
   width: 2px;
}


.EmojiPicker > div::-webkit-scrollbar-thumb {
   background: #7f7f7f !important;
   border-radius: 15px;
}

.search-emojis {
   display: flex;
   align-items: center;
   justify-content: center;

   height: 10%;
   width: 100%;
}

.search-emojis > input {
   padding-left: 10px;
   color: #f4f4f4;
   outline: none;
   border: none;
   border-radius: 9999px;
   background-color: #4f4b5a;
   height: 100%;
   width: calc(100% - 30px);
}

.emojiList {
   width: calc(100% + 3px);
   height: 76%;

   overflow-y: auto;
   overflow-x: hidden;
}

.emojiList > div {
   display: flex;
   flex-direction: row;
   flex-wrap: wrap;
}

.emojiList > div > span {
   width: 25px;
   height: 25px;
   cursor: pointer;

   display: flex;
   justify-content: center;
   font-size: 15px;
}

.emojiList > div > span:hover {
   font-size: 16px;
}

.category-select {
   position: relative;
   height: 30px;
   width: 100%;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: space-between;
}

.category-select > button {
   width: 26px;
   height: 26px;
   border: none;
   outline: none;
   background-color: #7f7f7f;
   border-radius: 50px;
   display: flex;
   align-items: center;
   justify-content: center;
   cursor: pointer;
   font-size: 14px;
}

.category-select > button:hover {
   background-color: #E85C5C;
   font-size: 16px;
}


@keyframes appear-opacity {
   from {
      opacity: 0;
      width: 0;
      height: 0;
   }
   to {
      opacity: 1;
      width: 270px;
      height: 290px;
   }
}
</style>
