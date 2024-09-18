/**
* 异常 接口
*
*  author： xiaoke
*  date： 2020-08-24 21:37:54
*/


import request from '@/plugins/request';

/**
* 异常列表
* @param query
*/
export function listError (query) {
return request({
url: '/system/error/list',
method: 'get',
params: query
})
}

/**
* 异常分页
* @param query
*/
export function pageError (query) {
return request({
url: '/system/error/page',
method: 'get',
params: query
})
}

/**
* 根据id删除异常
* @param id
*/
export function delError (id) {
return request({
url: '/system/error/' + id,
method: 'delete'
})
}

/**
* 添加异常
* @param obj
*/
export function addError(obj) {
return request({
url: '/system/error',
method: 'post',
data: obj
})
}


/**
* 修改异常
* @param obj
*/
export function updateError (obj) {
return request({
url: '/system/error',
method: 'put',
data: obj
})
}


/**
* 根据id查询异常
* @param id
*/
export function getError (id) {
return request({
url: '/system/error/' + id,
method: 'get'
})
}
