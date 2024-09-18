/**
 * 集群域名 接口
 *
 *  author： xiaoke
 *  date： 2024-08-18 23:43:43
 */


import request from '@/plugins/request';

/**
 * 集群域名列表
 * @param query
 */
export function listDomain (query) {
    return request({
        url: '/kube/domain/list',
        method: 'get',
        params: query
    })
}


/**
 * 集群域名分页
 * @param query
 */
export function pageDomain (query) {
    return request({
        url: '/kube/domain/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除集群域名
 * @param id
 */
export function delDomain (id) {
    return request({
        url: '/kube/domain/' + id,
        method: 'delete'
    })
}
/**
 * 添加集群域名
 * @param obj
 */
export function addDomain(obj) {
    return request({
        url: '/kube/domain',
        method: 'post',
        data: obj
    })
}


/**
 * 修改集群域名
 * @param obj
 */
export function updateDomain (obj) {
    return request({
        url: '/kube/domain',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询集群域名
 * @param id
 */
export function getDomain (id) {
    return request({
        url: '/kube/domain/get-by-id/' + id,
        method: 'get'
    })
}



