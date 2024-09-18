<template>
    <Layout class="i-layout">
        <Sider v-if="!isMobile && !hideSider" class="i-layout-sider" :class="siderClasses" :width="menuSideWidth">
            <i-menu-side :hide-logo="isHeaderStick &&  showHeader" />
        </Sider>
        <Layout class="i-layout-inside" :class="insideClasses">
            <transition name="fade-quick">
                <Header class="i-layout-header" style="height: 70px;line-height: 70px" :class="headerClasses" :style="headerStyle" v-show="showHeader">
                    <i-header-collapse v-if="(isMobile ) && !hideSider" @on-toggle-drawer="handleToggleDrawer" />
                    <i-header-reload v-if="!isMobile && showReload" @on-reload="handleReload" />
                    <i-header-namespace v-if="!isMobile" class="i-layout-header-left"></i-header-namespace>
                    <div class="i-layout-header-right">
                        <i-header-fullscreen v-if="isDesktop && showFullscreen" />
                        <i-header-notice v-if="showNotice" />
                        <i-header-user />
                    </div>

                </Header>
            </transition>
            <Content class="i-layout-content" :class="contentClasses">
                <div class="i-layout-content-main">
                    <keep-alive :include="keepAlive">
                        <router-view v-if="loadRouter" />
                    </keep-alive>
                </div>
            </Content>
            <i-copyright />
        </Layout>
        <div v-if="isMobile && !hideSider">
            <Drawer v-model="showDrawer" placement="left" :closable="false" :class-name="drawerClasses">
                <i-menu-side />
            </Drawer>
        </div>
        <global-web-socket uri="/xiaoke-kube/ws"/>
    </Layout>
</template>
<script>
    import iMenuSide from './menu-side';
    import iHeaderLogo from './header-logo';
    import iHeaderCollapse from './header-collapse';
    import iHeaderReload from './header-reload';
    import iHeaderFullscreen from './header-fullscreen';
    import iHeaderNotice from './header-notice';
    import iHeaderUser from './header-user';
    import iHeaderNamespace from './header-namespace';
    import iCopyright from '@/components/copyright';

    import { mapState, mapGetters, mapMutations } from 'vuex';
    import Setting from '@/setting';

    import { requestAnimation } from '@/libs/util';
    import GlobalWebSocket from '@/components/WebSocket/GlobalWebSocket'
    export default {
        name: 'BasicLayout',
        components: { iMenuSide, iCopyright, iHeaderLogo, iHeaderCollapse, iHeaderReload, iHeaderUser, iHeaderFullscreen,iHeaderNotice,iHeaderNamespace, GlobalWebSocket },
        data () {
            return {
                showDrawer: false,
                ticking: false,
                headerVisible: true,
                oldScrollTop: 0,
                isDelayHideSider: false, // hack，当从隐藏侧边栏的 header 切换到正常 header 时，防止 Logo 抖动
                loadRouter: true
            }
        },
        computed: {
            ...mapState('admin/layout', [
                'isMobile',
                'isTablet',
                'isDesktop',
                'menuCollapse',
                'showNotice',
                'showFullscreen',
                'showReload'
            ]),
            ...mapState('admin/page', [
                'keepAlive'
            ]),
            ...mapGetters('admin/menu', [
                'hideSider'
            ]),
            // 这时，即使没有开启 headerStick，仍然按开启处理
            isHeaderStick () {
                return false;
            },
            showHeader () {
                let visible = true;
                if (!this.headerVisible) visible = false;
                return visible;
            },
            headerClasses () {
                return [
                    `i-layout-header-color-light`,
                    {
                        'i-layout-header-fix': true,
                        'i-layout-header-fix-collapse': this.menuCollapse,
                        'i-layout-header-mobile': this.isMobile,
                        'i-layout-header-stick': this.isHeaderStick && !this.isMobile,
                        'i-layout-header-with-menu': false,
                        'i-layout-header-with-hide-sider': this.hideSider || this.isDelayHideSider
                    }
                ];
            },
            headerStyle () {
                const menuWidth = this.isHeaderStick ? 0 : this.menuCollapse ? 80 : Setting.menuSideWidth;
                return this.isMobile  ? {} : {
                    width: `calc(100% - ${menuWidth}px)`,
                    'min-width': '1300px'
                };
            },
            siderClasses () {
                return {
                    'i-layout-sider-fix': true,
                };
            },
            contentClasses () {
                return {
                    'i-layout-content-fix-with-header': true,
                    'i-layout-content-with-tabs': this.tabs,
                    'i-layout-content-with-tabs-fix': this.tabs && this.tabsFix
                };
            },
            insideClasses () {
                return {
                    'i-layout-inside-fix-with-sider': true,
                    'i-layout-inside-fix-with-sider-collapse':  this.menuCollapse,
                    'i-layout-inside-with-hide-sider': this.hideSider,
                    'i-layout-inside-mobile': this.isMobile
                };
            },
            drawerClasses () {
                let className = 'i-layout-drawer';
                return className;
            },
            menuSideWidth () {
                return this.menuCollapse ? 80 : Setting.menuSideWidth;
            }
        },
        watch: {
            hideSider () {
                this.isDelayHideSider = true;
                setTimeout(() => {
                    this.isDelayHideSider = false;
                }, 0);
            },
            '$route' (to, from) {
                if (to.name === from.name) {
                    // 相同路由，不同参数，跳转时，重载页面
                    if (Setting.sameRouteForceUpdate) {
                        this.handleReload();
                    }
                }
            }
        },
        methods: {
            ...mapMutations('admin/layout', [
                'updateMenuCollapse'
            ]),
            ...mapMutations('admin/page', [
                'keepAlivePush',
                'keepAliveRemove'
            ]),
            handleToggleDrawer (state) {
                if (typeof state === 'boolean') {
                    this.showDrawer = state;
                } else {
                    this.showDrawer = !this.showDrawer;
                }
            },
            handleReload () {
                // 针对缓存的页面也生效
                const isCurrentPageCache = this.keepAlive.indexOf(this.$route.name) > -1;
                const pageName = this.$route.name;
                if (isCurrentPageCache) {
                    this.keepAliveRemove(pageName);
                }
                this.loadRouter = false;
                this.$nextTick(() => {
                    this.loadRouter = true;
                    if (isCurrentPageCache) {
                        this.keepAlivePush(pageName);
                    }
                });
            }
        },
        mounted () {
        },
        beforeDestroy () {
        },
        created () {
            if (this.isTablet) this.updateMenuCollapse(true);
        }
    }
</script>
