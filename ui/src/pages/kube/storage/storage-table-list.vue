<!--
   存储
   author： xiaoke
   date： 2024-08-03 17:00:02
-->

<template>
    <div>
        <div style="text-align: right">
            <Button v-permission="{'value':'kube_storage_add'}" type="primary" size="large"
                    @click="handleEvent('add',null)" style="width: 140px">
                创建存储
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
                <el-table-column prop="name"
                                 width="200"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="名称">
                </el-table-column>
                <el-table-column
                        align="left"
                        label="关联服务"
                        width="200">
                    <template slot-scope="scope">
                        <div v-if="scope.row.relationAppList!=null && scope.row.relationAppList.length>0">
                            <Tooltip placement="top" :transfer="true">
                                <span style="cursor: pointer">{{scope.row.relationAppList.length}}个</span>
                                <div slot="content">
                                    <p v-for="item in scope.row.relationAppList"><span v-text="item.name"></span></p>
                                </div>
                            </Tooltip>
                        </div>
                        <span style="cursor: pointer" v-else>0个</span>
                    </template>
                </el-table-column>
                <el-table-column prop="capacity"
                                 width="200"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="容量">
                    <template slot-scope="scope">
                        <span v-text="scope.row.capacity+'GB'"></span>
                    </template>
                </el-table-column>
                <el-table-column prop="strategy"
                                 width="200"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="读写策略">
                    <template slot-scope="scope">
                        <span v-if="scope.row.strategy=='ReadWriteMany'">多节点读写</span>
                        <span v-if="scope.row.strategy=='ReadOnlyMany'">多节点只读</span>
                        <span v-if="scope.row.strategy=='ReadWriteOnce'">单节点读写</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime"
                                 width="200"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="创建时间">
                </el-table-column>
                <el-table-column prop="remark"

                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="描述">
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                        width="150">
                    <template slot-scope="scope">
                        <Poptip @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                            <el-button
                                    style="color: #F45236"
                                    v-permission="{'value':'kube_storage_del'}"
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
    import {pageStorage,} from '@/api/kube/storage'
    import appUtils from '@/utils/appUtils';

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
                pageStorage(this.query).then(res => {
                    this.loading = false
                    if (res.state) {
                        this.tableData = res.data.records
                        this.count = res.data.total
                    } else {
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
