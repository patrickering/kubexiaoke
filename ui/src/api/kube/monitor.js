/**
 *  接口
 *
 *  author： chendengwen
 *  date： 2019-11-09 23:51:39
 */
import request from '@/plugins/request';
/**
 * cpu
 * @param query
 */
export function getCpu (query) {
    return request({
        url: '/kube/monitor/cpu',
        method: 'get',
        params: query
    })
}

/**
 * 内存
 * @param query
 */
export function getMemory (query) {
    return request({
        url: '/kube/monitor/memory',
        method: 'get',
        params: query
    })
}
/**
 * 网络IO
 * @param query
 */
export function getNetworkIo () {
    return request({
        url: '/kube/monitor/network/io',
        method: 'get',
    })
}
/**
 * 集群内存
 * @param query
 */
export function getClusterMemory () {
    return request({
        url: '/kube/monitor/cluster/memory',
        method: 'get',
    })
}
/**
 * 集群CPU
 * @param query
 */
export function getClusterCpu () {
    return request({
        url: '/kube/monitor/cluster/cpu',
        method: 'get',
    })
}
