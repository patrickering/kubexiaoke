<!--
   存储
   author： xiaoke
   date： 2024-08-03 17:00:02
-->

<template>
   <div>
      <!-- 编辑存储 start -->
      <Modal
              v-model="showEdit"
              :title="editType?'编辑存储':'添加存储'"
              width="600">
         <Form ref="storageForm" :model="storageForm"
               :rules="storageValidate" :label-width="100" label-position="right">
            <Row>
               <Col :span="24">
                  <FormItem label="名称：" prop="name">
                     <Input v-model="storageForm.name" size="large" clearable  placeholder="请输入名称" ></Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="容量：" prop="capacity">
                     <Input v-model="storageForm.capacity" size="large" number placeholder="请输入容量">
                        <span slot="append">GB</span>
                     </Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="读写策略：" prop="strategy">
                     <RadioGroup v-model="storageForm.strategy" size="large">
                        <Radio label="ReadWriteMany">多节点读写</Radio>
                        <Radio label="ReadOnlyMany">多节点只读</Radio>
                        <Radio label="ReadWriteOnce">单节点读写</Radio>
                     </RadioGroup>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="描述：" prop="remark">
                     <Input type="textarea" v-model="storageForm.remark" clearable    placeholder="请输入描述" ></Input>
                  </FormItem>
               </Col>
            </Row>
         </Form>
         <div slot="footer">
            <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
         </div>
      </Modal>
      <!-- 编辑存储 end -->
   </div>
</template>
<script>
   import {
      getStorage,
      delStorage,
      addStorage,
      updateStorage
   } from '@/api/kube/storage'
   import appUtils from '@/utils/appUtils';

   export default {
      props: {
         //扩展数据
         extend:{
            type: Object
         }
      },
      data() {

         return {
            // 存储表单
            storageForm: {
               id: null,
               name: null,
               capacity: null,
               strategy: 'ReadWriteMany',
               remark: null,
            }
            //保存密等
            , saveLoading: false
            //参数验证
            , storageValidate: {
               name:[
                  { required: true, message: '名称不能为空', trigger: 'blur'},
                  { max: 100, message: '名称最大长度100', trigger: 'blur' },
               ],
               capacity:[
                  {type: 'number', required: true, message: '容量不能为空', trigger: 'blur'},
               ],
               strategy:[
                  { required: true, message: '请选择读写策略', trigger: 'blur'},
               ],
               remark:[
                  { max: 255, message: '描述最大长度255', trigger: 'blur' },
               ]
            }
            //是否显示编辑
            ,showEdit:false
            //编辑类型 true=编辑 false=添加
            ,editType:true
         }
      },
      computed: {},
      methods: {
         /**
          * 事件
          * @param type 事件类型
          * @param data 事件数据
          */
         handleEvent(type,data){
            switch (type) {
               case 'add':
                  //添加
                  this.editType = false
                  this.edit(null)
                  break;
               case 'edit':
                  //修改
                  this.editType = true
                  this.edit(data)
                  break;
               case 'del':
                  //删除
                  this.rowDel(data)
                  break;
            }
         },
         /**
          * 处理数据更新
          */
         handleUpdate (storageForm) {
            updateStorage(storageForm).then(res => {
               if(res.state){
                  appUtils.showSuccess(res.msg)
                  this.refresh()
                  this.showEdit = false
               }else {
                  appUtils.showError(res.msg)
               }
               this.saveLoading = false
            }).catch(() => {
               this.saveLoading = false
            })
         },

         /**
          * 处理数据添加
          */
         handleSave(storageForm) {
            addStorage(storageForm).then(res => {
               this.saveLoading = false
               if (res.state) {
                  this.refresh()
                  appUtils.showSuccess(res.msg)
                  this.showEdit = false
               } else {
                  appUtils.showError(res.msg)
               }
            }).catch(() => {
               this.saveLoading = false
            })
         },

         /**
          * 打开编辑
          * @param row
          */
         edit(row) {
            if (this.$refs['storageForm'] != null) {
               this.$refs['storageForm'].resetFields();
            }

            if (row != null) {
               let loading = appUtils.showLoading()
               getStorage(row.id).then(res=>{
                  appUtils.hideLoading(loading)
                  if(res.state){
                     let model = res.data
                     this.storageForm = model
                     this.showEdit = true
                  }else{
                     appUtils.showError(res.msg)
                  }
               }).catch(err=>{
                  appUtils.hideLoading(loading)
               })
            } else {
               this.storageForm = {
                  id: null,
                  name: null,
                  capacity: null,
                  strategy: 'ReadWriteMany',
                  remark: null,
               }
               this.showEdit = true
            }
         },
         /**
          * 保存数据
          */
         saveEdit() {
            this.$refs['storageForm'].validate((valid) => {
               if (!valid) {
                  return false
               }
               let storageForm = JSON.parse(JSON.stringify(this.storageForm))
               this.saveLoading = true
               if (storageForm.id != null) {
                  this.handleUpdate(storageForm)
               } else {
                  this.handleSave(storageForm)
               }
            })
         },

         /**
          * 删除数据
          */
         rowDel(row) {
            let loading = appUtils.showLoading('正在删除...')
            delStorage(row.id).then((res) => {
               appUtils.hideLoading(loading)
               if (res.state) {
                  this.refresh()
                  appUtils.showSuccess(res.msg)
               } else {
                  appUtils.showError(res.msg)
               }
            }).catch(function () {
               appUtils.hideLoading(loading)
            })
         },
         /**
          * 刷新数据
          */
         refresh(){
            this.$emit('on-refresh');
         }
      },
      mounted() {
      }
   }
</script>
