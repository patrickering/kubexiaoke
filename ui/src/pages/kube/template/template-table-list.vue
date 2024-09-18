<!--
   模板
   author： xiaoke
   date： 2024-08-17 21:23:43
-->

<template>
   <div>
      <Row>
         <Col :span="18">
            <div style="color: #202224;font-size: 24px;font-weight: 600">模板列表</div>
         </Col>
         <Col :span="6" style="text-align: right">
            <Button v-permission="{'value':'kube_template_add'}" type="primary" size="large"
                    @click="handleEvent('add',null)" style="width: 140px">
               创建模板
            </Button>
         </Col>
      </Row>

      <div v-loading="loading">
         <el-table
                 :data="tableData"
                 :header-row-style="()=>{return 'color: #202224'}"
                 :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                 style="width: 100%;margin-top: 10px">
            <el-table-column prop="name"

                             align="left"
                             :show-overflow-tooltip="true"
                             label="名称">
            </el-table-column>
            <el-table-column prop="type"
                             align="left"
                             :show-overflow-tooltip="true"
                             label="类型">

               <template slot-scope="scope">
                  <Tag v-if="scope.row.type==0" color="#1F8ECF">配置文件</Tag>
                  <Tag v-if="scope.row.type==1" color="#1F8ECF">Dockerfile</Tag>
               </template>
            </el-table-column>
            <el-table-column prop="createName"
                             width="180"
                             align="left"
                             :show-overflow-tooltip="true"
                             label="创建人">
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
                          v-permission="{'value':'kube_template_edit'}"
                          type="text"
                          size="mini"
                          @click="handleEvent('edit',scope.row)">编辑
                  </el-button>
                  <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',scope.row)" transfer confirm title="确认删除?">
                     <el-button
                             style="color: #F45236"
                             v-permission="{'value':'kube_template_del'}"
                             type="text"
                             size="mini"
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
      pageTemplate,
   } from '@/api/kube/template'
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
            , loading: false
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
            pageTemplate(this.query).then(res => {
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
