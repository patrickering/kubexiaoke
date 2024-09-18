/**
* 模板 接口
*
*  author： xiaoke
*  date： 2024-08-17 21:23:43
*/


import request from '@/plugins/request';

/**
* 模板列表
* @param query
*/
export function listTemplate (query) {
    return request({
        url: '/kube/template/list',
        method: 'get',
        params: query
    })
}


/**
* 模板分页
* @param query
*/
export function pageTemplate (query) {
    return request({
        url: '/kube/template/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除模板
* @param id
*/
export function delTemplate (id) {
    return request({
        url: '/kube/template/' + id,
        method: 'delete'
    })
}
/**
* 添加模板
* @param obj
*/
export function addTemplate(obj) {
    return request({
        url: '/kube/template',
        method: 'post',
        data: obj
    })
}


/**
* 修改模板
* @param obj
*/
export function updateTemplate (obj) {
    return request({
        url: '/kube/template',
        method: 'put',
        data: obj
    })
}


/**
* 根据id查询模板
* @param id
*/
export function getTemplate (id) {
    return request({
        url: '/kube/template/get-by-id/' + id,
        method: 'get'
    })
}



