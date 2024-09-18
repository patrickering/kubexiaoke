<!--
   租户
   author： 超级管理员
   date： 2022-07-30 10:09:29
-->

<template>
    <div>
        <!-- 编辑租户 start -->
        <Modal
                v-model="showEdit"
                :title="editType?'编辑租户':'添加租户'"
                width="600">
            <Form ref="tenantForm" :model="tenantForm"
                  :rules="tenantValidate" :label-width="100" label-position="right">
                <Row>
                    <Col :span="24">
                        <FormItem label="名称：" prop="name">
                            <Input v-model="tenantForm.name" clearable  placeholder="请输入名称" ></Input>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="是否启用：" prop="enable">
                            <Switch v-model="tenantForm.enable"   />
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" long :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑租户 end -->
    </div>
</template>
<script>
    import {
        getTenant,
        delTenant,
        addTenant,
        updateEnableTenant,
        updateTenant
    } from '@/api/system/tenant'

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
                // 租户表单
                tenantForm: {
                    id: null,
                    name: null,
                    enable: true,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , tenantValidate: {
                    name:[
                        { required: true, message: '名称不能为空', trigger: 'blur'},
                        { max: 100, message: '名称最大长度100', trigger: 'blur' },
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
                    case 'enable':
                        //修改是否启用
                        this.enableChange(data)
                        break;
                }
            },
            /**
             * 处理数据更新
             */
            handleUpdate () {
                updateTenant(this.tenantForm).then(res => {
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
            handleSave() {
                addTenant(this.tenantForm).then(res => {
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
                if (this.$refs['tenantForm'] != null) {
                    this.$refs['tenantForm'].resetFields();
                }

                if (row != null) {
                    let loading = appUtils.showLoading()
                    getTenant(row.id).then(res=>{
                        appUtils.hideLoading(loading)
                        if(res.state){
                            let model = res.data
                            this.tenantForm = model
                            this.showEdit = true
                        }else{
                            appUtils.showError(res.msg)
                        }
                    }).catch(err=>{
                        appUtils.hideLoading(loading)
                    })
                } else {
                    this.tenantForm = {
                        id: null,
                        name: null,
                        enable: true,
                    }
                    this.showEdit = true
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['tenantForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    this.saveLoading = true
                    if (this.tenantForm.id != null) {
                        this.handleUpdate()
                    } else {
                        this.handleSave()
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel(row) {
                let loading = appUtils.showLoading('正在删除...')
                delTenant(row.id).then((res) => {
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
             * 修改是否启用
             */
            enableChange(row){
                let loading = appUtils.showLoading('正在修改...')
                updateEnableTenant({id:row.id,enable:row.enable}).then((res) => {
                    appUtils.hideLoading(loading)
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                    } else {
                        row.enable = !row.enable
                        appUtils.showError(res.msg)
                    }
                }).catch(function () {
                    row.enable = !row.enable
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
