<template>
   <div class="Select" v-if="groups.length > 0">
      <span v-if="placeholder !== ''">Votre {{ placeholder.toLowerCase() }} : </span>
      <div>
         <w-select @input="selected" :items="groups" color="black" bg-color="grey-light3" :placeholder="placeholder" no-unselect></w-select>
         <button v-if="validate" class="validate">✓</button>
      </div>
   </div>
</template>

<script>
export default {
   name: "SelectGroup",
   props: {
      groups: {
         type: Array,
         required: true,
         // The list of elements you want the user to select.
      },
      validate: {
         type: Boolean,
         default: false
         // If you need to press a button to validate your choice or no.
      },
      placeholder: {
         type: String,
         default: "",
      },
      index: {
         type: Number,
         required: true
      }
   },
   methods: {
      selected(group) {
         this.$emit('selected', {
            index: this.index,
            group: group,
         });
      }
   }
}
</script>

<style scoped>
.Select {
   display: flex;
   flex-direction: column;
   align-items: flex-start;
   justify-content: space-between;
   width: 100%;
   margin-bottom: 10px;
}

span {
   font-size: 18px;
   font-weight: 600;
   color: #F4F4F4;
   margin-bottom: 5px;
}

.Select > div {
   width: 100%;
   display: flex;
   align-items: center;
   justify-content: space-between;
}

.validate {
   width: 28px;
   height: 28px;
   margin-left: 15px;
   background-color: #5CE8AD;
   color: #F4F4F4;
   outline: none;
   border: none;
   border-radius: 50%;
   cursor: pointer;
}

.validate:hover {
   transform: scale(1.1);
}
</style>