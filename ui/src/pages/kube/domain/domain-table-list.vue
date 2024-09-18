<!--
   集群域名
   author： xiaoke
   date： 2024-08-18 23:43:43
-->

<template>
    <div>
        <Row :gutter="32" v-if="tableData!=null && tableData.length>0">
            <Col :span="12" v-for="item in tableData" :key="item.id" style="border-radius: 8px">
                <Card dis-hover>
                    <Row>
                        <Col :span="12">
                            <span style="color: #1F8ECF;font-size: 24px;font-weight: 600" v-text="item.domain"></span>
                        </Col>
                        <Col :span="12" style="text-align: right">
                            <div style="line-height: 36px">
                                <el-button
                                        v-permission="{'value':'kube_domain_edit'}"
                                        type="text"
                                        size="mini"
                                        @click="handleEvent('edit',item)">编辑
                                </el-button>
                                <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',item)" transfer confirm
                                        title="确认删除?">
                                    <el-button
                                            style="color: #F45236"
                                            v-permission="{'value':'kube_domain_del'}"
                                            type="text"
                                            size="mini"
                                    >删除
                                    </el-button>
                                </Poptip>
                            </div>
                        </Col>
                    </Row>
                </Card>
            </Col>
        </Row>
        <el-empty v-if="tableData!=null&& tableData.length<=0"></el-empty>

        <el-skeleton :loading="loading" :count="2" animated/>
    </div>
</template>
<script>
    import {listDomain,} from '@/api/kube/domain'
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
                tableData: null,
                loading:false
            }
        },
        computed: {},
        methods: {
            /**
             * 搜索数据
             */
            search() {
                this.getData()
            },

            /**
             * 获取数据
             */
            getData() {
                this.loading = true
                listDomain(this.query).then(res => {
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
