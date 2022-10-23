<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">

          <el-dropdown-item @click.native="handleGetUserByUsername">
            <span>{{username}}</span>
          </el-dropdown-item>

          <router-link to="/">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>

          <el-dropdown-item @click.native="handleOpen">
            <span>修改密码</span>
          </el-dropdown-item>

          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 用户修改密码弹出层 -->
    <el-dialog title="修改密码" :visible.sync="dialogChangePassword">
       <el-form :model="changePassword" ref="changePassword" :rules="changepass">
        <el-form-item label="新密码" label-width="120px" prop="password">
          <el-input v-model="changePassword.password" type="password" autocomplete="off" style="width: 320px;"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" label-width="120px" prop="changepa">
          <el-input v-model="changePassword.changepa" type="password" autocomplete="off" style="width: 320px;"></el-input>
        </el-form-item>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChangePassword = false">取 消</el-button>
        <el-button type="primary" @click="handleChangePass">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 用户个人消息弹出层 -->
    <el-dialog
      :visible.sync="dialogVisible"
      width="60%"
      :before-close="handleClose">
      <div class="user-info">
        <div class="info">
         <el-descriptions title="我的详细信息">
           <el-descriptions-item label="真实姓名">{{userInfo.realname}}</el-descriptions-item>
           <el-descriptions-item label="用户名">{{userInfo.username}}</el-descriptions-item>
           <el-descriptions-item label="性别">{{userInfo.sex === 1 ? '男':'女'}}</el-descriptions-item>
           <el-descriptions-item label="手机号">{{userInfo.phone}}</el-descriptions-item>
           <el-descriptions-item label="邮箱">{{userInfo.email}}</el-descriptions-item>
           <el-descriptions-item label="生日">{{userInfo.birthday}}</el-descriptions-item>
           <el-descriptions-item label="角色">
             <span v-if="userInfo.role === 'admin'" style="color: #d63031;">超级管理员</span>
             <span v-if="userInfo.role === 'owner'" style="color: #1abc9c;">物业管理员</span>
             <span v-if="userInfo.role === 'user'"  style="color: #3498db;">普通用户</span>
           </el-descriptions-item>
           <el-descriptions-item label="联系地址" span="3">{{userInfo.address}}</el-descriptions-item>
           <el-descriptions-item label="备注">{{userInfo.note}}</el-descriptions-item>
        </el-descriptions>
        </div>
        <div class="avatar">
          <el-avatar :size="100" @error="errorHandler">
                <img :src="userAvatar"/>
              </el-avatar>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

import { getInfo, getUserByUsername } from '@/api/user'
import { getToken, removeToken } from '@/utils/auth'

import { changePass } from '@/api/user'

import { MessageBox, Message } from 'element-ui'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },
  data(){

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

    return{
      token:'',
      username:'',
      dialogChangePassword: false,
      dialogVisible:false,
      //修改密码
      changePassword:{
        id:'',
        password:'',
        changepa:''
      },
      //用户具体信息
      userInfo:{},
      //用户头像
      userAvatar:'',
      //表单验证
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
  mounted() {
    this.handleGetUser()
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    //退出
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)

      Message({
        message: '退出成功，谢谢您的使用',
        type: 'success',
        duration: 5 * 1000
      })
    },
    //修改密码重新登录
    async changePasslogout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },

    //关闭用户信息的弹出层
    handleClose(){
      this.dialogVisible = false
    },
    //头像加载失败的回调
    errorHandler() {
      return true
    },

    //显示用户信息
    handleGetUser(){
      this.token = getToken()
      getInfo(this.token).then((res) =>{
        this.username = res.result.name
      })
    },
    //根据当前用户的登录名获取当前用户详细信息
    handleGetUserByUsername(){
      getUserByUsername(this.username).then((res) => {
        this.userInfo = res.result
        this.userAvatar = this.userInfo.avatar

        this.dialogVisible = true
      })
    },

    //打开弹出层
    handleOpen(){
      this.dialogChangePassword = true
    },
    //提交修改密码请求
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
              //修改成功后，调用退出方法，清除旧token，重新登录
              this.changePasslogout()

              Message({
                message: '修改成功，请重新登录',
                type: 'success',
                duration: 5 * 1000
              })
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
    }

  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
.user-info{
  width: 100%;
  display: flex;
  .info{
    width: 85%;
  }
  .avatar{
    width: 20%;
    text-align: center;
    margin-top: 35px;
  }
}
</style>
