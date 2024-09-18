import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

const pre = 'monitor-';

export default {
    path: '/monitor',
    name: 'monitor',
    redirect: {
        name: `${pre}monitor`
    },
    meta,
    component: BasicLayout,
    children: [
        {
            path: 'redis',
            name: `${pre}redis`,
            meta: {
                ...meta,
                title: '缓存监控',
                closable: false
            },
            component: () => import('@/pages/monitor/redis')
        },
        //code 代码生成专用请保留
    ]
};
