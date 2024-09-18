<template>
    <div class="login-content login1">
        <div class="login-info">
            <img src="@/assets/images/logo_head.png" class="login-logo" alt/>
            <username style="margin-top: 63px"></username>
        </div>
        <p class="con-statement">
            <span>小可容器云</span>
        </p>
    </div>
</template>
<script>
    import mixins from "../mixins";
    import username from "./username";

    export default {
        mixins: [mixins],
        components: {username},
        data() {
            return {
                loginType: 'username'
            };
        },
        methods: {},
        created() {
        }
    };
</script>
<style lang="scss">
    .ivu-login-submit button {
        background-color: rgb(10, 91, 228);
        border-color: rgb(10, 91, 228);
        color: #fff;
    }

    .login1 {
        position: relative;
        width: 100%;
        height: 100%;
        background: url("../../../assets/images/background.png") no-repeat top;
        background-size: cover;

        .ivu-login {
            .showlabel {
                display: none;
            }

            .ivu-input-large {
                height: 54px;
                line-height: 54px;
                background: transparent;
                border-radius: 10px;
            }

            .ivu-input-prefix,
            .ivu-input-suffix {
                width: 64px;
            }

            .ivu-input-prefix i,
            .ivu-input-suffix i {
                font-size: 24px;
                line-height: 54px;
            }

            .ivu-input-with-prefix {
                padding-left: 64px;
            }

            .ivu-btn-large {
                height: 54px;
                border-radius: 10px;
            }

            .ivu-login-submit {
                font-size: 18px;
                margin-top: 62px;

                span {
                    letter-spacing: 10px;
                }
            }
        }

        .login-info {
            width: 521px;
            background: #ffffff;
            border-radius: 8px;
            padding: 70px 60px;
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            right: 8%;

            .login-logo {
                width: 50%;
                display: block;
            }
        }

        .con-statement {
            font-size: 14px;
            color: #fff;
            position: fixed;
            bottom: 12px;
            left: 50%;
            transform: translateX(-50%);

            span {
                &:first-child {
                    margin-right: 20px;
                }
            }
        }
    }
</style>
