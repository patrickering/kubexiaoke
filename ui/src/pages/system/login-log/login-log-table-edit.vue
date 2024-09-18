<!--
   登录日志
   author： xiaoke
   date： 2022-01-23 15:30:21
-->

<template>
    <div>
        <!-- 编辑登录日志 start -->
        <Modal
                v-model="showEdit"
                title="编辑登录日志"
                width="600">
            <Form ref="loginLogForm" :model="loginLogForm"
                  :rules="loginLogValidate" :label-width="100" label-position="right">
                <Row>
                    <Col :span="24">
                        <FormItem label="类型：" prop="type">
                            <Select v-model="loginLogForm.type"  clearable filterable>
                                <Option v-for="item in extend.loginTypeList" :key="item.value" :value="item.value" :label="item.label"></Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="IP：" prop="ip">
                            <Input v-model="loginLogForm.ip" clearable   ></Input>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="来源：" prop="source">
                            <Select v-model="loginLogForm.source"  clearable filterable>
                                <Option v-for="item in extend.LoginSource" :key="item.value" :value="item.value" :label="item.label"></Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="Token：" prop="token">
                            <Input v-model="loginLogForm.token" clearable   ></Input>
                        </FormItem>
                    </Col>
                    <Col :span="24">
                        <FormItem label="参数：" prop="param">
                            <Input type="textarea" v-model="loginLogForm.param" clearable     ></Input>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑登录日志 end -->
    </div>
</template>
<script>
    import {
        delLoginLog,
        addLoginLog,
        updateLoginLog
    } from '@/api/system/loginLog'



    import appUtils from '@/utils/appUtils';
    import {rule} from '@/utils/validateRules';

    export default {
        props: {
            //扩展数据
            extend:{
                type: Object
            }
        },
        data() {
            return {
                // 登录日志表单
                loginLogForm: {
                    id: null,
                    type: null,
                    ip: null,
                    source: null,
                    token: null,
                    param: null,
                    delFlag: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , loginLogValidate: {
                    type:[
                        {required: true, message: '类型不能为空', trigger: 'blur'},
                    ],
                    ip:[
                        {required: true, message: 'IP不能为空', trigger: 'blur'},
                    ],
                    token:[
                        {required: true, message: 'Token不能为空', trigger: 'blur'},
                    ],
                    param:[
                        {required: true, message: '参数不能为空', trigger: 'blur'},
                    ]
                }
                //是否显示编辑
                ,showEdit:false
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
                        this.edit(null)
                        break;
                    case 'edit':
                        //修改
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
            handleUpdate: function () {
                updateLoginLog(this.loginLogForm).then(res => {
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
            handleSave: function () {
                addLoginLog(this.loginLogForm).then(res => {
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
                if (this.$refs['loginLogForm'] != null) {
                    this.$refs['loginLogForm'].resetFields();
                }
                this.showEdit = true
                if (row != null) {
                    let model = JSON.parse(JSON.stringify(row))
                    this.loginLogForm = model
                } else {
                    this.loginLogForm = {
                        id: null,
                        type: null,
                        ip: null,
                        source: null,
                        token: null,
                        param: null,
                        delFlag: null,
                        createBy: null,
                        createTime: null,
                        updateBy: null,
                        updateTime: null
                    }
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['loginLogForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    this.saveLoading = true
                    if (this.loginLogForm.id != null) {
                        this.handleUpdate()
                    } else {
                        this.handleSave()
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel: function (row) {
                let loading = appUtils.showLoading('正在删除...')
                delLoginLog(row.id).then((res) => {
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
            },
        },
        mounted() {
        }
    }
</script>
