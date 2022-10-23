<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>

          <el-tooltip class="item" effect="light" content="批量解冻" placement="top-start">
            <el-button type="danger" icon="el-icon-refresh-left" circle @click="handleThawAll" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
              <el-button type="success" icon="el-icon-download" circle @click="handleExportXls('被冻结的用户')"/>
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button type="info" icon="el-icon-refresh" circle @click="handleReload" />
          </el-tooltip>

        </el-row>
      </div>
      <!-- 右边框 -->
      <div class="right-input">
        <!-- 选择框 -->
        <el-select v-model="value" placeholder="根据权限">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <!-- 输入框 -->
        <el-input v-model.trim="keyword" placeholder="请输入真实姓名" style="width: 30%" />
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
      <el-table-column align="center" show-overflow-tooltip fixed prop="realname" label="真实姓名" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="username" label="用户名" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="password" label="密码" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="phone" label="电话号码" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="email" label="邮箱" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="role" label="角色" width="120" />

      <el-table-column align="center" show-overflow-tooltip prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == 0" type="text" size="medium">正常</el-button>
          <el-button v-else type="text" size="medium" style="color: red;">冻结</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="isDel" label="删除状态" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isDel == 0" type="text" size="medium">未被删除</el-button>
          <el-button v-else type="text" size="medium" style="color: red;">已被删除</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="avatar" label="头像" width="75">
        <template slot-scope="scope">
          <el-popover placement="right" title="" trigger="hover">
            <img :src="scope.row.avatar" style="height: 200px;width: 200px">
            <img slot="reference" :src="scope.row.avatar" :alt="scope.row.avatar" style="height: 35px;width: 35px">
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="birthday" label="生日" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.birthday | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="sex" label="性别" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.sex == 0" type="text" size="medium">女</el-button>
          <el-button v-else type="text" size="medium">男</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" show-overflow-tooltip prop="address" label="地址" width="300" />

      <el-table-column align="center" show-overflow-tooltip prop="freezeBy" label="操作者" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="freezeTime" label="冻结时间" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.delTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <!-- <el-button type="text" size="small" @click="handleClick(scope.row)">查看</el-button> -->
          <el-button type="text" size="small" @click="handleThaw(scope.row)">解冻</el-button>
          <!-- <el-button type="text" size="small" style="color: red;" @click="handleDelete(scope.row)">删除</el-button> -->
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

  </div>
</template>

<script>
import {
  formatDate
} from '@/utils/formatDate'
import {
  thawUser,
  getUserById,
  getFreezeUserList,
  thawAllUser,
  exportThawUser
} from '@/api/user'
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

      // 分页数据
      pageCurrent: 1,
      pageSize: 10,
      total: 0,
      // 存放冻结的id
      ids: '',
      // 查询关键字
      keyword: ''

    }
  },
  created() {
    this.handleGetUserList(this.pageCurrent, this.pageSize)
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
      if (this.keyword === '' && this.value === '') {
        Message({
          message: '请输入关键字或选择权限查询',
          type: 'wranning',
          duration: 3 * 1000
        })
      } else {
        this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)
      }
    },

    //导出所有用户
    handleExportXls(fileName){
       if (!fileName || typeof fileName != 'string') {
      				fileName = '导出文件'
      		}
      exportThawUser(fileName).then((res) => {
          //
          const aLink = document.createElement("a");

          let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=utf-8"})

          aLink.href = URL.createObjectURL(blob)

          aLink.download = fileName + '.xlsx'

          aLink.click()

          document.body.appendChild(aLink)
          })
    },

    // 重置按钮
    handleClear() {
      // 清空输入框跟选择框
      this.keyword = ''
      this.value = ''
      // 重新刷新列表
      this.handleGetUserList(this.pageCurrent, this.pageSize)
    },

    // 分页，每页显示的条数
    handleSizeChange(val) {
      this.pageSize = val
      this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)
    },
    // 分页，切换上下页
    handleCurrentChange(val) {
      this.pageCurrent = val
      this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)
    },

    // 解冻用户
    handleThaw(val) {
      MessageBox.confirm('确定解冻该用户？', '解冻用户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        // 成功回调
        if (action === 'confirm') {
          // 发起解冻请求
          thawUser(val.id).then((res) => {
            if (res.code === 200) {
              Message({
                message: '解冻成功',
                type: 'success',
                duration: 5 * 1000
              })
              // 刷新用户列表
              this.handleGetUserList(this.pageCurrent, this.pageSize)
            } else {
              Message({
                message: '解冻失败',
                type: 'error',
                duration: 5 * 1000
              })
            }
          })
        }
      }).catch((err) => {
        if (err === 'cancel') {
          Message({
            message: '取消解冻',
            type: 'info',
            duration: 5 * 1000
          })
        }
        if (err === 'close') {
          Message({
            message: '取消解冻',
            type: 'info',
            duration: 5 * 1000
          })
        }
      })
    },

    // 批量解冻选中的用户
    handleThawAll() {
      if (this.ids === '') {
        Message({
          message: '请选择需要恢复的用户',
          type: 'warning',
          duration: 5 * 1000
        })
      } else {
        MessageBox.confirm('您确定解冻选中用户吗？', '批量解冻确认', {
          confirmButtonText: '确认解冻',
          cancelButtonText: '取消解冻',
          type: 'warning'
        }).then((action) => {
          // 确认按钮的回调
          if (action === 'confirm') {
            thawAllUser(this.ids).then((res) => {
              if (res.success === true) {
                // 刷新用户列表
                this.handleGetUserList(this.pageCurrent, this.pageSize)
                Message({
                  message: res.message,
                  type: 'success',
                  duration: 5 * 1000
                })
              } else {
                // 刷新用户列表
                this.handleGetUserList(this.pageCurrent, this.pageSize)
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
              message: '取消批量解冻',
              type: 'info',
              duration: 5 * 1000
            })
          }
          // 关闭按钮的回调
          if (err === 'close') {
            Message({
              message: '取消批量解冻',
              type: 'info',
              duration: 5 * 1000
            })
          }
        })
      }
    },

    // 将单行数据封装为对象，进行打印
    handleClick(row) {
      // console.log(row)
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

    // 获取所有用户信息，分页
    handleGetUserList(pageCurrent, pageSize, keyword, value) {
      getFreezeUserList(pageCurrent, pageSize, keyword, value).then((res) => {
        if (res.result.records.length === 0) {
          Message({
            message: '无相关数据',
            type: 'error',
            duration: 5 * 1000
          })
          // 用户列表赋值
          this.tableData = res.result.records
        } else {
          // 用户列表赋值
          this.tableData = res.result.records
          // 总记录数赋值
          this.total = res.result.total
        }
      })
    }

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
    width: 40%;
  }

  .right-input {
    display: inherit;
    justify-content: space-around;
    width: 50%;
  }

  .page {
    width: 100%;
    margin-top: 1rem;
    text-align: center;
  }
</style>
