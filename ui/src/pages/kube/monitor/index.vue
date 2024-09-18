<!--
   监控
   author： xiaoke
   date： 2024-08-03 17:00:02
-->

<template>
    <div class="monitor" style="width: 1220px;margin: 0 auto;margin-top: 20px; padding: 20px">
        <div>
            <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                  style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                <div style="color: #202224;font-size: 24px;font-weight: 600">集群网络IO</div>
                <div v-if="!ioEmpty" id="ioChart" style="height: 300px"></div>
                <el-empty v-if="ioEmpty" description="暂无数据"></el-empty>
            </Card>
        </div>
        <div style="margin-top: 30px">
            <Row :gutter="28">
                <Col :span="12">
                    <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                          style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                        <div style="color: #202224;font-size: 24px;font-weight: 600">集群内存</div>
                        <div id="clusterMemoryChart" style="height: 300px"></div>
                        <Row v-if="clusterMemory!=null">
                            <Col :span="12" style="text-align: center">
                                <div style="color:#000000;font-size: 24px;font-weight: 600">
                                    <span v-text="clusterMemory.used.toFixed(2)"></span>
                                    <span>GB</span>
                                </div>
                                <div style="color: #A3A3A3;font-size: 18px;margin-top: 10px">已使用</div>
                            </Col>
                            <Col :span="12" style="text-align: center;border-left: 1px solid #E0E0E0">
                                <div style="color:#000000;font-size: 24px;font-weight: 600">
                                    <span v-text="clusterMemory.total.toFixed(2)"></span>
                                    <span>GB</span>
                                </div>
                                <div style="color: #A3A3A3;font-size: 18px;margin-top: 10px">总量</div>
                            </Col>
                        </Row>
                    </Card>
                </Col>
                <Col :span="12">
                    <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                          style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                        <div style="color: #202224;font-size: 24px;font-weight: 600">集群CPU</div>
                        <div id="clusterCpuChart" style="height: 300px"></div>
                        <Row v-if="clusterCpu!=null">
                            <Col :span="12" style="text-align: center">
                                <div style="color:#000000;font-size: 24px;font-weight: 600">
                                    <span v-text="clusterCpu.used.toFixed(2)"></span>
                                    <span>Cores</span>
                                </div>
                                <div style="color: #A3A3A3;font-size: 18px;margin-top: 10px">已使用</div>
                            </Col>
                            <Col :span="12" style="text-align: center;border-left: 1px solid #E0E0E0">
                                <div style="color:#000000;font-size: 24px;font-weight: 600">
                                    <span v-text="clusterCpu.total.toFixed(2)"></span>
                                    <span>Cores</span>
                                </div>
                                <div style="color: #A3A3A3;font-size: 18px;margin-top: 10px">已使用</div>
                            </Col>
                        </Row>
                    </Card>
                </Col>
            </Row>


            <div style="margin-top: 30px">
                <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                      style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px;margin-bottom: 10px">
                    <div style="color: #202224;font-size: 20px;font-weight: 600;">节点信息</div>
                    <Card v-for="(item,index) in nodeList" :key="index" ref="card" :bordered="false" :dis-hover="true"
                          :padding="16" style="margin-bottom: 10px;border-bottom: 1px solid #F2F2F2">
                        <Row :gutter="24">
                            <Col span="12">
                                <div style="display: flex;align-items: center;cursor: pointer"
                                     @click="item.open = !item.open">
                                    <Tag v-for="conditionsItem in item.status.conditions"
                                         v-if="conditionsItem.reason=='KubeletReady'"
                                         :color="conditionsItem.status=='True'?'success':'error'"
                                         v-text="conditionsItem.status=='True'?'正常':'异常'"></Tag>
                                    <div style="color: #202224;font-size: 22px;font-weight: 600;margin-left: 10px"
                                         v-text="item.metadata.name"></div>
                                    <span style="color: #202224;font-size: 22px;font-weight: 600;margin-left: 10px"
                                          v-for="ipItem in item.status.addresses" v-if="ipItem.type=='InternalIP'"
                                          v-text="'('+ipItem.address+')'" InternalIP></span>
                                </div>
                            </Col>
                            <Col span="12" style="text-align: right">
                                <div style="display: flex;align-items: center;justify-content: right;cursor: pointer">
                                    <Button type="primary" ghost
                                            style="width: 100px;margin-right: 10px" @click="getNode(item)">节点详情
                                    </Button>
                                    <Icon v-if="!item.open" type="ios-arrow-forward"
                                          @click="item.open = !item.open"
                                          style="font-size: 26px;font-weight: 600;cursor: pointer"/>
                                    <Icon v-else type="ios-arrow-down"
                                          @click="item.open = !item.open"
                                          style="font-size: 26px;font-weight: 600;cursor: pointer"/>
                                </div>
                            </Col>
                        </Row>

                        <div style="margin-top: 30px" v-if="item.open">
                            <Row :gutter="24">
                                <Col :span="6">
                                    <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                                          style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                                        <div style="color: #202224;font-size: 28px;font-weight: 600">
                                            {{item.status.capacity.cpu.number}}Core
                                        </div>
                                        <div style="color: #828282;font-size: 18px">CUP</div>
                                    </Card>
                                </Col>
                                <Col :span="6">
                                    <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                                          style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px;">
                                        <div style="color: #202224;font-size: 28px;font-weight: 600">
                                            {{(item.status.capacity.memory.number/1024/1024/1024).toFixed(2)}}Gi
                                        </div>
                                        <div style="color: #828282;font-size: 18px">内存</div>
                                    </Card>
                                </Col>
                                <Col :span="6">
                                    <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                                          style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                                        <div style="color: #202224;font-size: 28px;font-weight: 600"
                                             v-text="item.status.nodeInfo.kubeletVersion"></div>
                                        <div style="color: #828282;font-size: 18px">Kubelet 版本</div>
                                    </Card>
                                </Col>
                                <Col :span="6">
                                    <Card ref="card" :bordered="false" :dis-hover="true" :padding="16"
                                          style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px">
                                        <div style="color: #202224;font-size: 28px;font-weight: 600"
                                             v-text="item.status.nodeInfo.containerRuntimeVersion"></div>
                                        <div style="color: #828282;font-size: 18px">Docker版本</div>
                                    </Card>
                                </Col>
                            </Row>
                            <!--<Card ref="card" :bordered="false" :dis-hover="true" :padding="16" style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px;margin-top: 30px">
                                <div style="color:#202224;font-size: 20px;font-weight: 600;">镜像</div>
                                <el-table
                                        :show-header="false"
                                        :height="170"
                                        size="mini"
                                        :data="item.status.images"
                                        style="width: 100%">
                                    <el-table-column
                                            prop="date"
                                            label="镜像">
                                        <template slot-scope="scope">
                                            <span v-text="scope.row.names[1]"></span>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </Card>-->
                        </div>
                    </Card>
                </Card>
            </div>
        </div>
        <Modal
                v-model="showNodeDetail"
                footer-hide
                title="节点详情"
                width="1000">
            <node-detail v-if="showNodeDetail" :node-name="nodeName"></node-detail>
        </Modal>

    </div>
</template>

<script>
    import {getNodeList} from "@/api/kube/node";
    import {getClusterCpu, getClusterMemory, getNetworkIo,} from "@/api/kube/monitor";
    import echarts from "echarts";
    import "echarts/theme/macarons.js";
    import nodeDetail from './node-detail'

    export default {
        components: {
            nodeDetail
        },
        data() {
            return {
                loading: false,
                ioLoading: false,
                current: 1,
                ioChart: null,
                clusterMemoryChart: null,
                clusterCpuChart: null,
                ioEmpty: false,
                initChartTimer: null,
                clusterMemory: null,
                clusterCpu: null,
                nodeList: [],
                showNodeDetail: false,
                nodeName: null
            }
        },
        created() {
        },
        mounted() {
            this.initChart();
            this.getNodeList();
        },
        watch: {},
        computed: {},
        methods: {
            tabLabel(h, project) {
                const top = h('div', {
                    style: {
                        position: 'absolute',
                        top: '10px',
                        left: '20px'
                    }
                })

                const labelName = h('span', project.name.substring(0, 1) || '未命名');
                const label = h('div', {
                    class: 'monitor-label',
                    style: {
                        background: this.hexToRgb('#1F8ECF'),
                        color: project.color
                    }
                }, [labelName])

                const titleName = h('span', project.name || '未命名');
                const title = h('div', {
                    class: 'monitor-title',
                    style: {}
                }, [titleName])

                const createTime = h('span', '标识：' + project.sign);
                const create = h('div', {
                    class: 'monitor-create',
                    style: {}
                }, [createTime])
                return h('div', {
                    class: 'monitor-box',
                    style: {
                        border: this.current == project.id ? '1px solid #1F8ECF' : '1px solid #FFFFFF'
                    }
                }, [top, label, title, create]);
            },
            hexToRgb(hex) {
                // 移除十六进制颜色代码中的'#'
                let sanitizedHex = hex.replace("#", "");

                // 解析红、绿、蓝值
                let r = parseInt(sanitizedHex.substring(0, 2), 16);
                let g = parseInt(sanitizedHex.substring(2, 4), 16);
                let b = parseInt(sanitizedHex.substring(4, 6), 16);

                return `rgb(${r}, ${g}, ${b},0.1)`;
            },
            //初始化图表
            initChart() {
                this.initIoChart();
                this.initClusterMemoryChart();
                this.initClusterCpuChart();
                if (this.initChartTimer != null) {
                    clearInterval(this.initChartTimer);
                    this.initChartTimer = null;
                }
                this.initChartTimer = setInterval(() => {
                    this.initIoChart();
                    this.initClusterMemoryChart();
                    this.initClusterCpuChart();
                }, 5000);
            },
            initIoChart() {
                getNetworkIo().then((res) => {
                    if (res.data == null) {
                        if (this.ioChart != null) {
                            this.ioChart.dispose();
                        }
                        this.ioEmpty = true
                        return;
                    }
                    this.ioEmpty = false
                    this.$nextTick(() => {
                        this.ioChart = echarts.init(
                            document.getElementById("ioChart"),
                            "macarons"
                        );
                        this.ioChart.setOption({
                            tooltip: {
                                trigger: "axis",
                            },
                            legend: {
                                data: ["接收", "发送"],
                            },
                            grid: {
                                left: "3%",
                                right: "4%",
                                bottom: "3%",
                                containLabel: true,
                            },
                            xAxis: {
                                type: "category",
                                boundaryGap: false,
                                data: res.data.timeList,
                            },
                            yAxis: {
                                type: "value",
                                axisLabel: {
                                    formatter: "{value} MB",
                                },
                            },
                            series: [
                                {
                                    name: "接收",
                                    type: "line",
                                    stack: "总量",
                                    data: res.data.value.received,
                                },
                                {
                                    name: "发送",
                                    type: "line",
                                    stack: "总量",
                                    data: res.data.value.sent,
                                },
                            ],
                        });
                    })
                });
            },
            //集群内存
            initClusterMemoryChart() {
                getClusterMemory().then((res) => {
                    this.clusterMemory = res.data;
                    if (res.data == null) {
                        if (this.clusterMemoryChart != null) {
                            this.clusterMemoryChart.dispose();
                        }
                        return;
                    }
                    this.clusterMemoryChart = echarts.init(
                        document.getElementById("clusterMemoryChart"),
                        "macarons"
                    );
                    let value = (res.data.used / res.data.total) * 100;

                    this.clusterMemoryChart.setOption({
                        tooltip: {
                            formatter: "{a} <br/>{b} : {c}%",
                        },
                        series: [
                            {
                                name: '内存',
                                type: "gauge",
                                detail: {
                                    formatter: "{value}%\n使用率",
                                    valueAnimation: true,
                                    fontSize: 22,
                                    color: '#1F8ECF',
                                    offsetCenter: [0, '60%']
                                },
                                data: [{value: value.toFixed(0), name: "使用率"}],
                            },
                        ],
                    });
                });
            },
            //集群CPU
            initClusterCpuChart() {
                getClusterCpu().then((res) => {
                    this.clusterCpu = res.data;
                    if (res.data == null) {
                        if (this.clusterCpuChart != null) {
                            this.clusterCpuChart.dispose();
                        }
                        return;
                    }
                    this.clusterCpuChart = echarts.init(
                        document.getElementById("clusterCpuChart"),
                        "macarons"
                    );
                    let value = (res.data.used / res.data.total) * 100;

                    this.clusterCpuChart.setOption({
                        tooltip: {
                            formatter: "{a} <br/>{b} : {c}%",
                        },
                        series: [
                            {
                                name: 'CPU',
                                type: "gauge",
                                detail: {
                                    formatter: "{value}%\n使用率",
                                    valueAnimation: true,
                                    fontSize: 22,
                                    color: '#1F8ECF',
                                    offsetCenter: [0, '60%']
                                },
                                data: [{value: value.toFixed(0), name: "使用率"}],
                            },
                        ],
                    });
                });
            },
            //获取节点
            getNodeList() {
                getNodeList().then((res) => {
                    if (res.state && res.data != null) {
                        res.data.forEach((obj, index) => {
                            res.data[index].open = true
                        })
                    }
                    this.nodeList = res.data;
                });
            },
            getNode(item) {
                this.nodeName = item.metadata.name
                this.showNodeDetail = true
            }
        },
        destroyed() {
            if (this.initChartTimer != null) {
                clearInterval(this.initChartTimer);
                this.initChartTimer = null;
            }
        },
    }
</script>

<style lang="scss">
    .monitor-box {
        margin-top: 10px;
        width: 262px;
        height: 95px;
        border-radius: 8px;
        background-color: #FFFFFF;
        margin-right: 20px;
        position: relative;
        box-shadow: 6px 6px 54px 0px rgba(0, 0, 0, 0.05);
    }

    .monitor-label {
        position: absolute;
        top: 10px;
        right: 10px;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        font-size: 26px;
        font-weight: 600;
        border-radius: 20px;
    }

    .monitor-title {
        position: absolute;
        top: 15px;
        left: 20px;
        color: #202224;
        font-size: 22px;
        font-weight: 600;
    }

    .monitor-create {
        position: absolute;
        top: 55px;
        left: 20px;
        color: #ACACAC;
        font-size: 14px;
    }


    .monitor .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-nav-container {
        height: 115px;
    }

    .monitor .i-layout-tabs {
        background: none;
    }

    .monitor .i-layout-tabs-main {
        background: none;
        margin: 0px !important;
    }

    .monitor .i-layout-tabs .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab {
        height: 115px;
        background: none;
    }

    .monitor .ivu-tabs-nav-prev {
        top: 30px;
        z-index: 99;
        left: 0px;
    }

    .monitor .ivu-tabs-nav-prev .ivu-icon {
        font-size: 36px !important;
        background-color: #FFFFFF;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        border-radius: 50%;
    }

    .monitor .ivu-tabs-nav-next {
        top: 30px;
        z-index: 99;
        right: 5px;
    }

    .monitor .ivu-tabs-nav-next .ivu-icon {
        font-size: 36px !important;
        background-color: #FFFFFF;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        border-radius: 50%;
    }

    .monitor .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab {
        padding: 0px 0px 0px;
    }

    .monitor .i-layout-tabs .ivu-tabs-nav-scrollable {
        padding: 0 0px !important;
    }
</style>
