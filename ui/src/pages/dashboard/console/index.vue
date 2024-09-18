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

        <div style="background: #FFFFFF;padding: 40px;border-radius: 16px;margin-top: 30px;">
            <Row>
                <Col span="8" style="border-right: 1px solid #E5E5E5">
                    <div style="width: 120px;margin: 0 auto">
                        <div style="display: flex;align-items: center;text-align: center;cursor: pointer"
                             @click="toPage('/kube/app/Deployment')">
                            <img src="@/assets/images/app-icon.png" width="24" height="24">
                            <div style="margin-left: 15px">无状态应用</div>
                        </div>
                        <div style="display: flex;align-items: center;text-align: center;margin-top: 40px;cursor: pointer"
                             @click="toPage('/kube/domain')">
                            <img src="@/assets/images/domain-icon.png" width="24" height="24">
                            <div style="margin-left: 15px">域名</div>
                        </div>
                    </div>
                </Col>
                <Col span="8" style="border-right: 1px solid #E5E5E5">
                    <div style="width: 120px;margin: 0 auto">
                        <div style="display: flex;align-items: center;text-align: center;cursor: pointer"
                             @click="toPage('/kube/config')">
                            <img src="@/assets/images/setting-icon.png" width="24" height="24">
                            <div style="margin-left: 15px">配置</div>
                        </div>
                        <div style="display: flex;align-items: center;text-align: center;margin-top: 40px;cursor: pointer"
                             @click="toPage('/kube/error-record')">
                            <img src="@/assets/images/error-icon.png" width="24" height="24">
                            <div style="margin-left: 15px">异常记录</div>
                        </div>
                    </div>

                </Col>
                <Col span="8">
                    <div style="width: 120px;margin: 0 auto">
                        <div style="display: flex;align-items: center;text-align: center;cursor: pointer"
                             @click="toPage('/kube/storage')">
                            <img src="@/assets/images/storage-icon.png" width="24" height="24">
                            <div style="margin-left: 15px">存储</div>
                        </div>
                        <div style="display: flex;align-items: center;text-align: center;margin-top: 40px;cursor: pointer"
                             @click="toPage('/kube/storage')">
                            <img src="@/assets/images/handle-icon.png" width="24" height="24">
                            <div style="margin-left: 15px">操作审计</div>
                        </div>
                    </div>

                </Col>
            </Row>
        </div>

        <div style="margin-top: 30px" v-if="namespace!=null">
            <Row :gutter="30">
                <Col span="8">
                    <div style="background: #FFFFFF;padding: 24px;border-radius: 16px">
                        <div style="color: #202224;font-weight: 600;font-size: 24px">CUP资源</div>
                        <div style="color: #909090;font-size: 14px;font-weight: 600;margin-top: 40px">逻辑资源请求 <span>（{{toDecimal(namespace.nowCpuRequest)}}/{{namespace.cpuRequest}} Core）</span>
                        </div>
                        <div style="margin-top: 5px">
                            <Progress style="width: 100%;" :stroke-color="['#3DC0A2', '#3DC0A2']" :stroke-width="4"
                                      :percent="percentage(namespace.nowCpuRequest,namespace.cpuRequest)"/>
                        </div>
                        <div style="color: #909090;font-size: 14px;font-weight: 600;margin-top: 40px">逻辑资源上限 <span>（{{toDecimal(namespace.nowCpuLimit)}}/{{namespace.cpuLimit}} Core）</span>
                        </div>
                        <div style="margin-top: 5px">
                            <Progress style="width: 100%;" :stroke-color="['#F78D7E', '#F78D7E']" :stroke-width="4"
                                      :percent="percentage(namespace.nowCpuLimit,namespace.cpuLimit)"/>
                        </div>
                    </div>
                </Col>
                <Col span="8">
                    <div style="background: #FFFFFF;padding: 24px;border-radius: 16px">
                        <div style="color: #202224;font-weight: 600;font-size: 24px">内存资源</div>
                        <div style="color: #909090;font-size: 14px;font-weight: 600;margin-top: 40px">逻辑资源请求 <span>（{{toDecimal(namespace.nowMemoryRequest)}}/{{namespace.memoryRequest}} GB）</span>
                        </div>
                        <div style="margin-top: 5px">
                            <Progress style="width: 100%;" :stroke-color="['#3DC0A2', '#3DC0A2']" :stroke-width="4"
                                      :percent="percentage(namespace.nowMemoryRequest,namespace.memoryRequest)"/>
                        </div>
                        <div style="color: #909090;font-size: 14px;font-weight: 600;margin-top: 40px">逻辑资源上限 <span>（{{toDecimal(namespace.nowMemoryLimit)}}/{{namespace.memoryLimit}} GB）</span>
                        </div>
                        <div style="margin-top: 5px">
                            <Progress style="width: 100%;" :stroke-color="['#F78D7E', '#F78D7E']" :stroke-width="4"
                                      :percent="percentage(namespace.nowMemoryLimit,namespace.memoryLimit)"/>
                        </div>
                    </div>
                </Col>
                <Col span="8">
                    <div style="background: #FFFFFF;padding: 24px;border-radius: 16px">
                        <div style="color: #202224;font-weight: 600;font-size: 24px">存储资源</div>
                        <div style="margin-top: 20px">
                            <el-table
                                    size="mini"
                                    height="158"
                                    :header-row-style="()=>{return 'color: #202224'}"
                                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                                    :data="storageList"
                                    style="width: 100%">
                                <div slot="empty">
                                    <el-empty></el-empty>
                                </div>
                                <el-table-column
                                        prop="name"
                                        label="名称"
                                        width="150">
                                </el-table-column>
                                <el-table-column
                                        prop="capacity"
                                        label="容量"
                                        width="180">
                                    <template slot-scope="scope">
                                        <span v-text="scope.row.capacity+'GB'"></span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="strategy"
                                        label="类型">
                                    <template slot-scope="scope">
                                        <span v-if="scope.row.strategy=='ReadWriteMany'">多节点读写</span>
                                        <span v-if="scope.row.strategy=='ReadOnlyMany'">多节点只读</span>
                                        <span v-if="scope.row.strategy=='ReadWriteOnce'">单节点读写</span>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </Col>
            </Row>
        </div>

        <Card v-else  ref="card" :bordered="false"  :dis-hover="true"  :padding="16" style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px;margin-top: 30px">
            <el-skeleton :loading="namespace==null" :count="2" animated/>
        </Card>
    </div>
</template>
<script>
    import {stateCount} from '@/api/kube/app'
    import dataUtils from "../../../utils/dataUtils";
    import {getNamespace} from '@/api/kube/namespace'
    import Setting from "../../../setting";
    import appUtils from "../../../utils/appUtils";
    import {listStorage} from '@/api/kube/storage'

    export default {
        name: 'dashboard-monitor',
        computed: {},
        data() {
            return {
                appStateCount: {all: 0, run: 0, error: 0, stop: 0}
                //命名空间
                , namespace: null
                //存储
                , storageList: null
            }
        },
        methods: {
            //计算百分比
            percentage(args, args1) {
                if (args == null || args1 == null || args1 == 0) {
                    return 0;
                }
                return Math.floor((args / args1) * 100)
            },
            toDecimal(x) {
                var f = parseFloat(x);
                if (isNaN(f)) {
                    return;
                }
                f = Math.round(x * 100) / 100;
                return f;
            },
            //应用状态统计
            getStateCount() {
                stateCount().then(res => {
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
            //跳转页面
            toPage(path) {
                this.$router.push(path)
            },
            //获取命名空间信息
            getNamespace() {
                let namespace = dataUtils.getData(Setting.key.namespaceInfo)
                getNamespace(namespace.id).then(res => {
                    appUtils.res(res, () => {
                        this.namespace = res.data
                    })
                })
            },
            //获取存储
            getStorage() {
                listStorage().then(res => {
                    appUtils.res(res, () => {
                        this.storageList = res.data
                    })
                })
            },
        },
        mounted() {
            this.getStateCount()
            this.getNamespace()
            this.getStorage()
            this.$EventBus.$on('app_count', (data) => {
                this.getStateCount()
            })
        },
        destroyed: function () {
            this.$EventBus.$off('app_count')
        },
    }
</script>
