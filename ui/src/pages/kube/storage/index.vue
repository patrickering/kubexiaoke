<!--
   存储
   author： xiaoke
   date： 2024-08-03 17:00:02
-->

<template>
   <div >
      <Card ref="card" :bordered="false" :dis-hover="true"  :padding="32" style="border-radius: 8px">
          <Row>
              <Col :span="18">
                  <div style="color: #202224;font-size: 24px;font-weight: 600">存储列表</div>
              </Col>
              <Col :span="6">
                  <storage-table-search v-model="storageQuery" :extend="storageExtend" @on-submit="storageSearch" @on-reset="storageSearch"></storage-table-search>
              </Col>
          </Row>
           <div style="margin-top: 10px">
              <storage-table-list ref="storageTable" :extend="storageExtend" :query="storageQuery"  @on-events="storageHandleEvent"></storage-table-list>
           </div>
           <div>
              <storage-table-edit ref="storageEdit" :extend="storageExtend" @on-refresh="$refs.storageTable.getData()"></storage-table-edit>
           </div>
      </Card>
   </div>
</template>

<script>
   import storageTableSearch from './storage-table-search';
   import storageTableList from './storage-table-list';
   import storageTableEdit from './storage-table-edit';

   export default {
      components: {
            storageTableSearch,
            storageTableList,
            storageTableEdit
      },
      data() {
         return {
            // 存储查询条件
            storageQuery: {
                name: null,
                size: 10,
                current: 1
            },

            //存储扩展数据（用于字典类数据）
            storageExtend: {


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
          * 存储搜索
          */
         storageSearch() {
             this.$refs.storageTable.search()
         },
         /**
          * 存储事件
          * @param type 存储事件类型
          * @param data 存储事件数据
          */
         storageHandleEvent(obj){
             this.$refs.storageEdit.handleEvent(obj.type,obj.data)
         },
      }
   }
</script>

<style lang="scss" scoped>
</style>
