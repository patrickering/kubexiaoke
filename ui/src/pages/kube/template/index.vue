<!--
   模板管理
   author： xiaoke
   date： 2024-08-17 21:23:44
-->

<template>
   <div style="width: 1200px;margin: 0 auto;margin-top: 10px">
      <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
           <div>
              <template-table-list ref="templateTable" :extend="templateExtend" :query="templateQuery"  @on-events="templateHandleEvent"></template-table-list>
           </div>
           <div>
              <template-table-edit ref="templateEdit" :extend="templateExtend" @on-refresh="$refs.templateTable.getData()"></template-table-edit>
           </div>
      </Card>
   </div>
</template>

<script>
   import templateTableList from './template-table-list';
   import templateTableEdit from './template-table-edit';




   export default {
      components: {
            templateTableList,
            templateTableEdit
      },
      data() {
         return {
            // 模板查询条件
            templateQuery: {
                size: 10,
                current: 1
            },

            //模板扩展数据（用于字典类数据）
            templateExtend: {


            },

         }
      },
      created() {
      },
      mounted() {
      },
      computed: {},
      methods: {
         /**
          * 模板事件
          * @param type 模板事件类型
          * @param data 模板事件数据
          */
         templateHandleEvent(obj){
             this.$refs.templateEdit.handleEvent(obj.type,obj.data)
         },
      }
   }
</script>

<style lang="scss" scoped>
</style>
