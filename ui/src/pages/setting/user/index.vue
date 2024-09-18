<template>
    <div>
        <Row :gutter="24">
            <Col :xl="7" :lg="7" :md="24" :sm="24" :xs="24" class="ivu-mb">
                <Card :bordered="false" dis-hover>
                    <div class="ivu-text-center">
                        <Avatar :src="info.avatar" class="setting-user-avatar" />
                        <p class="ivu-m-8">
                            <strong style="font-size: 20px">{{ info.name }}</strong>
                        </p>
                        <p>{{ info.nickname }}</p>
                    </div>
                    <Divider dashed />
                </Card>
            </Col>
            <Col :xl=17 :lg="17" :md="24" :sm="24" :xs="24">
                <Card :bordered="false" dis-hover :padding="0">
                    <div class="ivu-pt-8">
                        <Tabs v-model="currentTab">
                            <TabPane label="消息" name="message" />
                        </Tabs>
                    </div>
                    <div class="ivu-pl ivu-pr ivu-pb">
                        <div v-if="currentTab === 'message'" >
                            <el-empty description="暂无数据"></el-empty>
                        </div>
                    </div>
                </Card>
            </Col>
        </Row>
    </div>
</template>
<script>
    import {mapState} from 'vuex';
    export default {
        name: 'setting-user',
        components: {},
        computed: {
            ...mapState('admin/user', [
                'info'
            ])
        },
        data () {
            return {
                currentTab: 'message',
            }
        },
        methods: {

        }
    }
</script>
<style lang="less">
    .setting-user{
        &-avatar{
            width: 72px;
            height: 72px;
            border-radius: 50%;
        }
    }
</style>
