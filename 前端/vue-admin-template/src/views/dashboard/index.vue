<template>
  <div class="dashboard-container">
    <component :is="currComponent"></component>
   <!-- <AdminPanel v-permission="['admin']" />
    <OwnerPanel v-permission="['owner']" />
    <UserPanel v-permission="['user']" /> -->
  </div>
</template>

<script>

import { getInfo } from '@/api/user'
import { getToken } from '@/utils/auth'

import AdminPanel from './components/admin-panel.vue'

import OwnerPanel from './components/owner-panel.vue'

import UserPanel from './components/user-panel.vue'

// import permission from '@/directive/permission/index.js' // 权限判断指令

export default {

  // directives: { permission },
  components: {
    AdminPanel,
    OwnerPanel,
    UserPanel
  },

  data() {
    return {
      //当前用户权限
      role:'',
      //当前显示的组件
      currComponent:''
    }
  },
  mounted() {
    this.getComponent()
  },
  methods: {

    //动态加载组件，防止控制台因为组件同时加载导致数据错误
    getComponent(){
      let token = getToken()
      getInfo(token).then((res) =>{
        this.role = res.result.roles[0]
        //拿到用户的权限，根据用户权限加载对应的组件
        if(this.role === 'admin'){
          this.currComponent = 'AdminPanel'
        }
        else if(this.role === 'owner'){
          this.currComponent = 'OwnerPanel'
        }
        else{
          this.currComponent = 'UserPanel'
        }
      })

    }
  }
}
</script>

<style lang="scss" scoped>
  .dashboard {
    &-container {
      margin: 30px;
    }

    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
</style>
