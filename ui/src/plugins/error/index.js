import util from '@/libs/util';

export default {
    install (Vue, options) {
        Vue.config.errorHandler = function (error, instance, info) {
            Vue.nextTick(() => {
                // 只在开发模式下打印 log
                if (process.env.NODE_ENV === 'development') {
                    util.log.capsule('XiaoKe Kube', 'ErrorHandler', 'error');
                    util.log.error('>>>>>> 错误信息 >>>>>>');
                    console.log(info);
                    util.log.error('>>>>>> Vue 实例 >>>>>>');
                    console.log(instance);
                    util.log.error('>>>>>> Error >>>>>>');
                    console.log(error)
                }
            })
        }
    }
}
