import request from '@/plugins/request';

export function list(query) {
  return request({
    url: '/system/param/page',
    method: 'get',
    params: query
  })
}

export function fetchList(query) {
  return request({
    url: '/system/param/page',
    method: 'get',
    params: query
  })
}
export function fetchListRule(query) {
  return request({
    url: '/system/param/pageRule',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/system/param',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/system/param/' + id,
    method: 'get'
  })
}

export function getPublicKey(publicKey) {
  return request({
    url: '/system/param/publicValue/' + publicKey,
    method: 'get'
  })
}

export function getByKey(publicKey) {
  return request({
    url: '/system/param/get-by-key/' + publicKey,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/system/param/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/system/param',
    method: 'put',
    data: obj
  })
}
