<!--
   命名空间
   author： xiaoke
   date： 2024-08-03 15:00:35
-->

<template>
    <div class="namespace">
        <div>
            <namespace-table-list ref="namespaceTable" :extend="namespaceExtend" :query="namespaceQuery"
                                  @on-events="namespaceHandleEvent"></namespace-table-list>
        </div>
        <div>
            <namespace-table-edit ref="namespaceEdit" :extend="namespaceExtend"
                                  @on-refresh="$refs.namespaceTable.getData()"></namespace-table-edit>
        </div>

    </div>
</template>

<script>
    import namespaceTableSearch from './namespace-table-search';
    import namespaceTableList from './namespace-table-list';
    import namespaceTableEdit from './namespace-table-edit';

    export default {
        components: {
            namespaceTableSearch,
            namespaceTableList,
            namespaceTableEdit
        },
        props: {
            projectId: null
        },
        data() {
            return {
                // 命名空间查询条件
                namespaceQuery: {
                    projectId: null,
                    size: 10,
                    current: 1
                },
                //命名空间扩展数据（用于字典类数据）
                namespaceExtend: {
                    projectId: null
                },

            }
        },
        created() {
        },
        mounted() {
            this.namespaceExtend.projectId = this.projectId
            this.namespaceQuery.projectId = this.projectId
        },
        computed: {},
        methods: {
            /**
             * 命名空间搜索
             */
            namespaceSearch() {
                this.$refs.namespaceTable.search()
            },
            /**
             * 命名空间事件
             * @param type 命名空间事件类型
             * @param data 命名空间事件数据
             */
            namespaceHandleEvent(obj) {
                this.$refs.namespaceEdit.handleEvent(obj.type, obj.data)
            },
        }
    }
</script>
