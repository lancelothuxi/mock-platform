import request from '@/utils/request'

// 查询mock配置列表
export function listConfig(query) {
  return request({
    url: '/mock/config/list',
    method: 'get',
    params: query
  })
}

// 查询mock配置详细
export function getConfig(id) {
  return request({
    url: '/mock/config/' + id,
    method: 'get'
  })
}

// 新增mock配置
export function addConfig(data) {
  return request({
    url: '/mock/config',
    method: 'post',
    data: data
  })
}

// 修改mock配置
export function updateConfig(data) {
  return request({
    url: '/mock/config',
    method: 'put',
    data: data
  })
}

// 删除mock配置
export function delConfig(id) {
  return request({
    url: '/mock/config/' + id,
    method: 'delete'
  })
}


export function changeConfigStatus(id, enabled) {
  return request({
    url: '/mock/config/changeStatus',
    method: 'post',
    data: {
      "id":id,
      "enabled":enabled
    }
  })
}
