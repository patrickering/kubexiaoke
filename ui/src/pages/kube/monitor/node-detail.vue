<!--
   监控
   author： xiaoke
   date： 2024-08-03 17:00:02
-->

<template>
    <div class="node-detail" v-if="node!=null">
        <el-descriptions :title="node.metadata.name" :column="3">
            <el-descriptions-item label="标签" :span="3">
                <Tag v-for="(label,labelKey) in node.metadata.labels" :key="labelKey" color="#1F8ECF"
                     v-text="labelKey+':'+label"></Tag>
            </el-descriptions-item>
            <el-descriptions-item label="注释" :span="3">
                <Tag v-for="(annotation,annotationKey) in node.metadata.annotations" :key="annotationKey"
                     color="#1F8ECF" v-text="annotationKey+':'+annotation"></Tag>
            </el-descriptions-item>

            <el-descriptions-item label="地址" :span="3">
                <Tag v-for="(address,addressKey) in node.status.addresses" :key="addressKey" color="#1F8ECF"
                     v-text="address.type+':'+address.address"></Tag>
            </el-descriptions-item>
            <el-descriptions-item label="污点" :span="3" v-if="node.spec.taints!=null && node.spec.taints.length>0">
                <Tag v-for="(taint,taintKey) in node.spec.taints" :key="taintKey" color="#1F8ECF"
                     v-text="taint.key+':'+taint.effect"></Tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
                <span v-text="$Date(node.metadata.creationTimestamp).format('YYYY-MM-DD HH:mm:ss')"></span>
            </el-descriptions-item>

            <el-descriptions-item label="容器组 CIDR">
                <span v-text="node.spec.podCIDR"></span>
            </el-descriptions-item>
            <el-descriptions-item label="无法调度">
                <span v-text="node.unschedulable?'是':'否'"></span>
            </el-descriptions-item>
        </el-descriptions>


        <el-descriptions title="系统信息" :column="2" style="margin-top: 30px">
            <el-descriptions-item label="机器 ID">
                <span v-text="node.status.nodeInfo.machineID"></span>
            </el-descriptions-item>
            <el-descriptions-item label="系统 UUID">
                <span v-text="node.status.nodeInfo.systemUUID"></span>
            </el-descriptions-item>
            <el-descriptions-item label="启动 ID">
                <span v-text="node.status.nodeInfo.bootID"></span>
            </el-descriptions-item>
            <el-descriptions-item label="内核版本">
                <span v-text="node.status.nodeInfo.kernelVersion"></span>
            </el-descriptions-item>
            <el-descriptions-item label="操作系统镜像">
                <span v-text="node.status.nodeInfo.osImage"></span>
            </el-descriptions-item>
            <el-descriptions-item label="容器运行时版本">
                <span v-text="node.status.nodeInfo.containerRuntimeVersion"></span>
            </el-descriptions-item>
            <el-descriptions-item label="Kubelet 版本">
                <span v-text="node.status.nodeInfo.kubeletVersion"></span>
            </el-descriptions-item>
            <el-descriptions-item label="Kube-Proxy 版本">
                <span v-text="node.status.nodeInfo.kubeProxyVersion"></span>
            </el-descriptions-item>
            <el-descriptions-item label="操作系统">
                <span v-text="node.status.nodeInfo.operatingSystem"></span>
            </el-descriptions-item>
            <el-descriptions-item label="架构">
                <span v-text="node.status.nodeInfo.architecture"></span>
            </el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 30px">
            <div class="el-descriptions__title" style="color: #303133">资源分配</div>
            <div style="margin-top: 30px">
                <Row>
                    <Col :span="8">
                        <div id="allocationCpu" style="width: 100%;height: 200px"></div>
                        <div style="width: 200px;margin: 0 auto">
                            <CellGroup>
                                <Cell title="请求" :extra="cpuRequest.toFixed(2)+'Core'">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #1F8ECF"></div>
                                    </div>
                                </Cell>
                                <Cell title="限制" :extra="cpuLimit.toFixed(2)+'Core'">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #D5D5D5"></div>
                                    </div>
                                </Cell>
                                <Cell title="总量" :extra="node.status.capacity.cpu.number+'Core'">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #FFFFFF"></div>
                                    </div>
                                </Cell>
                            </CellGroup>
                        </div>
                    </Col>
                    <Col :span="8">
                        <div id="allocationMemory" style="width: 100%;height: 200px"></div>
                        <div style="width: 200px;margin: 0 auto">
                            <CellGroup>
                                <Cell title="请求" :extra="(memoryRequest/1024/1024/1024).toFixed(2)+'Gi'">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #1F8ECF"></div>
                                    </div>
                                </Cell>
                                <Cell title="限制" :extra="(memoryLimit/1024/1024/1024).toFixed(2)+'Gi'">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #D5D5D5"></div>
                                    </div>
                                </Cell>
                                <Cell title="总量" :extra="(node.status.capacity.memory.number/1024/1024/1024).toFixed(2)+'Gi'">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #FFFFFF"></div>
                                    </div>
                                </Cell>
                            </CellGroup>
                        </div>
                    </Col>
                    <Col :span="8">
                        <div id="allocationPod" style="width: 100%;height: 200px"></div>
                        <div style="width: 200px;margin: 0 auto">
                            <CellGroup>
                                <Cell title="已分配" :extra="podList.length">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #1F8ECF"></div>
                                    </div>
                                </Cell>
                                <Cell title="总量" :extra="node.status.capacity.pods.number">
                                    <div slot="icon">
                                        <div style="width: 10px;height: 10px;background-color: #FFFFFF"></div>
                                    </div>
                                </Cell>
                            </CellGroup>
                        </div>
                    </Col>
                </Row>
            </div>
        </div>

        <Card ref="card" :bordered="false" :dis-hover="true" :padding="16" style="box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);border-radius: 14px;margin-bottom: 10px;margin-top: 30px">
            <div style="color: #202224;font-size: 20px;font-weight: 600;">Pod信息</div>
            <el-table
                    :data="podList"
                    :header-row-style="()=>{return 'color: #202224'}"
                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                    style="width: 100%;margin-top: 10px">
                <div slot="empty">
                    <el-empty></el-empty>
                </div>
                <el-table-column
                        prop="name"
                        label="名称">
                </el-table-column>
                <el-table-column
                        width="160"
                        prop="phase"
                        label="状态">
                </el-table-column>
                <el-table-column
                        width="100"
                        prop="restartCount"
                        label="重启次数">
                </el-table-column>
                <el-table-column
                        width="160"
                        align="center"
                        prop="createTime"
                        label="创建时间">
                    <template slot-scope="scope">
                        <span v-text="$Date(scope.row.createTime).format('YYYY-MM-DD HH:mm:ss')"></span>
                    </template>
                </el-table-column>
            </el-table>
        </Card>
    </div>
</template>

<script>
    import {getNode} from "@/api/kube/node";
    import {getPodList} from "@/api/kube/pod";
    import echarts from "echarts";
    import "echarts/theme/macarons.js";

    export default {
        name: 'node-detail',
        components: {},
        props: {
            nodeName: null
        },
        data() {
            return {
                node: null,
                cpuRequest:0,
                cpuLimit:0,
                memoryRequest:0,
                memoryLimit:0,
                podList:[],
            }
        },
        created() {
        },
        mounted() {
            this.getNode()
        },
        watch: {},
        computed: {},
        methods: {
            getNode() {
                getNode(this.nodeName).then(res => {
                    this.node = res.data
                    getPodList().then(podRes => {
                        console.log(podRes)
                        this.$nextTick(() => {
                            this.podList = podRes.data.filter(item => item.nodeName == res.data.metadata.name)
                            this.initAllocationCpu()
                            this.initAllocationMemory()
                            this.initAllocationPod()
                        })
                    })

                })
            },
            initAllocationCpu() {
                this.cpuRequest = 0;
                this.cpuLimit = 0;
                this.podList.forEach((obj) => {
                    this.cpuRequest += obj.cpuRequest
                    this.cpuLimit += obj.cpuLimit
                })
                let allocationCpuChart = echarts.init(
                    document.getElementById("allocationCpu")
                );
                allocationCpuChart.setOption({
                    title: {
                        text: "CPU分配",
                        subtext: "",
                        left: "center",
                        bottom: "bottom",
                        textStyle: {
                            fontSize: 18
                        },
                    },
                    backgroundColor: '#ffffff',
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}:({d}%)"
                    },
                    series: [{
                        name: '限制值',
                        type: 'pie',
                        radius: ['30%', '52%'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: [{
                            value: this.cpuLimit,
                            name: '已使用',
                            itemStyle: {
                                color: '#19be6b'
                            }
                        }, {
                            value: this.node.status.capacity.cpu.number - this.cpuLimit,
                            name: '未使用',
                            itemStyle: {
                                color: '#D5D5D5'
                            }
                        }],
                        hoverAnimation: false
                    }, {
                        name: '请求值',
                        type: 'pie',
                        radius: ['54%', '72%'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: [{
                            value: this.cpuRequest,
                            name: '已使用',
                            itemStyle: {
                                color: '#1F8ECF'
                            }
                        }, {
                            value: this.node.status.capacity.cpu.number - this.cpuRequest,
                            name: '未使用',
                            itemStyle: {
                                color: '#D5D5D5'
                            }
                        }],
                        hoverAnimation: false
                    }]
                })
            },
            initAllocationMemory() {
                this.memoryRequest = 0;
                this.memoryLimit = 0;
                this.podList.forEach((obj) => {
                    this.memoryRequest += obj.memoryRequest
                    this.memoryLimit += obj.memoryLimit
                })

                let allocationCpuChart = echarts.init(
                    document.getElementById("allocationMemory")
                );
                allocationCpuChart.setOption({
                    title: {
                        text: "内存分配",
                        subtext: "",
                        left: "center",
                        bottom: "bottom",
                        textStyle: {
                            fontSize: 18
                        },
                    },
                    backgroundColor: '#ffffff',
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}:({d}%)"
                    },
                    series: [{
                        name: '限制值',
                        type: 'pie',
                        radius: ['30%', '52%'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: [{
                            value: this.memoryLimit,
                            name: '已使用',
                            itemStyle: {
                                color: '#19be6b'
                            }
                        }, {
                            value: this.node.status.capacity.memory.number - this.memoryLimit,
                            name: '未使用',
                            itemStyle: {
                                color: '#D5D5D5'
                            }
                        }],
                        hoverAnimation: false
                    }, {
                        name: '请求值',
                        type: 'pie',
                        radius: ['54%', '72%'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: [{
                            value: this.memoryRequest,
                            name: '已使用',
                            itemStyle: {
                                color: '#1F8ECF'
                            }
                        }, {
                            value: this.node.status.capacity.memory.number - this.memoryRequest,
                            name: '未使用',
                            itemStyle: {
                                color: '#D5D5D5'
                            }
                        }],
                        hoverAnimation: false
                    }]
                })
            },
            initAllocationPod() {
                let allocationCpuChart = echarts.init(
                    document.getElementById("allocationPod")
                );
                allocationCpuChart.setOption({
                    title: {
                        text: "Pod分配",
                        subtext: "",
                        left: "center",
                        bottom: "bottom",
                        textStyle: {
                            fontSize: 18
                        },
                    },
                    backgroundColor: '#ffffff',
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}:({d}%)"
                    },
                    series: [ {
                        name: 'Pod',
                        type: 'pie',
                        radius: ['54%', '72%'],
                        color: ['#1F8ECF', '#D5D5D5'],
                        label: {
                            normal: {
                                show: false
                            }
                        },
                        data: [{
                            value: this.podList.length,
                            name: '已分配'
                        }, {
                            value: this.node.status.capacity.pods.number - this.podList.length,
                            name: '未分配'
                        }],
                        hoverAnimation: false
                    }]
                })
            },
        },
        destroyed() {

        },
    }
</script>

<style lang="scss">
    .node-detail .el-descriptions-item__content {
        display: block;
    }
</style>
