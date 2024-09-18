<!--
   异常
   author： xiaoke
   date： 2020-08-24 21:37:54
-->

<template>
   <div>
      <el-table
              v-if="tableData!=null&&tableData.length>0"
              :data="tableData"
              size="mini"
              border
              @expand-change="expandChange"
              stripe
              :header-row-style="()=>{return 'color: #5c6b77'}"
              :header-cell-style="()=>{return 'background: #f8f8f9;line-height:35px'}"
              style="width: 100%;margin-top: 10px">
         <el-table-column type="expand">
            <template slot-scope="props">
               <pre class="layui-code" style="max-height: 600px;overflow: auto">
                  {{props.row.message}}
               </pre>
            </template>
         </el-table-column>
         <el-table-column prop="type"
                          width="100"
                          align="center"
                          :show-overflow-tooltip="true"
                          label="类型">
            <template slot-scope="scope">
               <Tag  v-for="(item,index) in extend.errorTypeList" :key="index" v-if="item.value==scope.row.type" :color="item.description" v-text="item.label"></Tag>
            </template>
         </el-table-column>
         <el-table-column prop="title"
                          align="left"
                          :show-overflow-tooltip="true"
                          label="标题">
         </el-table-column>
         <el-table-column prop="path"
                          align="left"
                          :show-overflow-tooltip="true"
                          label="路径">
         </el-table-column>
         <el-table-column prop="createTime"
                          align="center"
                          width="180"
                          :show-overflow-tooltip="true"
                          label="创建时间">
         </el-table-column>
         <el-table-column
                 label="操作"
                 align="center"
                 width="150">
            <template slot-scope="scope">
               <Poptip @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                  <el-button
                          v-permission="{'value':'system_error_del'}"
                          type="text"
                          size="mini"
                          icon="el-icon-delete"
                  >删除
                  </el-button>
               </Poptip>
            </template>
         </el-table-column>
      </el-table>
      <el-empty v-else></el-empty>

      <div style="display: flex;justify-content: flex-end;">
         <Page v-if="count>0" style="margin-top: 10px;margin-bottom: 10px" transfer @on-change="pageChange"
               @on-page-size-change="sizeChange"
               :current="query.current"
               prev-text="上一页"
               next-text="下一页" :total="count" show-elevator show-total show-sizer/>
      </div>
   </div>
</template>
<script>
   import {
      listError,
      pageError
   } from '@/api/system/error'

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
            tableData: []
            //数据总行数
            , count: 0
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
            let loading = appUtils.showLoading()
            pageError(this.query).then(res => {
               appUtils.hideLoading(loading)
               if(res.state){
                  this.tableData = res.data.records
                  this.count = res.data.total

               }else {
                  appUtils.showError(res.msg)
               }
            }).catch(err => {
               appUtils.hideLoading(loading)
            })
         },
         expandChange(){
            this.$nextTick(()=>{
               layui.code({
                  title:'异常信息',
                  about:false
               });
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
          * 事件
          * @param type 事件类型
          * @param data 事件数据
          */
         handleEvent(type, data) {
            this.$emit('on-events', {type: type, data: data});
         }
      },
      mounted() {
         this.getData()
      }
   }
</script>
