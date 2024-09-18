<!--
   登录日志
   author： xiaoke
   date： 2022-01-23 15:30:21
-->

<template>
    <div >
        <Card ref="card" :bordered="true" :dis-hover="true"  :padding="16">
            <div>
                <login-log-table-search v-model="loginLogQuery" :extend="loginLogExtend" @on-submit="loginLogSearch" @on-reset="loginLogSearch"></login-log-table-search>
            </div>
            <div>
                <login-log-table-list ref="loginLogTable" :extend="loginLogExtend" :query="loginLogQuery" @on-fullscreen="handleFullscreen" @on-events="loginLogHandleEvent"></login-log-table-list>
            </div>
            <div>
                <login-log-table-edit ref="loginLogEdit" :extend="loginLogExtend" @on-refresh="$refs.loginLogTable.getData()"></login-log-table-edit>
            </div>
        </Card>
    </div>
</template>

<script>


    import screenfull from 'screenfull';
    import loginLogTableSearch from './login-log-table-search';
    import loginLogTableList from './login-log-table-list';
    import loginLogTableEdit from './login-log-table-edit';



    import { remote } from '@/api/system/dict'

    export default {
        name: 'model-index',
        components: {
            loginLogTableSearch,
            loginLogTableList,
            loginLogTableEdit
        },
        data() {
            return {
                // 登录日志查询条件
                loginLogQuery: {
                    type: null,
                    ip: null,
                    source: null,
                    size: 10,
                    current: 1
                },

                //登录日志扩展数据（用于字典类数据）
                loginLogExtend: {
                    // 类型
                    loginTypeList: [],
                    // 来源
                    LoginSource: [],

                },

            }
        },
        created() {
        },
        mounted() {
            /**
             * 获取类型列表
             */
            this.getLoginTypeList()
            /**
             * 获取来源列表
             */
            this.getLoginSource()
        },
        computed: {},
        methods: {
            /**
             * 登录日志搜索
             */
            loginLogSearch() {
                this.$refs.loginLogTable.search()
            },

            /**
             * 获取类型列表
             */
            getLoginTypeList(){
                remote('login_type').then(res=>{
                    if(res.state){
                        this.loginLogExtend.loginTypeList = res.data
                    }else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            /**
             * 获取来源列表
             */
            getLoginSource(){
                remote('login_source').then(res=>{
                    if(res.state){
                        this.loginLogExtend.LoginSource = res.data
                    }else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            /**
             * 表格全屏
             * @param state 状态
             */
            handleFullscreen(state) {
                if (state) {
                    screenfull.request(this.$refs.card.$el);
                } else {
                    screenfull.exit();
                }
            },
            /**
             * 登录日志事件
             * @param type 登录日志事件类型
             * @param data 登录日志事件数据
             */
            loginLogHandleEvent(obj){
                this.$refs.loginLogEdit.handleEvent(obj.type,obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
