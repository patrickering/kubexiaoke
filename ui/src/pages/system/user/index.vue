<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <div style="width: 1200px;margin: 0 auto;margin-top: 10px">
        <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
            <Row>
                <Col :span="4">
                    <div style="color: #202224;font-size: 24px;font-weight: 600">用户管理</div>
                </Col>
                <Col :span="20">
                    <table-form ref="form" v-model="query" :extend="extend"  @on-submit="search" @on-reset="search"/>
                </Col>
            </Row>

            <table-list ref="table" :extend="extend" :query="query" @on-events="handleEvent"/>
            <table-edit ref="edit" :extend="extend" @on-refresh="search"/>
        </Card>
    </div>
</template>
<script>
    import tableForm from './table-form';
    import tableList from './table-list';
    import tableEdit from './table-edit';
    import {deptRoleList} from '@/api/system/role'
    import {remote} from '@/api/system/dict'
    export default {
        name: 'system-param',
        components: {tableForm, tableList, tableEdit},
        data() {
            return {
                // 查询条件
                query: {
                    username:null,
                    name:null,
                    roleId:null,
                    phone:null,
                    email:null,
                    nickname:null,
                    sex:null,
                    loginFlag:null,
                    size: 10,
                    current: 1
                },
                //扩展数据（用于字典类数据）
                extend: {
                    roleList:[],
                    sexList:[],
                    statusList:[]
                }
            }
        },
        created() {
        },
        mounted() {
            this.getRole();
            this.getSex();
            this.getStatus();
        },
        computed: {},
        methods: {
            /**
             * 获取角色
             * */
            getRole(){
                deptRoleList().then(res=>{
                    if(res.state){
                        this.extend.roleList = res.data
                    }
                })
            },
            /*
            * h获取性别
            * */
            getSex(){
                remote('gender').then(res => {
                    if(res.state){
                        this.extend.sexList = res.data
                    }
                })
            },
            /**
             * 状态
             */
            getStatus(){
                remote('status_type').then(res => {
                    if(res.state){
                        this.extend.statusList = res.data
                    }
                })
            },

            /**
             * 搜索
             */
            search() {
                this.$refs.table.search()
            },
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(obj){
                this.$refs.edit.handleEvent(obj.type,obj.data)
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>
