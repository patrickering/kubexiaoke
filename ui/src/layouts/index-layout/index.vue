<template>
    <Layout class="i-layout" style="background: #FFFFFF !important;" v-resize="handleResize">
        <Layout class="i-layout-inside" style="background: #FFFFFF !important;">
            <transition name="fade-quick">
                <Header class="i-layout-header" id="head_top" style="height: 70px;line-height: 70px">
                    <div style="width:1200px; margin:0 auto; display:flex;justify-content: space-between;height:62px;">
                        <div style="display:flex;align-items:center;cursor: pointer">
                            <img @click="$router.push('/project')" src="@/assets/images/logo_head.png" alt=""style="height:54px;object-fit: cover;">
                        </div>
                        <!-- style="height: 65px;" -->
                        <Menu mode="horizontal" theme="light" :active-name="$route.path">
                            <MenuItem  name="/project" to="/project" class="headtilte" v-permission="{'value':'project'}">
                                <div style="display: flex;align-items: center; ">
                                    <img v-if="$route.path=='/project'" src="@/assets/icon/menu-project-select.png" width="24" height="24" alt="">
                                    <img v-else src="@/assets/icon/menu-project.png" width="24" height="24" alt="">
                                    <span style="margin-left: 10px">项目</span>
                                </div>
                            </MenuItem>
                            <MenuItem name="/colony-monitor" to="/colony-monitor" class="headtilte" v-permission="{'value':'monitor'}">
                                <div style="display: flex;align-items: center; ">
                                    <img src="@/assets/icon/menu-monitor.png" width="24" height="24" alt="">
                                    <span style="margin-left: 10px">监控</span>
                                </div>
                            </MenuItem>
                            <Submenu name="/system/user" class="headtilte index-sub-menu">
                                <template slot="title">
                                    <div style="display: inline-block">
                                        <div style="display: flex;align-items: center; ">
                                            <img src="@/assets/icon/menu-auth.png" width="24" height="24" alt="">
                                            <span style="margin-left: 10px">权限管理</span>
                                        </div>
                                    </div>
                                </template>
                                <div style="text-align: center;" :style="{width: width+'px'}">
                                    <div style="width: 400px;margin: 0 auto">
                                        <Row>
                                            <Col :span="12" style="border-right: 1px solid #F2F2F2" v-permission="{'value':'user'}">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/user'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/user')">用户管理</div>
                                            </Col>
                                            <Col :span="12" v-permission="{'value':'role'}">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/role'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/role')">角色管理</div>
                                            </Col>
                                            <Col :span="12" style="border-right: 1px solid #F2F2F2" v-permission="{'value':'menu'}">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/menu'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/menu')">菜单管理</div>
                                            </Col>
                                            <Col :span="12" v-permission="{'value':'push'}">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/push'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/push')">推送设置</div>
                                            </Col>
                                            <Col :span="12" style="border-right: 1px solid #F2F2F2" v-permission="{'value':'template'}">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/template'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/template')">模板管理</div>
                                            </Col>
                                            <Col :span="12" style="border-right: 1px solid #F2F2F2" v-permission="{'value':'kube_domain_view'}">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/domain'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/domain')">域名管理</div>
                                            </Col>
                                            <!--<Col :span="12">
                                                <div class="index-sub-menu-title" :style="{color:$route.path=='/codebase'?'#1F8ECF':'#5D5D5D'}" @click="$router.push('/codebase')">仓库管理</div>
                                            </Col>-->
                                        </Row>
                                    </div>

                                    <!--<MenuItem class="ul_li" name="/user" to="/user">用户管理</MenuItem>
                                    <MenuItem class="ul_li" name="/role" to="/role">角色管理</MenuItem>
                                    <MenuItem class="ul_li" name="/menu" to="/menu">菜单管理</MenuItem>
                                    <MenuItem class="ul_li" name="/system/push" to="/system/push">推送设置</MenuItem>
                                    <MenuItem class="ul_li" name="/template" to="/template">模板管理</MenuItem>
                                    <MenuItem class="ul_li" name="/system/codebase" to="/system/codebase">仓库管理</MenuItem>-->
                                </div>
                            </Submenu>
                        </Menu>

                        <div class="i-layout-header-right">
                            <div style="margin-right: 10px">
                                <!--<Avatar style="margin-right: 5px;margin-top: -3px" src="/images/defaultAvatar.png" />
                                <el-button type="text" @click="loginOut">退出登录</el-button>-->
                                <i-header-user />
                            </div>
                        </div>
                    </div>
                </Header>
            </transition>
            <Content class="i-layout-content" :class="contentClasses">
                <keep-alive :include="keepAlive">
                    <router-view v-if="loadRouter" />
                </keep-alive>
            </Content>
        </Layout>
    </Layout>
</template>
<script>
    import iHeaderUser from "./header-user";

    import { mapActions, mapGetters, mapMutations, mapState } from "vuex";
    import Setting from "@/setting";

    import { requestAnimation } from "@/libs/util";

    export default {
        name: "HomeLayout",
        components: {
            iHeaderUser,
        },
        data() {
            return {
                ticking: false,
                headerVisible: true,
                oldScrollTop: 0,
                isDelayHideSider: false, // hack，当从隐藏侧边栏的 header 切换到正常 header 时，防止 Logo 抖动
                loadRouter: true,
                // 计时器
                timer: "",
                refreshTime: null,
                namespaceInfo: null, //命名空间
                width:0,
                height:0
            };
        },
        computed: {
            ...mapState("admin/page", ["keepAlive"]),
            ...mapState("admin/user", ["info"]),
            contentClasses() {
                return {
                    "i-layout-content-fix-with-header": this.headerFix,
                    "i-layout-content-with-tabs": this.tabs,
                    "i-layout-content-with-tabs-fix": this.tabs && this.tabsFix,
                };
            },
        },
        watch: {
            $route(to, from) {
                if (to.path === from.path) {
                    // 相同路由，不同参数，跳转时，重载页面
                    if (Setting.sameRouteForceUpdate) {
                        this.handleReload();
                    }
                }
            },
        },
        methods: {
            ...mapMutations("admin/layout", ["updateMenuCollapse"]),
            ...mapActions("admin/account", ["logout"]),
            loginOut() {
                this.logout({
                    confirm: true,
                    vm: this,
                });
            },
            handleScroll() {
                if (!this.headerHide) return;

                const scrollTop =
                    document.body.scrollTop + document.documentElement.scrollTop;

                if (!this.ticking) {
                    this.ticking = true;
                    requestAnimation(() => {
                        if (this.oldScrollTop > scrollTop) {
                            this.headerVisible = true;
                        } else if (scrollTop > 300 && this.headerVisible) {
                            this.headerVisible = false;
                        } else if (scrollTop < 300 && !this.headerVisible) {
                            this.headerVisible = true;
                        }
                        this.oldScrollTop = scrollTop;
                        this.ticking = false;
                    });
                }
            },
            handleHeaderWidthChange() {
                const $breadcrumb = this.$refs.breadcrumb;
                if ($breadcrumb) {
                    $breadcrumb.handleGetWidth();
                    $breadcrumb.handleCheckWidth();
                }
                const $menuHead = this.$refs.menuHead;
                if ($menuHead) {
                    $menuHead.handleGetMenuHeight();
                }
            },
            handleReload() {
                this.loadRouter = false;
                this.$nextTick(() => {
                    this.loadRouter = true;
                });
            },
            handleResize(){
                this.height = document.body.clientHeight
                this.width = document.body.clientWidth
            },
        },
        mounted() {
            document.addEventListener("scroll", this.handleScroll, { passive: true });
            this.height = document.body.clientHeight
            this.width = document.body.clientWidth
        },
        beforeDestroy() {
            document.removeEventListener("scroll", this.handleScroll);
        },
        created() {
            if (this.isTablet) this.updateMenuCollapse(true);
        },
        destroyed() {},
    };
</script>
<style>
    #head_top .ivu-menu-horizontal.ivu-menu-light:after{
        height: 0 !important;
    }
    #head_top .ivu-menu-item-active{
        border-bottom:2px solid  #1F8ECF;
        color: #202020 !important;
        font-weight: 600 !important;
        font-family: PingFang SC !important;
        font-size: 16px !important;
    }
    #head_top .headtilte{
        font-weight: 600 !important;
        font-family: PingFang SC !important;
        font-size: 16px !important;
        color: #929292;
        padding: 0 !important;
        padding-top: 2px;
        margin-right: 60px;
    }
    #head_top .ivu-menu-submenu:hover,
    #head_top .ivu-menu-horizontal{
        height: auto;
    }
    #head_top .ul_li{
        text-align: center;
        padding: 0;
        height: 35px;
        line-height: 35px;
    }
    .index-sub-menu .ivu-select-dropdown{
        position: fixed !important;
        top: 60px !important;
        left: 0px !important;
        /*box-shadow: none !important;*/
        box-shadow: 0px 6px 3px 0px rgb(0 0 0 / 20%);
    }

    .index-sub-menu .index-sub-menu-title:hover {
        text-decoration: underline;
    }
</style>
