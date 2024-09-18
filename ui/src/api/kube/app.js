/**
* 应用 接口
*
*  author： xiaoke
*  date： 2024-08-03 15:34:01
*/


import request from '@/plugins/request';

/**
* 应用列表
* @param query
*/
export function listApp (query) {
    return request({
        url: '/kube/app/list',
        method: 'get',
        params: query
    })
}


/**
* 应用分页
* @param query
*/
export function pageApp (query) {
    return request({
        url: '/kube/app/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除应用
* @param id
*/
export function delApp (id) {
    return request({
        url: '/kube/app/' + id,
        method: 'delete'
    })
}
/**
* 添加应用
* @param obj
*/
export function addApp(obj) {
    return request({
        url: '/kube/app',
        method: 'post',
        data: obj
    })
}


/**
* 修改应用
* @param obj
*/
export function updateApp (obj) {
    return request({
        url: '/kube/app',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询应用
* @param id
*/
export function getApp (id) {
    return request({
        url: '/kube/app/get-by-id/' + id,
        method: 'get'
    })
}

/**
 * 应用状态统计
 * @param query
 */
export function stateCount (query) {
    return request({
        url: '/kube/app/state-count',
        method: 'get',
        params: query
    })
}

/**
 * 获取已使用端口
 */
export function usePort () {
    return request({
        url: '/kube/app/use-port',
        method: 'get'
    })
}

/**
 * 应用日志
 * @param id
 */
export function getLog (params) {
    return request({
        hideLoading:true,
        url: '/kube/app/get-log',
        method: 'get',
        params:params
    })
}

/**
 * 重启应用
 * @param id
 */
export function restartApp (id) {
    return request({
        url: '/kube/app/restart/' + id,
        method: 'get'
    })
}


/**
 * 停止应用
 * @param id
 */
export function stopApp (id) {
    return request({
        url: '/kube/app/stop/' + id,
        method: 'delete'
    })
}

/**
 * 启动应用
 * @param id
 */
export function startApp (id) {
    return request({
        url: '/kube/app/start/' + id,
        method: 'put'
    })
}

/**
 * 伸缩应用
 * @param id
 */
export function telescopicApp (data) {
    return request({
        url: '/kube/app/telescopic',
        method: 'put',
        data:data
    })
}

/**
 * 删除pod
 * @param podName
 */
export function deletePod (podName) {
    return request({
        url: '/kube/app/delete-pod/' + podName,
        method: 'delete'
    })
}

/**
 * 获取节点
 */
export function getNodeList () {
    return request({
        url: '/kube/app/node-list',
        method: 'get'
    })
}
/**
 * 获取节点详情
 */
export function getNode (nodeName) {
    return request({
        url: '/kube/app/node/'+nodeName,
        method: 'get'
    })
}
