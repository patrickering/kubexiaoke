<!--
   异常记录
   author： xiaoke
   date： 2024-08-22 23:06:02
-->

<template>
    <div>
        <!-- 编辑异常记录 start -->
        <Modal
                v-model="showEdit"
                :title="editType?'编辑异常记录':'添加异常记录'"
                width="600">
            <Form ref="errorRecordForm" :model="errorRecordForm"
                  :rules="errorRecordValidate" :label-width="100" label-position="right">
                <Row>
                    <Col :span="24">
                        <FormItem label="命名空间：" prop="namespace">
                            <Input v-model="errorRecordForm.namespace" clearable placeholder="请输入命名空间"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="标题：" prop="title">
                            <Input v-model="errorRecordForm.title" clearable placeholder="请输入标题"></Input>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="已读：" prop="readFlag">
                            <Switch v-model="errorRecordForm.readFlag"/>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" long :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑异常记录 end -->
    </div>
</template>
<script>
    import {
        addErrorRecord,
        batchDeleteErrorRecord,
        delErrorRecord,
        getErrorRecord,
        updateErrorRecord,
        updateReadFlagErrorRecord
    } from '@/api/kube/errorRecord'
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
                // 异常记录表单
                errorRecordForm: {
                    id: null,
                    namespace: null,
                    title: null,
                    readFlag: false,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , errorRecordValidate: {
                    namespace: [
                        {max: 100, message: '命名空间最大长度100', trigger: 'blur'},
                    ],
                    title: [
                        {required: true, message: '标题不能为空', trigger: 'blur'},
                        {max: 100, message: '标题最大长度100', trigger: 'blur'},
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
                    case 'batchDelete':
                        //批量删除
                        if (data == null || data.length <= 0) {
                            appUtils.showError('请选择要删除的数据！')
                            break;
                        }
                        this.batchDelete(data)
                        break;
                    case 'read-flag':
                        //修改已读
                        this.readFlagChange(data)
                        break;
                }
            },
            /**
             * 处理数据更新
             */
            handleUpdate(errorRecordForm) {
                updateErrorRecord(errorRecordForm).then(res => {
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
            handleSave(errorRecordForm) {
                addErrorRecord(errorRecordForm).then(res => {
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
                if (this.$refs['errorRecordForm'] != null) {
                    this.$refs['errorRecordForm'].resetFields();
                }

                if (row != null) {
                    let loading = appUtils.showLoading()
                    getErrorRecord(row.id).then(res => {
                        appUtils.hideLoading(loading)
                        if (res.state) {
                            let model = res.data
                            this.errorRecordForm = model
                            this.showEdit = true
                        } else {
                            appUtils.showError(res.msg)
                        }
                    }).catch(err => {
                        appUtils.hideLoading(loading)
                    })
                } else {
                    this.errorRecordForm = {
                        id: null,
                        namespace: null,
                        title: null,
                        readFlag: false,
                    }
                    this.showEdit = true
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['errorRecordForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    let errorRecordForm = JSON.parse(JSON.stringify(this.errorRecordForm))
                    this.saveLoading = true
                    if (errorRecordForm.id != null) {
                        this.handleUpdate(errorRecordForm)
                    } else {
                        this.handleSave(errorRecordForm)
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel(row) {
                let loading = appUtils.showLoading('正在删除...')
                delErrorRecord(row.id).then((res) => {
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
             * 批量删除数据
             */
            batchDelete(data) {
                if (data == null || data.length <= 0) {
                    appUtils.showError('请选择数据！')
                }
                this.$confirm('确定删除已选择数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let idList = data.map((item) => {
                        return item.id
                    })
                    batchDeleteErrorRecord(idList).then(res => {
                        if (res.state) {
                            this.refresh()
                            appUtils.showSuccess(res.msg)
                        } else {
                            appUtils.showError(res.msg)
                        }
                    })
                })
            },
            /**
             * 修改已读
             */
            readFlagChange(row) {
                let loading = appUtils.showLoading('正在修改...')
                updateReadFlagErrorRecord({id: row.id, readFlag: true}).then((res) => {
                    appUtils.hideLoading(loading)
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                    } else {
                        row.readFlag = !row.readFlag
                        appUtils.showError(res.msg)
                    }
                }).catch(function () {
                    row.readFlag = !row.readFlag
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
