<!--
   项目人员
   author： xiaoke
   date： 2024-08-27 00:01:32
-->

<template>
    <div>
        <!-- 编辑项目人员 start -->
        <Modal
                v-model="showEdit"
                :title="editType?'编辑项目人员':'添加项目人员'"
                width="600">
            <Form ref="projectUserForm" :model="projectUserForm"
                  :rules="projectUserValidate" :label-width="100" label-position="right">
                <Row>
                    <Col :span="24">
                        <FormItem label="项目ID：" prop="projectId">
                            <InputNumber style="width: 100%;" v-model="projectUserForm.projectId"      placeholder="请输入项目ID"></InputNumber>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="用户ID：" prop="userId">
                            <InputNumber style="width: 100%;" v-model="projectUserForm.userId"      placeholder="请输入用户ID"></InputNumber>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="是否关注：" prop="followFlag">
                            <Switch v-model="projectUserForm.followFlag"   />
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" long :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑项目人员 end -->
    </div>
</template>
<script>
    import {
        getProjectUser,
        delProjectUser,
        addProjectUser,
        updateProjectUser
    } from '@/api/kube/projectUser'
    import appUtils from '@/utils/appUtils';

    export default {
        props: {
            //扩展数据
            extend:{
                type: Object
            }
        },
        data() {

            return {
                // 项目人员表单
                projectUserForm: {
                    id: null,
                    projectId: null,
                    userId: null,
                    followFlag: false,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , projectUserValidate: {
                    projectId:[
                        {type: 'number', required: true, message: '项目ID不能为空', trigger: 'blur'},
                    ],
                    userId:[
                        {type: 'number', required: true, message: '用户ID不能为空', trigger: 'blur'},
                    ]
                }
                //是否显示编辑
                ,showEdit:false
                //编辑类型 true=编辑 false=添加
                ,editType:true
            }
        },
        computed: {},
        methods: {
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(type,data){
                switch (type) {
                    case 'add':
                        //添加
                        this.editType = false
                        this.edit(null)
                        break;
                    case 'edit':
                        //修改
                        this.editType = true
                        this.edit(data)
                        break;
                    case 'del':
                        //删除
                        this.rowDel(data)
                        break;
                    case 'save':
                        //保存用户
                        this.handleSave({
                            projectId:this.extend.projectId,
                            userId:data.userId
                        })
                        break;
                }
            },
            /**
             * 处理数据更新
             */
            handleUpdate (projectUserForm) {
                updateProjectUser(projectUserForm).then(res => {
                    if(res.state){
                        appUtils.showSuccess(res.msg)
                        this.refresh()
                        this.showEdit = false
                    }else {
                        appUtils.showError(res.msg)
                    }
                    this.saveLoading = false
                }).catch(() => {
                    this.saveLoading = false
                })
            },

            /**
             * 处理数据添加
             */
            handleSave(projectUserForm) {
                addProjectUser(projectUserForm).then(res => {
                    this.saveLoading = false
                    if (res.state) {
                        this.refresh()
                        appUtils.showSuccess(res.msg)
                        this.showEdit = false
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(() => {
                    this.saveLoading = false
                })
            },

            /**
             * 打开编辑
             * @param row
             */
            edit(row) {
                if (this.$refs['projectUserForm'] != null) {
                    this.$refs['projectUserForm'].resetFields();
                }

                if (row != null) {
                    let loading = appUtils.showLoading()
                    getProjectUser(row.id).then(res=>{
                        appUtils.hideLoading(loading)
                        if(res.state){
                            let model = res.data
                            this.projectUserForm = model
                            this.showEdit = true
                        }else{
                            appUtils.showError(res.msg)
                        }
                    }).catch(err=>{
                        appUtils.hideLoading(loading)
                    })
                } else {
                    this.projectUserForm = {
                        id: null,
                        projectId: null,
                        userId: null,
                        followFlag: false,
                    }
                    this.showEdit = true
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['projectUserForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    let projectUserForm = JSON.parse(JSON.stringify(this.projectUserForm))
                    this.saveLoading = true
                    if (projectUserForm.id != null) {
                        this.handleUpdate(projectUserForm)
                    } else {
                        this.handleSave(projectUserForm)
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel(row) {
                let loading = appUtils.showLoading('正在删除...')
                delProjectUser(row.id).then((res) => {
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
            refresh(){
                this.$emit('on-refresh');
            }
        },
        mounted() {
        }
    }
</script>
