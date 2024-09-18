/**
 * 验证工具
 * xiaoke
 * 2018年6月11日 16:58:34
 */

let checkUtils = {}

/**
 * 判断是否为Null
 * 是：true
 * 否：false
 */

checkUtils.isNull = function (obj) {
    if (Object.prototype.toString.call(obj) === '[object Null]' || obj === null) {
        return true
    } else {
        return false
    }
}

/**
 * 判断是否为对象
 * 是：true
 * 否：false
 */
checkUtils.isObject = function (obj) {
    if (Object.prototype.toString.call(obj) === '[object Object]') {
        return true
    } else {
        return false
    }
}

/**
 * 判断是否为数组
 * 是：true
 * 否：false
 */
checkUtils.isArray = function (arr) {
    if (arr instanceof Array) {
        return true
    } else {
        return false
    }
}

/**
 * 判断是否为boolean
 * 是：true
 * 否：false
 */
checkUtils.isBoolean = function (obj) {
    if (Object.prototype.toString.call(obj) === '[object Boolean]') {
        return true
    } else {
        return false
    }
}

/**
 * 判断是否为方法
 * 是：true
 * 否：false
 */
checkUtils.isFunction = function (func) {
    if (Object.prototype.toString.call(func) === '[object Function]') {
        return true
    } else {
        return false
    }
}

/**
 * 判断是否为未定义
 * 是：true
 * 否：false
 */
checkUtils.isUndefined = function (obj) {
    if (Object.prototype.toString.call(obj) === '[object Undefined]' || obj === undefined) {
        return true
    } else {
        return false
    }
}

/**
 * 判断数组中是否包含val
 * @param arr
 * @param val
 * @returns {boolean}
 * @constructor
 */

checkUtils.IsInArray = function (arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] === val) {
            return true
        }
    }
    return false
}

// 检测手机号
checkUtils.checkMobile = (m) => {
    if (!m || !/^1[3-9][0-9]{9}$/.test(m)) {
        return false
    } else {
        return true
    }
}

// 校验身份证号格式
checkUtils.checkIDCard = (idcode) => {
    if (!idcode || idcode.length !== 18) return false
    // 加权因子
    var weightFactor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
    // 校验码
    var checkCode = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']
    var code = idcode + ''
    var last = idcode[17] // 最后一个
    var seventeen = code.substring(0, 17)
    // ISO 7064:1983.MOD 11-2
    // 判断最后一位校验码是否正确
    var arr = seventeen.split('')
    var len = arr.length
    var num = 0
    for (var i = 0; i < len; i++) {
        num = num + arr[i] * weightFactor[i]
    }
    // 获取余数
    var resisue = num % 11
    var lastNo = checkCode[resisue]
    var idcardPatter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/
    // 判断格式是否正确
    var format = idcardPatter.test(idcode)
    // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
    return last === lastNo && format
}

// 检测ip
checkUtils.checkIp = (ip) => {
    var re = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    return re.test(ip)
}

export default checkUtils
