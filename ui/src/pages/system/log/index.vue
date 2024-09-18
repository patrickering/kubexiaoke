<template>
    <div>
        <Card :bordered="false" dis-hover class="list-table-list-card ivu-mt" ref="card">
            <Form class="searchForm" :label-width="labelWidth" :label-position="labelPosition">
                <Row :gutter="24" type="flex">
                    <Col v-bind="grid">
                        <FormItem label="IP地址：" prop="type" label-for="type">
                            <Input v-model="query.remoteAddr" clearable placeholder="请输入IP地址"/>
                        </FormItem>
                    </Col>
                    <Col v-bind="grid">
                        <FormItem label="类型：" prop="system" label-for="system">
                            <Select v-model="query.type" clearable placeholder="请选择类型">
                                <Option v-for="item in logTypeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col v-bind="grid">
                        <FormItem label="请求方式：" prop="system" label-for="system">
                            <Select v-model="query.method" clearable  placeholder="请选择请求方式">
                                <Option v-for="item in methodTypeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col v-bind="grid">
                        <FormItem label="创建时间：" prop="applicationTime" label-for="applicationTime">
                            <el-date-picker
                                    v-model="time"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    style="width: 100%"
                                    size="small"
                                    type="datetimerange"
                                    range-separator="-"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </FormItem>
                    </Col>
                </Row>
                <Row :gutter="24" type="flex" justify="end">
                    <Col v-bind="grid" class="ivu-text-right">
                        <FormItem>
                            <Button type="primary" style="margin-left: 10px;margin-bottom: 10px" @click="getList">查询
                            </Button>
                            <Button @click="rel" type="default" style="margin-left: 10px;margin-bottom: 10px">重置</Button>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <!--<Button v-permission="{'value':'sys_dict_add'}" style="margin-bottom: 10px"
                    icon="md-add" type="primary"
                    class="layui-btn layui-btn-normal layui-btn-sm" @click="edit(null)">
                添加
            </Button>-->
            <div ref="tableRef">
                <el-table
                        v-if="count>0"
                        :data="tableData"
                        size="mini"
                        :height="tableHeight"
                        border
                        stripe
                        :header-row-style="()=>{return 'color: #5c6b77'}"
                        :header-cell-style="()=>{return 'background: #f8f8f9;line-height:35px'}"
                        style="width: 100%">
                    <el-table-column prop="title"
                                     width="200"
                                     label="标题">
                    </el-table-column>
                    <el-table-column prop="type"
                                     width="100"
                                     align="center"
                                     label="类型">
                        <template slot-scope="scope">
                            <span v-text="handleType(scope.row)"></span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remoteAddr"
                                     width="120"
                                     align="center"
                                     label="IP地址">
                    </el-table-column>
                    <el-table-column prop="method"
                                     width="100"
                                     align="center"
                                     label="请求方式">
                    </el-table-column>
                    <el-table-column prop="requestUri"
                                     :show-overflow-tooltip="true"
                                     label="请求地址">
                    </el-table-column>
                    <el-table-column prop="params"
                                     :show-overflow-tooltip="true"
                                     label="请求参数">
                    </el-table-column>
                    <el-table-column prop="time"
                                     width="100"
                                     align="center"
                                     label="请求时间">
                    </el-table-column>
                    <el-table-column prop="createTime"
                                     width="180"
                                     align="center"
                                     label="创建时间">
                    </el-table-column>
                    <el-table-column prop="createName"
                                     width="120"
                                     align="center"
                                     label="操作人">
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            fixed="right"
                            align="center"
                            width="100">
                        <template slot-scope="scope">
                            <Poptip style="margin-left: 10px" transfer confirm title="确认删除?" @on-ok="handleDel(scope.row)">
                                <el-button
                                        v-permission="{'value':'sys_log_del'}"
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
                      @on-page-size-change="sizeChange"
                      prev-text="上一页"
                      next-text="下一页" :total="count" show-elevator show-total show-sizer/>
            </div>
        </Card>
    </div>
</template>

<script>
    import {delObj, fetchList} from '@/api/system/log'
    import {remote} from '@/api/system/dict'
    import appUtils from "@/utils/appUtils";
    import {mapState} from 'vuex';
    export default {
        name: 'system-log',
        data() {
            return {
                grid: {
                    xl: 6,
                    lg: 6,
                    md: 12,
                    sm: 24,
                    xs: 24
                },
                time: [],
                logDateTime: [],
                tableData: [],
                query: {// 查询条件
                    size: 10,
                    current: 1
                },
                count: 0,
                logTypeList: [],
                methodTypeList: [{
                    value: 'POST',
                    label: 'POST'
                }, {
                    value: 'GET',
                    label: 'GET'
                }, {
                    value: 'PUT',
                    label: 'PUT'
                }, {
                    value: 'DELETE',
                    label: 'DELETE'
                }
                ]
                , tableHeight: 0
            }
        },
        created() {
        },
        mounted() {
            this.getDict();
        },
        computed: {
            ...mapState('admin/layout', [
                'isMobile'
            ]),
            labelWidth() {
                return this.isMobile ? undefined : 100;
            },
            labelPosition() {
                return this.isMobile ? 'top' : 'right';
            }
        },
        methods: {
            handleResize() {
                if (this.$refs.tableRef != null) {
                    this.tableHeight = document.body.clientHeight - this.$refs.tableRef.getBoundingClientRect().top - 95
                }
            },
            //获取日志类型字典列表
            getDict() {
                remote('log_type').then(res => {
                    this.logTypeList = res.data
                    this.getList()
                })
            },
            rel() {
                this.query = {// 查询条件
                    size: 10,
                    current: 1
                }
                this.getList();
            },
            //获取数据
            getList() {
                if(this.time!=null && this.time.length>0){
                    this.query.startTime = this.time[0]
                    this.query.endTime = this.time[1]
                }else {
                    this.query.startTime = null
                    this.query.endTime = null
                }
                let loading = appUtils.showLoading()
                fetchList(this.query).then(response => {
                    this.tableData = response.data.records
                    this.count = response.data.total
                    appUtils.hideLoading(loading)
                }).catch(err => {
                    appUtils.hideLoading(loading)
                })
            },
            handleDel: function (row) {
                var _this = this
                this.$confirm('是否确认删除日志?', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    return delObj(row.id)
                }).then(() => {
                    this.getList(this.page)
                    appUtils.showSuccess('删除成功')
                }).catch(function () {
                })
            },
            //处理类型
            handleType(row) {
                let type = this.logTypeList.filter(item => item.value == row.type);
                if (type != null && type.length > 0) {
                    return type[0].label
                }
                return ''
            },
            // 页数发生改变
            pageChange(val) {
                this.query.current = val
                this.getList()
            },
            // 每页显示条数发生改变
            sizeChange(val) {
                this.query.size = val
                this.query.current = 1
                this.getList()
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>

