<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <div>
        <Card :bordered="false" dis-hover class="list-table-list-card ivu-mt" ref="card">
            <table-form ref="form" v-model="query" :extend="extend"  @on-submit="search" @on-reset="search"/>
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
                    size: 10,
                    current: 1
                },
                //扩展数据（用于字典类数据）
                extend: {

                }
            }
        },
        created() {
        },
        mounted() {

        },
        computed: {},
        methods: {
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
