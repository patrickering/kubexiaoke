<!--
   模板
   author： xiaoke
   date： 2024-08-17 21:23:43
-->

<template>
   <div>
      <!-- 编辑模板 start -->
      <Modal
              v-model="showEdit"
              :title="editType?'编辑模板':'添加模板'"
              width="800">
         <Form ref="templateForm" :model="templateForm"
               :rules="templateValidate" :label-width="100" label-position="right">
            <Row>
               <Col :span="12">
                  <FormItem label="名称：" prop="name">
                     <Input v-model="templateForm.name" clearable  placeholder="请输入名称" ></Input>
                  </FormItem>
               </Col>
               <Col :span="12">
                  <FormItem label="类型：" prop="type">
                     <RadioGroup v-model="templateForm.type">
                        <Radio :label="0" border>配置文件</Radio>
                        <Radio :label="1" border>Dockerfile</Radio>
                     </RadioGroup>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="内容：" prop="content">
                     <code-editor v-model="templateForm.content"></code-editor>
                  </FormItem>
               </Col>
            </Row>
         </Form>
         <div slot="footer">
            <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
         </div>
      </Modal>
      <!-- 编辑模板 end -->
   </div>
</template>
<script>
   import {
      getTemplate,
      delTemplate,
      addTemplate,
      updateTemplate
   } from '@/api/kube/template'
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
            // 模板表单
            templateForm: {
               id: null,
               name: null,
               type: 0,
               content: null,
            }
            //保存密等
            , saveLoading: false
            //参数验证
            , templateValidate: {
               name:[
                  { required: true, message: '名称不能为空', trigger: 'blur'},
                  { max: 100, message: '名称最大长度100', trigger: 'blur' },
               ],
               type:[
                  {type: 'number', required: true, message: '类型不能为空', trigger: 'blur'},
               ],
               content:[
                  { required: true, message: '内容不能为空', trigger: 'blur'},
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
         handleUpdate (templateForm) {
            updateTemplate(templateForm).then(res => {
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
         handleSave(templateForm) {
            addTemplate(templateForm).then(res => {
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
            if (this.$refs['templateForm'] != null) {
               this.$refs['templateForm'].resetFields();
            }

            if (row != null) {
               let loading = appUtils.showLoading()
               getTemplate(row.id).then(res=>{
                  appUtils.hideLoading(loading)
                  if(res.state){
                     let model = res.data
                     this.templateForm = model
                     this.showEdit = true
                  }else{
                     appUtils.showError(res.msg)
                  }
               }).catch(err=>{
                  appUtils.hideLoading(loading)
               })
            } else {
               this.templateForm = {
                  id: null,
                  name: null,
                  type: 0,
                  content: null,
               }
               this.showEdit = true
            }
         },
         /**
          * 保存数据
          */
         saveEdit() {
            this.$refs['templateForm'].validate((valid) => {
               if (!valid) {
                  return false
               }
               let templateForm = JSON.parse(JSON.stringify(this.templateForm))
               this.saveLoading = true
               if (templateForm.id != null) {
                  this.handleUpdate(templateForm)
               } else {
                  this.handleSave(templateForm)
               }
            })
         },

         /**
          * 删除数据
          */
         rowDel(row) {
            let loading = appUtils.showLoading('正在删除...')
            delTemplate(row.id).then((res) => {
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
