/**
 * 配置版本 接口
 *
 *  author： xiaoke
 *  date： 2024-08-07 16:28:56
 */


import request from '@/plugins/request';

/**
 * 配置版本列表
 * @param query
 */
export function listConfigVersion (query) {
    return request({
        url: '/kube/config-version/list',
        method: 'get',
        params: query
    })
}


/**
 * 配置版本分页
 * @param query
 */
export function pageConfigVersion (query) {
    return request({
        url: '/kube/config-version/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除配置版本
 * @param id
 */
export function delConfigVersion (id) {
    return request({
        url: '/kube/config-version/' + id,
        method: 'delete'
    })
}
/**
 * 添加配置版本
 * @param obj
 */
export function addConfigVersion(obj) {
    return request({
        url: '/kube/config-version',
        method: 'post',
        data: obj
    })
}


/**
 * 修改配置版本
 * @param obj
 */
export function updateConfigVersion (obj) {
    return request({
        url: '/kube/config-version',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询配置版本
 * @param id
 */
export function getConfigVersion (id) {
    return request({
        url: '/kube/config-version/get-by-id/' + id,
        method: 'get'
    })
}



/**
 * 根据配置文件查询
 * @param configId
 */
export function getByConfig (configId) {
    return request({
        url: '/kube/config-version/config/'+configId,
        method: 'get',
    })
}
