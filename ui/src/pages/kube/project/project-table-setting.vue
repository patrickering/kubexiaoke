<!--
   项目
   author： xiaoke
   date： 2024-08-03 14:21:50
-->

<template>
    <div class="project-setting" style="padding-left: 10px">

        <div style="display: flex;align-items: center;">
            <div>
                <Button type="primary" v-if="showType=='edit'">基础设置</Button>
                <div v-if="showType!='edit'" style="color: #929292;font-weight: 600;font-size: 18px;cursor: pointer"
                     @click="showType='edit'">基础设置
                </div>
            </div>
            <div style="margin-left: 20px">
                <Button type="primary" v-if="showType=='user'">人员设置</Button>
                <div v-if="showType!='user'" style="color: #929292;font-weight: 600;font-size: 18px;cursor: pointer"
                     @click="showType='user'">人员设置
                </div>
            </div>
        </div>


        <!-- 编辑项目 start -->
        <Form ref="projectForm" :model="projectForm"
              v-if="showType=='edit'"
              style="width: 500px;margin-top: 20px"
              :rules="projectValidate" :label-width="140" label-position="left">
            <Row>
                <Col :span="24">
                    <FormItem label="项目名称：" prop="title">
                        <Input v-model="projectForm.title" clearable placeholder="请输入项目名称" size="large"></Input>
                    </FormItem>
                </Col>
                <Col :span="24">
                    <FormItem label="项目描述：" prop="remark">
                        <Input type="textarea" v-model="projectForm.remark" clearable placeholder="请输入项目描述"
                               size="large"></Input>
                    </FormItem>
                </Col>
                <Col :span="24">
                    <FormItem label="项目颜色：" prop="color">
                        <ColorPicker v-model="projectForm.color" size="large"/>
                    </FormItem>
                </Col>
            </Row>
        </Form>
        <div v-if="showType=='edit'" style="text-align: right">
            <Poptip style="margin-left: 10px" @on-ok="rowDel()" transfer confirm title="确认删除?">
                <Button type="error" size="large" ghost style="margin-right: 10px" >删除项目</Button>
            </Poptip>
            <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存项目</Button>
        </div>
        <!-- 编辑项目 end -->
        <project-user v-if="showType=='user'" style="margin-top: 30px" :project-id="projectForm.id" :project="projectForm"></project-user>
    </div>
</template>
<script>
    import {delProject, getProject, updateProject} from '@/api/kube/project'
    import appUtils from '@/utils/appUtils';
    import projectUser from '../project-user';

    export default {
        components:{
            projectUser
        },
        props: {
            //扩展数据
            extend: {
                type: Object
            },
            id: null
        },
        data() {
            return {
                // 项目表单
                projectForm: {
                    id: null,
                    title: null,
                    remark: null,
                    color: null,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , projectValidate: {
                    title: [
                        {required: true, message: '标题不能为空', trigger: 'blur'},
                        {max: 100, message: '标题最大长度100', trigger: 'blur'},
                    ],
                    remark: [
                        {max: 255, message: '描述最大长度255', trigger: 'blur'},
                    ],
                    color: [
                        {max: 20, message: '颜色最大长度20', trigger: 'blur'},
                    ]
                }
                //是否显示编辑
                , showEdit: false
                //编辑类型 true=编辑 false=添加
                , editType: true
                , showType: 'edit'
            }
        },
        computed: {},
        methods: {
            /**
             * 处理数据更新
             */
            handleUpdate(projectForm) {
                updateProject(projectForm).then(res => {
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                        this.refresh()
                        this.showEdit = false
                    } else {
                        appUtils.showError(res.msg)
                    }
                    this.saveLoading = false
                }).catch(() => {
                    this.saveLoading = false
                })
            },
            /**
             * 打开编辑
             * @param row
             */
            getProject(row) {
                if (this.$refs['projectForm'] != null) {
                    this.$refs['projectForm'].resetFields();
                }
                getProject(this.id).then(res => {
                    if (res.state) {
                        let model = res.data
                        this.projectForm = model
                        this.showEdit = true
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(err => {
                })
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['projectForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    let projectForm = JSON.parse(JSON.stringify(this.projectForm))
                    this.saveLoading = true
                    this.handleUpdate(projectForm)
                })
            },

            /**
             * 删除数据
             */
            rowDel() {
                let loading = appUtils.showLoading('正在删除...')
                delProject(this.id).then((res) => {
                    appUtils.hideLoading(loading)
                    if (res.state) {
                        this.refresh()
                        appUtils.showSuccess(res.msg)
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(function () {
                    appUtils.hideLoading(loading)
                })
            },
            /**
             * 刷新数据
             */
            refresh() {
                this.$emit('on-refresh');
            }
        },
        mounted() {
            this.getProject()
        }
    }
</script>
<style>
    .project-setting .ivu-form .ivu-form-item-label {
        color: #8D8D8D;
        font-size: 20px;
    }
</style>
