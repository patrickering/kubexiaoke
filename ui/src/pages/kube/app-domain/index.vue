<!--
   应用域名
   author： xiaoke
   date： 2024-08-19 00:39:43
-->

<template>
    <div >
        <Card ref="card" :bordered="false" :dis-hover="true"  :padding="32" style="border-radius: 8px">
            <Row>
                <Col :span="16">
                    <div style="color: #202224;font-size: 24px;font-weight: 600">域名列表</div>
                </Col>
                <Col :span="8">
                    <app-domain-table-search v-model="appDomainQuery" :extend="appDomainExtend" @on-submit="appDomainSearch" @on-reset="appDomainSearch"></app-domain-table-search>
                </Col>
            </Row>
            <div style="margin-top: 10px">
                <app-domain-table-list ref="appDomainTable" :extend="appDomainExtend" :query="appDomainQuery"  @on-events="appDomainHandleEvent"></app-domain-table-list>
            </div>
            <div>
                <app-domain-table-edit ref="appDomainEdit" :extend="appDomainExtend" @on-refresh="$refs.appDomainTable.getData()"></app-domain-table-edit>
            </div>
        </Card>
    </div>
</template>

<script>
    import appDomainTableSearch from './app-domain-table-search';
    import appDomainTableList from './app-domain-table-list';
    import {listDomain} from '@/api/kube/domain'
    import appDomainTableEdit from './app-domain-table-edit';

    export default {
        components: {
            appDomainTableSearch,
            appDomainTableList,
            appDomainTableEdit
        },
        data() {
            return {
                // 应用域名查询条件
                appDomainQuery: {
                    domainId: null,
                    size: 10,
                    current: 1
                },

                //应用域名扩展数据（用于字典类数据）
                appDomainExtend: {

                    //域名ID集合
                    domainList:[],

                },

            }
        },
        created() {
        },
        mounted() {
            /**
             * 获取域名ID列表
             */
            this.listDomain()
        },
        computed: {},
        methods: {
            /**
             * 应用域名搜索
             */
            appDomainSearch() {
                this.$refs.appDomainTable.search()
            },
            /**
             * 获取域名ID列表
             */
            listDomain(){
                listDomain().then(res=>{
                    if(res.state){
                        this.appDomainExtend.domainList = res.data
                    }
                })
            },
            /**
             * 应用域名事件
             * @param type 应用域名事件类型
             * @param data 应用域名事件数据
             */
            appDomainHandleEvent(obj){
                this.$refs.appDomainEdit.handleEvent(obj.type,obj.data)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
