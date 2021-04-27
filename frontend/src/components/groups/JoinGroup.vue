<template>
   <w-dialog
         :fullscreen="joinGroup.fullscreen"
         :width="joinGroup.width"
         :persistent-no-animation="joinGroup.persistentNoAnimation"
         @close="$emit('close')"
         :persistent="persistent"
         v-model="joinGroup.show"
   >
      <div class="joinGroup">
         <span style="margin-bottom: 10px; font-size: 19px; font-weight: 600; color: #454150">Rejoindre un groupe :</span>
         <span v-if="name !== ''">{{ name }}</span>
         <div style="width: 100%;">
            <w-input color="darkgray" autocomplete="off" @keydown="join" required style="margin-bottom: 10px; width: 90%" :readonly="readonly" name="groupID" v-model="groupID">
               Identifiant du groupe
            </w-input>
            <w-input color="darkgray" autocomplete="off" required style="margin-bottom: 10px; width: 90%" type="password" name="groupKey"
                     @keydown="join" v-model="groupToken">Clé du groupe
            </w-input>
         </div>
         <button class="submit" style="background-color: #61e85c; margin-bottom: 10px;" @click="join">
            Rejoindre !
         </button>
         <button class="submit" style="background-color: #E85C5C;" @click="joinGroup.show = false; $emit('cancel')">
            Annuler
         </button>
      </div>
   </w-dialog>
</template>

<script>
import {mapGetters} from "vuex";

export default {
   name: "JoinGroup",
   props: {
      id: Number,
      readonly: {
         type: Boolean,
         default: false,
      },
      name: {
         type: String,
         default: ""
      },
      persistent: {
         type: Boolean,
         default: false
      }
   },
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
            show: true,
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
   mounted() {
      if (this.id)
         this.groupID = String(this.id);
   }
}
</script>

<style scoped>
.submit {
   height: 35px;
   width: 150px;
   font-size: 15px;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 999px;
   color: #F4F4F4;
   cursor: pointer;
}

.joinGroup {
   height: 250px;
   width: 100%;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
}
</style>