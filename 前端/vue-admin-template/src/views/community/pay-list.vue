<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>

          <el-tooltip class="item" effect="light" content="新增缴费" placement="top-start">
            <el-button type="primary"  icon="el-icon-circle-plus-outline" circle @click="handleOpen" />
          </el-tooltip>


          <el-tooltip class="item" effect="light" content="批量删除" placement="top-start">
            <el-button type="danger" icon="el-icon-delete" circle @click="handleDelAll" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button type="info" @click="handleReload" icon="el-icon-refresh" circle />
          </el-tooltip>

        </el-row>
      </div>
      <!-- 右边框 -->
      <div class="right-input">

        <div class="block">
          <el-date-picker
            v-model="selectTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </div>

        <!-- 输入框 -->
        <el-input placeholder="请输入缴费人名称" style="width: 20%" v-model.trim="keyword" />
        <div class="">
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh-right" @click="handleClear">重置</el-button>
        </div>
      </div>

    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border style="width: 100%" stripe max-height="520px"
              @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" type="index" fixed prop="index" label="#" width="55" />
      <el-table-column align="center"  show-overflow-tooltip fixed prop="amountType" label="缴费类型" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="collectionName" label="收费人名称" width="225" />
      <el-table-column align="center" show-overflow-tooltip prop="realname" label="缴费人名称" width="225" />
      <el-table-column align="center" show-overflow-tooltip prop="payAmount" label="缴费金额" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="amountTime" label="缴费时间" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="payType" label="缴费方式" width="200" />

      <el-table-column align="center" show-overflow-tooltip prop="collectionReason" label="缴费明细" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="note" label="备注" width="200" />

      <el-table-column align="center" show-overflow-tooltip prop="createBy" label="创建人" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="createTime" label="创建时间" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" show-overflow-tooltip prop="updateBy" label="更新人" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="updateTime" label="更新时间" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.updateTime != null">{{ scope.row.updateTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleDel(scope.row.id)" style="color: red;">删除</el-button>
          <el-button type="text" size="small" @click="handleClick(scope.row)">查看</el-button>
        </template>
      </el-table-column>

    </el-table>
    <!-- 分页 -->
    <div class="page">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageCurrent"
                     :page-size="pageSize" background layout="total,prev, pager, next" :total="total" />
    </div>

 <!-- 弹出层 -->
    <el-dialog title="新增缴费" width="45%" :visible.sync="dialog" @close="handleClose">
      <el-form ref="amount" :model="amount" :rules="rules">

        <el-form-item label="付款类型" :label-width="formLabelWidth" prop="amountType">
          <el-select v-model="amount.amountType" placeholder="请选择付款类型" style="width: 75%;">
            <el-option label="电费" value="0" />
            <el-option label="水费" value="1" />
            <el-option label="燃气费" value="2" />
            <el-option label="物业费" value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="金额" :label-width="formLabelWidth" prop="payAmount">
          <el-input v-model="amount.payAmount" style="width: 75%;" placeholder="请输入金额"/>
        </el-form-item>

        <el-form-item label="付款方式" :label-width="formLabelWidth" prop="payType">
          <el-select v-model="amount.payType" placeholder="请选择付款方式" style="width: 75%;">
            <el-option label="现金" value="0" />
            <el-option label="微信" value="1" />
            <el-option label="支付宝" value="2" />
          </el-select>
        </el-form-item>

        <el-form-item label="付款人" :label-width="formLabelWidth" prop="payName">
          <el-autocomplete
            style="width: 75%;"
            v-model="amount.payName"
            :fetch-suggestions="querySearchAsync"
            :trigger-on-focus="false"
            placeholder="请输入消息发送对象"
            @select="handleSelect"
          ></el-autocomplete>
        </el-form-item>

        <el-form-item label="款项事由" :label-width="formLabelWidth" prop="collectionReason">
          <el-input v-model="amount.collectionReason" type="textarea" rows="3" style="width: 75%;" />
        </el-form-item>

        <el-form-item label="备注" :label-width="formLabelWidth" prop="note">
          <el-input v-model="amount.note" type="textarea" rows="3" style="width: 75%;" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleSavePay">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog class="el-dialoger" @close="handleSubmitQuit"
      title="收费二维码"
      :visible.sync="dialogErWeiMa"
      width="20%">
      <!-- 二维码 -->
      <div id="qrCode" ref="qrCodeDiv"></div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="handleSubmitQuit">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      :visible.sync="dialogVisible"
      width="60%"
      :before-close="handleCloseDialog">
         <el-descriptions title="缴费明细">
           <el-descriptions-item label="缴费类型">{{amountInfo.amountType}}</el-descriptions-item>
           <el-descriptions-item label="收费人名称">{{amountInfo.collectionName}}</el-descriptions-item>
           <el-descriptions-item label="缴费人名称">{{amountInfo.realname}}</el-descriptions-item>
           <el-descriptions-item label="缴费金额">{{amountInfo.payAmount}}元</el-descriptions-item>
           <el-descriptions-item label="缴费时间">{{amountInfo.amountTime}}</el-descriptions-item>
           <el-descriptions-item label="缴费方式">{{amountInfo.payType}}</el-descriptions-item>
           <el-descriptions-item label="缴费明细" span="3">{{amountInfo.collectionReason}}</el-descriptions-item>
           <el-descriptions-item label="备注">{{amountInfo.note}}</el-descriptions-item>
        </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>



  </div>
</template>

<script>
  import QRCode from 'qrcodejs2' //二维码
  import {
    formatDate
  } from '@/utils/formatDate'
  import {
    getUserList,
    saveAmount,
    getAmountList,
    exportAmount,
    deleteAmount,
    deleteAllAmount,
    getAmountById
  } from '@/api/pay'
  import {
    MessageBox,
    Message
  } from 'element-ui'


  export default {
    //时间转换器
    filters: {
      formatDate(time) {
        // time = time * 1000
        let date = new Date(time)
        return formatDate(date, 'yyyy-MM-dd')
      }
    },
    data() {

      //表单校验规则
      const validatePayAmount = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入金额'))
        }
        setTimeout(() => {
          var pattern = /^\d+.?\d{0,2}$/;
            if (!pattern.test(value)) {
            callback(new Error('请输入数字值'));
          } else {
            callback();
          }
        }, 500);
      }

      const validateAmountType = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请选择付款类型'))
        } else {
          callback()
        }
      }

      const validatePayName = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入付款人名称'))
        } else {
          callback()
        }
      }

      const validatePayType = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请选择支付方式'))
        } else {
          callback()
        }
      }

      return {
        name: "qrCode",
        dialogErWeiMa:false,
        dialogVisible:false,
        // 列表数据
        tableData: [],
        formLabelWidth: '80px',
        dialog:false,
        //分页数据
        pageCurrent: 1,
        pageSize: 10,
        total: 0,
        //存放删除的id
        ids: [],
        //查询关键字
        keyword: '',
        selectTime: '',
        startTime: '',
        endTime: '',
        ids:'',

        //缴费明细对象
        amount:{
          amountType:'',
          payAmount:'',
          payType:'',
          payId:'',
          collectionReason:'',
          note:'',
          payName:'',
          costType:''//缴费的款项
        },

        restaurants: [],
        //state: '',
        timeout:  null,
        list:[],
        //缴费明细对象
        amountInfo:{},

        //表单校验
        rules:{
          amountType: [{
            validator: validateAmountType,
            trigger: 'change'
          }],
          payAmount: [{
            validator: validatePayAmount,
            trigger: 'blur'
          }],
          payName: [{
            validator: validatePayName,
            trigger: 'blur'
          }],
          payType: [{
            validator: validatePayType,
            trigger: 'change'
          }]
        }

      }
    },
    created() {
      this.handleGetAmountList(this.pageCurrent, this.pageSize)
      this.restaurants = this.loadAll()

    },

    methods: {

      //二维码
       bindQRCode() {
        new QRCode(this.$refs.qrCodeDiv, {
          text: '模拟扫码付款',
          width: 200,
          height: 200,
          colorDark: "#333333", //二维码颜色
          colorLight: "#ffffff", //二维码背景色
          correctLevel: QRCode.CorrectLevel.L//容错率，L/M/H
        })
      },

      // 多选与反选
      handleSelectionChange(val) {
        let idList = new Array()
        for (let i = 0; i < val.length; i++) {
          //存放选中的id
          idList.push(val[i].id)
        }
        this.ids = idList.toString()
      },
      //删除
      handleDel(id){
        MessageBox.confirm('确定删除该缴费明细？', '删除缴费明细', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then((action) => {
          // 成功回调
          if (action === 'confirm') {
            deleteAmount(id).then((res) => {
              if (res.success === true) {
                Message({
                  message: res.message,
                  type: 'success',
                  duration: 5 * 1000
                })
                // 刷新列表
                this.handleGetAmountList(this.pageCurrent, this.pageSize)
              } else {
                Message({
                  message: res.message,
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            })
          }
        }).catch((err) => {
          if (err === 'cancel') {
            Message({
              message: '取消删除',
              type: 'info',
              duration: 5 * 1000
            })
          }
          if (err === 'close') {
            Message({
              message: '取消删除',
              type: 'info',
              duration: 5 * 1000
            })
          }
        })
      },

      //关闭缴费明细弹出层
      handleCloseDialog(){
        this.dialogVisible = false
      },

      //批量删除
      handleDelAll(){
        if (this.ids === '') {
          Message({
            message: '请选择需要删除的缴费明细',
            type: 'warning',
            duration: 5 * 1000
          })
        } else {
          MessageBox.confirm('您确定删除选中缴费明细吗？', '批量删除确认', {
            confirmButtonText: '确认删除',
            cancelButtonText: '取消删除',
            type: 'warning'
          }).then((action) => {
            // 确认按钮的回调
            if (action === 'confirm') {
              // 批量删除接口
              deleteAllAmount(this.ids).then((res) => {
                if (res.success === true) {
                  // 刷新
                  this.handleGetAmountList(this.pageCurrent, this.pageSize)
                  Message({
                    message: res.message,
                    type: 'success',
                    duration: 5 * 1000
                  })
                } else {
                  // 刷新
                  this.handleGetAmountList(this.pageCurrent, this.pageSize)
                  Message({
                    message: res.message,
                    type: 'error',
                    duration: 5 * 1000
                  })
                }
              })
            }
          }).catch((err) => {
            // 取消按钮的回调
            if (err === 'cancel') {
              Message({
                message: '取消批量删除',
                type: 'info',
                duration: 5 * 1000
              })
            }
            // 关闭按钮的回调
            if (err === 'close') {
              Message({
                message: '取消批量删除',
                type: 'info',
                duration: 5 * 1000
              })
            }
          })
        }
      },

     

      //查询按钮点击事件
      handleQuery() {
        //关键字和权限为空时不能查询
        if (this.keyword === '' && this.selectTime == '') {
          Message({
            message: '请输入关键字或选择权限查询',
            type: 'warning',
            duration: 3 * 1000
          });
        } else {
          this.handleGetAmountList(this.pageCurrent, this.pageSize, this.keyword)
        }
      },

      //关闭弹出层
      handleClose(){
        this.$refs.amount.resetFields()
        this.dialog = false
      },

      //打开弹出层
      handleOpen(){
        this.dialog = true
      },

      //重置按钮
      handleClear() {
        //清空输入框跟选择框
        this.keyword = '';
        this.selectTime = '';
        //重新刷新列表
        this.handleGetAmountList(this.pageCurrent, this.pageSize)
      },

      //分页，每页显示的条数
      handleSizeChange(val) {
        this.pageSize = val
        this.handleGetAmountList(this.pageCurrent, this.pageSize, this.keyword)
      },
      //分页，切换上下页
      handleCurrentChange(val) {
        this.pageCurrent = val
        this.handleGetAmountList(this.pageCurrent, this.pageSize, this.keyword)
      },


      // 将单行数据封装为对象，进行打印
      handleClick(row) {
        this.amountInfo = row
        //打开弹出层
        this.dialogVisible = true
      },

      //下拉框选择事件
      handleSelect(val) {
        //console.log(val)
      },


      //重新刷新页面
      handleReload() {
        //刷新页面
        location.reload();
      },

      formatDate(time) {
        // time = time * 1000
        let date = new Date(time)
        return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
      },

      //新增缴费
      handleSavePay(){
        this.$refs.amount.validate(valid => {
          if (valid) {
            if(this.amount.payId != ''){
              this.amount.costType = this.amount.amountType
              //当付款方式不是现金时，生成二维码
              if(this.amount.payType != 0){

                Message({
                  message: '请扫描二维码进行支付',
                  type: 'warning',
                  duration: 3 * 1000
                });

                 //生成二维码
                this.$nextTick(function () {
                   this.bindQRCode();
                 })
                //二维码出现
                this.dialogErWeiMa = true

              }else{
                saveAmount(this.amount).then((res) => {
                  if (res.success === false) {
                    Message({
                      message: res.message,
                      type: 'error',
                      duration: 5 * 1000
                    })
                  } else {
                    Message({
                      message: res.message,
                      type: 'success',
                      duration: 5 * 1000
                    })
                    // 刷新列表
                    this.handleGetAmountList(this.pageCurrent, this.pageSize, this.keyword)
                    // 关闭新增界面
                    this.dialog = false
                  }
                })
              }
            }else{
              Message({
                message: '未找到该用户',
                type: 'error',
                duration: 5 * 1000
              })
            }
          } else {
            Message({
              message: '请完善信息',
              type: 'error',
              duration: 5 * 1000
            })
          }
        })
      },

      //二维码提交按钮
      handleSubmit(){
        //发起请求
        saveAmount(this.amount).then((res) => {
          if (res.success === false) {
            Message({
              message: res.message,
              type: 'error',
              duration: 5 * 1000
            })
          } else {
            Message({
              message: res.message,
              type: 'success',
              duration: 5 * 1000
            })
            // 刷新列表
            this.handleGetAmountList(this.pageCurrent, this.pageSize, this.keyword)
            // 关闭新增界面
            this.dialog = false
          }
        })
        //二维码弹出层
        this.dialogErWeiMa = false
        //重置二维码
        this.$refs.qrCodeDiv.innerHTML = ""
      },
      //取消扫码
      handleSubmitQuit(){
        //二维码弹出层
        this.dialogErWeiMa = false
        //重置二维码
        this.$refs.qrCodeDiv.innerHTML = ""
      },


      //获取缴费记录
      handleGetAmountList(pageCurrent, pageSize, keyword) {

        if(this.selectTime != ''){
          this.startTime = this.formatDate(this.selectTime[0])
          this.endTitme = this.formatDate(this.selectTime[1])
          let startTime = this.startTime;
          let endTime = this.endTitme;

          getAmountList(pageCurrent, pageSize, keyword, startTime, endTime).then((res) => {
            if(res.result.records.length === 0){
              Message({
                message: '无相关数据',
                type: 'error',
                duration: 5 * 1000
              });
            }else{
              //列表赋值
              this.tableData = res.result.records
              //总记录数赋值
              this.total = res.result.total
            }
          })
        }else{
          let startTime = '';
          let endTime = '';
          getAmountList(pageCurrent, pageSize, keyword, startTime, endTime).then((res) => {

            if(res.result.records.length === 0){
              Message({
                message: '无相关数据',
                type: 'error',
                duration: 5 * 1000
              });
              //列表赋值
              this.tableData = res.result.records
              //总记录数赋值
              this.total = res.result.total
            }else{
              //列表赋值
              this.tableData = res.result.records
              //总记录数赋值
              this.total = res.result.total
            }

          })
        }
      },

      //弹出层搜索引擎
      loadAll() {
        getUserList().then((res) =>{
          for(let i = 0; i< res.result.length;i++){
            //设value和id
            this.list.push({value:res.result[i].realname,id:res.result[i].id})
          }
        })
        return this.list
      },
      querySearchAsync(queryString, cb) {
        var restaurants = this.restaurants;
        var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          if(results.length == 0){
            results.push({value:'无匹配用户',id:'-1'})
            cb(results);
          }else{
            cb(results);
          }
        }, 1000 * Math.random());
      },
      createStateFilter(queryString) {
        let state = this.amount.payName
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      handleSelect(item) {
        //id赋值
        this.amount.payId = item.id
      }



    }
  }
</script>
<style lang="scss" scoped>
  .controls {
    width: 100%;
    height: 3.375rem;
    display: flex;
    line-height: 3.375rem;
    justify-content: space-between;
    /* align-items: center; */
  }

  .left-botton {
    width: 30%;
  }

  .right-input {
    display: inherit;
    justify-content: space-around;
    width: 65%;
  }

  .page {
    width: 100%;
    margin-top: 1rem;
    text-align: center;
  }

  .el-dialoger .el-dialog__body{
        display: flex;
        justify-content: center;
        align-items: center;
  }
</style>
