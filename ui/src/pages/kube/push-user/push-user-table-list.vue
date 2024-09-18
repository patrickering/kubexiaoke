<!--
   推送接收人
   author： xiaoke
   date： 2024-08-28 00:33:59
-->

<template>
    <div>
        <div style="text-align: right">
            <Button v-permission="{'value':'kube_pushUser_add'}" type="primary" size="large"
                    @click="handleEvent('add',null)" style="width: 140px">
                添加接收人
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
                <el-table-column prop="user.name"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="姓名">
                </el-table-column>
                <el-table-column prop="user.username"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="用户名">
                </el-table-column>
                <el-table-column prop="user.phone"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="手机号">
                </el-table-column>
                <el-table-column prop="user.email"
                                 align="left"
                                 :show-overflow-tooltip="true"
                                 label="邮箱">
                </el-table-column>
                <el-table-column prop="pushType"
                                 align="center"
                                 :show-overflow-tooltip="true"
                                 label="推送类型">
                    <template slot-scope="scope">
                        <div v-if="scope.row.pushType!=null">
                            <span v-for="(item,index) in scope.row.pushType" :key="index">
                                <Tag v-if="item=='email'" color="#1F8ECF">邮件</Tag>
                            </span>
                        </div>

                    </template>
                </el-table-column>
                <el-table-column prop="contentType"
                                 align="center"
                                 :show-overflow-tooltip="true"
                                 label="内容类型">
                    <template slot-scope="scope">
                        <Tag v-if="scope.row.contentType==0" color="#1F8ECF">异常</Tag>
                        <Tag v-if="scope.row.contentType==1" color="#1F8ECF">操作</Tag>
                        <Tag v-if="scope.row.contentType==2" color="#1F8ECF">全部</Tag>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                        width="150">
                    <template slot-scope="scope">
                        <el-button
                                v-permission="{'value':'kube_pushUser_edit'}"
                                type="text"
                                size="mini"
                                @click="handleEvent('edit',scope.row)">编辑
                        </el-button>
                        <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm
                                title="确认删除?">
                            <el-button
                                    style="color: #F45236"
                                    v-permission="{'value':'kube_pushUser_del'}"
                                    type="text"
                                    size="mini"
                            >删除
                            </el-button>
                        </Poptip>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>
<script>
    import {listPushUser,} from '@/api/kube/pushUser'
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
                , loading: false
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
                listPushUser(this.query).then(res => {
                    this.loading = false
                    if (res.state) {
                        res.data.forEach((obj,index)=>{
                            if (obj.pushType != null) {
                                res.data[index].pushType = JSON.parse(obj.pushType)
                            }
                        })
                        this.tableData = res.data
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(err => {
                    this.loading = false
                })
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
