import request from '@/utils/request'

//新增公告
export function saveAnno(data) {
  return request({
    url: '/anno/save',
    method: 'post',
    data
  })
}

//获取所有公告
export function getAnnoList(pageCurrent, pageSize, keyword, startTime, endTime) {
  return request({
    url: '/anno/list',
    method: 'get',
    params: {
      pageCurrent,
      pageSize,
      keyword,
      startTime,
      endTime
    }
  })
}

//新增公告
export function getAnnoById(id) {
  return request({
    url: '/anno/getAnnoById',
    method: 'get',
    params: {
      id
    }
  })
}
