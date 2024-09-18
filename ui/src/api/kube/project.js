/**
* 项目 接口
*
*  author： xiaoke
*  date： 2024-08-03 14:21:50
*/


import request from '@/plugins/request';

/**
* 项目列表
* @param query
*/
export function listProject (query) {
    return request({
        url: '/kube/project/list',
        method: 'get',
        params: query
    })
}


/**
* 项目分页
* @param query
*/
export function pageProject (query) {
    return request({
        url: '/kube/project/page',
        method: 'get',
        params: query
    })
}

/**
* 根据id删除项目
* @param id
*/
export function delProject (id) {
    return request({
        url: '/kube/project/' + id,
        method: 'delete'
    })
}
/**
* 添加项目
* @param obj
*/
export function addProject(obj) {
    return request({
        url: '/kube/project',
        method: 'post',
        data: obj
    })
}


/**
* 修改项目
* @param obj
*/
export function updateProject (obj) {
    return request({
        url: '/kube/project',
        method: 'put',
        data: obj
    })
}
/**
* 根据id查询项目
* @param id
*/
export function getProject (id) {
    return request({
        url: '/kube/project/get-by-id/' + id,
        method: 'get'
    })
}



