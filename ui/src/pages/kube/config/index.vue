<!--
   配置
   author： xiaoke
   date： 2024-08-03 16:10:50
-->

<template>
    <div>
        <Card ref="card" :bordered="false" :dis-hover="true" :padding="32" style="border-radius: 8px">
            <Row>
                <Col :span="14">
                    <div style="color: #202224;font-size: 24px;font-weight: 600">配置列表</div>
                </Col>
                <Col :span="10">
                    <config-table-search v-model="configQuery" :extend="configExtend" @on-submit="configSearch"
                                         @on-reset="configSearch"></config-table-search>
                </Col>
            </Row>
            <div style="margin-top: 10px">
                <config-table-list ref="configTable" :extend="configExtend" :query="configQuery"
                                   @on-events="configHandleEvent"></config-table-list>
            </div>
            <div>
                <config-table-edit ref="configEdit" :extend="configExtend"
                                   @on-refresh="$refs.configTable.getData()"></config-table-edit>
            </div>
        </Card>
    </div>
</template>

<script>
    import configTableSearch from './config-table-search';
    import configTableList from './config-table-list';
    import configTableEdit from './config-table-edit';


    export default {
        components: {
            configTableSearch,
            configTableList,
            configTableEdit
        },
        data() {
            return {
                // 配置查询条件
                configQuery: {
                    name: null,
                    type: null,
                    size: 10,
                    current: 1
                },

                //配置扩展数据（用于字典类数据）
                configExtend: {},

            }
        },
        created() {
        },
        mounted() {
        },
        computed: {},
        methods: {
            /**
             * 配置搜索
             */
            configSearch() {
                this.$refs.configTable.search()
            },
            /**
             * 配置事件
             * @param type 配置事件类型
             * @param data 配置事件数据
             */
            configHandleEvent(obj) {
                this.$refs.configEdit.handleEvent(obj.type, obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
