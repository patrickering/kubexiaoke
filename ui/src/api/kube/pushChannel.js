/**
 *  接口
 *
 *  author： chendengwen
 *  date： 2019-11-09 23:51:39
 */
import request from '@/plugins/request';



/**
 * 根据type查询
 * @param id
 */
export function typePushChannel (type,pushId) {
    return request({
        url: '/kube/push-channel/' + type,
        method: 'get',
        params:{
            pushId
        }
    })
}

/**
 * 修改推送渠道
 * @param id
 */
export function updatePushChannel (data) {
    return request({
        url: '/kube/push-channel/',
        method: 'put',
        data:data
    })
}
