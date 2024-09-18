<!--
   推送接收人
   author： xiaoke
   date： 2024-08-28 00:33:59
-->

<template>
   <div >
      <Card ref="card" :bordered="false" :dis-hover="true"  :padding="32" style="border-radius: 8px">
          <Row>
              <Col :span="14">
                  <div style="color: #202224;font-size: 24px;font-weight: 600">推送接收人</div>
              </Col>
          </Row>
           <div>
              <push-user-table-list ref="pushUserTable" :extend="pushUserExtend" :query="pushUserQuery"  @on-events="pushUserHandleEvent"></push-user-table-list>
           </div>
           <div>
              <push-user-table-edit ref="pushUserEdit" :extend="pushUserExtend" @on-refresh="$refs.pushUserTable.getData()"></push-user-table-edit>
           </div>
      </Card>
   </div>
</template>

<script>
   import pushUserTableSearch from './push-user-table-search';
   import pushUserTableList from './push-user-table-list';
   import pushUserTableEdit from './push-user-table-edit';




   export default {
      components: {
            pushUserTableSearch,
            pushUserTableList,
            pushUserTableEdit
      },
      data() {
         return {
            // 推送接收人查询条件
            pushUserQuery: {
                namespace: null,
                size: 10,
                current: 1
            },

            //推送接收人扩展数据（用于字典类数据）
            pushUserExtend: {


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
          * 推送接收人搜索
          */
         pushUserSearch() {
             this.$refs.pushUserTable.search()
         },
         /**
          * 推送接收人事件
          * @param type 推送接收人事件类型
          * @param data 推送接收人事件数据
          */
         pushUserHandleEvent(obj){
             this.$refs.pushUserEdit.handleEvent(obj.type,obj.data)
         },
      }
   }
</script>

<style lang="scss" scoped>
</style>
