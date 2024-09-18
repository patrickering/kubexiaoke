/**
 * 令牌 接口
 *
 *  author： chendengwen
 *  date： 2020-07-18 13:00:20
 */
import request from '@/plugins/request';

/**
 * 令牌分页
 * @param query
 */
export function pageToken (query) {
    return request({
        url: '/system/token/page',
        method: 'post',
        data: query
    })
}
/**
 * 根据token删除令牌
 * @param id
 */
export function delToken (token) {
    return request({
        url: '/system/token/' + token,
        method: 'delete'
    })
}
