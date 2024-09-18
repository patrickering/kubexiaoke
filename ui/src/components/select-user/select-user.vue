<template>
    <div>

        <Button v-if="button" type="primary" size="large" @click="openSelect" style="width: 140px">
            {{buttonText}}
        </Button>
        <el-input v-else readonly
                  v-model="showName"
                  :size="size"
                  :placeholder="placeholder">
            <el-button @click="openSelect" slot="append">{{buttonText}}</el-button>
        </el-input>


        <Modal
                v-model="showUser"
                :title="modalTitle"
                :width="width">
            <RadioGroup v-model="selectUser" style="width: 100%">
                <Input v-model="query.name" size="large" style="width: 200px" clearable  placeholder="请输入姓名" ></Input>
                <Input v-model="query.username" size="large" style="width: 200px;margin-left: 5px" clearable  placeholder="请输入用户名" ></Input>
                <Input v-model="query.phone" size="large" style="width: 200px;margin-left: 5px" clearable  placeholder="请输入手机号" ></Input>
                <Button type="primary" size="large" @click="searchUser" style="margin-left: 10px">查询</Button>
                <div v-loading="loading">
                    <el-table
                            :data="tableData"
                            @row-click="rowClick"
                            :header-row-style="()=>{return 'color: #202224'}"
                            :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                            style="width: 100%;margin-top: 10px">
                        <div slot="empty">
                            <el-empty></el-empty>
                        </div>
                        <el-table-column prop="name"
                                         align="center"
                                         width="60"
                                         :show-overflow-tooltip="true"
                                         label="选择">
                            <template slot-scope="scope">
                                <Radio :label="scope.row.id">
                                    <span></span>
                                </Radio>
                            </template>
                        </el-table-column>
                        <el-table-column prop="statusName"
                                         align="center"
                                         :show-overflow-tooltip="true"
                                         label="状态">
                            <template slot-scope="scope">
                                <span v-if="scope.row.loginFlag=='0'">正常</span>
                                <span v-else>锁定</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name"
                                         align="left"
                                         :show-overflow-tooltip="true"
                                         label="姓名">
                        </el-table-column>
                        <el-table-column prop="username"
                                         align="left"
                                         :show-overflow-tooltip="true"
                                         label="用户名">
                        </el-table-column>
                        <el-table-column prop="phone"
                                         align="left"
                                         :show-overflow-tooltip="true"
                                         label="手机号">
                        </el-table-column>
                        <el-table-column prop="role"
                                         align="center"
                                         :show-overflow-tooltip="true"
                                         label="角色">
                            <template slot-scope="scope">
                                <Tag color="primary" v-text="scope.row.role.roleName"></Tag>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </RadioGroup>

            <div slot="footer">
                <Row>
                    <Col span="18">
                        <div style="display: flex;justify-content: flex-start;">
                            <Page v-if="count>0" style="margin-bottom: 10px" transfer @on-change="pageChange"
                                  :page-size="10"
                                  @on-page-size-change="sizeChange"
                                  :current="query.current"
                                  prev-text="上一页"
                                  next-text="下一页" :total="count" show-elevator show-total show-sizer/>
                        </div>
                    </Col>
                    <Col span="6">
                        <Button size="large" @click="okClear">清空</Button>
                        <Button type="primary" size="large" @click="okSelect">选择</Button>
                    </Col>
                </Row>
            </div>
        </Modal>
    </div>
</template>

<script>
    import appUtils from '@/utils/appUtils';
    import {getUser, pageUser} from '@/api/system/user'

    export default {
        name: 'select-user',
        props: {
            //值
            value: {},
            //显示姓名
            name: {},
            //展位信息
            placeholder: {
                default:'请选择'
            },
            //弹出层标题
            modalTitle:{
                default:'选择用户'
            },
            //弹出层宽度
            width:{
                default:'800'
            },
            //表单尺寸
            size:{
                default:'small'
            },
            //按钮文字
            buttonText:{
                default:'选择'
            },
            button:{
                default:false
            },
            notId:{
                default:[]
            }
        },
        data () {
            return {
                //是否显示用户选择弹窗
                showUser: false,
                //列表数据
                tableData: []
                //数据总行数
                , count: 0
                // 用户查询条件
                , query: {
                    name: null,
                    username: null,
                    phone: null,
                    notId: null,
                    size: 10,
                    current: 1
                },
                //显示姓名
                showName: null,
                //已选择用户
                selectUser: null
                ,loading:false
            }
        },
        watch: {
            name () {
                this.showName = this.name
            },
            value(){
                if(this.value==null){
                    this.showName = null
                }else {
                    if(this.name==null){
                        this.getUser()
                    }
                }
            }
        },
        computed: {},
        methods: {
            /**
             * 根据ID获取用户
             */
            getUser(){
                if(this.value!=null){
                    getUser(this.value).then(res=>{
                        if(res.state && res.data!=null){
                            this.showName = res.data.name
                        }
                    })
                }else {
                    this.showName = null
                }

            },
            /**
             * 获取数据
             */
            getData () {
                this.selectUser = null
                this.loading = true
                if(this.notId!=null){
                    this.query.notId = this.notId.toString()
                }

                pageUser(this.query).then(res => {
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
            pageChange (val) {
                this.query.current = val
                this.getData()
            },
            /**
             * 每页显示条数发生改变
             */
            sizeChange (val) {
                this.query.size = val
                this.query.current = 1
                this.getData()
            },
            /**
             * 打开选择用户
             */
            openSelect () {
                this.showUser = true
                this.getData()
            },
            /**
             * 确认选择用户
             */
            okSelect () {
                this.$emit('input', this.selectUser)
                this.$emit('on-change', this.selectUser)
                if (this.selectUser != null) {
                    let staff = this.tableData.filter(item => item.id == this.selectUser)
                    this.showName = staff[0].name
                } else {
                    this.showName = null
                }
                this.showUser = false
            },
            /**
             * 清空选择用户
             */
            okClear () {
                this.selectUser = null
                this.$emit('input', this.selectUser)
                this.$emit('on-change', this.selectUser)
                this.showName = null
                this.showUser = false
            },
            searchUser(){
                this.query.current = 1
                this.getData()
            },
            /**
             * 单元格点击事件
             * @param row
             * @param column
             * @param event
             */
            rowClick(row, column, event){
                this.selectUser =  row.id
            }
        },
        mounted () {

        },
    }
</script>
