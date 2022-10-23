<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>
          <el-tooltip class="item" effect="light" content="新增缴费" placement="top-start">
            <el-button type="primary" style="margin-left: 10px;"  icon="el-icon-circle-plus-outline" circle @click="handleOpen" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
            <el-link :href="href">
              <el-button style="margin-left: 10px;" type="success" icon="el-icon-download" circle />
            </el-link>

          </el-tooltip>
          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button style="margin-left: 10px;" type="info" @click="handleReload" icon="el-icon-refresh" circle />
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
        <el-input placeholder="请输入收费人名称" style="width: 20%" v-model.trim="keyword" />
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
      <el-table-column align="center" show-overflow-tooltip prop="payAmount" label="缴费金额" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="amountTime" label="缴费时间" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="payType" label="缴费方式" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="collectionReason" label="缴费明细" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="note" label="备注" width="200" />
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
               <!-- 用户端不能直接选择现金支付，现金支付只支持管理员端 -->
               <!-- <el-option label="现金" value="0" /> -->
               <el-option label="微信" value="1" />
               <el-option label="支付宝" value="2" />
             </el-select>
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


  </div>
</template>

<script>
    import QRCode from 'qrcodejs2' //二维码
  import {
    formatDate
  } from '@/utils/formatDate'
  import {
    getAmountList,
    saveAmount
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
        // 列表数据
        tableData: [],
        formLabelWidth: '80px',
        dialog:false,

        //分页数据
        pageCurrent: 1,
        pageSize: 10,
        total: 0,
        //存放删除的id
        ids: '',
        //查询关键字
        keyword: '',
        selectTime: '',
        startTime: '',
        endTime: '',
        href: 'http://localhost:9001/sys/exportLogin?fileName=登录日志',

        //缴费明细对象
        amount:{
          amountType:'',
          payAmount:'',
          payType:'',
          collectionReason:'',
          note:'',
          costType:''//缴费的款项
        },

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
          payType: [{
            validator: validatePayType,
            trigger: 'change'
          }]
        }

      }
    },
    created() {
      this.handleGetAmountList(this.pageCurrent, this.pageSize)
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


      //重置按钮
      handleClear() {
        //清空输入框跟选择框
        this.keyword = '';
        this.selectTime = '';
        //重新刷新列表
        this.handleGetAmountList(this.pageCurrent, this.pageSize)
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
        //console.log(row)
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
              //日志列表赋值
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
              //日志列表赋值
              this.tableData = res.result.records
              //总记录数赋值
              this.total = res.result.total
            }else{
              //日志列表赋值
              this.tableData = res.result.records
              //总记录数赋值
              this.total = res.result.total
            }

          })
        }
      },



    }
  }
</script>
<style>
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
</style>
