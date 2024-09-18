/**
 * 应用pod记录 接口
 *
 *  author： xiaoke
 *  date： 2024-08-14 23:23:56
 */


import request from '@/plugins/request';

/**
 * 应用pod记录列表
 * @param query
 */
export function listAppPodRecord (query) {
    return request({
        url: '/kube/app-pod-record/list',
        method: 'get',
        params: query
    })
}

/**
 * 应用pod日志
 * @param query
 */
export function appPodRecordLog (query) {
    return request({
        url: '/kube/app-pod-record/pod-history-log',
        method: 'get',
        params: query
    })
}
