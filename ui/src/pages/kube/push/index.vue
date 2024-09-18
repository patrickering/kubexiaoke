<template>
    <div style="width: 1200px;margin: 0 auto;margin-top: 10px">
        <Card :bordered="false" dis-hover :padding="32"
              style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);" v-if="push!=null">
            <Tabs v-model="selectTabs" @on-click="changeTabs">
                <div slot="extra">是否开启推送：
                    <Switch v-model="push.open" @on-change="changeOpen"/>
                </div>
                <TabPane label="邮件" name="email">
                    <el-tabs v-model="selectChannelTabs" tab-position="left" @tab-click="changeTabPane"
                             v-if="push.open">
                        <el-tab-pane label="163邮箱" name="163">
                            <Form :model="form163" :label-width="100">
                                <FormItem label="是否开启：">
                                    <Switch v-model="form163.open"/>
                                </FormItem>
                                <FormItem label="权重：">
                                    <InputNumber :min="0" size="large" v-model="form163.weight"></InputNumber>
                                    <Alert style="margin-top: 5px" type="warning">权重越大优先级越高</Alert>
                                </FormItem>
                                <FormItem label="端口：">
                                    <Input v-model.trim="form163.params.port" size="large" placeholder="请输入端口"></Input>
                                </FormItem>
                                <FormItem label="邮箱地址：">
                                    <Input v-model.trim="form163.params.from" size="large"
                                           placeholder="请输入邮箱地址"></Input>
                                </FormItem>
                                <FormItem label="账号：">
                                    <Input v-model.trim="form163.params.user" size="large" placeholder="请输入账号"></Input>
                                </FormItem>
                                <FormItem label="密码：">
                                    <Input v-model.trim="form163.params.pass" size="large" placeholder="请输入密码"></Input>
                                </FormItem>
                                <FormItem>
                                    <Button type="primary" size="large" @click="updatePushChannel(form163)">保存</Button>
                                </FormItem>
                            </Form>
                        </el-tab-pane>
                        <el-tab-pane label="qq邮箱" name="qq">
                            <Form :model="formQQ" :label-width="100">
                                <FormItem label="是否开启：">
                                    <Switch v-model="formQQ.open"/>
                                </FormItem>
                                <FormItem label="权重：">
                                    <InputNumber :min="0" size="large" v-model="formQQ.weight"></InputNumber>
                                    <Alert style="margin-top: 5px" type="warning">权重越大优先级越高</Alert>
                                </FormItem>
                                <FormItem label="端口：">
                                    <Input v-model.trim="formQQ.params.port" size="large" placeholder="请输入端口"></Input>
                                </FormItem>
                                <FormItem label="邮箱地址：">
                                    <Input v-model.trim="formQQ.params.from" size="large" placeholder="请输入邮箱地址"></Input>
                                </FormItem>
                                <FormItem label="账号：">
                                    <Input v-model.trim="formQQ.params.user" size="large" placeholder="请输入账号"></Input>
                                </FormItem>
                                <FormItem label="密码：">
                                    <Input v-model.trim="formQQ.params.pass" size="large" placeholder="请输入密码"></Input>
                                </FormItem>
                                <FormItem>
                                    <Button type="primary" size="large" @click="updatePushChannel(formQQ)">保存</Button>
                                </FormItem>
                            </Form>
                        </el-tab-pane>
                    </el-tabs>
                    <avue-empty v-else image="/img/open.png" desc="在右侧开启后进行设置"></avue-empty>
                </TabPane>
                <!--<TabPane label="短信" name="sms">
                    <el-tabs tab-position="left" v-model="selectChannelTabs" v-if="push.open">
                        <el-tab-pane label="华为云" name="sms-huawei">
                            <Form :model="formSmsHuaWei" :label-width="140">
                                <FormItem label="是否开启：">
                                    <Switch v-model="formSmsHuaWei.open"/>
                                </FormItem>
                                <FormItem label="权重：">
                                    <InputNumber :min="0" v-model="formSmsHuaWei.weight"></InputNumber>
                                    <Alert style="margin-top: 5px" type="warning">权重越大优先级越高</Alert>
                                </FormItem>
                                <FormItem label="接入地址：">
                                    <Input v-model="formSmsHuaWei.params.url" placeholder="请输入接入地址"></Input>
                                    <Alert style="margin-top: 5px" type="warning">
                                        登录管理控制台，从国内短信“应用管理”或国际/港澳台短信“应用管理”页面获取
                                    </Alert>
                                </FormItem>
                                <FormItem label="appKey：">
                                    <Input v-model="formSmsHuaWei.params.appKey" placeholder="请输入appKey"></Input>
                                </FormItem>
                                <FormItem label="appSecret：">
                                    <Input v-model="formSmsHuaWei.params.appSecret" placeholder="请输入appSecret"></Input>
                                </FormItem>
                                <FormItem label="短信通道号：">
                                    <Input v-model="formSmsHuaWei.params.sender" placeholder="请输入通道号"></Input>
                                </FormItem>
                                <FormItem label="应用异常模板ID：">
                                    <Input v-model="formSmsHuaWei.params.appTemplateId"
                                           placeholder="请输入应用异常模板ID"></Input>
                                    <Alert style="margin-top: 5px" type="warning">
                                        模板格式
                                        <template slot="desc">
                                            集群：{1}，命名空间：{2}， 应用：{3}，发生异常，请及时处理
                                        </template>
                                    </Alert>
                                </FormItem>
                                <FormItem label="操作模板ID：">
                                    <Input v-model="formSmsHuaWei.params.handleTemplateId"
                                           placeholder="请输入操作模板ID"></Input>
                                    <Alert style="margin-top: 5px" type="warning">
                                        模板格式
                                        <template slot="desc">
                                            集群：{1}，命名空间：{2}，执行{3}操作
                                        </template>
                                    </Alert>
                                </FormItem>
                                <FormItem label="签名：">
                                    <Input v-model="formSmsHuaWei.params.signature" placeholder="请输入签名"></Input>
                                </FormItem>
                                <FormItem>
                                    <Button type="primary" @click="updatePushChannel(formSmsHuaWei)">保存</Button>
                                </FormItem>
                            </Form>
                        </el-tab-pane>
                        <el-tab-pane label="阿里云">配置管理</el-tab-pane>
                        <el-tab-pane label="腾讯云">配置管理</el-tab-pane>
                        <el-tab-pane label="飞鸽传书">配置管理</el-tab-pane>
                    </el-tabs>
                    <avue-empty v-else image="/img/open.png" desc="在右侧开启后进行设置"></avue-empty>
                </TabPane>
                <TabPane label="电话" name="phone">
                    <el-tabs tab-position="left" style="height: 200px;" v-if="push.open">
                        <el-tab-pane label="飞鸽传书">用户管理</el-tab-pane>
                    </el-tabs>
                    <avue-empty v-else image="/img/open.png" desc="在右侧开启后进行设置"></avue-empty>
                </TabPane>
                <TabPane label="微信" name="wx">
                    <el-tabs tab-position="left" style="height: 200px;" v-if="push.open">
                        <el-tab-pane label="模板消息">用户管理</el-tab-pane>
                    </el-tabs>
                    <avue-empty v-else image="/img/open.png" desc="在右侧开启后进行设置"></avue-empty>
                </TabPane>-->
            </Tabs>
        </Card>
    </div>
</template>
<script>
    import {typePush, updatePush} from '@api/kube/push';
    import {typePushChannel, updatePushChannel} from '@api/kube/pushChannel';
    import appUtils from "@/utils/appUtils";

    export default {
        name: 'system-user',
        components: {},
        data() {
            return {
                selectTabs: 'email',//推送类型
                push: {//推送实体
                    id: null,
                    open: null,
                    type: null
                },
                selectChannelTabs: null,//选择的渠道选项卡
                pushChannel: null,
                form163: {//163表单
                    id: null,
                    name: null,
                    open: false,
                    pushId: null,
                    type: null,
                    weight: null,
                    params: {
                        port: 25,
                        from: null,
                        user: null,
                        pass: null,
                    }
                },
                formQQ: {//qq表单
                    id: null,
                    name: null,
                    open: false,
                    pushId: null,
                    type: null,
                    weight: null,
                    params: {
                        port: 587,
                        from: null,
                        user: null,
                        pass: null
                    }
                },
                formSmsHuaWei: {//短信-华为
                    id: null,
                    name: null,
                    open: false,
                    pushId: null,
                    type: null,
                    weight: null,
                    params: {
                        url: null,
                        appKey: null,
                        appSecret: null,
                        sender: 'csms12345678',
                        appTemplateId: null,
                        handleTemplateId: null,
                        signature: null,
                    }
                }
            }
        },
        methods: {
            //根据类型获取推送
            typePush() {
                typePush(this.selectTabs).then(res => {
                    if (res.state) {
                        this.push = res.data
                        if (this.selectTabs == 'email') {
                            this.selectChannelTabs = '163'
                        } else if (this.selectTabs == 'sms') {
                            this.selectChannelTabs = 'sms-huawei'
                        }
                        this.typePushChannel(this.selectChannelTabs)
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            //开启状态变化
            changeOpen() {
                updatePush({id: this.push.id, open: this.push.open}).then(res => {
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                    } else {
                        appUtils.showError(res.msg)
                        this.push.open = !this.push.open
                    }
                }).catch(err => {
                    this.push.open = !this.push.open
                })
            },
            //切换类型
            changeTabs() {
                this.typePush()
            },
            //获取推送渠道
            typePushChannel(type) {
                typePushChannel(type, this.push.id).then(res => {
                    if (res.state) {
                        this.pushChannel = res.data
                        if (res.data.params != null) {
                            if (type == '163') {
                                this.form163 = res.data
                                this.form163.params = JSON.parse(res.data.params)
                            } else if (type == 'qq') {
                                this.formQQ = res.data
                                this.formQQ.params = JSON.parse(res.data.params)
                            } else if (type == 'sms-huawei') {
                                this.formSmsHuaWei = res.data
                                this.formSmsHuaWei.params = JSON.parse(res.data.params)
                            }
                        }
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            //渠道变化
            changeTabPane(tab, event) {
                this.typePushChannel(tab.name)
            },
            //修改推送渠道
            updatePushChannel(form) {
                let model = JSON.parse(JSON.stringify(form))
                model.params = JSON.stringify(model.params)
                updatePushChannel(model).then(res => {
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            }
        },
        mounted() {
            this.typePush()
        }
    }
</script>
