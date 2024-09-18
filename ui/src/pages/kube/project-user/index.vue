<!--
   项目人员
   author： xiaoke
   date： 2024-08-27 00:01:33
-->

<template>
    <div >
        <Card ref="card" :bordered="false" :dis-hover="true"  :padding="0">
            <div>
                <project-user-table-list ref="projectUserTable" :extend="projectUserExtend" :query="projectUserQuery"  @on-events="projectUserHandleEvent"></project-user-table-list>
            </div>
            <div>
                <project-user-table-edit ref="projectUserEdit" :extend="projectUserExtend" @on-refresh="$refs.projectUserTable.getData()"></project-user-table-edit>
            </div>
        </Card>
    </div>
</template>

<script>
    import projectUserTableList from './project-user-table-list';
    import projectUserTableEdit from './project-user-table-edit';




    export default {
        components: {
            projectUserTableList,
            projectUserTableEdit
        },
        props:{
            projectId:null,
            project:null
        },
        data() {
            return {
                // 项目人员查询条件
                projectUserQuery: {
                    projectId:this.projectId,
                    size: 10,
                    current: 1
                },

                //项目人员扩展数据（用于字典类数据）
                projectUserExtend: {
                    projectId:this.projectId,
                    project:this.project,
                    // 是否关注
                    followFlagList: [
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
             * 项目人员事件
             * @param type 项目人员事件类型
             * @param data 项目人员事件数据
             */
            projectUserHandleEvent(obj){
                this.$refs.projectUserEdit.handleEvent(obj.type,obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
