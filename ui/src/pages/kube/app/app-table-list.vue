<!--
   应用
   author： xiaoke
   date： 2024-08-03 15:34:01
-->

<template>
    <div>

        <div style="text-align: right">
            <Button v-permission="{'value':'kube_app_add'}" type="primary" size="large" style="width: 140px"
                    @click="handleAdd">
                创建应用
            </Button>
        </div>
        <div v-loading="loading">
            <el-table
                    :data="appList"
                    :header-row-style="()=>{return 'color: #202224'}"
                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                    style="width: 100%;margin-top: 10px">
                <div slot="empty">
                    <el-empty></el-empty>
                </div>
                <el-table-column
                        prop="date"
                        label="状态"
                        align="center"
                        width="90">
                    <template slot-scope="scope">
                        <Tag v-if="scope.row.stateDict!=null" style="width: 60px;border-radius: 28px" size="medium"
                             :color="scope.row.stateDict.description">{{scope.row.stateDict.label}}
                        </Tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="name"
                        :show-overflow-tooltip="true"
                        width="200"
                        label="应用名称">
                    <template slot-scope="scope">
                        <p v-text="scope.row.name"
                           style="cursor: pointer;color: #282D32;font-weight: 600;font-size: 16px"
                           @click="showDetails(scope.row)"></p>
                        <div style="font-size: 12px;color: #282D32;font-weight: 600;margin-top: 8px">
                            <span>标识：</span>
                            <span v-text="scope.row.sign"></span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="nowCount"
                        align="center"
                        width="100"
                        label="部署数量">
                    <template slot-scope="scope">
                        <p style="color: #202224" v-text="scope.row.nowCount+'/'+scope.row.count"></p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="nowCount"
                        width="250"
                        :show-overflow-tooltip="true"
                        label="访问地址">
                    <template slot-scope="scope">
                        <div v-if="scope.row.serviceList!=null && scope.row.serviceList.length>0"
                             style="color: #282D32;font-size: 16px">
                            <Poptip placement="top" trigger="hover" :transfer="true" width="400">
                                <div v-for="item in scope.row.serviceList[0].portList">
                                    <p v-text="scope.row.serviceList[0].sign+':'+item.container"></p>
                                    <el-link type="primary" v-if="item.node!=null" v-text="visitUrl+':'+item.node"
                                             :href="visitUrl+':'+item.node" target="_blank"></el-link>
                                </div>
                                <div slot="content">
                                    <List border>
                                        <ListItem v-for="scopes in scope.row.serviceList" style="display: block;">
                                            <template v-for="itemmore in scopes.portList">
                                                <div style="display: block">
                                                    <p v-text="scopes.sign+':'+itemmore.container"></p>
                                                    <el-link type="primary" v-if="itemmore.node!=null"
                                                             v-text="visitUrl+':'+itemmore.node"
                                                             :href="visitUrl+':'+itemmore.node"
                                                             target="_blank"></el-link>
                                                </div>
                                            </template>
                                        </ListItem>
                                    </List>
                                </div>
                            </Poptip>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="imageVersion"
                        align="left"
                        :show-overflow-tooltip="true"
                        label="镜像/版本">
                    <template slot-scope="scope">
                        <p style="color: #6B7075;font-size: 14px">镜像：<span style="color: #202224"
                                                                           v-text="scope.row.image"></span></p>
                        <p style="color:#6B7075;font-size: 14px">版本：<span style="color: #202224"
                                                                          v-text="scope.row.imageVersion"></span></p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createName"
                        align="left"
                        width="300"
                        label="资源配置">
                    <template slot-scope="scope">
                        <p>
                            <Tag style="width: 50px" color="#1F8ECF">内存</Tag>
                            <span v-text="'请求：'+scope.row.memoryRequest+scope.row.memoryRequestCompany"></span>
                            <Divider type="vertical"/>
                            <span v-text="'上限：'+scope.row.memoryLimit+scope.row.memoryLimitCompany"></span>
                        </p>
                        <p>
                            <Tag style="width: 50px" color="#1F8ECF">CPU</Tag>
                            <span v-text="'请求：'+scope.row.cpuRequest+'Core'"></span>
                            <Divider type="vertical"/>
                            <span v-text="'上限：'+scope.row.cpuLimit+'Core'"></span>
                        </p>
                    </template>
                </el-table-column>
                <el-table-column
                        width="180"
                        align="center"
                        label="操作">
                    <template slot-scope="scope">
                        <Button v-permission="{'value':'kube_app_edit'}" type="text" @click="handleUpdate(scope.row)">
                            更新
                        </Button>
                        <el-divider direction="vertical"></el-divider>
                        <Dropdown :transfer="true" @on-click="(name) => handleClickMore(name, scope.row)">
                            <Button type="text">更多
                                <Icon type="ios-arrow-down"/>
                            </Button>
                            <DropdownMenu slot="list">
                                <DropdownItem v-permission="{'value':'kube_app_stop'}" name="stop"
                                              v-if="scope.row.state!=30&&scope.row.state!=40">停止
                                </DropdownItem>
                                <DropdownItem v-permission="{'value':'kube_app_start'}" name="start"
                                              v-if="scope.row.state==40">启动
                                </DropdownItem>
                                <DropdownItem v-permission="{'value':'kube_app_restart'}" name="restart"
                                              v-if="scope.row.state==20">重启
                                </DropdownItem>
                                <DropdownItem v-permission="{'value':'kube_app_del'}" name="delete">删除</DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="display: flex;justify-content: flex-end;">
            <Page v-if="count>0" style="margin-top: 10px;margin-bottom: 10px" transfer @on-change="pageChange"
                  :page-size="10"
                  @on-page-size-change="sizeChange"
                  :current="query.current"
                  prev-text="上一页"
                  next-text="下一页" :total="count" show-elevator show-total show-sizer/>
        </div>
    </div>
</template>
<script>
    import {delApp, pageApp, restartApp, startApp, stopApp} from '@/api/kube/app'
    import appUtils from '@/utils/appUtils';
    import {mapState} from 'vuex';

    export default {

        props: {
            query: {
                type: Object
            },
            //扩展数据
            extend: {
                type: Object
            }
        },
        computed: {
            ...mapState('app/app', [
                //'appCount',
            ])
        },
        data() {
            return {
                grid: {
                    xl: 6,
                    lg: 12,
                    md: 12,
                    sm: 12,
                    xs: 24
                }
                , count: 0//总数
                , save_loading: false// 是否显示保存加载
                , dictList: [] /// 状态字典列表
                , namespaceInfo: null//命名空间
                , visitUrl: null//访问地址
                , restaurants: []
                , appList: []
                , loading: false
            }
        },
        methods: {
            //跳转详情
            showDetails(item) {
                this.$router.push('/kube/app/details/' + item.type + '/' + item.id);
            },
            //添加事件
            handleAdd() {
                this.$router.push('edit/' + this.query.type + '/add');
            },
            //更新事件
            handleUpdate(item) {
                this.$router.push('edit/' + this.query.type + '/' + item.id);
            },
            //删除应用
            handleClickMore(name, item) {
                if (name === 'stop') {
                    this.stop(item)
                } else if (name === 'start') {
                    this.start(item)
                } else if (name === 'restart') {
                    this.restart(item)
                } else if (name === 'delete') {
                    this.$Modal.confirm({
                        title: '删除应用',
                        content: '确定删除应用“' + item.name + '”吗？',
                        onOk: () => {
                            delApp(item.id).then(res => {
                                if (res.state) {
                                    appUtils.showSuccess(res.msg)
                                    this.getData()
                                } else {
                                    appUtils.showError(res.msg)
                                }
                            })
                        }
                    });
                }
            },
            search() {
                this.query.page = 1
                this.getData()
            },
            //获取数据
            getData(hideLoading) {
                if (!hideLoading) {
                    this.loading = true
                }
                pageApp(this.query).then(res => {
                    this.loading = false
                    if (res.state) {
                        this.appList = res.data.records
                        this.count = res.data.total
                    }
                }).catch(err => {
                    this.loading = false
                })
            },
            //应用状态统计
            getStateCount() {
                this.$store.dispatch('app/app/getStateCount', {type: this.query.type})
            },
            // 页数发生改变
            pageChange(val) {
                this.query.current = val
                this.getData()
            },
            // 每页显示条数发生改变
            sizeChange(val) {
                this.query.size = val
                this.query.current = 1
                this.getData()
            },
            //重启应用
            restart(item) {
                this.$Modal.confirm({
                    loading: true,
                    title: '重启应用',
                    content: '确定重启应用“' + item.name + '”吗？',
                    onOk: () => {
                        restartApp(item.id).then(res => {
                            this.$Modal.remove()
                            if (res.state) {
                                appUtils.showSuccess(res.msg)
                                this.getData(true)
                            } else {
                                appUtils.showError(res.msg)
                            }
                        }).catch(err => {
                            this.$Modal.remove()
                        })
                    }
                });
            },
            //停止应用
            stop(item) {
                this.$Modal.confirm({
                    loading: true,
                    title: '停止应用',
                    content: '确定停止应用“' + item.name + '”吗？',
                    onOk: () => {
                        stopApp(item.id).then(res => {
                            this.$Modal.remove()
                            appUtils.res(res, () => {
                                appUtils.showSuccess(res.msg)
                                this.getData(true)
                            })
                        }).catch(err => {
                            this.$Modal.remove()
                        })
                    }
                });
            },
            //启动应用
            start(item) {
                this.$Modal.confirm({
                    loading: true,
                    title: '启动应用',
                    content: '确定启动应用“' + item.name + '”吗？',
                    onOk: () => {
                        startApp(item.id).then(res => {
                            this.$Modal.remove()
                            appUtils.res(res, () => {
                                appUtils.showSuccess(res.msg)
                                this.getData(true)
                            })
                        }).catch(err => {
                            this.$Modal.remove()
                        })
                    }
                });
            },

        },
        mounted() {
            let that = this
            this.$EventBus.$on('app_state', (data) => {
                that.getData(true)
            })
            this.getData();
            this.getStateCount();
        },
        destroyed: function () {
            this.$EventBus.$off('app_state')
        },
    }
</script>
