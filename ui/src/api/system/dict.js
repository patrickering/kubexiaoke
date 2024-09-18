import request from '@/plugins/request';

export function fetchList(query) {
  return request({
    url: '/system/dict/page',
    method: 'get',
    params: query
  })
}

export function fetchItemList(query) {
  return request({
    url: '/system/dict/item/page',
    method: 'get',
    params: query
  })
}

export function addItemObj(obj) {
  return request({
    url: '/system/dict/item',
    method: 'post',
    data: obj
  })
}

export function getItemObj(id) {
  return request({
    url: '/system/dict/item/' + id,
    method: 'get'
  })
}

export function delItemObj(id) {
  return request({
    url: '/system/dict/item/' + id,
    method: 'delete'
  })
}

export function putItemObj(obj) {
  return request({
    url: '/system/dict/item',
    method: 'put',
    data: obj
  })
}

export function addObj(obj) {
  return request({
    url: '/system/dict/',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/system/dict/' + id,
    method: 'get'
  })
}

export function delObj(row) {
  return request({
    url: '/system/dict/' + row.id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/system/dict/',
    method: 'put',
    data: obj
  })
}

export function remote(type) {
  return request({
    url: '/system/dict/type/' + type,
    method: 'get'
  })
}
