/**
 * 用户表 接口
 *
 *  author： chendengwen
 *  date： 2020-07-18 13:00:20
 */
import request from '@/plugins/request';

/**
 * 用户列表
 * @param query
 */
export function listUser(query) {
    return request({
        url: '/system/user/list',
        method: 'get',
        params: query
    })
}

/**
 * 用户分页
 * @param query
 */
export function pageUser(query) {
    return request({
        url: '/system/user/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除用户
 * @param id
 */
export function delUser(id) {
    return request({
        url: '/system/user/' + id,
        method: 'delete'
    })
}

/**
 * 添加用户
 * @param obj
 */
export function addUser(obj) {
    return request({
        url: '/system/user',
        method: 'post',
        data: obj
    })
}

/**
 * 修改用户
 * @param obj
 */
export function updateUser(obj) {
    return request({
        url: '/system/user',
        method: 'put',
        data: obj
    })
}

/**
 * 修改个人信息
 * @param obj
 */
export function updateInfo(obj) {
    return request({
        url: '/system/user/update/info',
        method: 'put',
        data: obj
    })
}

/**
 * 修改密码
 * @param obj
 */
export function updatePassword(obj) {
    return request({
        url: '/system/user/update/password',
        method: 'put',
        data: obj
    })
}

/**
 * 修改手机号
 * @param obj
 */
export function updatePhone(obj) {
    return request({
        url: '/system/user/update/phone',
        method: 'put',
        data: obj
    })
}

/**
 * 解绑微信
 * @param obj
 */
export function relieveWxOpenid() {
    return request({
        url: '/system/user/relieve/wx-openid',
        method: 'put'
    })
}

/**
 * 根据id查询用户表
 * @param id
 */
export function getUser(id) {
    return request({
        url: '/system/user/' + id,
        method: 'get'
    })
}


/**
 * 重制密码
 * @param obj
 */
export function resetPassword(obj) {
    return request({
        url: '/system/user/reset-password',
        method: 'put',
        data: obj
    })
}

/**
 * 解除用户限制
 * @param obj
 */
export function relieveLimit(username) {
    return request({
        url: '/system/user/relieve-limit/' + username,
        method: 'delete'
    })
}
