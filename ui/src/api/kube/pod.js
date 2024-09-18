/**
* 应用 接口
*
*  author： xiaoke
*  date： 2024-08-03 15:34:01
*/


import request from '@/plugins/request';

/**
 * 获取Pod
 */
export function getPodList () {
    return request({
        url: '/kube/pod/pod-list',
        method: 'get'
    })
}

