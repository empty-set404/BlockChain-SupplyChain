<!--
 * @description:
 * @param:
 * @return:
-->
<template>
  <el-row style="height: 100%;">
    <el-col :span="8" :offset="8" style="border-style: solid;border-color: #e6e6e6;margin-top: 5%">
      <el-row>
        <el-col :span="16" :offset="4">
          <el-form label-width="100px">
            <h3>注册界面</h3>
            <el-form-item label="组织名称:">
              <el-input type="primary" v-model="username"></el-input>
            </el-form-item>
            <el-form-item label="区块链地址:">
              <el-input type="primary" v-model="address"></el-input>
            </el-form-item>
            <el-form-item label="资金:">
              <el-input type="primary" v-model="credit"></el-input>
            </el-form-item>
            <el-form-item label="组织类型:">
              <el-radio-group v-model="orgType">
              <el-radio :label="0">公司</el-radio>
              <el-radio :label="1">银行</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-row style="padding-bottom:20px">
        <el-button type="primary" @click="register">注册</el-button>
        <el-button type="primary" @click="goback">返回</el-button>
      </el-row>
    </el-col>

  </el-row>

</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      orgType: 0,
      username: '',
      address:'',
      credit:1000,
      users:[]
    }
  },
  methods: {
    register: function () {
      if (this.address == ""){
        alert("区块链地址不能为空！")
      } else {
        // post can shu
        let postData = {
          username: this.username,
          orgType: this.orgType,
          address: this.address,
          credit: this.credit
        }
        // fa song qing qiu
        this.axios.post('/finance/org/register',postData).then((response) => {
          // pan duan zhuang tai 200
          if (response.data.code == 200) {
            // zhuan cookies shuju wei zi fu chuan
            this.users= JSON.parse(this.$cookies.get('users'))
            // pan duan fan hui shu ju shi fou wei kong
            if (this.users == null) {
              this.users = []
            }
          // ba can shu gei users
          this.users.push(postData)
            // cookies set can shu
          this.$cookies.set('users',JSON.stringify(this.users))
          alert('register ok')
          this.$router.push('/login')
          } else {
            alert(`register error', ${response.data.data}`)
          }
        })
      }
    },
    goback: function () {
      this.orgType = ''
      this.username =  ''
      this.address = ''
      this.$router.push('/login')
    }

  }
}
</script>

<style scoped>

</style>
