<!--
 * @description: 
 * @param: 
 * @return: 
-->
<template>
  <el-row style="height: 100%;">
    <el-col :span="8" :offset="8" style="border-style: solid;border-color: #e6e6e6;margin-top: 5%">
      <el-row >
        <el-col :span="16" :offset="4">
          <el-form label-width="80px">
            <h1>供应链金融应用</h1>
            <h3>登录界面</h3>
            <el-form-item label="登录用户">
              <el-select v-model="userName" placeholder="请选择公司" @visible-change="changeValue($event ,userName)">
                <el-option v-for="item in users" :key="item.split(':')[0]" :label="item" :value="item.split(':')[0]" style="text-align:left"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-col>

      </el-row>
      <el-row style="margin-bottom: 20px">
        <el-button type="primary" @click="login">登录</el-button>
        <el-button type="primary" @click="register">注册</el-button>
      </el-row>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      userName:'',
      users:[],
      selected_user_address:''
    }
  },
  methods: {
    login: function () {
      if (this.userName == "") {
        alert("请选择登录用户！")
      }else {
        let _user = JSON.parse(this.$cookies.get('users'))
        // console.log(_user)
        for (var i=0;i<_user.length;i++){
          if(_user[i].username == this.userName){
            console.log(_user[i].address)
            this.selected_user_address = _user[i].address
          }
        }

        let postData = {
          userName: this.userName,
          address: this.selected_user_address
        }
        

        this.axios.post('/finance/org/login', postData).then((response) => {
          if (response.data.code == 200) {
            console.log(response)
            this.$cookies.set('userName', this.userName)
            this.$cookies.set('address', this.selected_user_address)
            this.$cookies.set('orgType', response.data.data.companyType)
            // alert('登录成功')
            this.$router.push('/home')
          }else {
            alert(`登录失败, ${response.data.data}`)
          }
        })
      }


    },
    register: function () {
      this.$router.push('/register')
    },
    changeValue:function(callback,vc){
      console.log("回调参数"+callback);
      if(!callback){
        console.log(vc)
      }
    }
  },
  created: function(){
    let _user = JSON.parse(this.$cookies.get('users'))
    // console.log(_user)
    for (var i=0;i<_user.length;i++){
      // let _u = JSON.parse(_user[i])
      let select_str = _user[i].username.concat(':').concat(_user[i].orgType==0?'公司':'银行')
      this.users.push(select_str)
    }
    console.log(this.users)
  }
}
</script>

<style scoped>

</style>