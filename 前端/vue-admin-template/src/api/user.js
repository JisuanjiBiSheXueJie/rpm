import request from '@/utils/request'

//登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

//获取当前登录用户信息
export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

//登出
export function logout() {
  return request({
    url: '/user/logout',
    method: 'get'
  })
}

//获取验证码
export function getCode(key) {
  return request({
    url: `/user/code/${key}`,
    method: 'get',
  })
}

//保存用户
export function saveUser(data) {
  return request({
    url: '/admin/save',
    method: 'post',
    data
  })
}

//更新用户
export function updateUser(data) {
  return request({
    url: '/admin/update',
    method: 'post',
    data
  })
}

//删除用户
export function delUser(id) {
  return request({
    url: '/admin/delete',
    method: 'get',
    params: { id }
  })
}

//批量删除用户
export function delAllUser(ids) {
  return request({
    url: '/admin/deleteAll',
    method: 'get',
    params: { ids }
  })
}

//恢复用户
export function refreshUser(id) {
  return request({
    url: '/admin/refresh',
    method: 'get',
    params: { id }
  })
}

//批量恢复用户
export function refreshAllUser(ids) {
  return request({
    url: '/admin/refreshAll',
    method: 'get',
    params: { ids }
  })
}

//解冻用户
export function thawUser(id) {
  return request({
    url: '/admin/thaw',
    method: 'get',
    params: { id }
  })
}

//批量解冻用户
export function thawAllUser(ids) {
  return request({
    url: '/admin/thawAll',
    method: 'get',
    params: { ids }
  })
}

//通过id获取前用户
export function getUserById(id) {
  return request({
    url: '/admin/getUserById',
    method: 'get',
    params: { id }
  })
}

//冻结用户
export function changeStatus(id) {
  return request({
    url: '/admin/changeStatus',
    method: 'get',
    params: { id }
  })
}

//获取所有用户信息
export function getUserList(pageCurrent,pageSize,keyword,value) {
  return request({
    url: '/admin/list',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      keyword,
      value
    }
  })
}

//获取所有被删除用户信息
export function getDelUserList(pageCurrent,pageSize,keyword,value) {
  return request({
    url: '/admin/delList',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      keyword,
      value
    }
  })
}


//获取所有被冻结用户信息
export function getFreezeUserList(pageCurrent,pageSize,keyword,value) {
  return request({
    url: '/admin/freezeList',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      keyword,
      value
    }
  })
}


//修改密码
export function changePass(data) {
  return request({
    url: '/admin/changePass',
    method: 'post',
    data
  })
}

//重新跟配权限
export function changeRole(data) {
  return request({
    url: '/admin/changeRole',
    method: 'post',
    data
  })
}

//重新跟配权限
export function getUserByUsername(username) {
  return request({
    url: '/admin/getUserByUsername',
    method: 'get',
    params: {
     username
    }
  })
}

//导出所有用户为excel
export function exportUser(fileName) {
  return request({
    url: '/admin/exportUser',
    method: 'get',
    params: {
     fileName
    },
    responseType: 'blob'
  })
}

//导出被删除的用户为excel
export function exportDelUser(fileName) {
  return request({
    url: '/admin/exportDelUser',
    method: 'get',
    params: {
     fileName
    },
    responseType: 'blob'
  })
}

//导出被冻结的用户为excel
export function exportThawUser(fileName) {
  return request({
    url: '/admin/exportThawUser',
    method: 'get',
    params: {
     fileName
    },
    responseType: 'blob'
  })
}

