<template>
   <div class="SubjectCourse">
      <div class="teacher-view">
         <div class="stream-start-stop">

         </div>
      </div>
   </div>
</template>

<script>
export default {
   name: "SubjectCourse",
}
</script>

<style scoped>
.SubjectCourse {
   height: calc(100vh - 100px);
   width: 100%;
}


.teacher-view {
   display: flex;
   align-items: center;
   justify-content: space-between;

   min-height: 50px;
   background-color: #13111A;
}
</style>