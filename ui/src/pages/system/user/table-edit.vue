<template>
    <div>
        <!-- 编辑用户 start -->
        <Modal v-model="showEdit" :title="(userForm==null || userForm.id==null)?'添加用户':'编辑用户'" width="500">
            <Form ref="userForm" :model="userForm"
                  :rules="userValidate" :label-width="100">
                <FormItem label="角色：" prop="roleId">
                    <Select v-model="userForm.roleId" size="large" placeholder="请选择角色" clearable>
                        <Option v-for="item in extend.roleList" :value="item.roleId" :label="item.roleName"
                                :key="item.roleId"></Option>
                    </Select>
                </FormItem>
                <FormItem label="姓名：" prop="name">
                    <Input v-model="userForm.name" size="large" placeholder="请输入姓名" clearable/>
                </FormItem>
                <FormItem label="手机号：" prop="phone">
                    <Input v-model="userForm.phone" size="large" placeholder="请输入手机号" clearable/>
                </FormItem>
                <FormItem label="邮箱：" prop="email" label-for="email">
                    <Input v-model="userForm.email"  clearable size="large"  placeholder="请输入邮箱" element-id="email"/>
                </FormItem>
                <FormItem label="用户名：" prop="username">
                    <Input v-model="userForm.username" size="large" placeholder="请输入用户名" clearable/>
                </FormItem>
                <FormItem label="密码：" prop="password" v-if="!isEdit">
                    <Input v-model="userForm.password" size="large" placeholder="请输入密码" clearable/>
                </FormItem>
                <FormItem label="状态：" prop="loginFlag">
                    <RadioGroup v-model="userForm.loginFlag">
                        <Radio v-for="item in extend.statusList" size="large" :key="item.value" :label="item.value" border>
                            {{item.label}}
                        </Radio>
                    </RadioGroup>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large"  :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑用户表 end -->
    </div>
</template>
<script>
    import {addUser, delUser, relieveLimit, resetPassword, updateUser} from '@/api/system/user'
    import appUtils from '@/utils/appUtils';
    import secretUtils from '@/utils/secretUtils';
    import Setting from '../../../setting';
    import dataUtils from '../../../utils/dataUtils';

    export default {
        props: {
            //扩展数据
            extend: {
                type: Object
            }
        },
        data() {
            //验证密码强度验证
            const validatePass = (rule, value, callback) => {
                var patt = /(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[-#_*%$])^.{8,16}$/;
                if (patt.test(value)) {
                    callback();
                } else {
                    callback(new Error('密码包含大小写字母、数字、特殊字符'));
                }
            };
            return {
                // 用户表表单
                userForm: {
                    id: null,
                    deptId: null,
                    roleId: null,
                    username: null,
                    password: null,
                    name: null,
                    email: null,
                    phone: null,
                    avatar: null,
                    nickname: null,
                    sex: null,
                    loginFlag: '0'
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , userValidate: {
                    roleId: [
                        {required: true, type: 'number', message: '角色不能为空', trigger: 'change'}
                    ],
                    username: [
                        {required: true, message: '用户名不能为空', trigger: 'blur'},
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'},
                        {min: 8, max: 16, message: '密码长度8-16位', trigger: 'blur'},
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '姓名不能为空', trigger: 'blur'},
                        {min: 1, max: 20, message: '姓名长度1-18位', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '手机不能为空', trigger: 'blur'},
                        {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号'}
                    ],
                }
                //是否显示编辑
                , showEdit: false
                //选择用户
                , selectUser: null
                // 是否为编辑
                , isEdit: false
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
                        this.edit(null);
                        this.isEdit = false;
                        break;
                    case 'edit':
                        //修改
                        this.edit(data);
                        this.isEdit = true;
                        break;
                    case 'del':
                        //删除
                        this.rowDel(data);
                        break;
                    case 'reset':
                        //重置密码
                        this.reset(data);
                        break;
                    case 'limit':
                        //解除限制
                        let permission = secretUtils.permission(null, 'user_user_edit')
                        if (permission) {
                            this.limit(data)
                        }
                        break;
                }
            },

            /**
             * 处理数据更新
             */
            handleUpdate: function () {
                let that = this;
                const user = secretUtils.encryption({
                    data: that.userForm,
                    key: Setting.passwordSalt,
                    param: ['password']
                })
                updateUser(user).then(res => {
                    if (res.state) {
                        appUtils.showSuccess(res.msg);
                        that.refresh();
                        that.showEdit = false;
                    } else {
                        appUtils.showError(res.msg);
                    }
                    that.saveLoading = false;
                }).catch(() => {
                    that.saveLoading = false;
                })
            },
            /**
             * 处理数据添加
             */
            handleSave: function () {
                let that = this;
                const user = secretUtils.encryption({
                    data: that.userForm,
                    key: Setting.passwordSalt,
                    param: ['password']
                })
                addUser(user).then(res => {
                    that.saveLoading = false;
                    if (res.state) {
                        that.refresh();
                        appUtils.showSuccess(res.msg);
                        that.showEdit = false;
                    } else {
                        appUtils.showError(res.msg);
                    }
                }).catch(() => {
                    that.showEdit = false;
                    that.saveLoading = false;
                })
            },
            /**
             * 打开编辑
             * @param row
             */
            edit(row) {
                if (this.$refs['userForm'] != null) {
                    this.$refs['userForm'].resetFields();
                }
                this.showEdit = true;
                if (row != null) {
                    this.selectUser = row;
                    let user = JSON.parse(JSON.stringify(row));
                    this.userForm = user
                    this.userForm.password = null
                } else {
                    this.userForm = {
                        id: null,
                        deptId: null,
                        roleId: null,
                        username: null,
                        password: null,
                        name: null,
                        email: null,
                        phone: null,
                        avatar: null,
                        nickname: null,
                        sex: null,
                        loginFlag: '0'
                    }
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['userForm'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    this.saveLoading = true;
                    if (this.userForm.id != null) {
                        this.handleUpdate();
                    } else {
                        this.handleSave();
                    }
                })
            },
            /**
             * 删除数据
             */
            rowDel: function (row) {
                let loading = appUtils.showLoading('正在删除...');
                delUser(row.id).then((res) => {
                    appUtils.hideLoading(loading);
                    if (res.state) {
                        this.refresh(this.page);
                        appUtils.showSuccess(res.msg);
                    } else {
                        appUtils.showError(res.msg);
                    }
                }).catch(function () {
                    appUtils.hideLoading(loading);
                })
            },
            /**
             * 刷新数据
             */
            refresh() {
                this.$emit('on-refresh');
            },
            /**
             * 重置密码
             */
            reset(data) {
                let value = ''
                this.$Modal.confirm({
                    title: '重置密码',
                    loading: true,
                    render: (h) => {
                        return h('Input', {
                            props: {
                                value: value,
                                autofocus: true,
                                placeholder: '请输入新密码'
                            },
                            on: {
                                input: (val) => {
                                    value = val;
                                }
                            }
                        })
                    },
                    onOk: () => {
                        if (value == null || value == '') {
                            appUtils.showError('请输入密码！')
                            this.$Modal.remove();
                            return
                        }
                        if (value.length < 8 || value.length > 16) {
                            appUtils.showError('密码长度8-16位！')
                            this.$Modal.remove();
                            return
                        }

                        var patt = /(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[-#_*%$])^.{8,16}$/;
                        if (!patt.test(value)) {
                            appUtils.showError('密码包含大小写字母、数字、特殊字符！')
                            this.$Modal.remove();
                            return
                        }

                        const user = secretUtils.encryption({
                            data: {id: data.id, password: value},
                            key: Setting.passwordSalt,
                            param: ['password']
                        })

                        resetPassword(user).then(res => {
                            if (res.state) {
                                appUtils.showSuccess(res.msg)
                            } else {
                                appUtils.showError(res.msg)
                            }
                            this.$Modal.remove();
                        }).catch(err => {
                            this.$Modal.remove();
                        })
                    }
                })
            },
            /**
             * 解除限制
             */
            limit(data) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>确定解除用户【' + data.username + '】锁定么？</p>',
                    loading: true,
                    onOk: () => {
                        relieveLimit(data.username).then(res => {
                            this.$Modal.remove()
                            if (res.state) {
                                appUtils.showSuccess(res.msg)
                                this.refresh(this.page);
                            } else {
                                appUtils.showError(res.msg)
                            }
                        }).catch(err => {
                            this.$Modal.remove()
                            appUtils.showError(err.msg)
                        })
                    }
                })

            },
        },
        mounted() {
        }
    }
</script>
