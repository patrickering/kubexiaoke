/**
* 命名空间 接口
*
*  author： xiaoke
*  date： 2024-08-03 15:00:35
*/


import request from '@/plugins/request';

/**
* 命名空间列表
* @param query
*/
export function listNamespace (query) {
    return request({
        url: '/kube/namespace/list',
        method: 'get',
        params: query
    })
}


/**
* 命名空间分页
* @param query
*/
export function pageNamespace (query) {
    return request({
        url: '/kube/namespace/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除命名空间
* @param id
*/
export function delNamespace (id) {
    return request({
        url: '/kube/namespace/' + id,
        method: 'delete'
    })
}
/**
* 添加命名空间
* @param obj
*/
export function addNamespace(obj) {
    return request({
        url: '/kube/namespace',
        method: 'post',
        data: obj
    })
}


/**
* 修改命名空间
* @param obj
*/
export function updateNamespace (obj) {
    return request({
        url: '/kube/namespace',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询命名空间
* @param id
*/
export function getNamespace (id) {
    return request({
        url: '/kube/namespace/get-by-id/' + id,
        method: 'get'
    })
}

/**
* 当前命名空间
* @param id
*/
export function currentNamespace () {
    return request({
        url: '/kube/namespace/current',
        method: 'get'
    })
}



