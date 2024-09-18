<!--
   项目
   author： xiaoke
   date： 2024-08-03 14:21:50
-->

<template>
    <div class="project">
        <Row>
            <Col span="12">
                <div style="color: #202224;font-size: 32px;font-weight: 600">
                    所有项目
                </div>
            </Col>
            <Col span="12">
                <div style="margin-top: 10px;float: right; display: flex;">
                    <Input v-model="query.title" size="large" prefix="ios-search" style="width: 332px;"
                           placeholder="搜索项目名称"/>
                    <Button v-permission="{'value':'kube_project_add'}" size="large" type="primary"
                            style="margin-left: 10px;width: 128px" @click="handleEvent('add',null)">
                        添加项目
                    </Button>
                </div>
                <div style="clear: both"></div>
            </Col>
        </Row>
        <div style="margin-top: 20px">
            <div class="i-layout-tabs" v-if="tableData!=null&& tableData.length>0">
                <div class="i-layout-tabs-main">
                    <Tabs
                            type="card"
                            :value="current"
                            :animated="false"
                            closable
                            :draggable="false"
                            @on-click="handleClickTab"
                    >
                        <TabPane
                                v-for="page in tableData"
                                :key="page.id"
                                :label="(h) => tabLabel(h, page)"
                                :name="page.id+''"
                        />
                    </Tabs>
                </div>
            </div>
            <el-empty v-if="tableData!=null&& tableData.length<=0" description="暂无项目信息"></el-empty>
            <el-skeleton :loading="loading" :count="2" animated/>
        </div>
    </div>
</template>
<script>
    import {listProject} from '@/api/kube/project'
    import {updateProjectUser,addProjectUser} from '@/api/kube/projectUser'
    import appUtils from '@/utils/appUtils';
    import projectTopSelect from '@/assets/images/project-top-select.png'
    import projectTop from '@/assets/images/project-top.png'
    import dataUtils from "../../../utils/dataUtils";
    import Setting from "../../../setting";

    export default {

        props: {
            query: {
                type: Object
            },
            //扩展数据
            extend: {
                type: Object
            }
        },
        data() {
            return {
                //列表数据
                tableData: null
                , current: 1
                , loading: false
                //选选中的项目ID
                , selectProjectId: null
                , saveLoading: false
            }
        },
        computed: {},
        methods: {
            tabLabel(h, project) {
                let that = this
                const top = h('div', {
                    style: {
                        position: 'absolute',
                        top: '10px',
                        left: '20px'
                    }
                }, [h('img', {
                    attrs: {
                        src: (project.projectUser!=null && project.projectUser.followFlag)?projectTopSelect:projectTop
                    },
                    on: {
                        click: () => {
                            if(project.projectUser!=null){
                                updateProjectUser({id:project.projectUser.id,followFlag:!project.projectUser.followFlag}).then(res=>{
                                    if(res.state){
                                        that.getData()
                                    }
                                })
                            }else {
                                let userInfo = dataUtils.getData(Setting.key.userInfo)
                                addProjectUser({projectId:project.id,followFlag:true,userId:userInfo.id}).then(res=>{
                                    if(res.state){
                                        that.getData()
                                    }
                                })
                            }

                        }
                    },
                    style: {
                        width: '24px',
                        height: '24px'
                    }
                })])

                const labelName = h('span', project.title.substring(0, 1) || '未命名');
                const label = h('div', {
                    class: 'project-label',
                    style: {
                        background: this.hexToRgb(project.color),
                        color: project.color
                    }
                }, [labelName])

                const titleName = h('span', project.title || '未命名');
                const title = h('div', {
                    class: 'project-title',
                    style: {}
                }, [titleName])

                const createTime = h('span', '创建时间：' + project.createTime);
                const create = h('div', {
                    class: 'project-create',
                    style: {}
                }, [createTime])

                return h('div', {
                    class: 'project-box',
                    style: {
                        border: this.current == project.id ? '1px solid #1F8ECF' : '1px solid #FFFFFF'
                    }
                }, [top, label, title, create]);
            },

            hexToRgb(hex) {
                // 移除十六进制颜色代码中的'#'
                let sanitizedHex = hex.replace("#", "");

                // 解析红、绿、蓝值
                let r = parseInt(sanitizedHex.substring(0, 2), 16);
                let g = parseInt(sanitizedHex.substring(2, 4), 16);
                let b = parseInt(sanitizedHex.substring(4, 6), 16);

                return `rgb(${r}, ${g}, ${b},0.1)`;
            },

            handleClickTab(name) {
                this.current = name
                let project = this.tableData.filter(item => item.id == name)[0]
                dataUtils.setData(Setting.key.project,project.id)
                this.$emit('on-change', project);
            },
            /**
             * 搜索数据
             */
            search() {
                this.getData()
            },

            /**
             * 获取数据
             */
            getData() {
                this.tableData = null
                this.loading = true
                listProject(this.query).then(res => {
                    this.loading = false
                    if (res.state) {
                        if (res.data != null && res.data.length > 0) {
                            this.current = res.data[0].id
                            dataUtils.setData(Setting.key.project,res.data[0].id)
                            this.$emit('on-change', res.data[0]);
                        }
                        this.tableData = res.data
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(err => {
                    appUtils.hideLoading(loading)
                    this.loading = false
                })
            },
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(type, data) {
                this.$emit('on-events', {type: type, data: data});
            },
        },
        mounted() {
            this.getData()
        }
    }
</script>
<style>
    .project-box {
        margin-top: 10px;
        width: 262px;
        height: 195px;
        border-radius: 14px;
        background-color: #FFFFFF;
        margin-right: 20px;
        position: relative;
        box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);
    }

    .project-label {
        position: absolute;
        top: 10px;
        right: 10px;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        font-size: 26px;
        font-weight: 600;
        border-radius: 20px;
    }

    .project-title {
        position: absolute;
        top: 40px;
        left: 20px;
        color: #202224;
        font-size: 22px;
        font-weight: 600;
    }

    .project-create {
        position: absolute;
        top: 100px;
        left: 20px;
        color: #ACACAC;
        font-size: 14px;
    }

    .project-button {
        position: absolute;
        bottom: 20px;
        left: 20px;
    }

    .project .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-nav-container {
        height: 215px;
    }

    .project .i-layout-tabs {
        background: none;
    }

    .project .i-layout-tabs-main {
        background: none;
        margin: 0px !important;
    }

    .project .i-layout-tabs .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab {
        height: 195px;
        background: none;
    }

    .project .ivu-tabs-nav-prev {
        top: 75px;
        z-index: 99;
        left: 0px;
    }

    .project .ivu-tabs-nav-prev .ivu-icon {
        font-size: 36px !important;
        background-color: #FFFFFF;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        border-radius: 50%;
    }

    .project .ivu-tabs-nav-next {
        top: 75px;
        z-index: 99;
        right: 5px;
    }

    .project .ivu-tabs-nav-next .ivu-icon {
        font-size: 36px !important;
        background-color: #FFFFFF;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        border-radius: 50%;
    }

    .project .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab {
        padding: 0px 0px 0px;
    }

    .project .i-layout-tabs .ivu-tabs-nav-scrollable {
        padding: 0 0px !important;
    }
</style>
