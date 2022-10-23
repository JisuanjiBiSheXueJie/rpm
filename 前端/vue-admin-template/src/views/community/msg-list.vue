<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>

          <el-tooltip class="item" effect="light" content="发送新消息" placement="top-start">
            <el-button type="primary" icon="el-icon-circle-plus-outline" circle @click="handleOpen" />
          </el-tooltip>

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
        <el-input v-model.trim="realname" placeholder="请输入接收者姓名" clearable style="width: 20%" />

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
      <el-table-column align="center" show-overflow-tooltip prop="content" label="消息内容" width="300" />
      <el-table-column align="center" show-overflow-tooltip prop="sendName" label="消息发送人" width="175" />
      <el-table-column align="center" show-overflow-tooltip prop="realname" label="消息接收人" width="175" />
      <el-table-column align="center" show-overflow-tooltip prop="sendTime" label="发送时间" width="175" />
      <el-table-column align="center" show-overflow-tooltip prop="isRed" label="用户是否已读" width="120">
       <template slot-scope="scope">
         <el-button v-if="scope.row.isRed == 0" style="color: red;" type="text" size="medium">未读</el-button>
         <el-button v-else type="text" size="medium" >已读</el-button>
       </template>
      </el-table-column>

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
          <el-button type="text" size="small" @click="handleDelMsg(scope.row.id)"  style="color: red;">删除</el-button>
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

    <!-- 弹出层 -->
    <el-dialog title="编辑消息" width="45%" :visible.sync="dialog" @close="handleClose">
      <el-form ref="msg" :model="msg" :rules="rules">

        <el-form-item label="消息标题" :label-width="formLabelWidth" prop="msgTitle">
          <el-input v-model="msg.msgTitle" style="width: 75%;"/>
        </el-form-item>

        <el-form-item label="接收人" :label-width="formLabelWidth" prop="realname">
          <el-autocomplete
            style="width: 75%;"
            v-model="msg.realname"
            :fetch-suggestions="querySearchAsync"
            :trigger-on-focus="false"
            placeholder="请输入消息接收人"
            @select="handleSelect"
          ></el-autocomplete>
        </el-form-item>

        <el-form-item label="消息内容" :label-width="formLabelWidth" prop="content">
          <el-input v-model="msg.content" type="textarea" rows="5" style="width: 75%;"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleSendMsg">确 定</el-button>
      </div>
    </el-dialog>



    <!-- 消息详情 -->
    <el-dialog
      title="消息详情"
      :visible.sync="centerDialogVisible"
      width="30%"
      center>
      <span>{{msgInfo.msgTitle}}</span></br>
      <span>{{msgInfo.content}}</span></br>
      <span>{{msgInfo.sendName}}</span></br>
      <span>{{msgInfo.realname}}</span></br>
      <span>用户阅读状态：<span>{{msgInfo.isRed === 1 ? '已读':'未读'}}</span></span></br>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      :visible.sync="centerDialogVisible"
      width="60%"
      :before-close="handleCloseDialog">
      <el-descriptions title="消息详情">
         <el-descriptions-item label="消息标题">{{msgInfo.msgTitle}}</el-descriptions-item>
         <el-descriptions-item label="消息发送者">{{msgInfo.sendName}}</el-descriptions-item>
         <el-descriptions-item label="消息接收者">{{msgInfo.realname}}</el-descriptions-item>
         <el-descriptions-item label="用户阅读状态" span="3">
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
    getUserList,
    sendMsg,
    getMsgList,
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

      // 标题验证
      const validateTitle = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入公告标题'))
        } else {
          callback()
        }
      }

      // 消息接收人
      const validateRealName = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入消息接收人'))
        } else {
          callback()
        }
      }

      // 内容验证
      const validateContent = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入公告内容'))
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
        ids:'',
        // 查询关键字
        keyword: '',
        realname:'',
        selectTime: '',
        startTime: '',
        endTime: '',

        // 弹出层
        dialog: false,

        //消息对象
        msg:{
          msgTitle:'',
          tosendId:'',
          content:'',
          realname:''
        },

        msgInfo:{},

        restaurants: [],
        //state: '',
        timeout:  null,
        list:[],

        // 表单验证
        rules: {
          msgTitle: [{
            validator: validateTitle,
            trigger: 'blur'
          }],
          realname: [{
            validator: validateRealName,
            trigger: 'blur'
          }],
          content: [{
            validator: validateContent,
            trigger: 'blur'
          }]
        }

      }
    },
    created() {
      this.handleGetMsgList(this.pageCurrent, this.pageSize)
      this.restaurants = this.loadAll()
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

      //关闭详情弹出层
      handleCloseDialog(){
        this.centerDialogVisible = false
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

      // 查看投诉建议
      handleClick(id) {
        getMsgById(id).then((res) =>{
          if(res.success === true){
            this.msgInfo = res.result
            this.centerDialogVisible = true
            this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
          }else{
            Message({
              message: '无相关数据',
              type: 'error',
              duration: 5 * 1000
            })
          }
        })
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

      // 时间转换器
      formatDate(time) {
        // time = time * 1000
        const date = new Date(time)
        return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
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

      //发送消息
      handleSendMsg(){
        this.$refs.msg.validate(valid => {
          if (valid) {
            if(this.msg.tosendId != ''){
              sendMsg(this.msg).then((res) => {
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
                  this.handleGetMsgList(this.pageCurrent, this.pageSize, this.keyword, this.realname)
                  // 关闭新增界面
                  this.dialog = false
                }
              })
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

      // 获取所有消息
      handleGetMsgList(pageCurrent, pageSize, keyword, realname) {
        if (this.selectTime != '') {
          this.startTime = this.formatDate(this.selectTime[0])
          this.endTitme = this.formatDate(this.selectTime[1])
          const startTime = this.startTime
          const endTime = this.endTitme

          getMsgList(pageCurrent, pageSize, keyword, realname, startTime, endTime).then((res) => {
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
          getMsgList(pageCurrent, pageSize, keyword, realname, startTime, endTime).then((res) => {
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
         let state = this.msg.realname
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      handleSelect(item) {
        //id赋值
        this.msg.tosendId = item.id
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
