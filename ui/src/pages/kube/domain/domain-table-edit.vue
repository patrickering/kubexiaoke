<!--
   集群域名
   author： xiaoke
   date： 2024-08-18 23:43:43
-->

<template>
    <div>
        <!-- 编辑集群域名 start -->
        <Modal
                v-model="showEdit"
                :title="editType?'编辑域名':'添加域名'"
                width="600">
            <Form ref="domainForm" :model="domainForm"
                  :rules="domainValidate" :label-width="100" label-position="right">
                <Row>
                    <Col :span="24">
                        <FormItem label="域名：" prop="domain">
                            <Input v-model="domainForm.domain" size="large" clearable  placeholder="请输入域名" ></Input>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑集群域名 end -->
    </div>
</template>
<script>
    import {
        getDomain,
        delDomain,
        addDomain,
        updateDomain
    } from '@/api/kube/domain'
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
                // 集群域名表单
                domainForm: {
                    id: null,
                    domain: null,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , domainValidate: {
                    domain:[
                        { required: true, message: '域名不能为空', trigger: 'blur'},
                        { max: 255, message: '域名最大长度255', trigger: 'blur' },
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
                }
            },
            /**
             * 处理数据更新
             */
            handleUpdate (domainForm) {
                updateDomain(domainForm).then(res => {
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
            handleSave(domainForm) {
                addDomain(domainForm).then(res => {
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
                if (this.$refs['domainForm'] != null) {
                    this.$refs['domainForm'].resetFields();
                }

                if (row != null) {
                    let loading = appUtils.showLoading()
                    getDomain(row.id).then(res=>{
                        appUtils.hideLoading(loading)
                        if(res.state){
                            let model = res.data
                            this.domainForm = model
                            this.showEdit = true
                        }else{
                            appUtils.showError(res.msg)
                        }
                    }).catch(err=>{
                        appUtils.hideLoading(loading)
                    })
                } else {
                    this.domainForm = {
                        id: null,
                        domain: null,
                    }
                    this.showEdit = true
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['domainForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    let domainForm = JSON.parse(JSON.stringify(this.domainForm))
                    this.saveLoading = true
                    if (domainForm.id != null) {
                        this.handleUpdate(domainForm)
                    } else {
                        this.handleSave(domainForm)
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel(row) {
                let loading = appUtils.showLoading('正在删除...')
                delDomain(row.id).then((res) => {
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
