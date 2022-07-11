<!--
 * @description: 
 * @param: 
 * @return: 
-->
<template>
  <el-row>
    <el-col :span="4"><div class="grid-content"><h1>供应链金融应用</h1></div></el-col>
    <el-col :span="15" >
      <div class="grid-content2"><h1>{{ this.username }}</h1></div>
    </el-col>
    <el-col :span="4"><div class="grid-content3">{{ this.address }}</div></el-col>
    <el-col :span="1"><div class="grid-content"><el-button type="text" size="medium" style="font-size:20px;color:red;padding-top:18px;" @click="logout">登出</el-button></div></el-col>
  </el-row>
</template>
 
 <script>
export default {
  name: "Header",
  data() {
    return {
      username: "",
      address: "",
    };
  },
  methods: {
    logout: function () {
      this.$cookies.remove("userName");
      this.$cookies.remove("address");
      this.$router.push("/login");
    },
  },
  mounted: function () {
    if (this.$cookies.get("address") == null) {
      alert("请先登录");
      this.$router.push("/login");
    }
  },
  created: function () {
    this.username = this.$cookies.get("userName");
    this.address = this.$cookies.get("address");
    console.log(this.username);
  },
};
</script>

<style>
.el-row {
}
.el-col {
  border-radius: 4px;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  text-align: right;
}
.grid-content2 {
  margin: 20px 0;
  text-align: right;
  font-size: 10px;
  color:chartreuse;
}
.grid-content3 {
  margin: 20px 0;
  text-align: right;
  font-size: 10px;
  color:chartreuse;
}
</style>