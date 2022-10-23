<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>
          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button type="info" icon="el-icon-refresh" circle @click="handleReload" />
          </el-tooltip>

        </el-row>
      </div>
      <!-- 右边框 -->
      <div class="right-input">

        <div class="block">
          <el-date-picker
            style="width: 100%;"
            v-model="selectTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </div>
        <!-- 输入框 -->
        <el-input v-model.trim="keyword" placeholder="请输入提交人姓名" clearable style="width: 20%" />
        <!-- 输入框 -->
        <el-select v-model="value" placeholder="请选择类型" style="width: 20%">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>

        <div class="">
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh-right" @click="handleClear">重置</el-button>
        </div>
      </div>

    </div>

    <!-- 表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      stripe
      max-height="520px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" type="index" fixed prop="index" label="#" width="55" />

      <el-table-column align="center" show-overflow-tooltip fixed prop="complaintsType" label="类型" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.complaintsType == 1" type="text" style="color: red;" size="medium">投诉</el-button>
          <el-button v-else type="text" size="medium">建议</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="content" label="内容" width="300" />
      <el-table-column align="center" show-overflow-tooltip prop="submitTime" label="提交时间" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="realname" label="提交人" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="note" label="备注" width="200" />

      <el-table-column align="center" show-overflow-tooltip prop="isRed" label="是否阅读" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isRed == 0" type="text" style="color: red;" size="medium">未读</el-button>
          <el-button v-else type="text" size="medium">已读</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="isReply" label="是否处理" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isReply == 0" type="text" style="color: red;" size="medium">未处理</el-button>
          <el-button v-else type="text" size="medium">已处理</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="processingResults" label="处理结果" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="processing" label="处理人" width="225" />
      <el-table-column align="center" show-overflow-tooltip prop="processingTime" label="处理时间" width="225" />



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
          <el-button type="text" size="small" @click="handleClick(scope.row.id)">查看</el-button>
          <el-button type="text" size="small" @click="handleOpen(scope.row.id)">处理</el-button>
          <el-button type="text" size="small"  style="color: red;">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <!-- 分页 -->
    <div class="page">
      <el-pagination
        :current-page="pageCurrent"
        :page-size="pageSize"
        background
        layout="total,prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 弹出层 -->
    <el-dialog :title="title" width="45%" :visible.sync="dialog" @close="handleClose">
      <el-form ref="compInfo" :model="compInfo" :rules="rules">

        <el-form-item label="内容" :label-width="formLabelWidth" prop="content">
          <el-input v-model="compInfo.content" type="textarea" rows="5" style="width: 75%;" :disabled="true"/>
        </el-form-item>

        <el-form-item label="备注" :label-width="formLabelWidth" prop="note">
          <el-input v-model="compInfo.note" type="textarea" rows="5" style="width: 75%;" :disabled="true"/>
        </el-form-item>

        <el-form-item label="处理结果" :label-width="formLabelWidth" prop="processingResults">
          <el-input v-model="compInfo.processingResults" type="textarea" rows="5" style="width: 75%;" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleUpdateComp">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :visible.sync="centerDialogVisible"
      width="60%"
      :before-close="handleCloseDialog">
      <el-descriptions title="投诉建议详情">
         <el-descriptions-item label="提交人">{{compInfo.realname}}</el-descriptions-item>
         <el-descriptions-item label="提交时间">{{compInfo.submitTime}}</el-descriptions-item>


         <el-descriptions-item label="类型">
             <span v-if="compInfo.complaintsType === 1" style="color: #F56C6C;"> 投诉 </span>
             <span v-if="compInfo.complaintsType === 2" style="color: #67C23A;"> 建议 </span>
         </el-descriptions-item>
         <el-descriptions-item label="处理结果" span="10">
             <span v-if="compInfo.isReply === 0" style="color: #F56C6C;"> 未处理 </span>
             <span v-if="compInfo.isReply === 1" style="color: #67C23A;"> 已处理 </span>
         </el-descriptions-item>

         <el-descriptions-item label="内容" span="10">{{compInfo.content}}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>


  </div>
</template>

<script>
import {
  formatDate
} from '@/utils/formatDate'

import {
  getCompList,
  getCompById,
  updateComp
} from '@/api/comp'

import {
  MessageBox,
  Message
} from 'element-ui'

export default {
  // 时间转换器
  filters: {
    formatDate(time) {
      // time = time * 1000
      const date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd')
    }
  },
  data() {

    // 内容验证
    const validateProcessingResults = (rule, value, callback) => {
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
      centerDialogVisible: false,
      title:'',

      // 分页数据
      pageCurrent: 1,
      pageSize: 10,
      total: 0,
      // 存放删除的id
      delList: [],
      // 查询关键字
      keyword: '',
      selectTime: '',
      startTime: '',
      endTime: '',

      // 弹出层
      dialog: false,

      //投诉建议详细信息对象
      compInfo:{},

      // 选择框数据
      options: [{
        value: 1,
        label: '投诉'
      }, {
        value: 2,
        label: '建议'
      }],
      value: '',

      // 表单验证
      rules: {
        processingResults: [{
          validator: validateProcessingResults,
          trigger: 'blur'
        }]
      }

    }
  },
  created() {
    this.handleGetCompList(this.pageCurrent, this.pageSize)
  },

  methods: {
    // 多选与反选
    handleSelectionChange(val) {
      const idList = new Array()
      for (let i = 0; i < val.length; i++) {
        // 存放选中的id
        idList.push(val[i].id)
      }
      this.delList = idList
    },

    // 查询按钮点击事件
    handleQuery() {
      // 关键字和权限为空时不能查询
      if (this.value === '' && this.keyword === '' && this.selectTime === '') {
        Message({
          message: '请输入关键字或选择权限查询',
          type: 'wranning',
          duration: 3 * 1000
        })
      } else {
        this.handleGetCompList(this.pageCurrent, this.pageSize, this.keyword, this.value)
      }
    },

    // 弹出层关闭
    handleClose() {
      this.dialog = false
      this.$refs.compInfo.resetFields()

    },

    handleCloseDialog(){
      this.centerDialogVisible = false
    },

    // 打开弹出层
    handleOpen(id) {
      getCompById(id).then((res) =>{
        if(res.success === true){
          this.compInfo = res.result

          if(this.compInfo.complaintsType === 1){
            this.title = '投诉处理'
          }else{
            this.title = '建议处理'
          }

          this.dialog = true
          this.handleGetCompList(this.pageCurrent, this.pageSize, this.keyword, this.value)
        }else{
          Message({
            message: '无相关数据',
            type: 'error',
            duration: 5 * 1000
          })
        }
      })

      this.dialog = true
    },

    // 重置按钮
    handleClear() {
      // 清空输入框跟选择框
      this.value = ''
      this.selectTime = ''
      this.keyword = ''
      // 重新刷新列表
      this.handleGetCompList(this.pageCurrent, this.pageSize)
    },

    // 分页，每页显示的条数
    handleSizeChange(val) {
      this.pageSize = val
      this.handleGetCompList(this.pageCurrent, this.pageSize, this.keyword, this.value)
    },
    // 分页，切换上下页
    handleCurrentChange(val) {
      this.pageCurrent = val
      this.handleGetCompList(this.pageCurrent, this.pageSize, this.keyword, this.value)
    },

    // 查看投诉建议
    handleClick(id) {
      getCompById(id).then((res) =>{
        if(res.success === true){
          this.compInfo = res.result
          this.centerDialogVisible = true
          this.handleGetCompList(this.pageCurrent, this.pageSize, this.keyword, this.value)
        }else{
          Message({
            message: '无相关数据',
            type: 'error',
            duration: 5 * 1000
          })
        }
      })

    },

    // 下拉框选择事件
    handleSelect(val) {
      // console.log(val)
    },

    // 重新刷新页面
    handleReload() {
      // 刷新页面
      location.reload()
    },

    // 时间转换器
    formatDate(time) {
      // time = time * 1000
      const date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    },

    //提交处理意见
    handleUpdateComp(){
      this.$refs.compInfo.validate(valid => {
        if (valid) {
          updateComp(this.compInfo).then((res) => {
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
              this.handleGetCompList(this.pageCurrent, this.pageSize, this.keyword, this.value)
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

    // 获取所有投诉建议
    handleGetCompList(pageCurrent, pageSize, keyword, value) {
      if (this.selectTime != '') {
        this.startTime = this.formatDate(this.selectTime[0])
        this.endTitme = this.formatDate(this.selectTime[1])
        const startTime = this.startTime
        const endTime = this.endTitme

        getCompList(pageCurrent, pageSize, keyword, value, startTime, endTime).then((res) => {
          if (res.result.records.length === 0) {
            Message({
              message: '无相关数据',
              type: 'error',
              duration: 5 * 1000
            })
          } else {
            // 列表赋值
            this.tableData = res.result.records
            // 总记录数赋值
            this.total = res.result.total
          }
        })
      } else {
        const startTime = ''
        const endTime = ''
        getCompList(pageCurrent, pageSize, keyword, value, startTime, endTime).then((res) => {
          if (res.result.records.length === 0) {
            Message({
              message: '无相关数据',
              type: 'error',
              duration: 5 * 1000
            })
            // 列表赋值
            this.tableData = res.result.records
            // 总记录数赋值
            this.total = res.result.total
          } else {
            // 列表赋值
            this.tableData = res.result.records
            // 总记录数赋值
            this.total = res.result.total
          }
        })
      }
    }
  }
}
</script>
<style lang="scss">
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
    width: 80%;
  }

  .page {
    width: 100%;
    margin-top: 1rem;
    text-align: center;
  }
  .block{
    width: 30%;
  }
</style>
