import request from '@/utils/request'

//获取所有投诉建议
export function getCompList(pageCurrent, pageSize, keyword, value, startTime, endTime) {
  return request({
    url: '/complaints/list',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      value,
      keyword,
      startTime,
      endTime
    }
  })
}

//获取单条投诉
export function getCompById(id) {
  return request({
    url: '/complaints/getCompById',
    method: 'get',
    params: {
      id
    }
  })
}


//提交处理意见
export function updateComp(data) {
  return request({
    url: '/complaints/update',
    method: 'post',
    data
  })
}
