<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <div>
        <div style="text-align: right">
            <Button v-permission="{'value':'user_user_add'}" type="primary" size="large" @click="handleEvent('add',null)" style="width: 140px">
                添加用户
            </Button>
        </div>
        <el-table v-if="count > 0"
                  :data="tableData"
                  :header-row-style="()=>{return 'color: #202224'}"
                  :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                  style="width: 100%;margin-top: 10px">
            <el-table-column prop="username"
                             label="用户名"
                             align="center"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="name"
                             label="姓名"
                             align="center"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column label="角色"
                             align="center"
                             :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <Tag  v-text=" scope.row.role.roleName"></Tag>
                </template>
            </el-table-column>
            <el-table-column prop="phone"
                             label="手机号"
                             align="center"
                             :show-overflow-tooltip="true">
            </el-table-column>

            <el-table-column prop="loginFlag"
                             label="账号状态"
                             width="80"
                             align="center"
                             :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span v-for="item in extend.statusList" :key="item.value" v-if="scope.row.loginFlag == item.value" v-text="item.label"></span>
                </template>
            </el-table-column>
            <el-table-column prop="limitCount"
                             label="锁定状态"
                             width="80"
                             align="center"
                             :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span v-if="scope.row.limitCount == null">正常</span>
                    <el-button
                            v-else-if="scope.row.limitCount == 0"
                            @click="handleEvent('limit',scope.row)"
                            type="text"
                            size="mini"
                            icon="el-icon-refresh" v-text="scope.row.limitTime+'后解除'">
                    </el-button>
                    <span v-else v-text="'剩余'+scope.row.limitCount+'次'"></span>
                </template>
            </el-table-column>
            <el-table-column prop="loginDate"
                             label="登录时间"
                             width="150"
                             align="center"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="createDate"
                             label="创建时间"
                             width="150"
                             align="center"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column label="操作" align="center" width="180">
                <template slot-scope="scope">
                    <el-button
                            v-permission="{'value':'user_user_edit'}"
                            type="text"
                            size="mini"
                            @click="handleEvent('edit',scope.row)">编辑
                    </el-button>
                    <el-button
                            v-permission="{'value':'user_user_resetPassword'}"
                            @click="handleEvent('reset',scope.row)"
                            type="text"
                            size="mini">重置密码
                    </el-button>
                    <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm
                            title="确认删除?">
                        <el-button
                                style="color: #F45236"
                                v-permission="{'value':'user_user_del'}"
                                type="text"
                                size="mini"
                        >删除
                        </el-button>
                    </Poptip>
                </template>
            </el-table-column>
        </el-table>
        <el-empty v-else></el-empty>
        <div style="display: flex;justify-content: flex-end;">
            <Page v-if="count>0" style="margin-top: 10px;margin-bottom: 10px" transfer @on-change="pageChange"
                  @on-page-size-change="sizeChange"
                  prev-text="上一页"
                  next-text="下一页" :total="count" show-elevator show-total show-sizer/>
        </div>
    </div>
</template>
<script>
    import {pageUser} from '@/api/system/user'
    import appUtils from '@/utils/appUtils';
    import dataUtils from "@/utils/dataUtils";

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
                pageUser(this.query).then(response => {
                    this.tableData = response.data.records
                    this.count = response.data.total
                    appUtils.hideLoading(loading)
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
             * 处理性别
             */
            handleSex(sex) {
                let ret = ""
                dataUtils.each(this.extend.sexList, (key, obj) => {
                    if (obj.value == sex) {
                        ret = obj.label
                    }
                })
                return ret
            },
            /**
             * 事件
             * @param type 事件类型
             * @param data 事件数据
             */
            handleEvent(type, data) {
                this.$emit('on-events', {type: type, data: data});
            }
        },
        mounted() {
            this.getData()
        }
    }
</script>
