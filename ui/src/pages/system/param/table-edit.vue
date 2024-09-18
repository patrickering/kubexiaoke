<template>
    <div>
        <!-- 编辑参数 start -->
        <Modal
                v-model="showEdit"
                :title="(paramForm==null || paramForm.publicId==null)?'添加参数':'编辑参数'"
                width="500">
            <Form ref="paramForm" :model="paramForm" :rules="paramValidate" :label-width="100">
                <FormItem label="名称：" prop="publicName">
                    <Input v-model="paramForm.publicName" placeholder="请输入名称"></Input>
                </FormItem>
                <FormItem label="键：" prop="publicKey">
                    <Input v-model="paramForm.publicKey" placeholder="请输入键"></Input>
                </FormItem>
                <FormItem label="值：" prop="publicValue">
                    <Input v-model="paramForm.publicValue" placeholder="请输入值"></Input>
                </FormItem>
                <FormItem label="编码：" prop="validateCode">
                    <Input v-model="paramForm.validateCode" placeholder="请输入编码"></Input>
                </FormItem>
                <FormItem label="类型：" prop="system">
                    <Select v-model="paramForm.system" placeholder="请选择类型">
                        <Option v-for="item in extend.dictTypeList" :value="item.value" :key="item.value">{{ item.label }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="状态：" prop="status">
                    <Select v-model="paramForm.status" placeholder="请选择状态">
                        <Option v-for="item in extend.statusTypeList" :key="item.value" :value="item.value">{{ item.label }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="业务类型：" prop="publicType">
                    <Select v-model="paramForm.publicType" placeholder="请选择业务类型">
                        <Option v-for="item in extend.paramTypeList" :value="item.value" :key="item.value">{{ item.label }}
                        </Option>
                    </Select>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" long :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑参数 end -->
    </div>
</template>
<script>
    import {addObj, delObj, putObj} from '@/api/system/param'
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
                //表单
                 paramForm: {}
                //保存密等
                , saveLoading: false
                //参数验证
                , paramValidate: {
                    publicName: [
                        {required: true, message: '请输名称', trigger: 'blur'},
                        {max: 30, message: '长度在 30 个字符', trigger: 'blur'},
                        {validator: rule.validatorNameCn, trigger: 'blur'}
                    ],
                    publicKey: [
                        {required: true, message: '请输入键', trigger: 'blur'},
                        {validator: rule.validatorKey, trigger: 'blur'}
                    ],
                    publicValue: [
                        {required: true, message: '请输入值', trigger: 'blur'}
                    ],
                    system: [
                        {required: true, message: '请选择类型', trigger: 'blur'}
                    ],
                    status: [
                        {required: true, message: '请选择状态', trigger: 'blur'}
                    ],
                    publicType: [
                        {required: true, message: '请选择业务类型', trigger: 'blur'}
                    ]
                },
                //是否显示编辑
                showEdit:false
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
                putObj(this.paramForm).then(res => {
                    appUtils.showSuccess(res.msg)
                    this.refresh()
                    this.showEdit = false
                    this.saveLoading = false
                }).catch(() => {
                    this.showEdit = false
                    this.saveLoading = false
                })
            },

            /**
             * 处理数据添加
             */
            handleSave: function () {
                addObj(this.paramForm).then(res => {
                    this.saveLoading = false
                    if (res.state) {
                        this.refresh()
                        appUtils.showSuccess(res.msg)
                        this.showEdit = false
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(() => {
                    this.showEdit = false
                    this.saveLoading = false
                })
            },

            /**
             * 打开编辑
             * @param row
             */
            edit(row) {
                if (this.$refs['paramForm'] != null) {
                    this.$refs['paramForm'].resetFields();
                }
                if (row != null) {
                    this.paramForm = JSON.parse(JSON.stringify(row))
                } else {
                    this.paramForm = {
                        system: ''
                    }
                }
                this.showEdit = true
            },

            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['paramForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    this.saveLoading = true
                    if (this.paramForm.publicId != null) {
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
                delObj(row.publicId).then((res) => {
                    appUtils.hideLoading(loading)
                    if (res.state) {
                        this.refresh(this.page)
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
