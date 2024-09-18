/**
* 凭证 接口
*
*  author： xiaoke
*  date： 2024-08-04 23:25:50
*/


import request from '@/plugins/request';

/**
* 凭证列表
* @param query
*/
export function listCredential (query) {
    return request({
        url: '/kube/credential/list',
        method: 'get',
        params: query
    })
}


/**
* 凭证分页
* @param query
*/
export function pageCredential (query) {
    return request({
        url: '/kube/credential/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除凭证
* @param id
*/
export function delCredential (id) {
    return request({
        url: '/kube/credential/' + id,
        method: 'delete'
    })
}
/**
* 添加凭证
* @param obj
*/
export function addCredential(obj) {
    return request({
        url: '/kube/credential',
        method: 'post',
        data: obj
    })
}


/**
* 修改凭证
* @param obj
*/
export function updateCredential (obj) {
    return request({
        url: '/kube/credential',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询凭证
* @param id
*/
export function getCredential (id) {
    return request({
        url: '/kube/credential/get-by-id/' + id,
        method: 'get'
    })
}



