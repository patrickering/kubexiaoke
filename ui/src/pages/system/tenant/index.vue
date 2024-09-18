<!--
    租户
   author： 超级管理员
   date： 2022-07-30 10:09:29
-->

<template>
    <div >
        <Card ref="card" :bordered="true" :dis-hover="true"  :padding="16">
            <div>
                <tenant-table-search v-model="tenantQuery" :extend="tenantExtend" @on-submit="tenantSearch" @on-reset="tenantSearch"></tenant-table-search>
            </div>
            <div>
                <tenant-table-list ref="tenantTable" :extend="tenantExtend" :query="tenantQuery"  @on-events="tenantHandleEvent"></tenant-table-list>
            </div>
            <div>
                <tenant-table-edit ref="tenantEdit" :extend="tenantExtend" @on-refresh="$refs.tenantTable.getData()"></tenant-table-edit>
            </div>
        </Card>
    </div>
</template>

<script>
    import tenantTableSearch from './tenant-table-search';
    import tenantTableList from './tenant-table-list';
    import tenantTableEdit from './tenant-table-edit';




    export default {
        components: {
            tenantTableSearch,
            tenantTableList,
            tenantTableEdit
        },
        data() {
            return {
                // 租户查询条件
                tenantQuery: {
                    name: null,
                    enable: null,
                    size: 10,
                    current: 1
                },

                //租户扩展数据（用于字典类数据）
                tenantExtend: {
                    // 是否启用
                    enableList: [
                        {label:"是",value:"1"},
                        {label:"否",value:"0"},
                    ],

                },

            }
        },
        created() {
        },
        mounted() {
        },
        computed: {},
        methods: {
            /**
             * 租户搜索
             */
            tenantSearch() {
                this.$refs.tenantTable.search()
            },

            /**
             * 租户事件
             * @param type 租户事件类型
             * @param data 租户事件数据
             */
            tenantHandleEvent(obj){
                this.$refs.tenantEdit.handleEvent(obj.type,obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
