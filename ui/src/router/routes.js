import BasicLayout from '@/layouts/basic-layout';
import IndexLayout from '@/layouts/index-layout';
import dashboard from './modules/dashboard';
import auth from './modules/auth';
import system from './modules/system';
import monitor from './modules/monitor';
import kube from './modules/kube';
//import-code --代码生成专用勿删


/**
 * 在主框架内显示
 */

const frameIn = [
    {
        path: '/',
        redirect: {
            name: 'project'
        },
        component: BasicLayout,
        children: [
            {
                path: 'index',
                name: 'index',
                redirect: {
                    name: 'project'
                }
            },
            {
                path: 'log',
                name: 'log',
                meta: {
                    title: '前端日志',
                    auth: true
                },
                component: () => import('@/pages/system/log')
            },
            // 刷新页面 必须保留
            {
                path: 'refresh',
                name: 'refresh',
                hidden: true,
                component: {
                    beforeRouteEnter (to, from, next) {
                        next(instance => instance.$router.replace(from.fullPath));
                    },
                    render: h => h()
                }
            },
            // 页面重定向 必须保留
            {
                path: 'redirect/:route*',
                name: 'redirect',
                hidden: true,
                component: {
                    beforeRouteEnter (to, from, next) {
                        next(instance => instance.$router.replace(JSON.parse(from.params.route)));
                    },
                    render: h => h()
                }
            }
        ]
    },
    dashboard,
    auth,
    system,
    monitor,
    kube,
    //variable-code --代码生成专用勿删,请保留最后的“,”
];

/**
 * 在主框架之外显示
 */

const frameOut = [
    // 登录
    {
        path: '/login',
        name: 'login',
        meta: {
            title: '$t:page.login.title'
        },
        component: () => import('@/pages/account/login')
    }
];

const frameIndex = [
    {
        path: '/',
        redirect: {
            name: 'project'
        },
        component: IndexLayout,
        children:[
            {
                path: '/project',
                name: 'project',
                meta: {
                    title: '项目',
                    auth: true
                },
                component: () => import('@/pages/kube/project')
            },{
                path: 'colony-monitor',
                name: 'colony-monitor',
                meta: {
                    title: '监控',
                    auth: true
                },
                component: () => import('@/pages/kube/monitor')
            },{
                path: 'user',
                name: 'user',
                meta: {
                    title: '用户管理',
                    auth: true
                },
                component: () => import('@/pages/system/user')
            },{
                path: 'role',
                name: 'role',
                meta: {
                    title: '角色管理',
                    auth: true
                },
                component: () => import('@/pages/system/role')
            },{
                path: 'menu',
                name: 'menu',
                meta: {
                    title: '菜单管理',
                    auth: true
                },
                component: () => import('@/pages/system/menu')
            },{
                path: 'template',
                name: 'template',
                meta: {
                    title: '模板管理',
                    auth: true
                },
                component: () => import('@/pages/kube/template')
            },{
                path: 'push',
                name: 'push',
                meta: {
                    title: '推送设置',
                    auth: true
                },
                component: () => import('@/pages/kube/push')
            },{
                path: 'setting',
                name: 'setting',
                meta: {
                    title: '推送设置',
                    auth: true
                },
                component: () => import('@/pages/setting/account')
            },{
                path: 'domain',
                name: 'domain',
                meta: {
                    title: '域名管理',
                    auth: true
                },
                component: () => import('@/pages/kube/domain')
            }
        ]
    },

];

/**
 * 错误页面
 */

const errorPage = [
    {
        path: '/403',
        name: '403',
        meta: {
            title: '403'
        },
        component: () => import('@/pages/system/error/403')
    },
    {
        path: '/500',
        name: '500',
        meta: {
            title: '500'
        },
        component: () => import('@/pages/system/error/500')
    },
    {
        path: '*',
        name: '404',
        meta: {
            title: '404'
        },
        component: () => import('@/pages/system/error/404')
    }
];

// 导出需要显示菜单的
export const frameInRoutes = frameIn;

// 重新组织后导出
export default [
    ...frameIn,
    ...frameOut,
    ...frameIndex,
    ...errorPage
];
