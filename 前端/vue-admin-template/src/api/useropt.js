import request from '@/utils/request'

//单个用户提交投诉建议
export function saveComp(data) {
  return request({
    url: '/complaints/save',
    method: 'post',
   data
  })
}


//单个用户获取投诉建议列表
export function getCompListByUser(pageCurrent, pageSize, value, startTime, endTime) {
  return request({
    url: '/complaints/listByUser',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      value,
      startTime,
      endTime
    }
  })
}
