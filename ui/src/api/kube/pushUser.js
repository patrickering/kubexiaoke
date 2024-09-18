/**
* 推送接收人 接口
*
*  author： xiaoke
*  date： 2024-08-28 00:33:59
*/


import request from '@/plugins/request';

/**
* 推送接收人列表
* @param query
*/
export function listPushUser (query) {
    return request({
        url: '/kube/push-user/list',
        method: 'get',
        params: query
    })
}


/**
* 推送接收人分页
* @param query
*/
export function pagePushUser (query) {
    return request({
        url: '/kube/push-user/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除推送接收人
* @param id
*/
export function delPushUser (id) {
    return request({
        url: '/kube/push-user/' + id,
        method: 'delete'
    })
}
/**
* 添加推送接收人
* @param obj
*/
export function addPushUser(obj) {
    return request({
        url: '/kube/push-user',
        method: 'post',
        data: obj
    })
}


/**
* 修改推送接收人
* @param obj
*/
export function updatePushUser (obj) {
    return request({
        url: '/kube/push-user',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询推送接收人
* @param id
*/
export function getPushUser (id) {
    return request({
        url: '/kube/push-user/get-by-id/' + id,
        method: 'get'
    })
}



