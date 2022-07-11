<!--
 * @description: 
 * @param: 
 * @return: 
-->
<template>
  <div id="app">
    <el-container>
      <el-header class="header">
        <Header />
      </el-header>
      <el-container>
        <el-aside width="200px">
          <navigator></navigator>
        </el-aside>
        <el-main style="padding-top: 50px" v-loading="loading">
          <el-row>
            <el-col :span="12" :offset="6">
              <el-row>
                用户名称：{{companyName}}
              </el-row>
              <el-row>
                账户类型：{{orgType==0?'公司':'银行'}}
              </el-row>
              <el-row style="padding-top: 10px">
                账户地址：{{ address }}
              </el-row>
              <el-row style="padding-top: 10px">
                余额：{{ credit }}
              </el-row>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Navigator from "@/components/Navigator";
import Header from "@/components/Header";

export default {
  name: "Individual",
  data() {
    return {
      dialogVisible: false,
      orgType: "",
      address: "",
      companyName: "",
      credit:"",
      loading: true
    };
  },
  components: {
    Navigator,
    Header,
  },
  methods: {
    logout: function () {
      this.$router.push("/login");
    },
    showDialog: function () {
      this.dialogVisible = true;
    },
    getCompany: function () {
      let address = this.$cookies.get("address");
      this.axios
        .get("/finance/org/getCompany?address=" + address)
        .then((response) => {
          console.log(response);
          if (response.data.code == 200) {
            this.companyName = response.data.data.companyName;
            this.address = response.data.data.companyAddress;
            this.orgType = response.data.data.companyType;
            this.credit = response.data.data.creditAsset;
          } else {
            alert(`请求内容有误, ${response.data.data}`);
          }
          this.loading = false;
        });
    }
  },
  mounted:function(){
    this.getCompany()
    // this.orgType = this.$cookies.get("orgType")
    // this.address = this.$cookies.get("address")
    // this.userName = this.$cookies.get("userName")
  },
};
</script>

<style scoped>
.header {
  background-color: #409eff;
  color: #fff;
  line-height: 20px;
}
.content {
  font-size: 16px;
}
</style>