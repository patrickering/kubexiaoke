<template>
    <div class="setting-account-account">
        <h2>账号绑定</h2>
        <div class="ivu-mt ivu-pl ivu-pr">
            <Row type="flex" justify="center" align="middle">
                <Col span="22">
                    <h4>
                        <Avatar size="small" :src="icon.wechat" />
                        <span>微信</span>
                    </h4>
                    <p v-if="info.wxOpenid==null||info.wxOpenid==''">当前未绑定微信账号</p>
                    <p v-else>绑定微信: <span style="margin-left: 5px" v-text="info.wxNickname"></span></p>
                </Col>
                <Col span="2" class="ivu-text-right">
                    <el-button v-if="info.wxOpenid!=null && info.wxOpenid!=''" type="text" @click="relieveWxOpenid" :loading="relieveLoading">解除绑定</el-button>
                </Col>
            </Row>
            <Divider />
        </div>
    </div>
</template>
<script>
    import iconWechat from '@/assets/svg/icon-social-wechat.svg';
    import {mapState} from 'vuex';
    import {relieveWxOpenid} from '@/api/system/user'
    import appUtils from "@/utils/appUtils";
    import util from "@/libs/util";
    import dataUtils from "@/utils/dataUtils";
    import Setting from '@/setting';
    export default {
        computed: {
            ...mapState('admin/user', [
                'info'
            ]),
        },
        data () {
            return {
                icon: {
                    wechat: iconWechat,
                },
                relieveLoading:false//解绑密等
            }
        },
        methods:{
            relieveWxOpenid(){
                this.$confirm('确定解除绑定?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.relieveLoading = true
                    relieveWxOpenid().then(res=>{
                        if(res.state){
                            let user = JSON.parse(JSON.stringify(this.info))
                            user.wxOpenid = null
                            user.wxNickname = null
                            dataUtils.setData(Setting.key.userInfo, user)
                            this.$store.dispatch('admin/user/set', user);
                            this.$store.dispatch('admin/user/load');
                            appUtils.showSuccess(res.msg)
                        }else {
                            appUtils.showError(res.msg)
                        }
                        this.relieveLoading = false
                    }).catch(err => {
                        this.relieveLoading = false
                    })
                })

            }
        }

    }
</script>
<style lang="less">
    .setting-account-account{
        .ivu-col{
            h4{
                span{
                    margin-right: 8px;
                }
            }
            p{
                margin-top: 8px;
                color: #808695;
            }
        }
    }
</style>
