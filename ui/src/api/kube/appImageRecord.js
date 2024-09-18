/**
 * 应用镜像更新记录 接口
 *
 *  author： xiaoke
 *  date： 2024-08-14 00:03:32
 */


import request from '@/plugins/request';

/**
 * 应用镜像更新记录列表
 * @param query
 */
export function listAppImageRecord (query) {
    return request({
        url: '/kube/app-image-record/list',
        method: 'get',
        params: query
    })
}


/**
 * 应用镜像更新记录分页
 * @param query
 */
export function pageAppImageRecord (query) {
    return request({
        url: '/kube/app-image-record/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除应用镜像更新记录
 * @param id
 */
export function delAppImageRecord (id) {
    return request({
        url: '/kube/app-image-record/' + id,
        method: 'delete'
    })
}
/**
 * 添加应用镜像更新记录
 * @param obj
 */
export function addAppImageRecord(obj) {
    return request({
        url: '/kube/app-image-record',
        method: 'post',
        data: obj
    })
}


/**
 * 修改应用镜像更新记录
 * @param obj
 */
export function updateAppImageRecord (obj) {
    return request({
        url: '/kube/app-image-record',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询应用镜像更新记录
 * @param id
 */
export function getAppImageRecord (id) {
    return request({
        url: '/kube/app-image-record/get-by-id/' + id,
        method: 'get'
    })
}


/**
 * 回滚镜像
 * @param query
 */
export function goBackAppImageRecord (id) {
    return request({
        url: '/kube/app-image-record/go-back/'+id,
        method: 'put',
    })
}
