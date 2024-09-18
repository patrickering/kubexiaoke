/**
 * 手机 接口
 *
 *  author： chendengwen
 *  date： 2020-07-18 13:00:20
 */
import request from '@/plugins/request';


/**
 * 发送手机验证码
 * @param query
 */
export function phoneCode (mobile,sign,code,type) {
    return request({
        url: '/system/mobile/phone-code/'+mobile,
        method: 'get',
        params:{sign,code,type}
    })
}

