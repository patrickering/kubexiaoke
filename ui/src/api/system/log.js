import request from '@/plugins/request';

export function fetchList(query) {
  return request({
    url: '/system/log/page',
    method: 'get',
    params: query
  })
}

export function delObj(id) {
  return request({
    url: '/system/log/' + id,
    method: 'delete'
  })
}

export function addObj(obj) {
  return request({
    url: '/system/log/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/system/log/' + id,
    method: 'get'
  })
}

export function putObj(obj) {
  return request({
    url: '/system/log/',
    method: 'put',
    data: obj
  })
}

export function sendLogs(logsList) {
  return request({
    url: '/system/log/logs',
    method: 'post',
    data: logsList
  })
}
