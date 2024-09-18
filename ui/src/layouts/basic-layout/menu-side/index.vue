<template>
    <div>
        <div class="i-layout-sider-logo" :class="{ 'i-layout-sider-logo-dark': siderTheme === 'dark' }">
            <transition name="fade-quick">
                <i-link to="/dashboard/console" v-show="!hideLogo">
                    <img src="@/assets/images/logo-small.png" v-if="menuCollapse">
                    <img src="@/assets/images/logo_head.png" v-else-if="siderTheme === 'light'">
                    <img src="@/assets/images/logo_head.png" v-else>
                </i-link>
            </transition>
        </div>
        <Menu
                ref="menu"
                class="left-menu i-layout-menu-side i-scrollbar-hide"
                :class="{ 'i-layout-menu-side-collapse': menuCollapse }"
                :theme="siderTheme"
                :accordion="true"
                :active-name="activePath"
                :open-names="openNames"
                width="auto">
            <template v-if="!menuCollapse" v-for="(item, index) in filterSider">
                <i-menu-side-item v-if="item.children === undefined || !item.children.length" :menu="item" :key="index" />
                <i-menu-side-submenu v-else :menu="item" :key="index" />
            </template>
            <template v-else>
                <Tooltip :content="tTitle(item.title)" placement="right" v-if="item.children === undefined || !item.children.length" :key="index">
                    <i-menu-side-item :menu="item" hide-title />
                </Tooltip>
                <i-menu-side-collapse v-else :menu="item" :key="index" top-level />
            </template>
        </Menu>
    </div>
</template>
<script>
    import iMenuSideItem from './menu-item';
    import iMenuSideSubmenu from './submenu';
    import iMenuSideCollapse from './menu-collapse';
    import tTitle from '../mixins/translate-title';

    import { mapState, mapGetters } from 'vuex';

    // 元素是否在可视区域
    function isElementInViewport (el) {
        let rect = el.getBoundingClientRect();
        return (rect.top >= 0 && rect.left >= 0 && rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && rect.right <= (window.innerWidth || document.documentElement.clientWidth));
    }

    export default {
        name: 'iMenuSide',
        mixins: [ tTitle ],
        components: { iMenuSideItem, iMenuSideSubmenu, iMenuSideCollapse },
        props: {
            hideLogo: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            ...mapState('admin/layout', [
                'siderTheme',
                'menuCollapse'
            ]),
            ...mapState('admin/menu', [
                'activePath',
                'openNames'
            ]),
            ...mapGetters('admin/menu', [
                'filterSider'
            ])
        },
        watch: {
            '$route': {
                handler () {
                    this.handleUpdateMenuState();
                },
                immediate: true
            },
            // 在展开/收起侧边菜单栏时，更新一次 menu 的状态
            menuCollapse () {
                this.handleUpdateMenuState();
            }
        },
        methods: {
            handleUpdateMenuState () {
                this.$nextTick(() => {
                    if (this.$refs.menu) {
                        this.$refs.menu.updateActiveName();
                        this.$refs.menu.updateOpened()
                        // 聚焦当前项
                        this.$nextTick(() => {
                            const $activeMenu = document.getElementsByClassName('ivu-menu-item ivu-menu-item-active ivu-menu-item-selected');
                            if ($activeMenu && $activeMenu.length && !isElementInViewport($activeMenu[0])) {
                                const activeMenuTop = $activeMenu[0].getBoundingClientRect().top;
                                const $menu = this.$refs.menu.$el;
                                setTimeout(() => {
                                    this.$ScrollTop($menu, {
                                        to: activeMenuTop,
                                        time: 0
                                    });
                                }, 300);
                            }
                        });
                    }
                });
            }
        }
    }
</script>
