/**
 * 应用域名 接口
 *
 *  author： xiaoke
 *  date： 2024-08-19 00:39:43
 */


import request from '@/plugins/request';

/**
 * 应用域名列表
 * @param query
 */
export function listAppDomain (query) {
    return request({
        url: '/kube/app-domain/list',
        method: 'get',
        params: query
    })
}


/**
 * 应用域名分页
 * @param query
 */
export function pageAppDomain (query) {
    return request({
        url: '/kube/app-domain/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除应用域名
 * @param id
 */
export function delAppDomain (id) {
    return request({
        url: '/kube/app-domain/' + id,
        method: 'delete'
    })
}
/**
 * 添加应用域名
 * @param obj
 */
export function addAppDomain(obj) {
    return request({
        url: '/kube/app-domain',
        method: 'post',
        data: obj
    })
}


/**
 * 修改应用域名
 * @param obj
 */
export function updateAppDomain (obj) {
    return request({
        url: '/kube/app-domain',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询应用域名
 * @param id
 */
export function getAppDomain (id) {
    return request({
        url: '/kube/app-domain/get-by-id/' + id,
        method: 'get'
    })
}



