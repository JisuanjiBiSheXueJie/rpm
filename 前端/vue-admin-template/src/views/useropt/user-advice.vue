<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>
          <el-tooltip class="item" effect="light" content="新增投诉" placement="top-start">
            <el-button  type="primary" icon="el-icon-circle-plus-outline" circle @click="handleOpen" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button  type="info" @click="handleReload" icon="el-icon-refresh" circle />
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
        <el-select v-model="value" placeholder="请选择类型">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
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

      <el-table-column align="center"  show-overflow-tooltip fixed prop="complaintsType" label="类型" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.complaintsType == 1" type="text" style="color: red;" size="medium">投诉</el-button>
          <el-button v-else type="text" size="medium" >建议</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="content" label="内容" width="300" />
      <el-table-column align="center" show-overflow-tooltip prop="submitTime" label="提交时间" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="note" label="备注" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="processingResults" label="处理结果" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="processing" label="处理人" width="225" />
      <el-table-column align="center" show-overflow-tooltip prop="processingTime" label="处理时间" width="225" />
      
    </el-table>
    <!-- 分页 -->
    <div class="page">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageCurrent"
                     :page-size="pageSize" background layout="total,prev, pager, next" :total="total" />
    </div>

    <!-- 弹出层 -->
    <el-dialog title="提交投诉意见" width="45%" :visible.sync="dialog" @close="handleClose">
        <el-form ref="comp" :model="comp" :rules="rules">


          <el-form-item label="类型" :label-width="formLabelWidth" prop="complaintsType">
            <el-select v-model="comp.complaintsType" placeholder="请选择提交类型" style="width: 75%;">
              <el-option label="投诉" value="1" />
              <el-option label="建议" value="2" />
            </el-select>
          </el-form-item>


          <el-form-item label="内容" :label-width="formLabelWidth" prop="content">
            <el-input v-model="comp.content" type="textarea" rows="5" style="width: 75%;" />
          </el-form-item>


          <el-form-item label="备注" :label-width="formLabelWidth" prop="note">
            <el-input v-model="comp.note" type="textarea" rows="5" style="width: 75%;" />
          </el-form-item>

        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleSaveComp">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    formatDate
  } from '@/utils/formatDate'
  import {
    saveComp,
    getCompListByUser
  } from '@/api/useropt'
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

      // 类型验证
      const validateComplaintsType = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请选择提交类型'))
        } else {
          callback()
        }
      }

      // 内容验证
      const validateContent = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入内容'))
        } else {
          callback()
        }
      }

      return {
        // 列表数据
        tableData: [],
        formLabelWidth: '80px',

        //分页数据
        pageCurrent: 1,
        pageSize: 10,
        total: 0,
        //存放删除的id
        delList: [],
        //查询关键字
        keyword: '',
        selectTime: '',
        startTime: '',
        endTime: '',

        //弹出层
        dialog:false,
        //投诉意见对象
        comp:{
          complaintsType:'',
          content:'',
          note:''
        },

        // 选择框数据
        options: [{
          value: 1,
          label: '投诉'
        }, {
          value: 2,
          label: '建议'
        }],
        value: '',

        //表单验证
        rules:{
          complaintsType: [{
            validator: validateComplaintsType,
            trigger: 'change'
          }],
          content: [{
            validator: validateContent,
            trigger: 'blur'
          }],
        }

      }
    },
    created() {
      this.handleGetCompListByUser(this.pageCurrent, this.pageSize)
    },

    methods: {
      // 多选与反选
      handleSelectionChange(val) {
        let idList = new Array()
        for (let i = 0; i < val.length; i++) {
          //存放选中的id
          idList.push(val[i].id)
        }
        this.delList = idList
      },

      //查询按钮点击事件
      handleQuery() {
        //关键字和权限为空时不能查询
        if (this.value === '' && this.selectTime == '') {
          Message({
            message: '请输入关键字或选择权限查询',
            type: 'wranning',
            duration: 3 * 1000
          });
        } else {
          this.handleGetCompListByUser(this.pageCurrent, this.pageSize, this.value)
        }
      },

      //弹出层关闭
      handleClose(){
        this.dialog = false
        this.$refs.comp.resetFields()
        // 重置表单输入框
        this.comp.complaintsType = ''
      },

      //打开弹出层
      handleOpen(){
        this.dialog = true
      },

      //新增投诉建议
      handleSaveComp(){
         this.$refs.comp.validate(valid => {
           if (valid) {
             saveComp(this.comp).then((res) => {
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
                 this.handleGetCompListByUser(this.pageCurrent, this.pageSize)
                 // 关闭新增界面
                 this.dialog = false
               }
             })
           } else {
             Message({
               message: '请完善信息',
               type: 'error',
               duration: 5 * 1000
             })
           }
         })
      },


      //重置按钮
      handleClear() {
        //清空输入框跟选择框
        this.value = '';
        this.selectTime = '';
        //重新刷新列表
        this.handleGetCompListByUser(this.pageCurrent, this.pageSize)
      },

      //分页，每页显示的条数
      handleSizeChange(val) {
        this.pageSize = val
        this.handleGetCompListByUser(this.pageCurrent, this.pageSize, this.value)
      },
      //分页，切换上下页
      handleCurrentChange(val) {
        this.pageCurrent = val
        this.handleGetCompListByUser(this.pageCurrent, this.pageSize, this.value)
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

      //时间转换器
      formatDate(time) {
        // time = time * 1000
        let date = new Date(time)
        return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
      },

      //获取用户自己的意见
      handleGetCompListByUser(pageCurrent, pageSize, value) {

        if(this.selectTime != ''){
          this.startTime = this.formatDate(this.selectTime[0])
          this.endTitme = this.formatDate(this.selectTime[1])
          let startTime = this.startTime;
          let endTime = this.endTitme;

          getCompListByUser(pageCurrent, pageSize, value, startTime, endTime).then((res) => {
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
          getCompListByUser(pageCurrent, pageSize, value, startTime, endTime).then((res) => {

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
    width: 10%;
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
