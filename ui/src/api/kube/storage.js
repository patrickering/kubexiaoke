/**
* 存储 接口
*
*  author： xiaoke
*  date： 2024-08-03 17:00:02
*/


import request from '@/plugins/request';

/**
* 存储列表
* @param query
*/
export function listStorage (query) {
    return request({
        url: '/kube/storage/list',
        method: 'get',
        params: query
    })
}


/**
* 存储分页
* @param query
*/
export function pageStorage (query) {
    return request({
        url: '/kube/storage/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除存储
* @param id
*/
export function delStorage (id) {
    return request({
        url: '/kube/storage/' + id,
        method: 'delete'
    })
}
/**
* 添加存储
* @param obj
*/
export function addStorage(obj) {
    return request({
        url: '/kube/storage',
        method: 'post',
        data: obj
    })
}


/**
* 修改存储
* @param obj
*/
export function updateStorage (obj) {
    return request({
        url: '/kube/storage',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询存储
* @param id
*/
export function getStorage (id) {
    return request({
        url: '/kube/storage/get-by-id/' + id,
        method: 'get'
    })
}



