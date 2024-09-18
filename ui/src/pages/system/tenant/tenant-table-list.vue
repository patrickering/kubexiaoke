<!--
   租户
   author： 超级管理员
   date： 2022-07-30 10:09:29
-->

<template>
    <div>
        <Button v-permission="{'value':'sys_tenant_add'}" type="primary" icon="md-add" @click="handleEvent('add',null)">
            新建
        </Button>
        <el-table
                v-if="tableData!=null&&tableData.length>0"
                :data="tableData"
                size="mini"
                border
                stripe
                :header-row-style="()=>{return 'color: #5c6b77'}"
                :header-cell-style="()=>{return 'background: #f8f8f9;line-height:35px'}"
                style="width: 100%;margin-top: 10px">
            <el-table-column prop="name"

                             align="left"
                             :show-overflow-tooltip="true"
                             label="名称">
            </el-table-column>
            <el-table-column prop="enable"

                             align="center"
                             :show-overflow-tooltip="true"
                             label="是否启用">
                <template slot-scope="scope">
                    <Switch v-model="scope.row.enable" @on-change="handleEvent('enable',scope.row)" />
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
                            v-permission="{'value':'sys_tenant_edit'}"
                            type="text"
                            size="mini"
                            icon="el-icon-edit"
                            @click="handleEvent('edit',scope.row)">编辑
                    </el-button>
                    <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                        <el-button
                                v-permission="{'value':'sys_tenant_del'}"
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
        pageTenant,
    } from '@/api/system/tenant'
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
                let loading = appUtils.showLoading()
                pageTenant(this.query).then(res => {
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
