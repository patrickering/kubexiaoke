/**
 * 系统角色表 接口
 *
 *  author： chensiqi
 *  date： 2019-08-05 18:05:38
 */
import request from '@/plugins/request';
/**
 * 系统角色表列表
 * @param query
 */
export function listSysRole (query) {
    return request({
        url: '/system/role/list',
        method: 'get',
        params: query
    })
}
/**
 * 系统角色表分页
 * @param query
 */
export function pageSysRole (query) {
    return request({
        url: '/system/role/page',
        method: 'get',
        params: query
    })
}
/**
 * 根据id删除系统角色表
 * @param id
 */
export function delSysRole (id) {
    return request({
        url: '/system/role/' + id,
        method: 'delete'
    })
}
/**
 * 编辑系统角色表
 * @param obj
 */
export function addSysRole (obj) {
    return request({
        url: '/system/role/',
        method: 'post',
        data: obj
    })
}

/**
 * 编辑系统角色表
 * @param obj
 */
export function editSysRole (obj) {
    return request({
        url: '/system/role/',
        method: 'put',
        data: obj
    })
}

/**
 * 根据id查询系统角色表
 * @param id
 */
export function getSysRole (id) {
    return request({
        url: '/system/role/' + id,
        method: 'get'
    })
}


export function permissionUpd (roleId, menuIds) {
    return request({
        url: '/system/role/menu',
        method: 'put',
        data: {
            roleId: roleId,
            menuIds: menuIds
        }
    })
}

export function fetchRoleTree (roleId) {
    return request({
        url: '/system/menu/tree/' + roleId,
        method: 'get'
    })
}


export function deptRoleList(query) {
    return request({
        url: '/system/role/list',
        method: 'get',
        params: query
    })
}
