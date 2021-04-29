<template>
   <div class="Planning">
      <SearchBar />
     <UploadFile />
   </div>
</template>

<script>
import SearchBar from "@/components/common/SearchBar";
import UploadFile from "@/components/UploadFile";
export default {
   name: "Planning",
   components: {SearchBar,UploadFile}
}
</script>

<style scoped>

</style>
