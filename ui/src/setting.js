/**
 * 房产经纪人 业务配置
 * */

const env = process.env.NODE_ENV;

const Setting = {
    /**
     * 基础配置
     * */
    // 网页标题的后缀
    titleSuffix:'小可容器云',
    // 路由模式，可选值为 history 或 hash
    routerMode: 'hash',
    // 应用的基路径
    routerBase: '/',
    // 页面切换时，是否显示模拟的进度条
    showProgressBar: true,
    // 接口请求地址
    apiBaseURL: env === 'development' ? '/xiaoke-kube' : '/xiaoke-kube',
    // 接口请求返回错误时，弹窗的持续时间，单位：秒
    modalDuration: 3,
    // 接口请求返回错误时，弹窗的类型，可选值为 Message 或 Notice
    errorModalType: 'Message',
    // Cookies 默认保存时间，单位：天
    cookiesExpires: 1,
    /**
     * 多语言配置
     * */
    i18n: {
        // 默认语言
        default: 'zh-CN',
        // 是否根据用户电脑配置自动设置语言（仅第一次有效）
        auto: false
    },
    /**
     * 布局配置
     * */
    // 侧边菜单宽度，单位 px，不可动态修改，需与 setting.less 的 @menuSideWidth 保持一致
    menuSideWidth: 240,
    layout: {
        // 侧边菜单栏是否默认折起
        menuCollapse: false,
        // 再次点击当前侧边菜单时，是否重载当前页面
        menuSiderReload: true,
        // 再次点击当前顶部菜单时，是否重载当前页面
        menuHeaderReload: false,
        // 侧边菜单折起时，是否在子菜单前显示父级菜单名称
        showCollapseMenuTitle: false,
        // 是否显示重载按钮
        showReload: true,
        // 是否显示通知
        showNotice: false,
        // 是否显示全屏
        showFullscreen: true,
        // 是否显示多语言
        showI18n: false,
        // 退出登录时，是否二次确认
        logoutConfirm: true
    },
    /**
     * 多页 Tabs
     * */
    page: {
        // 默认打开的页签
        opened: []
    },
    /**
     * 功能配置
     * */
    // 相同路由，不同参数间进行切换，是否强力更新
    sameRouteForceUpdate: false,
    // 是否使用动态侧边菜单
    dynamicSiderMenu: false,
    //缓存key
    key:{
        token:'TOKEN',
        userInfo:'USERINFO',
        menu:'MENU',
        menuActive:'menuActive',
        project: 'PROJECT',
        namespace: 'NAMESPACE',
        namespaceInfo: 'NAMESPACEINFO',
    },
    configUploadUrl:'/xiaoke-kube/kube/config/upload',
    //替换路由
    replaceRoute:[
        {from:'/kube/app/details/Deployment',to:'/kube/app/Deployment'},
        {from:'/kube/app/edit/Deployment',to:'/kube/app/Deployment'},
        {from:'/kube/build/edit',to:'/kube/build'},
        {from:'/kube/build/detail',to:'/kube/build'},
        {from:'/kube/build/log',to:'/kube/build'},
    ],
    passwordSalt:'xiaokekube123456',
    requestSalt:'xiaoke_kube@#',
};

export default Setting;
