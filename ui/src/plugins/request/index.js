import store from '@/store';
import axios from 'axios';
import util from '@/libs/util';
import Setting from '@/setting';
import dataUtils from '@/utils/dataUtils'

import { Message, Notice } from 'view-design';
import appUtils from '@/utils/appUtils';
import secretUtils from '@/utils/secretUtils';

let showOutLogin= true

// 创建一个错误
function errorCreate (msg) {
    const err = new Error(msg);
    errorLog(err);
    throw err;
}

// 记录和显示错误
function errorLog (err) {
    // 添加到日志
    store.dispatch('admin/log/push', {
        message: '数据请求异常',
        type: 'error',
        meta: {
            error: err
        }
    });
    // 打印到控制台
    if (process.env.NODE_ENV === 'development') {
        util.log.error('>>>>>> Error >>>>>>');
        console.log(err);
    }
    // 显示提示，可配置使用的 $Message 还是 $Notice 组件来显示
    if (Setting.errorModalType === 'Message') {
        appUtils.showError(err.message)
    } else if (Setting.errorModalType === 'Notice') {
        appUtils.showError(err.message)
    }
}

// 创建一个 axios 实例
const service = axios.create({
    baseURL: Setting.apiBaseURL,
    timeout: 5000000 // 请求超时时间
});

// 请求拦截器
service.interceptors.request.use(
    config => {
        // 在请求发送之前做一些处理
        const token = dataUtils.getData(Setting.key.token)
        // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
        config.headers['token'] = token;
        config.headers = secretUtils.createSign(config.headers)


        //添加namespace
        let namespace = dataUtils.getData(Setting.key.namespace)
        if (namespace != null) {
            config.headers['namespace'] = namespace;
        }else {
            config.headers['namespace'] = "";
        }
        return config;
    },
    error => {
        // 发送失败
        console.log(error);
        Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        // dataAxios 是 axios 返回数据中的 data
        const dataAxios = response.data;
        // 这个状态码是和后端约定的
        const { code } = dataAxios;
        // 根据 code 进行判断
        if (code === undefined) {
            // 如果没有 code 代表这不是项目后端开发的接口
            return dataAxios;
        } else {
            // 有 code 代表这是一个后端接口 可以进行进一步的判断
            switch (code) {
                case 0:
                    // [ 示例 ] code === 0 代表没有错误
                    return dataAxios;
                case 1:
                    // [ 示例 ] code === 0 代表没有错误\
                    errorCreate(dataAxios.msg);
                    break;
                case '1001':
                    // [ 示例 ] 其它和后台约定的 code
                    dataUtils.removeAllData();
                    location.reload()
                    break;
                default:
                    // 不是正确的 code
                    errorCreate(`${dataAxios.msg}: ${response.config.url}`);
                    break;
            }
        }
    },
    error => {
        if (error && error.response) {
            switch (error.response.status) {
                case 400:
                    error.message = '请求错误';
                    break;
                case 401:
                    error.message = '未授权，请登录';
                    if(showOutLogin){
                        appUtils.showError(error.message)
                        showOutLogin = false
                        dataUtils.removeAllData();
                        setTimeout(() => {
                            showOutLogin = true
                            location.reload()
                        }, 2000)
                    }
                    return ;
                case 403:
                    error.message = '拒绝访问';
                    break;
                case 404:
                    error.message = `请求地址出错: ${error.response.config.url}`;
                    break;
                case 408:
                    error.message = '请求超时';
                    break;
                case 426:
                    error.message = error.response.data.msg;
                    break;
                case 428:
                    error.message = error.response.data.msg;
                    break;
                case 500:
                    error.message = error.response.data.msg;
                    break;
                case 501:
                    error.message = '服务未实现';
                    break;
                case 502:
                    error.message = '网关错误';
                    break;
                case 503:
                    error.message = '服务不可用';
                    break;
                case 504:
                    error.message = '网关超时';
                    break;
                case 505:
                    error.message = 'HTTP版本不受支持';
                    break;
                case 506:
                    error.message = '重复访问';
                    break;
                default:
                    break;
            }
        }
        errorLog(error);
        return Promise.reject(error);
    }
);

export default service;
