import request from '@/plugins/request';

export function redisInfo() {
    return request({
        url: '/system/redis/info',
        method: 'get'
    })
}
