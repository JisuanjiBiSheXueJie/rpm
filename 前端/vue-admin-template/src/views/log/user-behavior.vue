<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
              <el-button type="success" icon="el-icon-download" circle @click="handleExportXls('操作日志')"/>
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

        <!-- 选择框 -->
        <el-select v-model="value" placeholder="根据权限">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <!-- 输入框 -->
        <el-input placeholder="请输入真实姓名" style="width: 20%" v-model.trim="keyword" />
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
      <el-table-column align="center" type="index" prop="index" label="#" width="55" />
      <el-table-column align="center" show-overflow-tooltip prop="optname" label="操作者名称" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="role" label="操作者角色" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="methods" label="方法" width="225" />
      <el-table-column align="center" show-overflow-tooltip prop="optType" label="操作类型" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="detail" label="描述" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="logLevel" label="操作等级" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="ip" label="操作IP" width="200" />
      <el-table-column align="center" show-overflow-tooltip prop="optTime" label="操作时间" width="250" />
      </el-table-column>

    </el-table>
    <!-- 分页 -->
    <div class="page">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageCurrent"
        :page-size="pageSize" background layout="total,prev, pager, next" :total="total" />
    </div>


  </div>
</template>

<script>
  import {
    formatDate
  } from '@/utils/formatDate'
  import {
   getOptLogs,
   exportOpt
  } from '@/api/log'
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
      return {
        // 列表数据
        tableData: [],
        formLabelWidth: '80px',

        // 选择框数据
        options: [{
          value: 'admin',
          label: '超级管理员'
        }, {
          value: 'owner',
          label: '物业管理员'
        }, {
          value: 'user',
          label: '业主'
        }],
        value: '',

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
        endTime: ''

      }
    },
    created() {
      this.handleGetLoginLogList(this.pageCurrent, this.pageSize)
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
      
      //导出登录日志
      handleExportXls(fileName){
         if (!fileName || typeof fileName != 'string') {
        				fileName = '导出文件'
        		}
        exportOpt(fileName).then((res) => {
            //
            const aLink = document.createElement("a");
      
            let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=utf-8"})
      
            aLink.href = URL.createObjectURL(blob)
      
            aLink.download = fileName + '.xlsx'
      
            aLink.click()
      
            document.body.appendChild(aLink)
            })
      },

      //查询按钮点击事件
      handleQuery() {
        //关键字和权限为空时不能查询
        if (this.keyword === '' && this.value === '' && this.selectTime == '') {
          Message({
            message: '请输入关键字或选择权限查询',
            type: 'wranning',
            duration: 3 * 1000
          });
        } else {
          this.handleGetLoginLogList(this.pageCurrent, this.pageSize, this.keyword, this.value)
        }
      },


      //重置按钮
      handleClear() {
        //清空输入框跟选择框
        this.keyword = '';
        this.value = '';
        this.selectTime = '';
        //重新刷新列表
        this.handleGetLoginLogList(this.pageCurrent, this.pageSize)
      },

      //分页，每页显示的条数
      handleSizeChange(val) {
        this.pageSize = val
        this.handleGetLoginLogList(this.pageCurrent, this.pageSize, this.keyword, this.value)
      },
      //分页，切换上下页
      handleCurrentChange(val) {
        this.pageCurrent = val
        this.handleGetLoginLogList(this.pageCurrent, this.pageSize, this.keyword, this.value)
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

      //获取所有用户信息，分页
      handleGetLoginLogList(pageCurrent, pageSize, keyword, value) {

        if(this.selectTime != ''){
          this.startTime = this.formatDate(this.selectTime[0])
          this.endTitme = this.formatDate(this.selectTime[1])
          let startTime = this.startTime;
          let endTime = this.endTitme;

          getOptLogs(pageCurrent, pageSize, keyword, value, startTime, endTime).then((res) => {
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
          getOptLogs(pageCurrent, pageSize, keyword, value, startTime, endTime).then((res) => {

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
    width: 80%;
  }

  .page {
    width: 100%;
    margin-top: 1rem;
    text-align: center;
  }
</style>
