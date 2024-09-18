<template>
    <div class="setting-account-safety">
        <h2>安全设置</h2>
        <div class="ivu-mt ivu-pl ivu-pr">
            <Row type="flex" justify="center" align="middle">
                <Col span="22">
                    <h4>账户密码</h4>
                    <p>绑定手机，并设置密码，帐号更安全</p>
                </Col>
                <Col span="2" class="ivu-text-right">
                    <el-button v-if="!showPassword" type="text" @click="showPassword=true">修改</el-button>
                    <el-button v-else type="text" @click="showPassword=false">取消</el-button>
                </Col>
            </Row>
            <Card shadow style="margin-top: 10px" v-if="showPassword">
                <Login ref="form" @on-submit="handleSubmit">
                    <Password name="password" style="width: 350px" placeholder="原密码"/>
                    <Poptip trigger="focus" placement="right" width="240">
                        <Password name="newPassword" style="width: 350px" :rules="passwordRule"
                                  placeholder="至少6位密码，区分大小写" @on-change="handleChangePassword"/>
                        <div slot="content" class="page-account-register-tip">
                            <div class="page-account-register-tip-title" :class="passwordTip.class">
                                强度：{{ passwordTip.strong }}
                            </div>
                            <Progress :percent="passwordTip.percent" hide-info :stroke-width="6"
                                      :stroke-color="passwordTip.color"/>
                            <div class="page-account-register-tip-desc">
                                请至少输入 6 个字符。请不要使用容易被猜到的密码。
                            </div>
                        </div>
                    </Poptip>
                    <Password name="passwordConfirm" style="width: 350px" :rules="passwordConfirmRule"
                              placeholder="确认密码"/>
                    <Submit style="width: 350px" :loading="passwordLoading">修改</Submit>
                </Login>
            </Card>

            <Divider/>
            <Row type="flex" justify="center" align="middle">
                <Col span="22">
                    <h4>绑定手机</h4>
                    <p v-if="info.phone!=null">已绑定手机号：{{info.phone}}</p>
                    <p v-else>当前未绑定手机号</p>
                </Col>
                <Col span="2" class="ivu-text-right">
                    <el-button v-if="!showPhone" type="text" @click="refreshCode">修改</el-button>
                    <el-button v-else type="text" @click="showPhone=false">取消</el-button>
                </Col>
            </Row>
            <Card shadow style="margin-top: 10px" v-if="showPhone">
                <Login ref="form" @on-submit="handleSubmitPhone">
                    <Mobile v-model="updatePhoneForm.phone" name="phone" style="width: 350px" placeholder="手机号"/>
                    <Captcha
                            v-model="updatePhoneForm.captcha"
                            style="width: 350px"
                            :rules="captchaRule"
                            class="update-phone-captcha"
                            name="captcha"
                            :count-down="0"
                            @on-get-captcha="refreshCode"
                            enter-to-submit
                    >
                        <img slot="text" :src="code.src" />
                    </Captcha>
                    <Captcha name="code" style="width: 350px" :field="['phone','captcha']" :before-click="handleBeforeClick" enter-to-submit />
                    <Submit style="width: 350px" :loading="phoneLoading">修改</Submit>
                </Login>
            </Card>
            <Divider/>
        </div>
    </div>
</template>
<script>
    import {mapState} from 'vuex';
    import {rule} from '@/utils/validateRules';
    import {updatePassword,updatePhone} from '@/api/system/user'
    import {phoneCode} from '@/api/system/mobile'
    import appUtils from "@/utils/appUtils";
    import util from "@/libs/util";
    import dataUtils from "@/utils/dataUtils";
    import Setting from '@/setting';
    import secretUtils from "@/utils/secretUtils";
    export default {
        computed: {
            ...mapState('admin/user', [
                'info'
            ]),
            // 密码强度提示文案
            passwordTip() {
                let strong = '非常强';
                let className = 'strong';
                let percent = this.passwordLen;
                let color = '#19be6b';

                if (this.passwordLen <= 25) {
                    strong = '弱';
                    className = 'low';
                    color = '#ed4014';
                } else if (this.passwordLen <= 50) {
                    strong = '中';
                    className = 'medium';
                    color = '#ff9900';
                } else if (this.passwordLen <= 75) {
                    strong = '强';
                    className = 'strong';
                    color = '#19be6b';
                }

                return {
                    strong,
                    class: `page-account-register-tip-${className}`,
                    percent: percent,
                    color
                }
            }
        },
        data() {
            //验证密码一致
            const validatePassCheck = (rule, value, callback) => {
                if (value !== this.$refs.form.formValidate.newPassword) {
                    callback(new Error('两次输入的密码不匹配！'));
                } else {
                    callback();
                }
            };
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
                //显示修改密码
                showPassword: false,
                //密码验证
                passwordRule: [
                    {required: true, message: '密码不能为空！', trigger: 'change'},
                    { min: 8, max: 16,message:'密码长度8-16位' , trigger: 'blur'},
                    {validator: validatePass, trigger: 'blur'}
                ],
                //确认密码验证
                passwordConfirmRule: [
                    {required: true, message: '确认密码不能为空！', trigger: 'change'},
                    {validator: validatePassCheck, trigger: 'change'}
                ],
                //图形验证码验证
                captchaRule: [
                    {
                        required: true,
                        message: "请输入验证码！",
                        trigger: "change"
                    }
                ],
                // 密码长度，在密码强度提示时作为判断依据
                passwordLen: 0,
                //修改密码加载状态
                passwordLoading: false,
                //显示修改手机号
                showPhone:false,
                //随机标识
                randomStr:null,
                //验证码
                code: {
                    src: "/code",
                    value: "",
                    len: 4,
                    type: "image"
                },
                updatePhoneForm:{
                    phone:null,
                    captcha:null
                },
                phoneLoading: false,
            }
        },
        methods: {
            handleChangePassword(val) {
                this.passwordLen = rule.checkPassWord(val);
            },
            /**
             * @description 注册
             */
            handleSubmit(valid, values) {
                if (valid) {
                    const {password, newPassword} = values;

                    const user = secretUtils.encryption({
                        data: {password, newPassword},
                        key: Setting.passwordSalt,
                        param: ['password']
                    })

                    const eruser = secretUtils.encryption({
                        data: user,
                        key: Setting.passwordSalt,
                        param: ['newPassword']
                    })

                    this.passwordLoading = true
                    updatePassword(eruser).then(res => {
                        if (res.state) {
                            appUtils.showSuccess(res.msg)
                        } else {
                            appUtils.showError(res.msg)
                        }
                        this.passwordLoading = false
                    }).catch(err => {
                        this.passwordLoading = false
                    })
                }
            },
            //刷新图形验证码
            refreshCode() {
                this.showPhone=true
                this.randomStr = util.randomLenNum(this.code.len, true);
                this.code.type === "text"
                    ? (this.code.value = util.randomLenNum(this.code.len))
                    : (this.code.src = `${Setting.apiBaseURL}/system/mobile/phone-image/${this.randomStr}`);
            },
            //获取验证码前事件
            handleBeforeClick(values){
                return new Promise(async resolve => {
                    phoneCode(values.phone,this.randomStr,values.captcha,'updatePhone').then(res=>{
                        if(res.state){
                            appUtils.showSuccess('获取验证码成功，请注意手机短信！','提示')
                            resolve();
                        }else {
                            appUtils.showError(res.msg)
                            this.refreshCode()
                        }
                    })
                });
            },
            /**
             * 确认修改手机号
             * @param valid
             * @param values
             */
            handleSubmitPhone(valid, values){
                if (valid) {
                    const {phone, code} = values;
                    this.phoneLoading = true
                    updatePhone({phone, code}).then(res => {
                        if (res.state) {
                            appUtils.showSuccess(res.msg)
                            let user = JSON.parse(JSON.stringify(this.info))
                            user.phone = phone
                            dataUtils.setData(Setting.key.userInfo, user)
                            this.$store.dispatch('admin/user/set', user);
                            this.$store.dispatch('admin/user/load');
                        } else {
                            appUtils.showError(res.msg)
                        }
                        this.phoneLoading = false
                    }).catch(err => {
                        this.phoneLoading = false
                    })
                }
            }

        }
    }
</script>
<style lang="less">
    .setting-account-safety {
        .ivu-col {
            p {
                margin-top: 8px;
                color: #808695;
            }
        }
    }
    .update-phone-captcha {
        button {
            border: none;
            padding: 0;
        }
    }
</style>
