/**
 * 加密工具
 * xiaoke
 * 2018年9月29日 22:05:21
 */

import Setting from '@/setting';
import dataUtils from '@/utils/dataUtils'
import md5 from 'js-md5';
var CryptoJS = require('crypto-js')
let secretUtils = {}
var key = CryptoJS.enc.Latin1.parse('abcdef0123456789')

// 加密
secretUtils.EncryptData = (data) => {
    var srcs = CryptoJS.enc.Utf8.parse(data)
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    })
    return encrypted.toString()
}

// 解密
secretUtils.DecryptData = (data) => {
    var decrypt = CryptoJS.AES.decrypt(data, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    })
    var result = JSON.parse(CryptoJS.enc.Utf8.stringify(decrypt).toString())
    return result
}

/**
 * 权限验证
 * @param el
 * @param data
 */
secretUtils.permission = (el, data) => {
    let have = false
    const userInfo = dataUtils.getData(Setting.key.userInfo)
    if(userInfo!=null){
        let permissions = userInfo.permissions
        if (permissions != null) {
            for(let i = 0;i<permissions.length;i++){
                if(permissions[i]==data){
                    have = true
                    break;
                }
            }
        }
    }
    if(!have){
        if(el!=null){
            el.remove()
        }

    }

    return have;
}

secretUtils.createUniqueId = function (n) {
    var random = function () { // 生成10-12位不等的字符串
        return Number(Math.random().toString().substr(2)).toString(36); // 转换成十六进制
    }
    var arr = [];

    function createId() {
        var num = random();
        var _bool = false;
        arr.forEach(v => {
            if (v === num) _bool = true;
        });
        if (_bool) {
            createId();
        } else {
            arr.push(num);
        }
    }

    var i = 0;
    while (i < n) {
        createId();
        i++;
    }
    return arr;
}

secretUtils.createSign = function (header) {
    let key = secretUtils.createUniqueId(1)[0];
    header['Request-Key'] = key
    var timestamp = Date.parse(new Date());
    header['Request-Timestamp'] = timestamp;
    header['Request-Sign'] = md5(Setting.requestSalt + key + timestamp).toUpperCase()

    //幂等key
    let idempotent = null
    for (let i = 0; i < 5; i++) {
        idempotent += secretUtils.createUniqueId(1)[0];
    }
    header['idempotent'] = idempotent;
    return header;
}


/**
 *密码加密处理
 */
secretUtils.encryption = (params) => {
    let {
        data,
        type,
        param,
        key
    } = params
    const result = JSON.parse(JSON.stringify(data))
    if (type === 'Base64') {
        param.forEach(ele => {
            result[ele] = btoa(result[ele])
        })
    } else {
        param.forEach(ele => {
            var data = result[ele]
            key = CryptoJS.enc.Latin1.parse(key)
            var iv = key
            // 加密
            var encrypted = CryptoJS.AES.encrypt(
                data,
                key, {
                    iv: iv,
                    mode: CryptoJS.mode.CBC,
                    padding: CryptoJS.pad.ZeroPadding
                })
            result[ele] = encrypted.toString()
        })
    }
    return result
}


export default secretUtils
