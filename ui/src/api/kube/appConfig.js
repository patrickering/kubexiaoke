/**
 *  接口
 *
 *  author： chendengwen
 *  date： 2019-11-09 23:51:39
 */
import request from '@/plugins/request';

/**
 * 编辑
 * @param obj
 */
export function getByVersion(versionId) {
    return request({
        url: '/kube/app-config/version/' + versionId,
        method: 'get'
    })
}

/**
 * 更新应用版本
 * @param obj
 */
export function updateAppVersion(obj) {
    return request({
        url: '/kube/app-config/update-app-version',
        method: 'put',
        data: obj
    })
}
