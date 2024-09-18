/**
* 配置 接口
*
*  author： xiaoke
*  date： 2024-08-03 16:10:50
*/


import request from '@/plugins/request';

/**
* 配置列表
* @param query
*/
export function listConfig (query) {
    return request({
        url: '/kube/config/list',
        method: 'get',
        params: query
    })
}


/**
* 配置分页
* @param query
*/
export function pageConfig (query) {
    return request({
        url: '/kube/config/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除配置
* @param id
*/
export function delConfig (id) {
    return request({
        url: '/kube/config/' + id,
        method: 'delete'
    })
}
/**
* 添加配置
* @param obj
*/
export function addConfig(obj) {
    return request({
        url: '/kube/config',
        method: 'post',
        data: obj
    })
}


/**
* 修改配置
* @param obj
*/
export function updateConfig (obj) {
    return request({
        url: '/kube/config',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询配置
* @param id
*/
export function getConfig (id) {
    return request({
        url: '/kube/config/get-by-id/' + id,
        method: 'get'
    })
}



