<template>
  <footer>
    <div id="flex-container">
      <router-link to="/main"
        ><i class="el-icon-house" style="font-size: 23px"></i
      ></router-link>
      <router-link to="/course"
        ><i class="el-icon-place" style="font-size: 23px"></i
      ></router-link>
      <router-link to="/user/walk"
        ><i class="el-icon-data-line" style="font-size: 23px"></i
      ></router-link>
      <router-link to="/user/mycourse"
        ><i class="el-icon-collection" style="font-size: 23px"></i
      ></router-link>
    </div>
  </footer>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style scoped>
#flex-container {
  height: 100%;
  display: flex;
  align-items: center;
  box-shadow: 0 0 20px -9px rgba(245, 244, 244, 0.945);
}

#flex-container a {
  flex: auto;
  width: 20%;
  padding: 7px;
  color: #ffffff;
  text-align: center;
  font-size: small;
  text-decoration: none;
}

/*#flex-container a:hover{*/
/*  color: #6cb9a2;*/
/*  background: white;*/
/*  opacity: 0.6;*/
/*}*/

#flex-container a.router-link-active {
  color: #183a1d;
  /*color: #9d794a;*/
  /*background: #da8a11;*/
  /*margin: 0 0px 2px 0px;*/
  border-radius: 25%;
  padding: 1px;
}
</style>
