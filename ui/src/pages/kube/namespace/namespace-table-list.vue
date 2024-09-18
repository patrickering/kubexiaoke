<!--
   命名空间
   author： xiaoke
   date： 2024-08-03 15:00:35
-->

<template>
   <div>
      <div style="margin-top: 20px">
         <Card :dis-hover="true" :padding="25" v-for="namespace in tableData" :key="namespace.id" style="margin-bottom: 30px">
            <Row>
               <Col span="8">
                  <div style="color: #1F8ECF;font-size: 20px;font-weight: 600">
                     命名空间：<span v-text="namespace.name"></span>
                  </div>
               </Col>
               <Col span="10">
                  <div style="display: flex;align-items: center;">
                     <div v-if="namespace.nameSpace!=null&&namespace.nameSpace.status!=null&&namespace.nameSpace.status.phase=='Active'" style="width: 58px;height: 24px;background-color: #3DB677;color: #FFFFFF;border-radius: 6px;text-align: center;line-height: 24px;font-size: 14px">
                        正常
                     </div>
                     <div v-else style="width: 58px;height: 24px;background-color: #ed4014;color: #FFFFFF;border-radius: 6px;text-align: center;line-height: 24px;font-size: 14px">
                        异常
                     </div>
                     <div style="background-color: rgba(31, 142, 207, 0.1);height: 24px;line-height: 24px;padding-left: 10px;padding-right: 10px;border-radius: 6px;margin-left: 10px">
                        <span style="font-size: 14px;color: #1F8ECF">标识：<span v-text="namespace.sign"></span></span>
                     </div>
                  </div>
               </Col>
               <Col span="6" style="text-align: right">
                  <div style="display: flex;align-items: center;">
                     <Button type="primary" style="margin-left: 20px" @click="toNamespace(namespace)">进入命名空间</Button>
                     <div style="width: 100px">
                        <img  v-permission="{'value':'kube_namespace_edit'}" src="@/assets/images/namespace-edit.png" @click="handleEvent('edit',namespace)" width="24" height="24" style="margin-right: 20px;cursor: pointer" alt="">
                        <Poptip style="margin-left: 10px" @on-ok="handleEvent('del',namespace)" transfer confirm title="确认删除?">
                           <img v-permission="{'value':'kube_namespace_del'}" src="@/assets/images/namespace-delete.png" width="24" height="24" style="cursor: pointer" alt="">
                        </Poptip>
                     </div>
                  </div>
               </Col>
            </Row>

            <Row :gutter="16" style="margin-top: 20px">
               <Col span="8">
                  <div style="background-color: rgba(245,245,245,0.5);border-radius: 6px;padding: 20px">
                     <div style="display: flex;align-items: center;">
                        <img src="@/assets/images/namespace-run.png" width="22" height="22" alt="">
                        <span style="color: #202224;font-size: 16px;font-weight: 600;margin-left: 10px">运行情况</span>
                     </div>
                     <div style="margin-top: 20px">
                        <Row :gutter="16">
                           <Col span="12" style="display: flex;align-items: center;">
                              <div style="color: #909090;font-size: 14px;font-weight: 600;width: 50px">应用</div>
                              <span style="color: #000000;font-size: 14px;margin-left: 20px;font-weight: 600" v-text="namespace.appCount"></span>
                           </Col>
                           <Col span="12" style="display: flex;align-items: center;">
                              <div style="color: #909090;font-size: 14px;font-weight: 600;width: 50px">运行中</div>
                              <span style="color: #000000;font-size: 14px;margin-left: 20px;font-weight: 600" v-text="namespace.runApp"></span>
                           </Col>
                           <Col span="12" style="margin-top: 10px;display: flex;align-items: center;">
                              <div style="color: #909090;font-size: 14px;font-weight: 600;width: 50px">异常</div>
                              <span style="color: #000000;font-size: 14px;margin-left: 20px;font-weight: 600" v-text="namespace.errorApp"></span>
                           </Col>
                           <Col span="12" style="margin-top: 10px;display: flex;align-items: center;">
                              <div style="color: #909090;font-size: 14px;font-weight: 600;width: 50px">停止</div>
                              <span style="color: #000000;font-size: 14px;margin-left: 20px;font-weight: 600" v-text="namespace.stopApp"></span>
                           </Col>
                        </Row>
                     </div>
                  </div>
               </Col>
               <Col span="8">
                  <div style="background-color: rgba(245,245,245,0.5);border-radius: 6px;padding: 20px">
                     <div style="display: flex;align-items: center;">
                        <img src="@/assets/images/namespace-cup.png" width="22" height="22" alt="">
                        <span style="color: #202224;font-size: 16px;font-weight: 600;margin-left: 10px">CPU</span>
                     </div>
                     <div style="margin-top: 20px;display: flex;align-items: center;" >
                        <div style="color: #909090;font-size: 14px;font-weight: 600">逻辑资源请求</div>
                        <Progress  style="width: 200px;margin-left: 30px" :stroke-color="['#3DC0A2', '#3DC0A2']" :stroke-width="4" :percent="percentage(namespace.nowCpuRequest,namespace.cpuRequest)" />
                     </div>
                     <div style="margin-top: 10px;display: flex;align-items: center;" >
                        <div style="color: #909090;font-size: 14px;font-weight: 600">逻辑资源上限</div>
                        <Progress  style="width: 200px;margin-left: 30px" :stroke-color="['#F78D7E', '#F78D7E']" :stroke-width="4" :percent="percentage(namespace.nowCpuLimit,namespace.cpuLimit)" />
                     </div>
                  </div>
               </Col>
               <Col span="8">
                  <div style="background-color: rgba(245,245,245,0.5);border-radius: 6px;padding: 20px">
                     <div style="display: flex;align-items: center;">
                        <img src="@/assets/images/namesparc-memory.png" width="22" height="22" alt="">
                        <span style="color: #202224;font-size: 16px;font-weight: 600;margin-left: 10px">内存</span>
                     </div>
                     <div style="margin-top: 20px;display: flex;align-items: center;" >
                        <div style="color: #909090;font-size: 14px;font-weight: 600">逻辑资源请求</div>
                        <Progress  style="width: 200px;margin-left: 30px" :stroke-color="['#3DC0A2', '#3DC0A2']" :stroke-width="4" :percent="percentage(namespace.nowMemoryRequest,namespace.memoryRequest)" />
                     </div>
                     <div style="margin-top: 10px;display: flex;align-items: center;" >
                        <div style="color: #909090;font-size: 14px;font-weight: 600">逻辑资源上限</div>
                        <Progress  style="width: 200px;margin-left: 30px" :stroke-color="['#F78D7E', '#F78D7E']" :stroke-width="4" :percent="percentage(namespace.nowMemoryLimit,namespace.memoryLimit)" />
                     </div>
                  </div>
               </Col>
            </Row>

         </Card>
      </div>
      <el-empty v-if="tableData!=null&& tableData.length<=0" description="暂无命名空间信息"></el-empty>
      <el-skeleton :loading="loading" :count="2" animated  />
   </div>
</template>
<script>
   import {
      listNamespace,
   } from '@/api/kube/namespace'
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
            this.query.current = 1
            this.getData()
         },

         /**
          * 获取数据
          */
         getData() {
            this.loading = true
            listNamespace(this.query).then(res => {
               this.loading = false
               if(res.state){
                  this.tableData = res.data
                  this.count = res.data.total
               }else {
                  appUtils.showError(res.msg)
               }
            }).catch(err => {
               this.loading = false
            })
         },
         toNamespace(namespace){
            dataUtils.setData(Setting.key.namespace,namespace.sign)
            dataUtils.setData(Setting.key.namespaceInfo,namespace)
            this.$router.push(
                    "/dashboard/console"
            );
         },
         //计算百分比
         percentage(args, args1) {
            if (args == null || args1 == null || args1 == 0) {
               return 0;
            }
            return Number(((args / args1) * 100).toFixed(0));
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
