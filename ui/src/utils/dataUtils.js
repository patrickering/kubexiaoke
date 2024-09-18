/**
 * 数据工具
 * xiaoke
 * 2018年6月12日 10:55:18
 */
import secretUtils from '@/utils/secretUtils'
import Setting from '@/setting';

var dataUtils = {}

/**
 * 遍历集合
 * @param obj
 * @param fn
 * @returns {utils}
 */
dataUtils.each = function (obj, fn) {
    let key = this
    let that = this
    if (typeof fn !== 'function') return that
    obj = obj || []
    if (obj.constructor === Object) {
        for (key in obj) {
            if (fn.call(obj[key], key, obj[key])) break
        }
    } else {
        for (key = 0; key < obj.length; key++) {
            if (fn.call(obj[key], key, obj[key])) break
        }
    }
    return that
}

/**
 * 存储数据
 * @param key
 * @param value
 */
dataUtils.setData = function (key, value) {
    sessionStorage.setItem(key, secretUtils.EncryptData(JSON.stringify(value)))
    // sessionStorage.setItem(key, JSON.stringify(value))
}

/**
 * 获取数据
 * @param key
 */
dataUtils.getData = function (key) {
    if (sessionStorage.getItem(key) == null) {
        return null
    }
    return secretUtils.DecryptData(sessionStorage.getItem(key))
    // return JSON.parse(sessionStorage.getItem(key))
}

/**
 * 移除数据
 * @param key
 */
dataUtils.removeData = function (key) {
    sessionStorage.removeItem(key)
}

/**
 * 移除数据
 * @param key
 */
dataUtils.removeAllData = function () {
    sessionStorage.clear()
}
/**
 * 格式化菜单数据
 * @param data
 * @param id
 * @returns {Array}
 */
dataUtils.getALLMenuTree = (data, id) => {
    var tree = []
    var i = 0
    data.forEach(function (item, index) {
        if (item.pid == id) {
            item.children = dataUtils.getALLMenuTree(data, item.id)
            if (item.children.length <= 0) {
                delete item.children
            }
            tree[i] = item
            i++
        }
    })
    return tree
}


/**
 * 删除菜单和子菜单
 * @param data
 * @param id
 * @returns {Array}
 * @name 李昂
 */
dataUtils.delALLMenuTree = (row, listId) => {
    listId.push(row.id)
    if (row.children != null && row.children.length > 0) {
        for (var item in row.children) {
            dataUtils.delALLMenuTree(row.children[item], listId)
        }
    }
    return listId
}

/**
 * 获取图片
 * @param url
 */
dataUtils.getImg = function (bucketName, fileName, thumbnail) {
    if (thumbnail == null) {
        thumbnail = ''
    }
    //admin/sysfile/
    return Setting.file.path + '/' + bucketName + '/' + thumbnail + fileName
}


/**
 * 根据base64 内容 取得 bolb
 *
 * @param dataURI
 * @returns Blob
 */
dataUtils.getBlobBydataURI = function(dataURI,type) {
    var binary = atob(dataURI.split(',')[1]);
    var array = [];
    for(var i = 0; i < binary.length; i++) {
        array.push(binary.charCodeAt(i));
    }
    return new Blob([new Uint8Array(array)], {type:type });
}

export default dataUtils
