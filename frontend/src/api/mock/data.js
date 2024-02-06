import request from '@/utils/request'

// 查询mock数据列表
export function listData(query) {
  return request({
    url: '/mock/data/list',
    method: 'get',
    params: query
  })
}

// 查询mock数据详细
export function getData(id) {
  return request({
    url: '/mock/data/' + id,
    method: 'get'
  })
}

// 新增mock数据
export function addData(data) {
  return request({
    url: '/mock/data',
    method: 'post',
    data: data
  })
}

// 修改mock数据
export function updateData(data) {
  return request({
    url: '/mock/data',
    method: 'put',
    data: data
  })
}

// 删除mock数据
export function delData(id) {
  return request({
    url: '/mock/data/' + id,
    method: 'delete'
  })
}
