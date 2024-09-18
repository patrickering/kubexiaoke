/**
* 异常记录 接口
*
*  author： xiaoke
*  date： 2024-08-22 23:06:02
*/


import request from '@/plugins/request';

/**
* 异常记录列表
* @param query
*/
export function listErrorRecord (query) {
    return request({
        url: '/kube/error-record/list',
        method: 'get',
        params: query
    })
}


/**
* 异常记录分页
* @param query
*/
export function pageErrorRecord (query) {
    return request({
        url: '/kube/error-record/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除异常记录
* @param id
*/
export function delErrorRecord (id) {
    return request({
        url: '/kube/error-record/' + id,
        method: 'delete'
    })
}
/**
* 批量删除异常记录
* @param idList
*/
export function batchDeleteErrorRecord (idList) {
    return request({
        url: '/kube/error-record/batch-delete',
        method: 'delete',
        data:idList
    })
}
/**
* 添加异常记录
* @param obj
*/
export function addErrorRecord(obj) {
    return request({
        url: '/kube/error-record',
        method: 'post',
        data: obj
    })
}


/**
* 修改异常记录
* @param obj
*/
export function updateErrorRecord (obj) {
    return request({
        url: '/kube/error-record',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询异常记录
* @param id
*/
export function getErrorRecord (id) {
    return request({
        url: '/kube/error-record/get-by-id/' + id,
        method: 'get'
    })
}




/**
* 修改已读
* @param obj
*/
export function updateReadFlagErrorRecord (obj) {
    return request({
        url: '/kube/error-record/update-read-flag',
        method: 'put',
        data: obj
    })
}

