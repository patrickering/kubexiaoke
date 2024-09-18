<!--
   异常
   author： xiaoke
   date： 2020-08-24 21:37:54
-->

<template>
   <div>
      <!-- 编辑异常 start -->
      <Modal
              v-model="showEdit"
              title="编辑异常"
              width="600">
         <Form ref="errorForm" :model="errorForm"
               :rules="errorValidate" :label-width="100" label-position="right">
            <Row>
               <Col :span="24">
                  <FormItem label="类型：" prop="type">
                     <Select v-model="errorForm.type"  clearable filterable>
                        <Option v-for="item in extend.errorTypeList" :key="item.value" :value="item.value">{{item.label}}</Option>
                    </Select>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="路径：" prop="path">
                     <Input v-model="errorForm.path" clearable   ></Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="异常信息：" prop="message">

                  </FormItem>
               </Col>
            </Row>
         </Form>
         <div slot="footer">
            <Button type="primary" size="large" long :loading="saveLoading" @click="saveEdit">保存</Button>
         </div>
      </Modal>
      <!-- 编辑异常 end -->
   </div>
</template>
<script>
   import {
      delError,
      addError,
      updateError
   } from '@/api/system/error'

   import appUtils from '@/utils/appUtils';
   import {rule} from '@/utils/validateRules';

   export default {
      props: {
         //扩展数据
         extend:{
            type: Object
         }
      },
      data() {
         return {
            // 异常表单
            errorForm: {
                     id: null,
                     type: null,
                     path: null,
                     message: null,
                     delFlag: null,
                     createBy: null,
                     createTime: null,
                     updateBy: null,
                     updateTime: null
            }
            //保存密等
            , saveLoading: false
                 //参数验证
            , errorValidate: {
            type:[
               {required: true, message: '类型不能为空', trigger: 'blur'},
            ],
            path:[
               {required: true, message: '路径不能为空', trigger: 'blur'},
               { max: 255, message: '路径最大长度255', trigger: 'blur' },
               { min: 1, message: '路径最小长度1', trigger: 'blur' },
            ],
            message:[
               {required: true, message: '异常信息不能为空', trigger: 'blur'},
            ]
            }
            //是否显示编辑
            ,showEdit:false
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
                  this.edit(null)
                  break;
               case 'edit':
                  //修改
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
         handleUpdate: function () {
            updateError(this.errorForm).then(res => {
               if(res.state){
                  appUtils.showSuccess(res.msg)
                  this.refresh()
                  this.showEdit = false
               }else {
                  appUtils.showError(res.msg)
               }
               this.saveLoading = false
            }).catch(() => {
               this.showEdit = false
               this.saveLoading = false
            })
         },

         /**
          * 处理数据添加
          */
         handleSave: function () {
            addError(this.errorForm).then(res => {
               this.saveLoading = false
               if (res.state) {
                  this.refresh()
                  appUtils.showSuccess(res.msg)
                  this.showEdit = false
               } else {
                  appUtils.showError(res.msg)
               }
            }).catch(() => {
               this.showEdit = false
               this.saveLoading = false
            })
         },

         /**
          * 打开编辑
          * @param row
          */
         edit(row) {
            if (this.$refs['errorForm'] != null) {
               this.$refs['errorForm'].resetFields();
            }
            this.showEdit = true
            if (row != null) {
               let model = JSON.parse(JSON.stringify(row))
               this.errorForm = model
            } else {
               this.errorForm = {
                        id: null,
                        type: null,
                        path: null,
                        message: null,
                        delFlag: null,
                        createBy: null,
                        createTime: null,
                        updateBy: null,
                        updateTime: null
               }
            }
         },

         /**
          * 保存数据
          */
         saveEdit() {
            this.$refs['errorForm'].validate((valid) => {
               if (!valid) {
                  return false
               }
               this.saveLoading = true
               if (this.errorForm.id != null) {
                  this.handleUpdate()
               } else {
                  this.handleSave()
               }
            })
         },

         /**
          * 删除数据
          */
         rowDel: function (row) {
            let loading = appUtils.showLoading('正在删除...')
            delError(row.id).then((res) => {
               appUtils.hideLoading(loading)
               if (res.state) {
                  this.refresh(this.page)
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
