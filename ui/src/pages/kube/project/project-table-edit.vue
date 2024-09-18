<!--
   项目
   author： xiaoke
   date： 2024-08-03 14:21:50
-->

<template>
   <div class="project-edit">
      <!-- 编辑项目 start -->
      <Modal
              v-model="showEdit"
              :title="editType?'编辑项目':'添加项目'"
              width="450">
         <Form ref="projectForm" :model="projectForm"
               :rules="projectValidate" :label-width="100" label-position="right">
            <Row>
               <Col :span="24">
                  <FormItem label="标题：" prop="title">
                     <Input v-model="projectForm.title" size="large" clearable  placeholder="请输入标题" ></Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="描述：" prop="remark">
                     <Input type="textarea" v-model="projectForm.remark" size="large" clearable    placeholder="请输入描述" ></Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="颜色：" prop="color">
                     <ColorPicker v-model="projectForm.color" size="large" />
                  </FormItem>
               </Col>
            </Row>
         </Form>
         <div slot="footer">
            <Button type="primary" size="large" long :loading="saveLoading" @click="saveEdit">保存</Button>
         </div>
      </Modal>
      <!-- 编辑项目 end -->
   </div>
</template>
<script>
   import {
      getProject,
      delProject,
      addProject,
      updateProject
   } from '@/api/kube/project'
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
            // 项目表单
            projectForm: {
               id: null,
               title: null,
               remark: null,
               color: null,
            }
            //保存密等
            , saveLoading: false
            //参数验证
            , projectValidate: {
               title:[
                  { required: true, message: '标题不能为空', trigger: 'blur'},
                  { max: 100, message: '标题最大长度100', trigger: 'blur' },
               ],
               remark:[
                  { max: 255, message: '描述最大长度255', trigger: 'blur' },
               ],
               color:[
                  { max: 20, message: '颜色最大长度20', trigger: 'blur' },
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
         handleUpdate (projectForm) {
            updateProject(projectForm).then(res => {
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
         handleSave(projectForm) {
            addProject(projectForm).then(res => {
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
            if (this.$refs['projectForm'] != null) {
               this.$refs['projectForm'].resetFields();
            }

            if (row != null) {
               let loading = appUtils.showLoading()
               getProject(row.id).then(res=>{
                  appUtils.hideLoading(loading)
                  if(res.state){
                     let model = res.data
                     this.projectForm = model
                     this.showEdit = true
                  }else{
                     appUtils.showError(res.msg)
                  }
               }).catch(err=>{
                  appUtils.hideLoading(loading)
               })
            } else {
               this.projectForm = {
                  id: null,
                  title: null,
                  remark: null,
                  color: null,
               }
               this.showEdit = true
            }
         },
         /**
          * 保存数据
          */
         saveEdit() {
            this.$refs['projectForm'].validate((valid) => {
               if (!valid) {
                  return false
               }
               let projectForm = JSON.parse(JSON.stringify(this.projectForm))
               this.saveLoading = true
               if (projectForm.id != null) {
                  this.handleUpdate(projectForm)
               } else {
                  this.handleSave(projectForm)
               }
            })
         },

         /**
          * 删除数据
          */
         rowDel(row) {
            let loading = appUtils.showLoading('正在删除...')
            delProject(row.id).then((res) => {
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
