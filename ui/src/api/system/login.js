import request from '@/plugins/request';
import Setting from '@/setting';

/**
 * 登录
 * @param params
 * @param randomStr
 */
export function login (params) {
    return request({
        url: '/system/login/login/'+params.type,
        method: 'get',
        params:params
    });
}

/**
 * 获取用户信息
 */
export function info () {
    return request({
        url: '/system/login/info',
        method: 'get'
    });
}

/**
 * 退出登录
 */
export function outLogin () {
    return request({
        url: '/system/login/out-login',
        method: 'delete'
    });
}
