<!--
   异常记录
   author： xiaoke
   date： 2024-08-22 23:06:02
-->

<template>
    <div>
        <Card ref="card" :bordered="false" :dis-hover="true" :padding="32" style="border-radius: 8px">
            <Row>
                <Col :span="18">
                    <div style="color: #202224;font-size: 24px;font-weight: 600">异常记录</div>
                </Col>
                <Col :span="6">
                    <error-record-table-search v-model="errorRecordQuery" :extend="errorRecordExtend"
                                               @on-submit="errorRecordSearch"
                                               @on-reset="errorRecordSearch"></error-record-table-search>
                </Col>
            </Row>
            <div style="margin-top: 10px">
                <error-record-table-list ref="errorRecordTable" :extend="errorRecordExtend" :query="errorRecordQuery"
                                         @on-events="errorRecordHandleEvent"></error-record-table-list>
            </div>
            <div>
                <error-record-table-edit ref="errorRecordEdit" :extend="errorRecordExtend"
                                         @on-refresh="$refs.errorRecordTable.getData()"></error-record-table-edit>
            </div>
        </Card>
    </div>
</template>

<script>
    import errorRecordTableSearch from './error-record-table-search';
    import errorRecordTableList from './error-record-table-list';
    import errorRecordTableEdit from './error-record-table-edit';


    export default {
        components: {
            errorRecordTableSearch,
            errorRecordTableList,
            errorRecordTableEdit
        },
        data() {
            return {
                // 异常记录查询条件
                errorRecordQuery: {
                    title: null,
                    size: 10,
                    current: 1
                },

                //异常记录扩展数据（用于字典类数据）
                errorRecordExtend: {
                    // 已读
                    readFlagList: [
                        {label: "是", value: "1"},
                        {label: "否", value: "0"},
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
             * 异常记录搜索
             */
            errorRecordSearch() {
                this.$refs.errorRecordTable.search()
            },
            /**
             * 异常记录事件
             * @param type 异常记录事件类型
             * @param data 异常记录事件数据
             */
            errorRecordHandleEvent(obj) {
                this.$refs.errorRecordEdit.handleEvent(obj.type, obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
