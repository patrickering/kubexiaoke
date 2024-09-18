<!--
   异常
   author： xiaoke
   date： 2020-08-24 21:37:54
-->

<template>
   <div>
      <Card :bordered="false" dis-hover class="list-table-list-card ivu-mt" ref="card">
         <table-form ref="form" v-model="query" :extend="extend"  @on-submit="search" @on-reset="search"/>
         <table-list ref="table" :extend="extend" :query="query" @on-events="handleEvent"/>
         <table-edit ref="edit" :extend="extend" @on-refresh="search"/>
      </Card>
   </div>
</template>

<script>
   import { remote } from '@/api/system/dict'
   import tableForm from './table-form';
   import tableList from './table-list';
   import tableEdit from './table-edit';

   export default {
      name: 'model-error',
      components: {tableForm, tableList,tableEdit},
      data() {
         return {
            // 查询条件
            query: {
               type: null,
               path: null,
               size: 10,
               current: 1
            },
            //扩展数据（用于字典类数据）
            extend: {
               // 类型
               errorTypeList: []
            }
         }
      },
      created() {
      },
      mounted() {
         /**
          * 获取类型列表
          */
         this.getErrorTypeList();
      },
      computed: {},
      methods: {
        /**
         * 获取类型列表
         */
        getErrorTypeList(){
            remote('error_type').then(res=>{
               if(res.state){
                  this.extend.errorTypeList = res.data
               }else {
                  appUtils.showError(res.msg)
               }
            })
         },
         /**
          * 搜索
          */
         search() {
            this.$refs.table.search()
         },
         /**
          * 事件
          * @param type 事件类型
          * @param data 事件数据
          */
         handleEvent(obj){
            this.$refs.edit.handleEvent(obj.type,obj.data)
         }
      }
   }
</script>

<style lang="scss" scoped>
</style>
