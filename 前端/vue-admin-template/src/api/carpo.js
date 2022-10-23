import request from '@/utils/request'


//获取所有车位信息
export function getCarpoList(pageCurrent, pageSize, keyword) {
  return request({
    url: '/carpo/list',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      keyword
    }
  })
}


//新增车位信息
export function saveCarpo(data) {
  return request({
    url: '/carpo/save',
    method: 'post',
    data
  })
}


//根据id获取车位信息
export function getCarPositionById(id) {
  return request({
    url: '/carpo/getCarPositionById',
    method: 'get',
    params:{ id }
  })
}


//更新车位信息
export function updateCarpo(data) {
  return request({
    url: '/carpo/update',
    method: 'post',
    data
  })
}


//删除车位
export function deleteCarpo(id) {
  return request({
    url: '/carpo/delete',
    method: 'get',
    params:{ id }
  })
}

//批量删除车位
export function deleteAllCarpo(ids) {
  return request({
    url: '/carpo/deleteAll',
    method: 'get',
    params:{ ids }
  })
}

//导出所有的车位
export function exportCarPosition(fileName) {
  return request({
    url: '/carpo/exportCarPosition',
    method: 'get',
    params: {
     fileName
    },
    responseType: 'blob'
  })
}
