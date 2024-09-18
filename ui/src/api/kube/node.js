/**
* 应用 接口
*
*  author： xiaoke
*  date： 2024-08-03 15:34:01
*/


import request from '@/plugins/request';

/**
 * 获取节点
 */
export function getNodeList () {
    return request({
        url: '/kube/node/node-list',
        method: 'get'
    })
}
/**
 * 获取节点详情
 */
export function getNode (nodeName) {
    return request({
        url: '/kube/node/node/'+nodeName,
        method: 'get'
    })
}
