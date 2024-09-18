import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

const pre = 'kube-';

export default {
    path: '/kube',
    name: 'kube',
    redirect: {
        name: `${pre}project`
    },
    meta,
    component: BasicLayout,
    children: [
        {
            path: 'project',
            name: `${pre}project`,
            meta: {
                ...meta,
                title: '项目',
                closable: false
            },
            component: () => import('@/pages/kube/project')
        },{
            path: 'namespace',
            name: `${pre}namespace`,
            meta: {
                ...meta,
                title: '命名空间',
                closable: false
            },
            component: () => import('@/pages/kube/namespace')
        },
        {
            path: 'app/:type',
            name: `${pre}app`,
            meta: {
                ...meta,
                title: '应用',
                closable: false
            },
            props: true,
            component: () => import('@/pages/kube/app')
        },{
            path: 'app/edit/:appType/:id',
            name: `${pre}deployment-edit`,
            meta: {
                ...meta,
                cache: false,
                title: '编辑应用'
            },
            props: true,
            component: () => import('@/pages/kube/app/edit')
        },{
            path: 'app/details/:appType/:id',
            name: `${pre}deployment-edit`,
            meta: {
                ...meta,
                cache: false,
                title: '编辑应用'
            },
            props: true,
            component: () => import('@/pages/kube/app/details')
        },
        {
            path: 'config',
            name: `${pre}config`,
            meta: {
                ...meta,
                title: '配置',
                closable: false
            },
            component: () => import('@/pages/kube/config')
        },{
            path: '/config/details/:id',
            name: `${pre}config`,
            meta: {
                ...meta,
                title: '配置详情',
                closable: false
            },
            props: true,
            component: () => import('@/pages/kube/config/details')
        },
        {
            path: 'storage',
            name: `${pre}storage`,
            meta: {
                ...meta,
                title: '存储',
                closable: false
            },
            component: () => import('@/pages/kube/storage')
        },
        {
            path: 'template',
            name: `${pre}template`,
            meta: {
                ...meta,
                title: '模板管理',
                closable: false
            },
            component: () => import('@/pages/kube/template')
        },
        {
            path: 'domain',
            name: `${pre}domain`,
            meta: {
                ...meta,
                title: '域名',
                closable: false
            },
            component: () => import('@/pages/kube/app-domain')
        },
        {
            path: 'error-record',
            name: `${pre}error-record`,
            meta: {
                ...meta,
                title: '异常记录',
                closable: false
            },
            component: () => import('@/pages/kube/error-record')
        },
        {
            path: 'push-user',
            name: `${pre}push-user`,
            meta: {
                ...meta,
                title: '推送接收人',
                closable: false
            },
            component: () => import('@/pages/kube/push-user')
        },
        //code 代码生成专用请保留
    ]
};
