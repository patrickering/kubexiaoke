// Vue
import Vue from 'vue';
import App from './App';

// 配置
import Setting from './setting';

// 混合
import mixinApp from '@/mixins/app';

// 插件
import plugins from '@/plugins';

// store
import store from '@/store/index';

// iView
import ViewUI from 'view-design';
//工具类
import secretUtils from '@/utils/secretUtils'
import dataUtils from '@/utils/dataUtils'

// 菜单和路由
import router from './router';
import menuHeader from '@/menu/header';
import { frameInRoutes } from '@/router/routes';

// 多语言
import i18n from '@/i18n';

// 方法
import { getHeaderName, getMenuSider, getSiderSubmenu } from '@/libs/system';

// 内置组件
import iLink from '@/components/link';
import './styles/index.less';

import ElementUI from 'element-ui'
import './styles/element-variables.scss'
import Avue from '@smallwei/avue';
import '@smallwei/avue/lib/index.css';

import vRegion from 'v-region'
import VueLazyLoad from 'vue-lazyload'


import selectTree from '@/components/select-tree/select-tree.vue'
import selectUser from '@/components/select-user/select-user.vue'
import selectTemplate from '@/components/select-template/index.vue'
import codeEditor from '@/components/code-editor/index.vue'

import VideoPlayer from 'vue-video-player'

const EventBus = new Vue();
Vue.prototype.$EventBus = EventBus;

if (window) window.$t = (key, value) => i18n.t(key, value);

Vue.prototype.$Date = require('dayjs')

Vue.use(plugins);
Vue.component('select-tree', selectTree);
Vue.component("select-user", selectUser);
Vue.component("select-template", selectTemplate);
Vue.component("code-editor", codeEditor);

Vue.use(ViewUI, {
    transfer:true,
    i18n: (key, value) => i18n.t(key, value)
});
Vue.use(ElementUI)
Vue.use(Avue);
Vue.use(vRegion, {});
Vue.component('i-link', iLink);
Vue.use(VideoPlayer)

/**
 * 绑定错误图片
 */
Vue.directive('error', {
    // 当被绑定的元素插入到 DOM 中时……
    inserted: function (el, binding) {
        if (el.getAttribute('src') == null) {
            el.setAttribute("src", "this.src='" + defaultImg + "'")
        }
        el.setAttribute("onerror", "this.src='" + defaultImg + "'")
    }
})



/**
 * 图片懒加载配置
 */
Vue.use(VueLazyLoad, {
    error: require('@/assets/images/default.png'),
    loading: require('@/assets/images/default.png')
})

/**
 * 注册权限验证
 */
Vue.directive('permission', {
    // 当被绑定的元素插入到 DOM 中时……
    inserted: function (el, binding) {
        if (binding.value == null) {
            return
        }
        secretUtils.permission(el, binding.value.value)
    }
})
import importDirective from '@/plugins/resize/directive' //文件路径
importDirective(Vue)

new Vue({
    mixins: [mixinApp],
    router,
    store,
    i18n,
    render: h => h(App),
    created () {
        // 处理路由 得到每一级的路由设置
        this.$store.commit('admin/page/init', frameInRoutes);
        // 设置顶栏菜单
        this.$store.commit('admin/menu/setHeader', menuHeader);
        // 加载用户登录的数据
        this.$store.dispatch('admin/account/load');
        // 初始化全屏监听
        this.$store.dispatch('admin/layout/listenFullscreen')
    },
    watch: {
        // 监听路由 控制侧边栏显示 标记当前顶栏菜单（如需要）
        '$route'(to, from) {
            const path = to.path;
            if (!Setting.dynamicSiderMenu) {
                let menu = dataUtils.getData(Setting.key.menu)
                if (menu != null) {
                    const headerName = getHeaderName(path, menu);
                    this.$store.commit('admin/menu/setHeaderName', headerName);
                    const filterMenuSider = getMenuSider(menu, headerName);
                    this.$store.commit('admin/menu/setSider', filterMenuSider);
                    this.$store.commit('admin/menu/setActivePath', path);

                    let openNames = ''
                    var replacePath = null
                    if(Setting.replaceRoute!=null){
                        for (let i = 0;i<Setting.replaceRoute.length;i++){
                            if (path.indexOf(Setting.replaceRoute[i].from) >= 0) {
                                replacePath = Setting.replaceRoute[i].to
                                break;
                            }
                        }
                    }
                    if (replacePath!=null) {
                        this.$store.commit('admin/menu/setActivePath', replacePath);
                        openNames = getSiderSubmenu(replacePath, menu);
                    } else {
                        this.$store.commit('admin/menu/setActivePath', path);
                        openNames = getSiderSubmenu(path, menu);
                    }
                    this.$store.commit('admin/menu/setOpenNames', openNames);
                }
            }
            this.appRouteChange(to, from);
        }
    }
}).$mount('#app');
