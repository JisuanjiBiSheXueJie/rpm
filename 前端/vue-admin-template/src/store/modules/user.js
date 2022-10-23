import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    roles: []
  }
}
const state = getDefaultState()
const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}
const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, code, key } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, code: code, key: key }).then(response => {
        // const { data } = response
        //console.log(response.result)
        commit('SET_TOKEN', response.result.token)
        setToken(response.result.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const  data  = response.result
        if (!data) {
          reject('验证失败，请从新登录')
        }
        // const { roles, name, avatar } = data
        const name = data.username
        const avatar = data.avatar
        //这里没有用原有的赋值方式，因为一是字段名不完全对应，二是roles必须是数组，而库里存的是个字符串 所以这里需要用split转一下
        //const roles = data.roles.split(',')
        //后台打包数组权限
        const roles = data.roles
        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('用户信息权限不能为空')
        }
        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        //console.log(state)
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        commit('SET_ROLES', [])
        //logut时这里会报错，暂时先注释掉
        //dispatch('tagsView/delAllViews', null, { root: true })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      commit('SET_ROLES', [])
      resolve()
    })
  },
  // dynamically modify permissions
  //变更角色时动态更新路由表
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'
    commit('SET_TOKEN', token)
    setToken(token)
    const { roles } = await dispatch('getInfo')
    resetRouter()
    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions
}
