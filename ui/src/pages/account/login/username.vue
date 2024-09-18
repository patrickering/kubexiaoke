<template>
    <div class="username-login">
        <Form ref="userForm" :model="userForm" @keydown.enter.native="handleSubmit" :rules="userFormRules" :label-width="0">
            <FormItem prop="username">
                <div style="position: relative">
                    <Input class="textIndent" v-model="userForm.username" size="large" style="width: 400px" placeholder="请输入用户名">
                    </Input>
                    <img style="position: absolute;top: 15px;left: 19px" src="@/assets/images/rusename-icon.png" width="24" height="24"/>
                </div>
            </FormItem>
            <FormItem prop="password">
                <div style="position: relative">
                    <Input class="textIndent" type="password" v-model="userForm.password" size="large" placeholder="请输入密码"></Input>
                    <img style="position: absolute;top: 15px;left: 19px" src="@/assets/images/password-icon.png" width="24" height="24"/>
                </div>

            </FormItem>
            <FormItem prop="captcha">
                <div style="display: flex;align-items: center">
                    <Input v-model="userForm.captcha" size="large" style="width: 234px" placeholder="请输入验证码"></Input>
                    <img :src="code.src" style="width: 150px;height:54px;border: 1px solid #E5E5E5;border-radius: 6px;margin-left: 17px;cursor: pointer" @click="refreshCode" />
                </div>
            </FormItem>
            <FormItem>
                <Button type="primary" :loading="loading" size="large"  long @click="handleSubmit" style="height: 54px;border-radius: 8px">登录</Button>
            </FormItem>
        </Form>

    </div>
</template>
<script>
    import { mapActions } from "vuex";
    import mixins from "../mixins";
    import util from "@/libs/util";
    import dataUtils from "@/utils/dataUtils";
    import Setting from "@/setting";
    import appUtils from "../../../utils/appUtils";

    export default {
        mixins: [mixins],
        components: {  },
        data() {
            return {
                userForm:{
                    username:null,
                    password:null,
                    captcha:null
                },
                loginType:'username',
                code: {
                    src: "/code",
                    value: "",
                    len: 4,
                    type: "image"
                },
                loginForm: {
                    code: "",
                    redomStr: ""
                },
                captchaRule: [
                    {
                        required: true,
                        message: "请输入验证码！",
                        trigger: "change"
                    }
                ],
                loading: false,
                userFormRules:{
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                    captcha: [
                        { required: true, message: '请输入验证码', trigger: 'blur' }
                    ],
                }
            };
        },
        methods: {
            ...mapActions("admin/account", ["login"]),
            refreshCode() {
                this.loginForm.code = "";
                this.loginForm.randomStr = util.randomLenNum(this.code.len, true);
                this.code.type === "text"
                    ? (this.code.value = util.randomLenNum(this.code.len))
                    : (this.code.src = `${Setting.apiBaseURL}/system/login/code/${this.loginForm.randomStr}`);
            },
            /**
             * @description 登录
             */
            handleSubmit() {
                this.$refs['userForm'].validate((valid) => {
                    if (valid) {
                        const { username, password, captcha } = this.userForm;
                        this.loading = true;
                        this.login({
                            username,
                            password,
                            randomStr: this.loginForm.randomStr,
                            captcha,
                            type:'USERNAME'
                        })

                            .then((res) => {
                                if(res.state){
                                    // 重定向对象不存在则返回顶层路径
                                    this.$router.replace(
                                        "/"
                                    );
                                }else {
                                    appUtils.showError(res.msg)
                                    this.refreshCode();
                                    this.loading = false;
                                }
                            })
                            .catch(err => {
                                this.refreshCode();
                                this.loading = false;
                            })
                    }
                })

            }
        },
        created() {
            this.refreshCode();
        }
    };
</script>

<style>
    .username-login .ivu-input-large{
        height: 54px;
        background-color: #F5F7F9;
        border-radius: 8px;
        text-indent: 20px;
    }
    .username-login .textIndent .ivu-input-large{
        text-indent: 64px;
    }
</style>
