<template>
    <div>
        <h2>基本设置</h2>
        <Form ref="form" :model="user" :rules="rules" label-position="top" class="ivu-mt">
            <FormItem label="姓名" prop="name">
                <Input v-model="user.name" size="large" placeholder="请输入姓名"/>
            </FormItem>
            <FormItem label="邮箱" prop="email">
                <Input v-model="user.email" size="large" placeholder="请输入邮箱"/>
            </FormItem>
            <FormItem>
                <Button type="primary" size="large" @click="handleSubmit" :loading="saveLoading">更新基本信息</Button>
            </FormItem>
        </Form>
    </div>
</template>
<script>
    import {mapState} from 'vuex';
    import {updateInfo} from '@/api/system/user'
    import appUtils from "@/utils/appUtils";
    import dataUtils from "@/utils/dataUtils";
    import Setting from '@/setting';

    export default {
        computed: {
            ...mapState('admin/user', [
                'info'
            ])
        },
        data() {
            return {
                rules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                        {type: 'string', max: 20, message: '姓名最大不能超过20个字符', trigger: 'change'}
                    ],
                    nickname: [
                        {required: true, message: '请输入昵称', trigger: 'blur'},
                        {type: 'string', max: 20, message: '昵称最大不能超过20个字符', trigger: 'change'}
                    ],
                    email: [
                        {required: true, message: '请输入邮箱', trigger: 'blur'},
                        {required: true, type: 'email', message: '请输入正确的邮箱', trigger: 'blur'},
                        {type: 'string', max: 50, message: '邮箱最大不能超过50个字符', trigger: 'change'}
                    ]
                },
                user: {},
                saveLoading: false,//保存加载
            }
        },
        methods: {
            handleSubmit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.saveLoading = true
                        updateInfo({
                            sex: this.user.sex,
                            name: this.user.name,
                            nickname: this.user.nickname,
                            email: this.user.email,
                            avatar: this.user.avatar,
                        }).then(async res => {
                            if (res.state) {
                                dataUtils.setData(Setting.key.userInfo, this.user)
                                this.$store.dispatch('admin/user/set', this.user);
                                this.$store.dispatch('admin/user/load');
                                appUtils.showSuccess(res.msg)
                            } else {
                                appUtils.showError(res.msg)
                            }
                            this.saveLoading = false
                        }).cache(err => {
                            this.saveLoading = false
                        })

                    }
                });
            },
            //上传路径
            uploadPath() {
                return Setting.apiBaseURL + '/system/file/upload'
            },
            //上传头像成功
            uploadAvatarSuccess(res, file, fileList) {
                if (res.state) {
                    this.user.avatar = res.data.url
                } else {
                    appUtils.showError(res.msg)
                }
            }
        },
        mounted() {
            this.user = JSON.parse(JSON.stringify(this.info))
        },
    }
</script>
<style lang="less">
    .setting-account-info {
        &-avatar {
            width: 90px;
            height: 90px;
        }
    }
</style>
