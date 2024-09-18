/**
 * 项目人员 接口
 *
 *  author： xiaoke
 *  date： 2024-08-27 00:01:32
 */


import request from '@/plugins/request';

/**
 * 项目人员列表
 * @param query
 */
export function listProjectUser (query) {
    return request({
        url: '/kube/project-user/list',
        method: 'get',
        params: query
    })
}


/**
 * 项目人员分页
 * @param query
 */
export function pageProjectUser (query) {
    return request({
        url: '/kube/project-user/page',
        method: 'get',
        params: query
    })
}

/**
 * 根据id删除项目人员
 * @param id
 */
export function delProjectUser (id) {
    return request({
        url: '/kube/project-user/' + id,
        method: 'delete'
    })
}
/**
 * 添加项目人员
 * @param obj
 */
export function addProjectUser(obj) {
    return request({
        url: '/kube/project-user',
        method: 'post',
        data: obj
    })
}


/**
 * 修改项目人员
 * @param obj
 */
export function updateProjectUser (obj) {
    return request({
        url: '/kube/project-user',
        method: 'put',
        data: obj
    })
}


/**
 * 根据id查询项目人员
 * @param id
 */
export function getProjectUser (id) {
    return request({
        url: '/kube/project-user/get-by-id/' + id,
        method: 'get'
    })
}



