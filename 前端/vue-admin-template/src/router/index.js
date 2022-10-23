import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  }
]

export const asyncRoutes = [
  //控制台路由
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '控制台中心',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '控制台中心', icon: 'dashboard', roles: ['admin','owner','user'] }
    }]
  },

  //我的消息
  {
    path: '/msg',
    component: Layout,
    children: [{
      path: 'user-msg',
      component: () => import('@/views/useropt/user-msg'),
      meta: { title: '我的消息', icon: 'message', roles: ['user'] }
    }]
  },

  //我的缴费
  {
    path: '/pay',
    component: Layout,
    children: [{
      path: 'user-pay',
      component: () => import('@/views/useropt/user-pay'),
      meta: { title: '我的缴费', icon: 'money', roles: ['user'] }
    }]
  },
  //建议投诉
  {
    path: '/advice',
    component: Layout,
    children: [{
      path: 'user-advice',
      component: () => import('@/views/useropt/user-advice'),
      meta: { title: '建议投诉', icon: 'documentation', roles: ['user'] }
    }]
  },
  //小区公告
  {
    path: '/anno',
    component: Layout,
    children: [{
      path: 'user-anno',
      component: () => import('@/views/useropt/user-anno'),
      meta: { title: '小区公告', icon: 'clipboard', roles: ['user'] }
    }]
  },
  //人员管理路由
  {
    path: '/example',
    component: Layout,
    redirect: '/example/user-list',
    name: '人员管理',
    meta: { title: '人员管理', icon: 'user', roles: ['admin','owner'] },
    children: [
      {
        path: 'user-list',
        name: 'User',
        component: () => import('@/views/user/user-list'),
        meta: { title: '用户列表', icon: 'tree-table' }
      },
      {
        path: 'del-list',
        name: 'Del',
        component: () => import('@/views/user/del-list'),
        meta: { title: '已删除用户', icon: 'el-icon-delete-solid', roles:['admin'] }
      },
      {
        path: 'freeze-list',
        name: 'Freeze',
        component: () => import('@/views/user/freeze-list'),
        meta: { title: '已冻结用户', icon: 'el-icon-s-release', roles:['admin'] }
      }
    ]
  },

  //小区管理路由
  {
    path: '/community',
    component: Layout,
    redirect: '/community/foolrs-list',
    name: '小区管理',
    meta: { title: '小区管理', icon: 'el-icon-office-building', roles: ['admin','owner'] },
    children: [
      {
        path: 'foolrs-list',
        name: 'Foolrs',
        component: () => import('@/views/community/foolrs-list'),
        meta: { title: '楼栋信息', icon: 'el-icon-s-home' }
      },
      {
        path: 'msg-list',
        name: 'Msg',
        component: () => import('@/views/community/msg-list'),
        meta: { title: '消息列表', icon: 'el-icon-message-solid' }
      },
      {
        path: 'anno-list',
        name: 'Anno',
        component: () => import('@/views/community/anno-list'),
        meta: { title: '公告列表', icon: 'anno' }
      },
      {
        path: 'carsp-list',
        name: 'Carsp',
        component: () => import('@/views/community/carsp-list'),
        meta: { title: '车位信息', icon: 'carp' }
      },
      {
        path: 'pay-list',
        name: 'Pay',
        component: () => import('@/views/community/pay-list'),
        meta: { title: '缴费明细', icon: 'money' }
      },
      {
        path: 'asset-list',
        name: 'Asset',
        component: () => import('@/views/community/asset-list'),
        meta: { title: '公共设施', icon: 'el-icon-s-operation' }
      },
      {
        path: 'comp-list',
        name: 'Complaints',
        component: () => import('@/views/community/comp-list'),
        meta: { title: '投诉建议', icon: 'el-icon-chat-dot-square' }
      }
    ]
  },

//日志路由
  {
    path: '/log',
    component: Layout,
    redirect: '/log/login-list',
    name: '系统管理',
    meta: { title: '系统管理', icon: 'el-icon-setting', roles: ['admin'] },
    children: [
      {
        path: 'login-list',
        name: 'Login',
        component: () => import('@/views/log/login-list'),
        meta: { title: '登录情况', icon: 'el-icon-s-order', roles: ['admin'] }
      },
      {
        path: 'update-list',
        name: 'Update',
        component: () => import('@/views/log/update-list'),
        meta: { title: '系统更新日志', icon: 'form', roles: ['admin'] }
      },
      {
        path: 'http://localhost:9001/druid/login.html',
        name: 'Druid',
        meta: { title: 'Druid数据库监控', icon: 'link', roles: ['admin'] }
      },
      {
        path: 'user-behavior',
        name: 'Behavior',
        component: () => import('@/views/log/user-behavior'),
        meta: { title: '用户行为', icon: 'el-icon-tickets', roles: ['admin'] }
      }

    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: false }
]


const createRouter = () => new Router({
  mode: 'hash', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
