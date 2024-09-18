<!--
   异常记录
   author： xiaoke
   date： 2024-08-22 23:06:02
-->

<template>
   <div>
      <div style="text-align: right">
         <Button v-permission="{'value':'kube_errorRecord_del'}"style="width: 140px" size="large" @click="handleEvent('batchDelete',selectData)" :disabled="selectData==null || selectData.length<=0" type="error">批量删除</Button>
      </div>
      <div v-loading="loading">
         <el-table
                 :data="tableData"
                 :header-row-style="()=>{return 'color: #202224'}"
                 :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                 @selection-change="handleSelectionChange"
                 style="width: 100%;margin-top: 10px">
            <div slot="empty">
               <el-empty></el-empty>
            </div>
            <el-table-column
                    align="center"
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column prop="title"

                             align="left"
                             :show-overflow-tooltip="true"
                             label="标题">
            </el-table-column>
            <el-table-column prop="readFlag"
                             width="100"
                             align="center"
                             :show-overflow-tooltip="true"
                             label="状态">
               <template slot-scope="scope">
                  <Tag style="width: 60px;border-radius: 28px;margin-left: 10px;text-align: center" v-if="scope.row.readFlag" size="medium" color="#19be6b" v-text="'已读'"></Tag>
                  <Tag style="width: 60px;border-radius: 28px;margin-left: 10px;text-align: center" v-else size="medium" color="#F3A057" v-text="'未读'"></Tag>
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
                  <el-button
                          v-if="!scope.row.readFlag"
                          type="text"
                          size="mini"
                          @click="handleEvent('read-flag',scope.row)">标记已读
                  </el-button>
                  <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                     <el-button
                             style="color: #F45236"
                             v-permission="{'value':'kube_errorRecord_del'}"
                             type="text"
                     >删除
                     </el-button>
                  </Poptip>
               </template>
            </el-table-column>
         </el-table>
      </div>
      <div style="display: flex;justify-content: flex-end;">
         <Page v-if="count>0" style="margin-top: 10px;margin-bottom: 10px" transfer @on-change="pageChange"
               :page-size="10"
               @on-page-size-change="sizeChange"
               :current="query.current"
               prev-text="上一页"
               next-text="下一页" :total="count" show-elevator show-total show-sizer/>
      </div>
   </div>
</template>
<script>
   import {
      pageErrorRecord,
   } from '@/api/kube/errorRecord'
   import appUtils from '@/utils/appUtils';
   import Setting from '@/setting';
   import dataUtils from '@/utils/dataUtils';
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
            //数据总行数
            , count: 0
            //选择的数据
            ,selectData: []
            ,loading:false
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
            pageErrorRecord(this.query).then(res => {
               this.loading = false
               if(res.state){
                  this.tableData = res.data.records
                  this.count = res.data.total
               }else {
                  appUtils.showError(res.msg)
               }
            }).catch(err => {
               this.loading = false
            })
         },
         /**
          * 页数发生改变
          */
         pageChange(val) {
            this.query.current = val
            this.getData()
         },

         /**
          * 每页显示条数发生改变
          */
         sizeChange(val) {
            this.query.size = val
            this.query.current = 1
            this.getData()
         },
         /**
          * 处理表格选择
          */
         handleSelectionChange(val){
            this.selectData = val
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
