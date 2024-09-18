<!--
   推送接收人
   author： xiaoke
   date： 2024-08-28 00:33:59
-->

<template>
   <div>
      <!-- 编辑推送接收人 start -->
      <Modal
              v-model="showEdit"
              :title="editType?'编辑推送接收人':'添加推送接收人'"
              width="600">
         <Form ref="pushUserForm" :model="pushUserForm"
               :rules="pushUserValidate" :label-width="100" label-position="right">
            <Row>
               <Col :span="24">
                  <FormItem label="用户：" prop="projectUserId">
                     <Select v-model="pushUserForm.projectUserId" size="large" filterable>
                        <Option v-for="item in userList" :key="item.id" :value="item.id" :label="item.user.name">
                           <span v-text="item.user.name"></span>
                           <span style="float:right;color:#ccc" v-text="item.user.username"></span>
                        </Option>
                     </Select>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="推送类型：" prop="pushType">
                     <CheckboxGroup size="large" v-model="pushUserForm.pushType">
                        <Checkbox  label="email">邮件</Checkbox>
                     </CheckboxGroup>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="内容类型：" prop="contentType">
                     <RadioGroup size="large" v-model="pushUserForm.contentType">
                        <Radio :label="0">异常</Radio>
                        <Radio :label="1">操作</Radio>
                        <Radio :label="2">全部</Radio>
                     </RadioGroup>
                  </FormItem>
               </Col>
            </Row>
         </Form>
         <div slot="footer">
            <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
         </div>
      </Modal>
      <!-- 编辑推送接收人 end -->
   </div>
</template>
<script>
   import {
      getPushUser,
      delPushUser,
      addPushUser,
      updatePushUser
   } from '@/api/kube/pushUser'
   import appUtils from '@/utils/appUtils';
   import Setting from '@/setting';
   import dataUtils from '@/utils/dataUtils';
   import {listProjectUser} from '@/api/kube/projectUser'

   export default {
      props: {
         //扩展数据
         extend:{
            type: Object
         }
      },
      data() {

         return {
            // 推送接收人表单
            pushUserForm: {
               id: null,
               projectUserId: null,
               pushType: null,
               contentType: null,
            }
            //保存密等
            , saveLoading: false
            //参数验证
            , pushUserValidate: {
               projectUserId:[
                  {type: 'number', required: true, message: '用户不能为空', trigger: 'blur'},
               ],
               pushType:[
                  {type: 'array', required: true, message: '推送类型不能为空', trigger: 'blur'},
               ],
               contentType:[
                  {type: 'number', required: true, message: '内容类型不能为空', trigger: 'blur'},
               ]
            }
            //是否显示编辑
            ,showEdit:false
            //编辑类型 true=编辑 false=添加
            ,editType:true
            ,userList:[]
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
         handleUpdate (pushUserForm) {
            updatePushUser(pushUserForm).then(res => {
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
         handleSave(pushUserForm) {
            addPushUser(pushUserForm).then(res => {
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
            if (this.$refs['pushUserForm'] != null) {
               this.$refs['pushUserForm'].resetFields();
            }
            this.projectUser()
            if (row != null) {
               let loading = appUtils.showLoading()
               getPushUser(row.id).then(res=>{
                  appUtils.hideLoading(loading)
                  if(res.state){
                     let model = res.data

                     if(model.pushType!=null){
                        model.pushType = JSON.parse(model.pushType)
                     }

                     this.pushUserForm = model
                     this.showEdit = true
                  }else{
                     appUtils.showError(res.msg)
                  }
               }).catch(err=>{
                  appUtils.hideLoading(loading)
               })
            } else {
               this.pushUserForm = {
                  id: null,
                  projectUserId: null,
                  pushType: null,
                  contentType: null,
               }
               this.showEdit = true
            }
         },
         /**
          * 保存数据
          */
         saveEdit() {
            this.$refs['pushUserForm'].validate((valid) => {
               if (!valid) {
                  return false
               }
               let pushUserForm = JSON.parse(JSON.stringify(this.pushUserForm))
               pushUserForm.pushType = JSON.stringify(pushUserForm.pushType)
               this.saveLoading = true
               if (pushUserForm.id != null) {
                  this.handleUpdate(pushUserForm)
               } else {
                  this.handleSave(pushUserForm)
               }
            })
         },

         /**
          * 删除数据
          */
         rowDel(row) {
            let loading = appUtils.showLoading('正在删除...')
            delPushUser(row.id).then((res) => {
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
         //获取项目人员
         projectUser() {
            let project = dataUtils.getData(Setting.key.project)
            listProjectUser({'projectId': project}).then(res => {
               this.userList = res.data
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
