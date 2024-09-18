/**
 * 租户 接口
 *
 *  author： 超级管理员
 *  date： 2022-07-30 10:09:29
 */


import request from '@/plugins/request';

/**
 * 租户列表
 * @param query
 */
export function listTenant (query) {
    return request({
        url: '/system/tenant/list',
        method: 'get',
        params: query
    })
}


/**
 * 租户分页
 * @param query
 */
export function pageTenant (query) {
    return request({
        url: '/system/tenant/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除租户
 * @param id
 */
export function delTenant (id) {
    return request({
        url: '/system/tenant/' + id,
        method: 'delete'
    })
}
/**
 * 添加租户
 * @param obj
 */
export function addTenant(obj) {
    return request({
        url: '/system/tenant',
        method: 'post',
        data: obj
    })
}


/**
 * 修改租户
 * @param obj
 */
export function updateTenant (obj) {
    return request({
        url: '/system/tenant',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询租户
 * @param id
 */
export function getTenant (id) {
    return request({
        url: '/system/tenant/get-by-id/' + id,
        method: 'get'
    })
}




/**
 * 修改是否启用
 * @param obj
 */
export function updateEnableTenant (obj) {
    return request({
        url: '/system/tenant/update-enable',
        method: 'put',
        data: obj
    })
}
