/**
 * 应用相关工具
 * xiaoke
 * 2018年8月6日 19:11:00
 */
import { Notice,Spin } from 'view-design';

let appUtils = {}

/**
 * 打开加载
 * @param that
 * @param msg
 * @returns {*}
 */
appUtils.showLoading = function (msg) {
    // eslint-disable-next-line no-undef
    Spin.show();
    //var loading = layer.msg(msg == null ? '正在加载，请稍后...' : msg, { icon: 16, time: false, shade: 0.1 })
    //return loading
}

/**
 * 关闭加载
 * @param loading
 */
appUtils.hideLoading = function (loading) {
    Spin.hide();
   /* if (loading != null) {
        // eslint-disable-next-line no-undef
        layer.close(loading)
    } else {
        // eslint-disable-next-line no-undef
        layer.closeAll()
    }*/
}

/**
 * 显示成功
 * @param that
 * @param msg
 */
appUtils.showSuccess = function (msg, title) {
    Notice.success({
        title: title == null ? '成功' : title,
        desc: msg
    });
}

appUtils.showError = function (msg, title) {
    Notice.error({
        title: title == null ? '失败' : title,
        desc: msg
    });
}

appUtils.showWarning = function (msg, title) {
    Notice.warning({
        title: title == null ? '警告' : title,
        desc: msg
    });
}

appUtils.res = function (res, success, error) {
    if (res.state) {
        if (success != null) {
            success()
        }
    } else {
        appUtils.showError(res.msg)
        if (error != null) {
            error()
        }
    }
}

export default appUtils
