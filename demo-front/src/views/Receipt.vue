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
        <el-main style="padding-top: 10px">
          <el-table :data="tableData" border style="width:100%" v-loading="loading">
            <el-table-column align="center" label="凭证ID" width="180">
              <template slot-scope="scope">
                <span>{{ scope.row.id }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建人地址">
              <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.address }}</span>
              </template>
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" width="300">
              <template slot-scope="scope">
                <el-button
                  @click="handleOpen(scope.$index, scope.row)"
                  size="small"
                  type="primary"
                  >查看交易历史</el-button>
                  <el-button
                  @click="handleOpenForm(scope.$index, scope.row)"
                  size="small"
                  type="success"
                  >购买凭证</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-row> </el-row>
        </el-main>
      </el-container>
    </el-container>
    <el-dialog title="交易历史" :visible.sync="dialogTableVisible">
      <el-table :data="historyTableData" v-loading="hisLoading">
<!--        ru guo you yi hang dui bu shang jiu xuan ran bu chu lai-->
        <el-table-column property="rHid" label="交易ID" width="100"></el-table-column>
        <el-table-column property="senderAddress" label="出让公司" ></el-table-column>
        <el-table-column property="receiverAddress" label="购买公司"></el-table-column>
        <el-table-column property="amount" label="金额" width="100"></el-table-column>
        <el-table-column property="createTime" label="转让时间" :formatter="formatTime"></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 购买凭证 -->
    <el-dialog title="购买凭证" :visible.sync="dialogFormVisible" width="500px">
      <el-form :model="form">
        <el-form-item label="出让公司:" label-width="100px" style="text-align:left">
          <el-input v-model="form.senderAddress" autocomplete="off" disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="购买公司:" label-width="100px" style="text-align:left">
          <el-input v-model="form.receiverAddress" autocomplete="off" disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="交易金额:" label-width="100px" style="text-align:left">
          <el-input v-model="form.amount" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="transfer()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Navigator from "@/components/Navigator";
import Header from "@/components/Header";

export default {
  name: "Receipt",
  components: {
    Navigator,
    Header,
  },
  data() {
    return {
      tableData: [],
      historyTableData: [],
      loading:true,
      hisLoading:true,
      dialogTableVisible: false,
      dialogFormVisible: false,
      currentRId:0,
      form: {
          rid:'',
          senderAddress: '',
          receiverAddress: '',
          amount:'',
          createTime:''
      },
      formLabelWidth: '100px',
      users:[],
    };
  },
  methods: {
    detail: function (queryAddress) {
      this.dialogVisible = true;
      let address = this.$cookies.get("address");
      this.axios
        .get(
          `/finance/query/getCompanyEntity?address=${address}&queryAddress=${queryAddress}`
        )
        .then((response) => {
          console.log(response);
          if (response.data.code == 200) {
            let inAddress = response.data.data.companyVO.address;
            let inName = response.data.data.companyVO.name;
            let inAmount = response.data.data.companyVO.amount;
            this.companyDetail = {
              address: inAddress,
              name: inName,
              amount: inAmount,
              senderReceiptList: response.data.data.senderReceiptList,
              accepterReceiptList: response.data.data.accepterReceiptList,
            };
          } else {
            alert(`请求内容有误, ${response.data.data}`);
          }
        });
    },
    transfer: function () {
      this.axios.post("/finance/query/transReceipt",this.form)
          //
        .then((response) => {
          console.log(response);
          if (response.data.code == 200) {
            alert("transter success")
            this.dialogFormVisible = false;
          } else {
            alert(`response have error, ${response.data.data}`);
          }
      });
    },
    query: function () {
      let address = this.$cookies.get("address");
      this.axios
        .get("/finance/query/getAllReceipt?address=" + address)
        .then((response) => {
          this.loading = false
          console.log(response);
          if (response.data.code == 200) {
            this.tableData = response.data.data;
          } else {
            alert(`请求内容有误, ${response.data.data}`);
          }
        });
    },
    getHistoryData: function (rid) {
      let address = this.$cookies.get("address");
      this.axios.get("/finance/query/getAllReceiptHistoryIds?address="+address +"&rid="+rid)
          .then(resp => {
            this.hisLoading = false;
            this.historyTableData = resp.data.data
            console.log("createTime=========="+this.historyTableData)
          })
      console.log(rid);
    },
    handleOpen(index, row) {
      console.log(index,row.id);
      this.dialogTableVisible = true
      this.getHistoryData(row.id)
    },
    handleOpenForm(index, row) {
      console.log(index,row);
      this.dialogFormVisible = true
      this.form.senderAddress = row.address
      this.form.rid = row.id.toString()
      this.form.receiverAddress = this.$cookies.get('address')
    },
    formatTime: function (row, column) {
      console.log(row,column);
      var date = new Date(row.createTime);
      var Y = date.getFullYear() + '-'
      var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
      var D = date.getDate() + ' '
      var h = date.getHours() + ':'
      var m = date.getMinutes() + ':'
      var s = (date.getSeconds()+1 < 10 ? '0'+(date.getSeconds()+1) : date.getSeconds()+1)
      var date_str = Y+M+D+h+m+s
      console.log(date_str);
      return date_str
    }
  },
  mounted() {
    this.query();
  }
};
</script>

<style>
.header {
  background-color: #409eff;
  color: #fff;
  line-height: 20px;
}

.content {
  font-size: 18px !important;
}
</style>
