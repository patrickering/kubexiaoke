/**
 * 弹性伸缩 接口
 *
 *  author： xiaoke
 *  date： 2024-08-09 16:29:23
 */


import request from '@/plugins/request';


/**
 * 根据id删除弹性伸缩
 * @param id
 */
export function delAppStretch (id) {
    return request({
        url: '/kube/app-stretch/' + id,
        method: 'delete'
    })
}
/**
 * 添加弹性伸缩
 * @param obj
 */
export function addAppStretch(obj) {
    return request({
        url: '/kube/app-stretch',
        method: 'post',
        data: obj
    })
}



