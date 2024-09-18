<!--
   应用域名
   author： xiaoke
   date： 2024-08-19 00:39:43
-->

<template>
    <div>
        <div style="text-align: right">
            <Button v-permission="{'value':'kube_appDomain_add'}" type="primary" size="large"
                    @click="handleEvent('add',null)" style="width: 140px">
                创建域名
            </Button>
        </div>
        <div v-loading="loading">
            <el-table
                    :data="tableData"
                    :header-row-style="()=>{return 'color: #202224'}"
                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                    style="width: 100%;margin-top: 10px">
                <div slot="empty">
                    <el-empty></el-empty>
                </div>
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <el-table
                                :data="props.row.appDomainRuleList"
                                border
                                style="width: 100%">
                            <el-table-column
                                    prop="app.name"
                                    label="应用"
                                    width="180">
                            </el-table-column>
                            <el-table-column
                                    prop="serviceSign"
                                    label="Service"
                                    width="160">
                            </el-table-column>
                            <el-table-column
                                    width="80"
                                    prop="port"
                                    label="端口">
                            </el-table-column>
                            <el-table-column
                                    prop="port.container"
                                    label="访问地址">
                                <template slot-scope="scope">
                                    <el-link type="primary"
                                             v-text="props.row.protocol+'://'+(props.row.domainPrefix=='@'?'':(props.row.domainPrefix+'.'))+props.row.domain.domain+scope.row.path"
                                             :href="props.row.protocol+'://'+(props.row.domainPrefix=='@'?'':(props.row.domainPrefix+'.'))+props.row.domain.domain+scope.row.path"
                                             target="_blank"></el-link>
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                </el-table-column>
                <el-table-column prop="protocol"
                                 :show-overflow-tooltip="true"
                                 label="域名">
                    <template slot-scope="scope">
                        <el-link type="primary"
                                 v-text="scope.row.protocol+'://'+(scope.row.domainPrefix=='@'?'':(scope.row.domainPrefix+'.'))+scope.row.domain.domain"
                                 :href="scope.row.protocol+'://'+(scope.row.domainPrefix=='@'?'':(scope.row.domainPrefix+'.'))+scope.row.domain.domain"
                                 target="_blank"></el-link>
                    </template>
                </el-table-column>
                <el-table-column prop="configVersion"
                                 width="200"
                                 :show-overflow-tooltip="true"
                                 label="证书">
                    <template slot-scope="scope">
            <span v-if="scope.row.configVersion!=null && scope.row.configVersion.config!=null"
                  v-text="scope.row.configVersion.config.name+'.v'+scope.row.configVersion.version"></span>
                        <span v-else>无</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createName"
                                 width="180"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="创建人">
                </el-table-column>
                <el-table-column prop="createTime"
                                 width="160"
                                 align="center"
                                 :show-overflow-tooltip="true"
                                 label="创建时间">
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                        width="150">
                    <template slot-scope="scope">
                        <el-button
                                v-permission="{'value':'kube_appDomain_edit'}"
                                type="text"
                                size="mini"
                                @click="handleEvent('edit',scope.row)">编辑
                        </el-button>
                        <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                            <el-button
                                    style="color: #F45236"
                                    v-permission="{'value':'kube_appDomain_del'}"
                                    type="text"
                                    size="mini"
                            >删除
                            </el-button>
                        </Poptip>
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
    import {
        pageAppDomain,
    } from '@/api/kube/appDomain'
    import appUtils from '@/utils/appUtils';
    import Setting from '@/setting';
    import dataUtils from '@/utils/dataUtils';
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
        data() {
            return {
                //列表数据
                tableData: []
                //数据总行数
                , count: 0
                ,loading:false
            }
        },
        computed: {},
        methods: {
            /**
             * 搜索数据
             */
            search() {
                this.query.current = 1
                this.getData()
            },

            /**
             * 获取数据
             */
            getData() {
                this.loading = true
                pageAppDomain(this.query).then(res => {
                    this.loading = false
                    if(res.state){
                        this.tableData = res.data.records
                        this.count = res.data.total
                    }else {
                        appUtils.showError(res.msg)
                    }
                }).catch(err => {
                    this.loading = false
                })
            },
            /**
             * 页数发生改变
             */
            pageChange(val) {
                this.query.current = val
                this.getData()
            },

            /**
             * 每页显示条数发生改变
             */
            sizeChange(val) {
                this.query.size = val
                this.query.current = 1
                this.getData()
            },
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(type, data) {
                this.$emit('on-events', {type: type, data: data});
            },
        },
        mounted() {
            this.getData()
        }
    }
</script>
