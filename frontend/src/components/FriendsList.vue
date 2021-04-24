<template>
   <div class="FriendsList">
      <div class="pannel">
         <span style="font-weight: 600; font-size: 14px; color: #f4f4f4; width: 100%; height: 30px; justify-self: flex-start; display: flex; align-items: center">Ajouter un ami</span>
         <button @click="showAddFriends = true">+</button>
         <AddUserInConv v-if="showAddFriends" @desactivatePopUp="showAddFriends = false" @addUsers="addUsers" :show-autocomplete="false"/>
      </div>
      <div class="incoming-invite" v-if="invite.length > 0">
         <span style="margin-bottom: 10px; font-weight: 600; font-size: 14px; color: #f4f4f4">Ils m'ont demandé en ami :</span>
         <div class="user" :key="user" v-for="user in invite">
            <div class="user-image">
               <span v-if="user.link_to_avatar === null">{{ user.name.charAt(0).toUpperCase() }}</span>
               <img :src="user.link_to_avatar" alt="" v-else >
            </div>
            <span style="color: #f4f4f4; width: 85%; font-size: 19px; font-weight: 600;">{{ user.name }}</span>
            <button class="accept-invite" @click="acceptInvite(user.id)">✅</button>
            <button class="refuse-invite" @click="refuseInvite(user.id)">❌</button>
         </div>
      </div>
      <div class="current-friends">
         <span v-if="getFriends.length === 0" style="font-weight: 600; font-size: 17px; color: #f4f4f4">Ajoute des amis pour les voir ici !</span>
         <span v-else style="margin-bottom: 10px; font-weight: 600; font-size: 17px; color: #f4f4f4">Vos amis :</span>
         <div class="user" :key="user" v-for="user in getFriends">
            <div class="user-image">
               <span v-if="user.link_to_avatar === null">{{ user.name.charAt(0).toUpperCase() }}</span>
               <img :src="user.link_to_avatar" alt="" v-else>
            </div>
            <span style="color: #f4f4f4; width: calc(100% - 50px); font-size: 19px; font-weight: 600;">{{
                  user.name
               }}</span>
         </div>
      </div>
   </div>
   <w-alert class="error-alert" v-if="showAlert" success plain>
      {{ alertText }}
   </w-alert>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import axios from "axios";
import AddUserInConv from "@/components/AddUser";

export default {
   name: "FriendsList",
   components: {AddUserInConv},
   data() {
      return {
         invite: [],
         alertText: "",
         showAlert: false,
         showAddFriends: false,
      }
   },
   methods: {
      addUsers(users) {
         users.forEach(username => {
            axios.get(`http://localhost:8080/api/users/${username}`).then(response => {
               if (response.data && response.data.id !== this.getUser.id) {
                  axios.post(`http://localhost:8080/api/friendships/inviteMembers/${this.getUser.id}`, {
                     friendships: [response.data.id]
                  }).then(() => {
                     this.alertText = "Utilisateur(s) ajouté(s)"
                     this.showAlert = true;
                     setTimeout(() => {
                        this.showAlert = false;
                     }, 4000)
                  });
               }
            })
         })
         this.showAddFriends = false;
      },
      acceptInvite(friend_id) {
         axios.put(`http://localhost:8080/api/friendships/acceptInvitation?sender_id=${friend_id}&receiver_id=${this.getUser.id}`).then(() => {
            axios.get(`http://localhost:8080/api/users/infos/${friend_id}`).then(friend => {
               const temp = {
                  id: friend.data.id,
                  username: friend.data.username,
                  name: friend.data.name,
                  link_to_avatar: friend.data.link_to_avatar,
               };
               let i = 0;
               this.invite.forEach(elt => {
                  if (elt.id === temp.id) {
                     this.invite.splice(i, 1);
                     return;
                  }
                  i++;
               })
               if (!this.friendsIncludes(temp))
                  this.getFriends.push(temp);
            });
         }).catch(err => {
            console.log(err);
         })
      },
      refuseInvite(friend_id) {
         axios.delete(`http://localhost:8080/api/friendships/refuseInvitation?sender_id=${friend_id}&receiver_id=${this.getUser.id}`).then(() => {
            axios.get(`http://localhost:8080/api/users/infos/${friend_id}`).then(friend => {
               const temp = {
                  id: friend.data.id,
                  username: friend.data.username,
                  name: friend.data.name,
                  link_to_avatar: friend.data.link_to_avatar,
               }
               let i = 0;
               this.invite.forEach(elt => {
                  if (elt.id === temp.id) {
                     this.invite.splice(i, 1);
                     return;
                  }
                  i++;
               })
            });
         }).catch(err => {
            console.log(err);
         })
      },
      friendsIncludes(obj) {
         let bool = false;
         this.getFriends.forEach(elt => {
            if (     elt.id === obj.id
                  && elt.link_to_avatar === obj.link_to_avatar
                  && elt.name === obj.name
                  && elt.username === obj.username)
               bool = true;
         });
         return bool;
      },
      ...mapActions(['setFriends'])
   },
   computed: {
      ...mapGetters(['getUser', 'getFriends', 'getCurrentConv'])
   },
   mounted() {
      if (!(JSON.stringify(this.getUser) === JSON.stringify({}))) {
         // Get all pending invites for the user.
         axios.get(`http://localhost:8080/api/friendships/getAllInvitations/${this.getUser.id}`).then(response => {
            response.data.forEach(elt => {
               if (!elt.status) {
                  axios.get(`http://localhost:8080/api/users/infos/${elt.sender_id}`).then(friend => {
                     const temp = {
                        id: friend.data.id,
                        username: friend.data.username,
                        name: friend.data.name,
                        link_to_avatar: friend.data.link_to_avatar,
                     }
                     if (!this.invite.includes(temp))
                        this.invite.push(temp)
                  })
               }
            })
         })

         // Get all friends of a user.
         axios.get(`http://localhost:8080/api/users/seeAllFriends/${this.getUser.id}`).then(response => {
            this.setFriends([]);
            response.data.forEach(elt => {
               const temp = {
                  id: elt.id,
                  username: elt.username,
                  name: elt.name,
                  last_name: elt.last_name,
                  link_to_avatar: elt.link_to_avatar,
               };
               if (!this.friendsIncludes(temp))
                  this.getFriends.push(temp);
            })
         })
      }
   }
}
</script>

<style scoped>
.FriendsList {
   width: 100%;
   height: 100%;
}

.pannel {
   width: 100%;
   height: 80px;
   background-color: #15131c;

   display: flex;
   align-items: center;
   justify-content: flex-end;

   padding: 20px;
}

.pannel > button {
   min-height: 30px;
   max-height: 30px;
   min-width: 30px;
   max-width: 30px;
   background-color: #E85C5C;
   cursor: pointer;
   border: none;
   outline: none;
   border-radius: 50%;
   display: flex;
   align-items: center;
   justify-content: center;
   font-size: 28px;
   font-weight: 600;
   margin-left: 10px;
   color: #f4f4f4;
}

.pannel > button:hover {
   transform: rotate(90deg);
   transition: transform 0.9s ease-in-out;
}

.incoming-invite, .current-friends {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;

   padding: 20px;

   width: 100%;
}

.user {
   display: flex;
   align-items: center;
   justify-content: space-between;
   margin-bottom: 10px;
   width: 100%;
   height: 50px;
   background-color: #454150;
   border-radius: 12px;
   padding-right: 15px;
   padding-left: 15px;
}

.user > button {
   border: none;
   outline: none;
   font-size: 20px;
   background: none;
   cursor: pointer;
}

.user-image {
   background-color: #7f7f7f;
   color: #f4f4f4;
   width: 35px;
   height: 35px;
   border-radius: 50%;

   font-size: 18px;
   font-weight: 600;

   display: flex;
   align-items: center;
   justify-content: center;
}

.user-image > img {
   width: 100%;
   height: 100%;
}


.error-alert {
   position: fixed;
   bottom: 50px;

   animation: opacity ease-in-out 4s;
}

@keyframes opacity {
   0% {
      opacity: 0;
   }
   10% {
      opacity: 1;
   }
   90% {
      opacity: 1;
   }
   100% {
      opacity: 0;
   }
}
</style>
