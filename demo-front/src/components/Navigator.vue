<!--
 * @description: 
 * @param: 
 * @return: 
-->
<template>
  <el-row class="tac">
    <el-col :span="24">
    <el-menu
        class="el-menu-vertical-demo"
        style="height: 100%;" @select="handleSelect">
      <el-menu-item index="1"><h2 v-bind:style="{color:homeMenuColor}">创建凭证</h2></el-menu-item>
      <el-menu-item index="2"><h2 v-bind:style="{color:receiptMenuColor}">凭证列表</h2></el-menu-item>
      <el-menu-item index="3"><h2 v-bind:style="{color:individualMenuColor}">账户信息</h2></el-menu-item>
    </el-menu>
    </el-col>
  </el-row>
</template>
 
<script>
export default {
  name: "Navigator",
  data() {
    return {
      homeMenuColor:'red',
      receiptMenuColor:'',
      individualMenuColor:''
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath)
      if (key == '1') {
        this.$router.push('/home')
      }else if (key == '2') {
        this.$router.push('/receipt')
      }else if (key == '3') {
        this.$router.push('/individual')
      }
    },
  },
  mounted:function(){
    console.log(this.$route.path)
      if (this.$route.path == '/home') {
        this.homeMenuColor = 'red'
        this.receiptMenuColor = ''
        this.individualMenuColor = ''
      }else if (this.$route.path == '/receipt') {
        this.receiptMenuColor = 'red'
        this.homeMenuColor = ''
        this.individualMenuColor = ''
      }else if (this.$route.path == '/individual') {
        this.individualMenuColor = 'red'
        this.receiptMenuColor = ''
        this.homeMenuColor = ''
      }
  }
}
</script>

<style>

</style>