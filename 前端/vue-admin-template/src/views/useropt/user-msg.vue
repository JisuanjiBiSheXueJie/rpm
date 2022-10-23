<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>

          <el-tooltip class="item" effect="light" content="批量删除" placement="top-start">
            <el-button type="danger" icon="el-icon-delete" circle @click="handleDeleteAllMsg" />
          </el-tooltip>

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
        <el-input v-model.trim="keyword" placeholder="请输入消息标题" clearable style="width: 20%" />
        <el-input v-model.trim="realname" placeholder="请输入发送者姓名" clearable style="width: 20%" />

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

      <el-table-column align="center" show-overflow-tooltip fixed prop="msgTitle" label="消息标题" width="200"/>
      <el-table-column align="center" show-overflow-tooltip prop="content" label="消息内容" width="550" />
      <el-table-column align="center" show-overflow-tooltip prop="sendName" label="消息发送人" width="175" />
      <el-table-column align="center" show-overflow-tooltip prop="realname" label="消息接收人" width="175" />
      <el-table-column align="center" show-overflow-tooltip prop="sendTime" label="发送时间" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="isRed" label="是否已读" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isRed == 0" type="text" size="medium">未读</el-button>
          <el-button v-else type="text" size="medium" style="color: red;">已读</el-button>
        </template>
      </el-table-column>


      <el-table-column align="center" fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleClick(scope.row)">查看</el-button>
          <el-button type="text" size="small" @click="handleDelMsg(scope.row.id)" style="color: red;">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <!-- 分页 -->
    <div class="page">
      <el-pagination
        :current-page="pageCurrent"
        :page-size="pageSize"
        background
        layout="total, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>


  <el-dialog
    :visible.sync="centerDialogVisible"
    width="60%">
    <el-descriptions title="消息详情">
       <el-descriptions-item label="消息标题">{{msgInfo.msgTitle}}</el-descriptions-item>
       <el-descriptions-item label="消息发送者">{{msgInfo.sendName}}</el-descriptions-item>
       <el-descriptions-item label="消息接收者">{{msgInfo.realname}}</el-descriptions-item>
       <el-descriptions-item label="是否阅读" span="3">
           <span v-if="msgInfo.isRed === 0" style="color: #F56C6C;"> 未读 </span>
           <span v-if="msgInfo.isRed === 1" style="color: #67C23A;"> 已读 </span>
       </el-descriptions-item>
       <el-descriptions-item label="消息内容" span="3">{{msgInfo.content}}</el-descriptions-item>
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
    getMsgListByUser,
    getMsgById,
    deleteMsg,
    deleteMsgAll
  } from '@/api/msg'

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

      return {
        // 列表数据
        tableData: [],
        formLabelWidth: '80px',
        centerDialogVisible: false,

        // 分页数据
        pageCurrent: 1,
        pageSize: 10,
        total: 0,
        // 存放删除的id
        ids:'',
        // 查询关键字
        keyword: '',
        realname:'',
        selectTime: '',
        startTime: '',
        endTime: '',

        msgInfo:{},

        //消息对象
        msg:{
          msgTitle:'',
          tosendId:'',
          content:''
        }

      }
    },
    created() {
      this.handleGetMsgList(this.pageCurrent, this.pageSize)
    },

    methods: {
      // 多选与反选
      handleSelectionChange(val) {
        const idList = new Array()
        for (let i = 0; i < val.length; i++) {
          // 存放选中的id
          idList.push(val[i].id)
        }
        this.ids = idList.toString()
      },

      // 查询按钮点击事件
      handleQuery() {
        // 关键字和权限为空时不能查询
        if (this.realname === '' && this.keyword === '' && this.selectTime === '') {
          Message({
            message: '请输入关键字或选择权限查询',
            type: 'wranning',
            duration: 3 * 1000
          })
        } else {
          this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
        }
      },

      // 重置按钮
      handleClear() {
        // 清空输入框跟选择框
        this.selectTime = ''
        this.keyword = ''
        this.realname = ''
        // 重新刷新列表
        this.handleGetMsgList(this.pageCurrent, this.pageSize)
      },

      // 分页，每页显示的条数
      handleSizeChange(val) {
        this.pageSize = val
        this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
      },
      // 分页，切换上下页
      handleCurrentChange(val) {
        this.pageCurrent = val
        this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
      },

     // 查看消息
     handleClick(row) {
       this.msgInfo = row
       this.centerDialogVisible = true
     },

      //打开弹出层
      handleOpen(){
        this.dialog = true
      },

      //关闭弹出层
      handleClose(){
        this.dialog = false
      },

      // 重新刷新页面
      handleReload() {
        // 刷新页面
        location.reload()
      },

      //删除消息
      handleDelMsg(id){
        MessageBox.confirm('确定删除该消息？', '删除消息', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then((action) => {
          // 成功回调
          if (action === 'confirm') {
            deleteMsg(id).then((res) => {
              if (res.success === true) {
                Message({
                  message: res.message,
                  type: 'success',
                  duration: 5 * 1000
                })
                // 刷新列表
                this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
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

      // 批量删除消息
      handleDeleteAllMsg() {
        if (this.ids === '') {
          Message({
            message: '请选择需要删除的设施',
            type: 'warning',
            duration: 5 * 1000
          })
        } else {
          MessageBox.confirm('您确定删除选中设施吗？', '批量删除', {
            confirmButtonText: '确认删除',
            cancelButtonText: '取消删除',
            type: 'warning'
          }).then((action) => {
            // 确认按钮的回调
            if (action === 'confirm') {
              // 批量删除接口
              deleteMsgAll(this.ids).then((res) => {
                if (res.success === true) {
                  Message({
                    message: res.message,
                    type: 'success',
                    duration: 5 * 1000
                  })
                  // 刷新列表
                  this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
                } else {
                  // 刷新列表
                  this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
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


      // 时间转换器
      formatDate(time) {
        // time = time * 1000
        const date = new Date(time)
        return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
      },

      // 获取所有消息
      handleGetMsgList(pageCurrent, pageSize, keyword, realname) {
        if (this.selectTime != '') {
          this.startTime = this.formatDate(this.selectTime[0])
          this.endTitme = this.formatDate(this.selectTime[1])
          const startTime = this.startTime
          const endTime = this.endTitme

          getMsgListByUser(pageCurrent, pageSize, keyword, realname, startTime, endTime).then((res) => {
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
          getMsgListByUser(pageCurrent, pageSize, keyword, realname, startTime, endTime).then((res) => {
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
      },

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
