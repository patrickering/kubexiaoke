<!--
   应用
   author： xiaoke
   date： 2024-08-03 15:34:02
-->

<template>
    <div>
        <Row :gutter="30">
            <Col span="6">
                <div style="background: #FFFFFF;padding: 16px;border-radius: 16px;position: relative">
                    <div style="font-size: 16px;font-weight: 500;color: #202224">应用总数</div>
                    <div style="color: #202224;font-size: 28px;font-weight: 700;margin-top: 16px"
                         v-text="appStateCount.all"></div>
                    <img src="@/assets/images/app-count.png" width="60" height="60"
                         style="position: absolute;top: 16px;right: 16px">
                </div>
            </Col>
            <Col span="6">
                <div style="background: #FFFFFF;padding: 16px;border-radius: 16px;position: relative">
                    <div style="font-size: 16px;font-weight: 500;color: #202224">运行中</div>
                    <div style="color: #202224;font-size: 28px;font-weight: 700;margin-top: 16px"
                         v-text="appStateCount.run"></div>
                    <img src="@/assets/images/app-run.png" width="60" height="60"
                         style="position: absolute;top: 16px;right: 16px">
                </div>
            </Col>
            <Col span="6">
                <div style="background: #FFFFFF;padding: 16px;border-radius: 16px;position: relative">
                    <div style="font-size: 16px;font-weight: 500;color: #202224">异常</div>
                    <div style="color: #202224;font-size: 28px;font-weight: 700;margin-top: 16px"
                         v-text="appStateCount.error"></div>
                    <img src="@/assets/images/app-error.png" width="60" height="60"
                         style="position: absolute;top: 16px;right: 16px">
                </div>
            </Col>
            <Col span="6">
                <div style="background: #FFFFFF;padding: 16px;border-radius: 16px;position: relative">
                    <div style="font-size: 16px;font-weight: 500;color: #202224">已停止</div>
                    <div style="color: #202224;font-size: 28px;font-weight: 700;margin-top: 16px"
                         v-text="appStateCount.stop"></div>
                    <img src="@/assets/images/app-stop.png" width="60" height="60"
                         style="position: absolute;top: 16px;right: 16px">
                </div>
            </Col>
        </Row>
        <Card ref="card" :bordered="false" :dis-hover="true" :padding="32" style="margin-top: 30px;border-radius: 8px">
            <Row>
                <Col :span="14">
                    <div style="color: #202224;font-size: 24px;font-weight: 600">应用列表</div>
                </Col>
                <Col :span="10" style="text-align: right">
                    <app-table-search v-model="appQuery" :extend="appExtend" @on-submit="appSearch"
                                      @on-reset="appSearch"></app-table-search>
                </Col>
            </Row>
            <div style="margin-top: 10px">
                <app-table-list ref="appTable" :extend="appExtend" :query="appQuery"
                                @on-events="appHandleEvent"></app-table-list>
            </div>
        </Card>
    </div>
</template>

<script>
    import appTableSearch from './app-table-search';
    import appTableList from './app-table-list';

    import {remote} from '@/api/system/dict'
    import {stateCount} from '@/api/kube/app'
    import dataUtils from '@/utils/dataUtils'

    export default {
        components: {
            appTableSearch,
            appTableList,
        },
        props: {
            type: null
        },
        computed: {},
        data() {
            return {
                // 应用查询条件
                appQuery: {
                    name: null,
                    type: null,
                    state: null,
                    size: 10,
                    current: 1
                },

                //应用扩展数据（用于字典类数据）
                appExtend: {
                    appStateList: [],
                    // 是否打开环境变量配置文件
                    openEnvConfigList: [
                        {label: "是", value: "1"},
                        {label: "否", value: "0"},
                    ],
                    // 是否打开自定义环境变量
                    openCustomEnvList: [
                        {label: "是", value: "1"},
                        {label: "否", value: "0"},
                    ],
                    // 是否添加配置文件
                    openConfigList: [
                        {label: "是", value: "1"},
                        {label: "否", value: "0"},
                    ],
                    // 是否启用健康检查
                    openHealthList: [
                        {label: "是", value: "1"},
                        {label: "否", value: "0"},
                    ],
                },
                appStateCount: {all: 0, run: 0, error: 0, stop: 0}
            }
        },
        created() {
        },
        mounted() {
            let that = this
            this.appQuery.type = this.type
            this.getAppState()
            this.getStateCount()
            this.$EventBus.$on('app_count', (data) => {
                that.getStateCount()
            })
        },
        methods: {
            /**
             * 应用搜索
             */
            appSearch() {
                this.$refs.appTable.search()
            },
            getAppState() {
                remote('app_state').then(res => {
                    if (res.state) {
                        this.appExtend.appStateList = res.data
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            /**
             * 应用事件
             * @param type 应用事件类型
             * @param data 应用事件数据
             */
            appHandleEvent(obj) {
                this.$refs.appEdit.handleEvent(obj.type, obj.data)
            },
            //应用状态统计
            getStateCount() {
                stateCount({type: this.type}).then(res => {
                    if (res.state) {
                        this.appStateCount.all = 0
                        this.appStateCount.run = 0
                        this.appStateCount.error = 0
                        this.appStateCount.stop = 0
                        dataUtils.each(res.data, (key, obj) => {
                            this.appStateCount.all += obj.count
                            if (obj.state == 20) {
                                this.appStateCount.run += obj.count
                            } else if (obj.state == 60) {
                                this.appStateCount.error = obj.count
                            } else if (obj.state == 40) {
                                this.appStateCount.stop = obj.count
                            }
                        })
                    }
                })
            },
        },
        destroyed: function () {
            this.$EventBus.$off('app_count')
        },

    }
</script>

<style lang="scss" scoped>
</style>
