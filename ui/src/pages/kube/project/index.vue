<!--
   项目
   author： xiaoke
   date： 2024-08-03 14:21:50
-->

<template>
    <div style="width: 1200px;margin: 0 auto;margin-top: 20px;">
        <div>
            <project-table-list ref="projectTable" :extend="projectExtend" :query="projectQuery"
                                @on-events="projectHandleEvent" @on-change="changeProject"></project-table-list>
        </div>
        <div>
            <project-table-edit ref="projectEdit" :extend="projectExtend"
                                @on-refresh="$refs.projectTable.getData()"></project-table-edit>
        </div>

        <div style="margin-top: 20px">
            <Card ref="card" :bordered="false"  :dis-hover="true"  :padding="16" style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                <Tabs value="namespace">
                    <TabPane label="集群/命名空间" name="namespace">
                        <Row>
                            <Col span="4" style="text-align: left">
                                <div v-permission="{'value':'kube_namespace_add'}" style="color: #1F8ECF;font-size: 18px;font-weight: 600;cursor: pointer" @click="$refs.projectNamespace.namespaceHandleEvent({type:'add',data:null})">添加命名空间</div>
                            </Col>
                        </Row>
                        <namespace ref="projectNamespace" v-if="selectProject!=null" :project-id="selectProject.id"></namespace>
                    </TabPane>
                    <TabPane label="项目设置" name="project">
                        <project-table-setting ref="projectSetting" v-if="selectProject!=null" @on-refresh="$refs.projectTable.getData()" :id="selectProject.id"></project-table-setting>
                    </TabPane>
                </Tabs>
            </Card>
        </div>
    </div>
</template>

<script>
    import projectTableSearch from './project-table-search';
    import projectTableList from './project-table-list';
    import projectTableEdit from './project-table-edit';
    import projectTableSetting from './project-table-setting';
    import namespace from '../namespace';

    export default {
        components: {
            projectTableSearch,
            projectTableList,
            projectTableEdit,
            projectTableSetting,
            namespace
        },
        data() {
            return {
                // 项目查询条件
                projectQuery: {
                    title: null,
                    remark: null
                },
                selectProject:null,
                //项目扩展数据（用于字典类数据）
                projectExtend: {}

            }
        },
        created() {
        },
        mounted() {
        },
        computed: {},
        methods: {
            /**
             * 项目搜索
             */
            projectSearch() {
                this.$refs.projectTable.search()
            },
            /**
             * 切换项目
             */
            changeProject(data){
                if(this.selectProject!=null && data.id == this.selectProject.id){
                    return
                }
                this.selectProject = null
                this.$nextTick(()=>{
                    this.selectProject = data
                })

            },
            /**
             * 项目事件
             * @param type 项目事件类型
             * @param data 项目事件数据
             */
            projectHandleEvent(obj) {
                this.$refs.projectEdit.handleEvent(obj.type, obj.data)
            },
        }
    }
</script>

<style>

</style>
