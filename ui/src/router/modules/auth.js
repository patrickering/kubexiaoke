import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

const pre = 'auth-';

export default {
    path: '/auth',
    name: 'auth',
    redirect: {
        name: `${pre}menu`
    },
    meta,
    component: BasicLayout,
    children: [
        {
            path: 'user',
            name: `${pre}user`,
            meta: {
                ...meta,
                title: '用户管理',
                closable: false
            },
            component: () => import('@/pages/system/user')
        },{
            path: 'menu',
            name: `${pre}menu`,
            meta: {
                ...meta,
                title: '菜单管理',
                closable: false
            },
            component: () => import('@/pages/system/menu')
        },{
            path: 'role',
            name: `${pre}role`,
            meta: {
                ...meta,
                title: '角色管理',
                closable: false
            },
            component: () => import('@/pages/system/role')
        },{
            path: 'tenant',
            name: `${pre}tenant`,
            meta: {
                ...meta,
                title: '租户管理',
                closable: false
            },
            component: () => import('@/pages/system/tenant')
        },
        //code 代码生成专用请保留
    ]
};
