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
        <el-main style="padding-top:10px">
          <el-form label-width="100px">
            <h3>创建新凭证信息</h3>
            <el-form-item label="创建人名称:">
              <label style="float:left">{{this.userName}}</label>
            </el-form-item>
            <el-form-item label="创建人地址:">
              <el-popover
              placement="top-start"
              width="240"
              trigger="hover"
              content="只能由登录用户所属地址创建新凭证">
              <el-input type="primary" slot="reference" v-model="address" disabled="disabled"></el-input>
              </el-popover>
            </el-form-item>
            <el-form-item>
            <h5 v-if="this.$cookies.get('orgType') == 1" style="color:red">(银行账户不能创建凭证)</h5>
              <el-button type="primary" @click="create" :disabled="isDisabled" >提交</el-button>
            </el-form-item>
          </el-form>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Navigator from "@/components/Navigator";
import Header from '@/components/Header';
// import config from '@/config'
export default {
  name: "Home",
  data() {
    return {
      address:'',
      userName:'',
      bank_content:'',
      isDisabled: false
    }
  },
  components: {
    Navigator,
    Header
  },
  methods: {
    create: function () {
      let address = this.$cookies.get('address')
      this.axios.get("/finance/query/createReceipt?address="+address).then((response) => {
      if (response.data.code == 200) {
        var transaction = response.data.data
        this.$alert('transaction'+transaction, 'create success', {
          confirmButtonText: '确定',
          type: 'success',
          customClass: 'winClass'
        });
      } else {
        alert(`post have error, ${response.data.date}`)
      }
      })
    }
  },
  created:function() {
    console.log("******* in home ******")
    this.address = this.$cookies.get('address')
    this.userName = this.$cookies.get('userName')
    if (this.$cookies.get('orgType') == 1){
      this.bank_content = '银行用户不能创建凭证'
      this.isDisabled = true
    }
  }
}
</script>

<style>
.header {
  background-color: #409EFF;
  color: #fff;
  line-height: 20px;
}
.content {
  font-size: 16px;
}
.winClass{
  width: 600px;
}
</style>
