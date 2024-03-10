<template>
  <div class="home">
    <el-container>
      <!-- <el-aside width="100px">
        <Nav></Nav>
      </el-aside> -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// import Nav from "../components/Nav.vue";
export default {
  name: "App",
  components: {
    // Nav,
  },
};
</script>

<style lang="scss" scoped>
.home {
  width: 1300px;
  height: 800px;
  background-color: rgb(39, 42, 55);
  border-radius: 15px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
</style>
