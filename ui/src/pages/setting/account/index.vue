<template>
    <div style="width: 1200px;margin: 0 auto;margin-top: 10px">
        <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
            <Row :gutter="24">
                <Col :xl="6" :lg="6" :md="0" :sm="0" :xs="0">
                    <Card shadow title="个人设置" icon="md-options" :padding="0">
                        <CellGroup @on-click="handleChangeCell" class="ivu-pt-8 ivu-pb-8">
                            <Cell title="基本设置" name="info" label="个人账户信息设置" :selected="currentType === 'info'" />
                            <Cell title="安全设置" name="safety" label="密码、邮箱等设置" :selected="currentType === 'safety'" />
                            <Cell title="账号绑定" name="account" label="绑定第三方社交账户" :selected="currentType === 'account'" />
                        </CellGroup>
                    </Card>
                </Col>
                <Col :xl="18" :lg="18" :md="24" :sm="24" :xs="24">
                    <Card :bordered="false" dis-hover>
                        <Row>
                            <Col :xl="0" :lg="0" :md="24" :sm="24" :xs="24">
                                <Tabs :value="currentType" @on-click="handleChangeCell">
                                    <TabPane label="基本设置" name="info" />
                                    <TabPane label="安全设置" name="safety" />
                                    <TabPane label="账号绑定" name="account" />
                                </Tabs>
                            </Col>
                        </Row>
                        <info v-if="currentType === 'info'" />
                        <safety v-if="currentType === 'safety'" />
                        <account v-if="currentType === 'account'" />
                    </Card>
                </Col>
            </Row>
        </Card>

    </div>
</template>
<script>
    import info from './info';
    import safety from './safety';
    import account from './account';

    export default {
        name: 'setting-account',
        components: { info, safety, account },
        data () {
            return {
                currentType: 'info' // info | safety | account
            }
        },
        methods: {
            handleChangeCell (name) {
                this.currentType = name;
            }
        }
    }
</script>
