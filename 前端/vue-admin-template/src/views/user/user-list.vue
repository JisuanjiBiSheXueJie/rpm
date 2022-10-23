<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>
          <el-tooltip class="item" effect="light" content="新增" placement="top-start">
            <el-button type="primary" icon="el-icon-circle-plus-outline" circle @click="handleOpen('save')" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="批量删除" placement="top-start">
            <el-button v-permission="['admin']" type="danger" icon="el-icon-delete" circle @click="handleDeleteAll" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
              <el-button type="success" icon="el-icon-download" circle @click="handleExportXls('用户表')"/>
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button  type="info" icon="el-icon-refresh" circle @click="handleReload" />
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
      <el-table-column align="center" show-overflow-tooltip prop="role" label="角色" width="150" >
        <template slot-scope="scope">
          <el-button v-if="scope.row.role == 'admin'" type="text" size="medium" style="color: #d63031;">超级管理员</el-button>
          <el-button v-if="scope.row.role == 'owner'" type="text" size="medium" style="color: #1abc9c;">物业管理员</el-button>
          <el-button v-if="scope.row.role == 'user'" type="text" size="medium">普通用户</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == 0" type="text" size="medium">正常</el-button>
          <el-button v-else type="text" size="medium" style="color: red;">冻结</el-button>
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
      <el-table-column align="center" show-overflow-tooltip prop="note" label="备注" width="300" />

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

      <el-table-column align="center" fixed="right" label="操作" width="290">
        <template slot-scope="scope">
          <el-button type="text" size="small" style="color: red;" @click="handleChangeStatus(scope.row.id)">冻结</el-button>
          <el-button type="text" size="small" @click="handleOpen('update',scope.row.id)">编辑</el-button>
          <el-button type="text" size="small" @click="handleOpenChangePassword(scope.row.id)">修改密码</el-button>
          <el-button type="text" size="small" style="color: #ff7675;" @click="handleOpenChangeRole(scope.row)">重新分配角色</el-button>
          <el-button type="text" size="small" style="color: red;" @click="handleDelete(scope.row)">删除</el-button>
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
    <el-dialog :title="title" width="55%" :visible.sync="dialog" @close="handleClose">
      <div class="user-up">
        <div class="from-left">
          <el-form ref="user" :model="user" :rules="rules">

            <el-form-item v-show>
              <el-input v-model="user.id" style="width: 75%;" />
            </el-form-item>

            <el-form-item label="真实名字" :label-width="formLabelWidth" prop="realname">
              <el-input v-model="user.realname" style="width: 75%;" />
            </el-form-item>

            <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
              <el-input v-model="user.username" style="width: 75%;" />
            </el-form-item>

            <el-form-item v-if="dialogStatus === 'save'" label="密码" :label-width="formLabelWidth" prop="password">
              <el-input v-model="user.password" type="password" style="width: 75%;" />
            </el-form-item>

            <el-form-item v-if="dialogStatus === 'save'" label="确认密码" :label-width="formLabelWidth" prop="checkPassword">
              <el-input v-model="checkPassword" type="password" style="width: 75%;" />
            </el-form-item>

            <el-form-item label="电话号码" :label-width="formLabelWidth" prop="phone">
              <el-input v-model="user.phone" style="width: 75%;" />
            </el-form-item>

            <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
              <el-input v-model="user.email" style="width: 75%;" />
            </el-form-item>

            <!-- 更新用户信息时不显示 -->
            <el-form-item v-if="dialogStatus === 'save'" label="状态" :label-width="formLabelWidth">
              <el-switch
                v-model="status"
                active-color="#ff4949"
                inactive-color="#13ce66"
                active-text="冻结"
                inactive-text="正常"
              />
            </el-form-item>

            <el-form-item label="性别" :label-width="formLabelWidth">
              <el-switch v-model="sex" active-text="男" inactive-text="女" />
            </el-form-item>

            <!-- 更新用户信息时不显示 -->
            <el-form-item v-if="dialogStatus === 'save'" label="角色" :label-width="formLabelWidth" prop="role">
              <el-select v-model="user.role" placeholder="请选择用户角色" style="width: 75%;">
                <el-option label="超级管理员" value="admin" />
                <el-option label="物业管理员" value="owner" />
                <el-option label="业主" value="user" />
              </el-select>
            </el-form-item>

            <el-form-item label="生日" :label-width="formLabelWidth">
              <el-date-picker v-model="user.birthday" placeholder="选择日期" style="width: 75%;" />
            </el-form-item>

            <el-form-item label="地址" :label-width="formLabelWidth" prop="address">
              <el-input v-model="user.address" style="width: 75%;" />
            </el-form-item>

            <el-form-item label="备注" :label-width="formLabelWidth">
              <el-input v-model="user.note" type="textarea" rows="5" style="width: 75%;" />
            </el-form-item>

          </el-form>
        </div>
        <div class="img-right">
          <el-upload
            class="avatar-uploader"
            :action="ipAddress + action"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button v-if="dialogStatus === 'save'" type="primary" @click="handleSaveUser">确 定</el-button>
        <el-button v-if="dialogStatus === 'update'" type="primary" @click="handleUpdateUser">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改密码 -->
    <el-dialog title="修改密码" :visible.sync="dialogChangePassword">
       <el-form :model="changePassword" ref="changePassword" :rules="changepass">
        <el-form-item label="新密码" :label-width="formLabelWidth" prop="password">
          <el-input v-model="changePassword.password" type="password" autocomplete="off" style="width: 320px;"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="changepa">
          <el-input v-model="changePassword.changepa" type="password" autocomplete="off" style="width: 320px;"></el-input>
        </el-form-item>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChangePassword = false">取 消</el-button>
        <el-button type="primary" @click="handleChangePass">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 重新分配权限 -->
    <el-dialog
      title="分配权限"
      :visible.sync="dialogChangeRole">

      <el-form :model="changeRole" ref="changeRole">
        <el-form-item label="当前用户名称" :label-width="formLabelWidth" prop="password">
          <span>{{changeRole.realname}}</span>
        </el-form-item>
        <el-form-item label="当前用户角色" :label-width="formLabelWidth" prop="password">
          <span v-if="changeRole.currRole === 0">普通用户</span>
          <span v-if="changeRole.currRole === 1" style="color: #1abc9c;">物业管理员</span>
          <span v-if="changeRole.currRole === 2" style="color: #d63031;">超级管理员</span>
        </el-form-item>
        <el-form-item label="请选择需要分配的角色" :label-width="formLabelWidth" prop="changepa">
          <el-radio-group v-model="changeRole.currRole">
            <el-radio :label="0">普通用户</el-radio>
            <el-radio :label="1" style="color: #1abc9c;">物业管理员</el-radio>
            <el-radio v-permission="['admin']" :label="2" style="color: #d63031;">超级管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogChangeRole = false">取 消</el-button>
        <el-button type="primary" @click="handleChangeRole">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>

import permission from '@/directive/permission/index.js' // 权限判断指令

import {
  formatDate
} from '@/utils/formatDate'

import {
  saveUser,
  delUser,
  getUserById,
  getUserList,
  updateUser,
  changeStatus,
  delAllUser,
  changePass,
  changeRole,
  exportUser
} from '@/api/user'
import {
  MessageBox,
  Message
} from 'element-ui'

export default {
  directives: { permission },
  // 时间转换器
  filters: {
    formatDate(time) {
      // time = time * 1000
      const date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd')
    }
  },
  data() {
    // 真实姓名验证
    const validateRealName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入真实姓名'))
      } else {
        callback()
      }
    }

    // 用户名验证
    const validateUserName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }

    // 密码验证
    const validatePassWord = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 6) {
        callback(new Error('密码长度必须大于六位'))
      } else {
        callback()
      }
    }

    // 确认密码验证
    const validateCheckPassWord = (rule, value, callback) => {
      if (this.user.password != this.checkPassword) {
        callback(new Error('两次密码不一致'))
      } else {
        callback()
      }
    }

    // 电话号码验证
    const validatePhone = (rule, value, callback) => {
      // 手机号正则表达式
      const phoneReg = /^[1][3,5,7,8,9][0-9]{9}$/
      if (!phoneReg.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }

    // 邮箱验证
    const validateEmail = (rule, value, callback) => {
      // 邮箱正则表达式
      const emailReg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/
      if (!emailReg.test(value)) {
        callback(new Error('请输入正确的邮箱'))
      } else {
        callback()
      }
    }

    // 角色验证
    const validateRoles = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请选择用户角色'))
      } else {
        callback()
      }
    }

    // 地址验证
    const validateAddress = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入地址'))
      } else {
        callback()
      }
    }

    // 修改密码验证
    const validateNewChangePass = (rule, value, callback) => {
     if (value === '') {
       callback(new Error('请输入密码'))
     } else if (value.length < 6) {
       callback(new Error('密码长度必须大于六位'))
     } else {
       callback()
     }
    }

    // 修改密码确认密码验证
    const validateAganinChangePass = (rule, value, callback) => {
      if (value != this.changePassword.password) {
        callback(new Error('两次密码不一致'))
      } else {
        callback()
      }
    }

    return {
      // 列表数据
      tableData: [],

      dialog: false,
      status: false,
      sex: false,
      imageUrl: '',
      action: '/upload/img',
      user: {
        id: '',
        username: '',
        realname: '',
        password: '',
        phone: '',
        email: '',
        role: '',
        avatar: '',
        status: 0,
        birthday: '',
        sex: 0,
        address: '',
        note: ''
      },
      //更改密码表单
      changePassword:{
        id:'',
        password:'',
        changepa:''
      },
      formLabelWidth: '220px',
      //权限分配弹出层
      dialogChangeRole:false,
      //默认权限
      changeRole:{
        id:'',
        realname:'',
        role: '',
        currRole:0
      },

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
      // 存放删除的id
      ids: '',
      // 查询关键字
      keyword: '',
      // 弹出层标题
      title: '',
      // 弹出层状态，默认为添加按钮显示
      dialogStatus: 'save',
      // 确认密码
      checkPassword: '',
      //当前环境的ip
      ipAddress:'',

      dialogChangePassword: false,

      // 表单验证规则
      rules: {
        realname: [{
          validator: validateRealName,
          trigger: 'blur'
        }],
        username: [{
          validator: validateUserName,
          trigger: 'blur'
        }],
        password: [{
          validator: validatePassWord,
          trigger: 'blur'
        }],
        checkPassword: [{
          validator: validateCheckPassWord,
          trigger: 'blur'
        }],
        role: [{
          validator: validateRoles,
          trigger: 'change'
        }],
        phone: [{
          validator: validatePhone,
          trigger: 'blur'
        }],
        address: [{
          validator: validateAddress,
          trigger: 'blur'
        }]
      },

      //修改密码确认表单
      changepass:{
        password: [{
          validator: validateNewChangePass,
          trigger: 'blur'
        }],
        changepa: [{
          validator: validateAganinChangePass,
          trigger: 'blur'
        }],
      }
    }
  },
  created() {
    this.handleGetUserList(this.pageCurrent, this.pageSize)
    this.ipAddress = process.env.VUE_APP_BASE_API
  },

  methods: {
    // 图片上传
    handleAvatarSuccess(res, file) {
      // 头像回显地址
      this.imageUrl = res.result.imgPath + res.result.fileName
      // 用户头像地址
      this.user.avatar = this.imageUrl
    },
    beforeAvatarUpload(file) {
      // 设置图片格式
     	const img_mimetype = ['image/jpeg', 'image/jpg', 'image/png']
      const isJPG = img_mimetype.includes(file.type)
     		// 限定图片大小
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能图片格式')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },

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
          type: 'warning',
          duration: 3 * 1000
        })
      } else {
        this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)
      }
    },

    //更改密码弹出层
    handleOpenChangePassword(id){
      this.changePassword.id = id
      this.dialogChangePassword = true
    },
    //修改密码
    handleChangePass(){
      this.$refs.changePassword.validate(valid => {
        if (valid) {
          changePass(this.changePassword).then((res) => {
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
              // 刷新用户列表
              this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)

               this.$refs.changePassword.resetFields()
              // 关闭新增界面
              this.dialogChangePassword = false
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
    //关闭更改密码弹出层
    handleCloseChangePassword(){
      this.dialogChangePassword = false
    },

    // 冻结账号
    handleChangeStatus(id) {
      MessageBox.confirm('是否冻结该用户？', '冻结用户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        // 成功回调
        if (action === 'confirm') {
          changeStatus(id).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              this.handleGetUserList(this.pageCurrent, this.pageSize)
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
            message: '取消冻结',
            type: 'info',
            duration: 5 * 1000
          })
        }
        if (err === 'close') {
          Message({
            message: '取消冻结',
            type: 'info',
            duration: 5 * 1000
          })
        }
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

    // 删除用户
    handleDelete(val) {
      if (val.id != 1) {
        MessageBox.confirm('确定删除该用户？', '删除用户', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then((action) => {
          // 成功回调
          if (action === 'confirm') {
            // 发起删除请求
            delUser(val.id).then((res) => {
              if (res.code === 200) {
                Message({
                  message: '删除成功',
                  type: 'success',
                  duration: 5 * 1000
                })
                // 刷新用户列表
                this.handleGetUserList(this.pageCurrent, this.pageSize)
              } else {
                Message({
                  message: '删除失败',
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
      } else {
        Message({
          message: '超级管理员不能删除',
          type: 'error',
          duration: 5 * 1000
        })
      }
    },

    // 批量删除选中的用户
    handleDeleteAll() {
      if (this.ids === '') {
        Message({
          message: '请选择需要删除的用户',
          type: 'warning',
          duration: 5 * 1000
        })
      } else {
        MessageBox.confirm('您确定删除选中用户吗？', '批量删除确认', {
          confirmButtonText: '确认删除',
          cancelButtonText: '取消删除',
          type: 'warning'
        }).then((action) => {
          // 确认按钮的回调
          if (action === 'confirm') {
            // 批量删除接口
            delAllUser(this.ids).then((res) => {
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

    //打开分配权限弹出层
    handleOpenChangeRole(val){
      this.dialogChangeRole = true
      //表单赋值
      this.changeRole.id = val.id
      this.changeRole.realname = val.realname
      if(val.role === 'user'){
        this.changeRole.currRole = 0
      }else if(val.role === 'owner'){
        this.changeRole.currRole = 1
      }else{
        this.changeRole.currRole = 2
      }

    },
    // 重新分配权限
    handleChangeRole() {
      //重新赋值权限
      if(this.changeRole.currRole == 0){
        this.changeRole.role = 'user'
      }else if(this.changeRole.currRole == 1){
        this.changeRole.role = 'owner'
      }else{
        this.changeRole.role = 'admin'
      }

      changeRole(this.changeRole).then((res) => {
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
          // 刷新用户列表
          this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)

          // 关闭弹出层
          this.dialogChangeRole = false
        }
      })
    },

    //导出所有用户
    handleExportXls(fileName){
       if (!fileName || typeof fileName != 'string') {
      				fileName = '导出文件'
      		}
      exportUser(fileName).then((res) => {
          //
          const aLink = document.createElement("a");

          let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=utf-8"})

          aLink.href = URL.createObjectURL(blob)

          aLink.download = fileName + '.xlsx'

          aLink.click()

          document.body.appendChild(aLink)
          })
    },
    // 下拉框选择事件
    handleSelect(val) {
      // console.log(val)
    },

    // 打开弹出层
    handleOpen(val, id) {
      // 打开新增页面
      if (val === 'save') {
        this.title = '新增用户'
        this.dialogStatus = 'save'
      }
      // 打开更新页面
      if (val === 'update') {
        this.title = '更新用户'
        this.dialogStatus = 'update'
        // 获取用户信息
        getUserById(id).then((res) => {
          // 给用户列表赋值
          this.user = res.result
          // 用户性别赋值
          this.user.sex === 0 ? this.sex = false : this.sex = true
          // 用户头像赋值
          this.imageUrl = this.user.avatar
        })
      }
      // 打开弹出层
      this.dialog = true
    },

    // 关闭弹出层
    handleClose() {
      // 打开弹出层
      this.dialog = false
      // 重置表单输入框
      this.$refs.user.resetFields()
      // 重置非用户对象数据
      this.checkPassword = ''
      this.sex = false
      this.status = false
      // 重置表单非输入框数据
      this.user.birthday = ''
      this.user.role = ''
      this.user.id = ''
      this.user.avatar = ''
      this.imageUrl = ''
    },

    // 重新刷新页面
    handleReload() {
      // 刷新页面
      location.reload()
    },

    // 获取所有用户信息，分页
    handleGetUserList(pageCurrent, pageSize, keyword, value) {
      getUserList(pageCurrent, pageSize, keyword, value).then((res) => {
        if (res.result.records.length === 0) {
          // 用户列表赋值
          this.tableData = res.result.records
          // 总记录数赋值
          this.total = res.result.total
          Message({
            message: '无相关数据',
            type: 'error',
            duration: 5 * 1000
          })
        } else {
          // 用户列表赋值
          this.tableData = res.result.records
          // 总记录数赋值
          this.total = res.result.total
        }
      })
    },

    // 添加用户
    handleSaveUser() {
      this.$refs.user.validate(valid => {
        if (valid) {
          // 判断状态
          this.status === false ? this.user.status = 0 : this.user.status = 1
          // 判断性别
          this.sex === false ? this.user.sex = 0 : this.user.sex = 1
          saveUser(this.user).then((res) => {
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
              // 刷新用户列表
              this.handleGetUserList(this.pageCurrent, this.pageSize)
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

    // 更新用户
    handleUpdateUser() {
      this.$refs.user.validate(valid => {
        if (valid) {
          // 判断状态
          this.status === false ? this.user.status = 0 : this.user.status = 1
          // 判断性别
          this.sex === false ? this.user.sex = 0 : this.user.sex = 1
          updateUser(this.user).then((res) => {
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
              // 刷新用户列表
              this.handleGetUserList(this.pageCurrent, this.pageSize, this.keyword, this.value)
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
  .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 50%;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
      border-color: #409EFF;
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
    .user-up{
      width: 100%;
      display: flex;
    }
    .from-left{
      width: 80%;
    }
    .img-right{
      width: 20%;
      margin-right: 100px;
    }
</style>
