/**
 * 登录日志 接口
 *
 *  author： xiaoke
 *  date： 2022-01-23 15:30:21
 */


import request from '@/plugins/request';

/**
 * 登录日志列表
 * @param query
 */
export function listLoginLog (query) {
    return request({
        url: '/system/login-log/list',
        method: 'get',
        params: query
    })
}


/**
 * 登录日志分页
 * @param query
 */
export function pageLoginLog (query) {
    return request({
        url: '/system/login-log/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除登录日志
 * @param id
 */
export function delLoginLog (id) {
    return request({
        url: '/system/login-log/' + id,
        method: 'delete'
    })
}

/**
 * 添加登录日志
 * @param obj
 */
export function addLoginLog(obj) {
    return request({
        url: '/system/login-log',
        method: 'post',
        data: obj
    })
}


/**
 * 修改登录日志
 * @param obj
 */
export function updateLoginLog (obj) {
    return request({
        url: '/system/login-log',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询登录日志
 * @param id
 */
export function getLoginLog (id) {
    return request({
        url: '/system/login-log/get-by-id/' + id,
        method: 'get'
    })
}


