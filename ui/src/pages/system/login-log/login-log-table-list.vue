<!--
   登录日志
   author： xiaoke
   date： 2022-01-23 15:30:21
-->

<template>
    <div >
        <div ref="tableRef">
            <el-table
                    v-if="tableData!=null&&tableData.length>0"
                    :data="tableData"
                    size="mini"
                    border
                    stripe
                    :header-row-style="()=>{return 'color: #5c6b77'}"
                    :header-cell-style="()=>{return 'background: #f8f8f9;line-height:35px'}"
                    :height="tableHeight"
                    style="width: 100%;margin-top: 10px">
                <el-table-column prop="type"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="类型">
                    <template slot-scope="scope">
                        <span v-for="(item,index) in extend.loginTypeList" :key="index" v-if="item.value==scope.row.type" v-text="item.label"></span>
                    </template>
                </el-table-column>
                <el-table-column prop="ip"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="IP">
                </el-table-column>
                <el-table-column prop="source"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="来源">
                </el-table-column>
                <el-table-column prop="token"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="Token">
                </el-table-column>
                <el-table-column prop="param"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="参数">
                </el-table-column>
                <el-table-column prop="createName"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="用户">
                </el-table-column>
                <el-table-column prop="createTime"
                                 width="180"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="创建时间">
                </el-table-column>
                <el-table-column
                        label="操作"
                        fixed="right"
                        align="center"
                        width="150">
                    <template slot-scope="scope">
                        <Poptip @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                            <el-button
                                    v-permission="{'value':'system_loginLog_del'}"
                                    type="text"
                                    size="mini"
                                    icon="el-icon-delete"
                            >删除
                            </el-button>
                        </Poptip>
                    </template>
                </el-table-column>
            </el-table>
            <el-empty v-else></el-empty>
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
        pageLoginLog,
    } from '@/api/system/loginLog'
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
                //是否全屏
                , tableFullscreen: false
                , tableHeight: 0
            }
        },
        computed: {},
        methods: {
            handleResize() {
                if (this.$refs.tableRef != null) {
                    this.tableHeight = document.body.clientHeight - this.$refs.tableRef.getBoundingClientRect().top - 95
                }
            },
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
                let loading = appUtils.showLoading()
                pageLoginLog(this.query).then(res => {
                    appUtils.hideLoading(loading)
                    if(res.state){
                        this.tableData = res.data.records
                        this.count = res.data.total
                    }else {
                        appUtils.showError(res.msg)
                    }
                }).catch(err => {
                    appUtils.hideLoading(loading)
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
             * 表格全屏
             */
            handleFullscreen() {
                this.tableFullscreen = !this.tableFullscreen;
                this.$emit('on-fullscreen', this.tableFullscreen);
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
