<template>
   <w-dialog
         :fullscreen="joinGroup.fullscreen"
         :width="joinGroup.width"
         :persistent="joinGroup.persistent"
         :persistent-no-animation="joinGroup.persistentNoAnimation"
         @close="$emit('close')"
   >
      <div class="joinGroup">
         <span style="margin-bottom: 10px; font-size: 19px; font-weight: 600; color: #454150">Rejoindre un groupe :</span>
         <div style="width: 100%;">
            <w-input color="darkgray" autocomplete="off" @keydown="join" required style="margin-bottom: 10px; width: 90%" name="groupID" v-model="groupID">
               Identifiant du groupe
            </w-input>
            <w-input color="darkgray" autocomplete="off" required style="margin-bottom: 10px; width: 90%" type="password" name="groupKey"
                     @keydown="join" v-model="groupToken">Clé du groupe
            </w-input>
         </div>
         <button class="submit" @click="join">
            Rejoindre !
         </button>
      </div>
   </w-dialog>
</template>

<script>
import {mapGetters} from "vuex";

export default {
   name: "JoinGroup",
   methods: {
      join(event) {
         if (this.groupID !== "" && this.groupToken !== "" && !this.didAdd) {
            if ((event.type === 'keydown' && event.keyCode === 13) || event.type === 'click') {
               this.$emit('user-entry', {
                  id: this.groupID,
                  token: this.groupToken
               });
               this.joinGroup.show = false;
               this.$emit('close');
               this.didAdd = true
            }
         }
      },
   },
   computed: {
      ...mapGetters(['getUser'])
   },
   data() {
      return {
         joinGroup: {
            fullscreen: false,
            persistent: false,
            persistentNoAnimation: false,
            width: 300
         },
         groupID: "",
         groupToken: "",
         didAdd: false
      }
   },
}
</script>

<style scoped>
.submit {
   height: 35px;
   width: 150px;
   background-color: #E85C5C;
   font-size: 15px;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 999px;
   color: #F4F4F4;
   cursor: pointer;
}

.joinGroup {
   height: 175px;
   width: 100%;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}
</style>