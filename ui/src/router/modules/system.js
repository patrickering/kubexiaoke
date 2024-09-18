import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

const pre = 'system-';

export default {
    path: '/system',
    name: 'system',
    redirect: {
        name: `${pre}system`
    },
    meta,
    component: BasicLayout,
    children: [
        {
            path: 'log',
            name: `${pre}log`,
            meta: {
                ...meta,
                title: '日志管理',
                closable: false
            },
            component: () => import('@/pages/system/log/')
        },{
            path: 'dict',
            name: `${pre}dict`,
            meta: {
                ...meta,
                title: '字典管理',
                closable: false
            },
            component: () => import('@/pages/system/dict/dict')
        },{
            path: 'param',
            name: `${pre}param`,
            meta: {
                ...meta,
                title: '参数管理',
                closable: false
            },
            component: () => import('@/pages/system/param')
        },{
            path: 'token',
            name: `${pre}token`,
            meta: {
                ...meta,
                title: '令牌管理',
                closable: false
            },
            component: () => import('@/pages/system/token')
        },{
            path: 'error',
            name: `${pre}error`,
            meta: {
                ...meta,
                title: '异常管理',
                closable: false
            },
            component: () => import('@/pages/system/error')
        },{
            path: 'login-log',
            name: `${pre}login-log`,
            meta: {
                ...meta,
                title: '登录日志',
                closable: false
            },
            component: () => import('@/pages/system/login-log')
        },{
            path: 'public-param/:paramKey',
            name: `${pre}public-param`,
            meta: {
                ...meta,
                title: '系统参数',
                closable: false
            },
            props: true,
            component: () => import('@/pages/system/public-param')
        },
    ]
};
