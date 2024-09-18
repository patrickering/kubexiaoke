/**
 *  接口
 *
 *  author： chendengwen
 *  date： 2019-11-09 23:51:39
 */
import request from '@/plugins/request';



/**
 * 查询列表
 * @param id
 */
export function listPush (params) {
    return request({
        url: '/kube/push/list',
        method: 'get',
        params
    })
}

/**
 * 根据type查询
 * @param id
 */
export function typePush (type) {
    return request({
        url: '/kube/push/' + type,
        method: 'get'
    })
}

/**
 * 修改推送
 * @param id
 */
export function updatePush (data) {
    return request({
        url: '/kube/push/',
        method: 'put',
        data:data
    })
}