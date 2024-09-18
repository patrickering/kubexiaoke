<!--
   项目人员
   author： xiaoke
   date： 2024-08-27 00:01:32
-->

<template>
    <div>

        <div style="text-align: right">
            <select-user v-permission="{'value':'kube_projectUser_add'}"
                         :not-id="userIdList"
                         :width="1000"
                         :button="true"
                         button-text="添加人员"
                         @on-change="changeUser"></select-user>
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
                <el-table-column prop="role"
                                 align="center"
                                 :show-overflow-tooltip="true"
                                 label="角色">
                    <template slot-scope="scope">
                        <div v-if="scope.row.user.roleList!=null">
                            <Tag v-for="item in scope.row.user.roleList" :key="item.roleId" color="primary" v-text="item.roleName"></Tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="statusName"
                                 align="center"
                                 :show-overflow-tooltip="true"
                                 label="状态">
                    <template slot-scope="scope">
                        <span v-if="scope.row.user.loginFlag=='0'">正常</span>
                        <span v-else>锁定</span>
                    </template>
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
                        <!--<el-button
                                v-permission="{'value':'kube_projectUser_edit'}"
                                type="text"
                                size="mini"
                                icon="el-icon-edit"
                                @click="handleEvent('edit',scope.row)">编辑
                        </el-button>-->
                        <Poptip @on-ok="handleEvent('del',scope.row)" transfer confirm
                                title="确认删除?">
                            <el-button
                                    v-if="showDelete(scope.row)"
                                    style="color: #F45236"
                                    v-permission="{'value':'kube_projectUser_del'}"
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
    import {listProjectUser,} from '@/api/kube/projectUser'
    import appUtils from '@/utils/appUtils';
    import dataUtils from "../../../utils/dataUtils";
    import Setting from "../../../setting";

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
        computed: {
            userIdList() {
                return  this.tableData.map((item) => item.userId)
            }
        },
        methods: {
            showDelete(row){
                let userInfo = dataUtils.getData(Setting.key.userInfo)
                if(row.userId!=userInfo.id && this.extend.project.createBy != row.userId){
                    return true
                }
                return false
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
                this.loading = true
                listProjectUser(this.query).then(res => {
                    this.loading = false
                    if (res.state) {
                        this.tableData = res.data
                    } else {
                        appUtils.showError(res.msg)
                    }
                }).catch(err => {
                    this.loading = false
                })
            },
            //选择用户
            changeUser(userId) {
                this.handleEvent('save', {userId: userId})
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
