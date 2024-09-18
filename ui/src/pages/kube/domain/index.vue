<!--
   集群域名
   author： xiaoke
   date： 2024-08-18 23:43:43
-->

<template>
    <div style="width: 1200px;margin: 0 auto;margin-top: 10px">
        <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
            <Row>
                <Col :span="12">
                    <div style="color: #202224;font-size: 20px;font-weight: 600;display: flex;align-items: center;">
                        <img src="@/assets/images/domain-info.png" width="24" height="24">
                        <span style="margin-left: 8px">域名</span>
                    </div>
                </Col>
                <Col :span="12" style="text-align: right">
                    <Button v-permission="{'value':'kube_domain_add'}" type="primary" ghost
                            style="width: 100px;margin-left: 10px" @click="domainHandleEvent({type:'add',data:null})">
                        添加域名
                    </Button>
                </Col>
            </Row>

            <Alert show-icon style="margin-top: 20px">
                <div style="color: #878787;font-size: 14px">
                    请将域名进行泛解析
                </div>
                <template slot="desc">
                    <span style="color: #3F3F3F;font-size: 16px;font-weight: 600">主机记录：xxx</span>
                    <span style="color: #3F3F3F;font-size: 16px;font-weight: 600;margin-left: 60px">记录类型：A</span>
                    <span style="color: #3F3F3F;font-size: 16px;font-weight: 600;margin-left: 60px">记录值：集群入口IP</span>
                </template>
            </Alert>
            <div>
                <domain-table-list ref="domainTable" :extend="domainExtend" :query="domainQuery"
                                   @on-events="domainHandleEvent"></domain-table-list>
            </div>
            <div>
                <domain-table-edit ref="domainEdit" :extend="domainExtend"
                                   @on-refresh="$refs.domainTable.getData()"></domain-table-edit>
            </div>
        </Card>


    </div>
</template>

<script>
    import domainTableList from './domain-table-list';
    import domainTableEdit from './domain-table-edit';


    export default {
        components: {
            domainTableList,
            domainTableEdit
        },
        data() {
            return {
                // 集群域名查询条件
                domainQuery: {
                    size: 10,
                    current: 1
                },

                //集群域名扩展数据（用于字典类数据）
                domainExtend: {},

            }
        },
        created() {
        },
        mounted() {
        },
        computed: {},
        methods: {
            /**
             * 集群域名事件
             * @param type 集群域名事件类型
             * @param data 集群域名事件数据
             */
            domainHandleEvent(obj) {
                this.$refs.domainEdit.handleEvent(obj.type, obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
