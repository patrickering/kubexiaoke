<!--
   命名空间
   author： xiaoke
   date： 2024-08-03 15:00:35
-->

<template>
    <div>
        <!-- 编辑命名空间 start -->
        <Modal
                v-model="showEdit"
                :title="editType?'编辑命名空间':'添加命名空间'"
                width="600">
            <Form ref="namespaceForm" :model="namespaceForm"
                  :rules="namespaceValidate" :label-width="100" label-position="right">
                <Row>
                    <Col :span="24">
                        <FormItem label="名称：" prop="name">
                            <Input v-model="namespaceForm.name" size="large" clearable placeholder="请输入名称"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="标识：" prop="sign">
                            <Input v-model="namespaceForm.sign" size="large" clearable placeholder="请输入标识"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="12">
                        <FormItem label="cpu请求：" prop="cpuRequest">
                            <div style="display: flex;align-items: center;">
                                <InputNumber style="width: 100%;" v-model="namespaceForm.cpuRequest"
                                             size="large"
                                             placeholder="请输入cpu请求"></InputNumber>
                                <div style="width: 50px;padding-left: 10px">Core</div>
                            </div>
                        </FormItem>
                    </Col>
                    <Col :span="12">
                        <FormItem label="cpu上限：" prop="cpuLimit">
                            <div style="display: flex;align-items: center;">
                                <InputNumber style="width: 100%;" v-model="namespaceForm.cpuLimit"
                                             size="large"
                                             placeholder="请输入cpu上限"></InputNumber>
                                <div style="width: 50px;padding-left: 10px">Core</div>
                            </div>
                        </FormItem>
                    </Col>
                    <Col :span="12">
                        <FormItem label="内存请求：" prop="memoryRequest">
                            <div style="display: flex;align-items: center;">
                                <InputNumber style="width: 100%;" v-model="namespaceForm.memoryRequest"
                                             size="large"
                                             placeholder="请输入内存请求"></InputNumber>
                                <div style="width: 50px;padding-left: 10px">GB</div>
                            </div>
                        </FormItem>
                    </Col>
                    <Col :span="12">
                        <FormItem label="内存上限：" prop="memoryLimit">
                            <div style="display: flex;align-items: center;">
                                <InputNumber style="width: 100%;" v-model="namespaceForm.memoryLimit"
                                             size="large"
                                             placeholder="请输入内存上限"></InputNumber>
                                <div style="width: 50px;padding-left: 10px">GB</div>
                            </div>

                        </FormItem>

                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑命名空间 end -->
    </div>
</template>
<script>
    import {addNamespace, delNamespace, getNamespace, updateNamespace} from '@/api/kube/namespace'
    import appUtils from '@/utils/appUtils';

    export default {
        props: {
            //扩展数据
            extend: {
                type: Object
            }
        },
        data() {

            return {
                // 命名空间表单
                namespaceForm: {
                    id: null,
                    projectId: null,
                    name: null,
                    sign: null,
                    cpuRequest: null,
                    cpuLimit: null,
                    memoryRequest: null,
                    memoryLimit: null,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , namespaceValidate: {
                    projectId: [
                        {type: 'number', required: true, message: '项目id不能为空', trigger: 'blur'},
                    ],
                    name: [
                        {max: 100, message: '名称最大长度100', trigger: 'blur'},
                    ],
                    sign: [
                        {max: 100, message: '标识最大长度100', trigger: 'blur'},
                    ]
                }
                //是否显示编辑
                , showEdit: false
                //编辑类型 true=编辑 false=添加
                , editType: true
            }
        },
        computed: {},
        methods: {
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(type, data) {
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
                }
            },
            /**
             * 处理数据更新
             */
            handleUpdate(namespaceForm) {
                updateNamespace(namespaceForm).then(res => {
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
             * 处理数据添加
             */
            handleSave(namespaceForm) {
                addNamespace(namespaceForm).then(res => {
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
                if (this.$refs['namespaceForm'] != null) {
                    this.$refs['namespaceForm'].resetFields();
                }

                if (row != null) {
                    let loading = appUtils.showLoading()
                    getNamespace(row.id).then(res => {
                        appUtils.hideLoading(loading)
                        if (res.state) {
                            let model = res.data
                            this.namespaceForm = model
                            this.showEdit = true
                        } else {
                            appUtils.showError(res.msg)
                        }
                    }).catch(err => {
                        appUtils.hideLoading(loading)
                    })
                } else {
                    this.namespaceForm = {
                        id: null,
                        projectId: this.extend.projectId,
                        name: null,
                        sign: null,
                        cpuRequest: null,
                        cpuLimit: null,
                        memoryRequest: null,
                        memoryLimit: null,
                    }
                    this.showEdit = true
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['namespaceForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    let namespaceForm = JSON.parse(JSON.stringify(this.namespaceForm))
                    this.saveLoading = true
                    if (namespaceForm.id != null) {
                        this.handleUpdate(namespaceForm)
                    } else {
                        this.handleSave(namespaceForm)
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel(row) {
                let loading = appUtils.showLoading('正在删除...')
                delNamespace(row.id).then((res) => {
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
        }
    }
</script>
