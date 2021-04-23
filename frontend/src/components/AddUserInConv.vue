<template>
   <div class="AddUserInConv" @click="clickEvent">
      <div class="content">
         Quel(s) utilisateur(s) voulez-vous ajouter ?

         <div class="users">
            <div :key="user" class="userInfo" v-for="user in usersToAdd">
               <div>
                  {{ user?.charAt(0).toUpperCase() }}
               </div>
               <span style="width: calc(100% - 85px)">
                  {{ user }}
               </span>
               <button class="close" @click="removeUser(user)">X</button>
            </div>
         </div>

         <div class="input-user">
            <input type="text" placeholder="Entrez un nom d'utilisateur..." autocomplete="off" list="friends-list" name="name_room" @keypress="isEnter">
            <datalist id="friends-list" v-if="showAutocomplete">
               <option :key="friend.id" v-for="friend in getFriends.filter(elt => !users.map(e => e.username).includes(elt.username))" :value="friend.username"></option>
            </datalist>
            <button @click="addUser">
               +
            </button>
         </div>

         <button class="submit" @click="$emit('addUsers', usersToAdd);">
            Ajouter les utilisateurs séléctionnés !
         </button>

         <button class="closePopUp" @click="$emit('desactivatePopUp')">
            X
         </button>
      </div>
   </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
   name: "AddUserInConv",
   props: {
      showAutocomplete: {
         required: true,
         type: Boolean
      },
   },
   methods: {
      clickEvent(event) {
         if (event.target.className === "AddUserInConv")
            this.$emit('desactivatePopUp');
      },
      addUser() {
         const inputValue = document.querySelector(".input-user > input").value;
         if (inputValue !== "" && !this.usersToAdd.includes(inputValue))
            this.usersToAdd.push(inputValue);
         // TODO : Push seulement si ami. sinon alert d'erreur.
         document.querySelector(".input-user > input").value = "";
      },
      removeUser(username) {
         this.usersToAdd = this.usersToAdd.filter(elt => elt !== username);
      },
      isEnter(event) {
         if (event.keyCode === 13)
            this.addUser();
      }
   },
   data() {
      return {
         usersToAdd: [],
         users: []
      }
   },
   computed: {
      ...mapGetters(['getFriends', 'getCurrentConv'])
   },
   mounted() {
      axios.get(`http://localhost:8080/api/room/findUserOfRoom?room_id=${this.getCurrentConv}`).then(response => {
         this.users = response.data;
      })
   }
}
</script>

<style scoped>
.AddUserInConv {
   z-index: 2000;
   position: fixed;
   top: 0;
   left: 0;
   width: 100vw;
   height: 100vh;
   animation: 0.4s ease-in-out appear-opacity;

   display: flex;
   align-items: center;
   justify-content: center;
}

.AddUserInConv:before {
   content: "";
   position: fixed;
   background-color: #2d2d2d;
   opacity: 0.3;
   width: 100vw;
   height: 100vh;
}

.content {
   border-radius: 12px;
   background-color: #F4F4F4;
   color: #454150;

   position: relative;

   font-size: 20px;
   font-weight: 600;

   width: 450px;
   max-height: 50vh;
   padding: 30px;

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}

.closePopUp {
   position: absolute;
   top: 0;
   right: 0;
   width: 25px;
   height: 25px;

   color: #F4F4F4;

   border-radius: 50%;
   background-color: #454150;
   border: none;
   outline: none;

   display: flex;
   align-items: center;
   justify-content: center;

   transform: translateY(-25%) translateX(25%);

   cursor: pointer;
}

.closePopUp:hover {
   background-color: #E85C5C;
}

.input-user {
   width: 100%;
   height: 30px;
   display: flex;
   align-items: center;
   justify-content: space-between;

   margin-top: 20px;
   margin-bottom: 20px;
}

.input-user > input {
   height: 100%;
   width: calc(100% - 50px);
   border: none;
   outline: none;
   background-color: #454150;
   color: #f4f4f4;
   padding-left: 13px;
   padding-right: 7px;
   border-radius: 9099px;
}
.input-user > input::placeholder {
   color: #969696;
}

.input-user > button {
   height: 30px;
   width: 30px;
   margin-left: 20px;
   background-color: #E85C5C;
   border: none;
   outline: none;
   border-radius: 50%;
   font-size: 28px;
   font-weight: 600;
   color: #f4f4f4;
   display: flex;
   align-items: center;
   justify-content: center;
   cursor: pointer;
}


.submit {
   border: none;
   outline: none;
   background-color: #E85C5C;
   font-size: 15px;
   color: #f4f4f4;
   font-weight: 600;
   padding-left: 10px;
   padding-right: 10px;
   height: 30px;
   border-radius: 5000px;
   cursor: pointer;
}

.users {
   margin-top: 15px;
   width: 100%;
   display: flex;
   flex-direction: column;
   align-content: center;
   justify-content: flex-start;

   overflow-y: auto;
   overflow-x: hidden;
   padding-right: 4px;
}

.userInfo {
   display: flex;
   align-items: center;
   justify-content: center;
   margin-bottom: 5px;
}

.userInfo > div {
   width: 35px;
   height: 35px;
   background-color: #7f7f7f;
   color: #f4f4f4;
   font-size: 20px;
   font-weight: 600;
   display: flex;
   align-items: center;
   justify-content: center;
   border-radius: 50%;
   margin-right: 15px;
}

.userInfo > button {
   width: 25px;
   height: 25px;
   background-color: #454150;
   font-size: 15px;
   color: #f4f4f4;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 50%;
   margin-left: 15px;
   cursor: pointer;
}

@keyframes appear-opacity {
   from {
      /* transform: translateX(100%); */
      opacity: 0;
   }
   to {
      /* transform: translateX(0); */
      opacity: 1;
   }
}
</style>