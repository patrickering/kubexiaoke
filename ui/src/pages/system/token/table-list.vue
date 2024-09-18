<!--
   用户表
   author： chendengwen
   date： 2020-07-18 13:00:20
-->
<template>
    <div>
        <el-table
                v-if="count>0"
                :data="tableData"
                size="small"
                border
                stripe
                :header-row-style="()=>{return 'color: #5c6b77'}"
                :header-cell-style="()=>{return 'background: #f8f8f9;line-height:35px'}"
                style="width: 100%;margin-top: 10px">

            <el-table-column prop="token"
                             :show-overflow-tooltip="true"
                             label="token"></el-table-column>

            <el-table-column prop="expire"
                             width="120"
                             align="center"
                             :show-overflow-tooltip="true"
                             label="过期时间（秒）"></el-table-column>
            <el-table-column prop="username"
                             :show-overflow-tooltip="true"
                             label="登录名"></el-table-column>
            <el-table-column prop="roleId"
                             :show-overflow-tooltip="true"
                             align="center"
                             width="200"
                             label="角色">
                <template slot-scope="scope">
                    <div v-if="scope.row.roleList!=null">
                        <Tag v-for="item in scope.row.roleList" :key="item.roleId" color="primary" v-text="item.roleName">primary</Tag>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="name"
                             :show-overflow-tooltip="true"
                             label="姓名"></el-table-column>
            <el-table-column prop="email"
                             :show-overflow-tooltip="true"
                             label="邮箱"></el-table-column>
            <el-table-column prop="phone"
                             align="center"
                             :show-overflow-tooltip="true"
                             label="手机"></el-table-column>
            <el-table-column prop="loginIp"
                             align="center"
                             :show-overflow-tooltip="true"
                             label="最后登录IP"></el-table-column>
            <el-table-column prop="loginDate"
                             width="150"
                             align="center"
                             :show-overflow-tooltip="true"
                             label="最后登录时间"></el-table-column>
            <el-table-column
                    label="操作"
                    align="center"
                    width="150">
                <template slot-scope="scope">
                    <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                        <el-button
                                v-permission="{'value':'user_user_del'}"
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
                  @on-page-size-change="sizeChange"
                  prev-text="上一页"
                  next-text="下一页" :total="count" show-elevator show-total show-sizer/>
        </div>
    </div>
</template>
<script>
    import {
        pageToken
    } from '@/api/system/token'
    import appUtils from '@/utils/appUtils';
    import dataUtils from "../../../utils/dataUtils";
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
                pageToken(this.query).then(response => {
                    this.tableData = response.data.records
                    this.count = response.data.total
                    appUtils.hideLoading(loading)
                }).catch(err=>{
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
            handleSex(sex){
                let ret = ""
                dataUtils.each(this.extend.sexList,(key,obj)=>{
                    if(obj.value==sex){
                        ret = obj.label
                    }
                })
                return ret
            },
            /**
             * 处理状态
             */
            handleStatus(status){
                let ret = ""
                dataUtils.each(this.extend.statusList,(key,obj)=>{
                    if(obj.value==status){
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
