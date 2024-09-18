<!--
   配置
   author： xiaoke
   date： 2024-08-03 16:10:50
-->

<template>
   <div>
      <!-- 编辑配置 start -->
      <Modal
              v-model="showEdit"
              :title="editType?'编辑配置':'添加配置'"
              width="600">
         <Form ref="configForm" :model="configForm"
               :rules="configValidate" :label-width="100" label-position="right">
            <Row>
               <Col :span="24">
                  <FormItem label="名称：" prop="name">
                     <Input :disabled="configForm.id!=null" v-model.trim="configForm.name" size="large" clearable  placeholder="请输入名称" ></Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="类型：" prop="type">
                     <Select v-model="configForm.type" size="large" :disabled="configForm.id!=null">
                        <Option value="0">键值对</Option>
                        <Option value="1">文件</Option>
                        <Option value="2">SSL证书</Option>
                     </Select>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="描述：" prop="remark">
                     <Input type="textarea" v-model="configForm.remark" clearable   placeholder="请输入描述" ></Input>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="配置：" prop="remark" v-if="configForm.id==null&&configForm.type==0">
                     <Row v-for="(item,index) in configContent" style="margin-bottom: 5px">
                        <Col span="8">
                           <Input v-model.trim="item.key" size="large" placeholder="键"/>
                        </Col>
                        <Col span="13">
                           <Input style="margin-left: 5px" size="large" v-model.trim="item.value" placeholder="值"/>
                        </Col>
                        <Col span="3">
                           <Icon v-if="configContent.length>1" type="md-close" @click="deleteConfigContent(index)"
                                 style="font-size: 24px;margin-top: 5px;cursor: pointer;color: #ed4014;margin-left: 10px"/>
                        </Col>
                     </Row>
                     <div style="color: #1F8ECF;font-size: 12px;cursor: pointer;margin-top: 5px" @click="addConfigContent">添加</div>
                  </FormItem>
               </Col>

               <Col :span="24">
                  <FormItem label="配置文件：" prop="remark" v-if="configForm.id==null&&configForm.type=='1'">
                     <!--<el-button type="text" @click="showTemplateEdit = true">{{ templateButton }}</el-button>-->
                     <Upload
                             ref="configUpload"
                             multiple
                             type="drag"
                             :format="['txt','conf','yml','config','xml','data','propertie']"
                             :headers="headers"
                             :action="getUploadUrl()"
                             :max-size="2048"
                             :on-success="uploadSuccess">
                        <div style="padding: 20px 0">
                           <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                           <p>点击或拖拽上传文件</p>

                        </div>
                     </Upload>
                     <p>上传类型：'txt','conf','yml','config','xml','data','propertie'</p>
                     <p>上传大小：最大2M</p>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="上传证书：" prop="remark" v-if="configForm.id==null&&configForm.type==2">
                     <Upload
                             ref="configUpload"
                             multiple
                             type="drag"
                             :format="['crt','pem']"
                             :headers="headers"
                             :action="getUploadUrl()"
                             :max-size="2048"
                             :on-success="uploadCertSuccess">
                        <div style="padding: 20px 0">
                           <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                           <p>点击或拖拽上传文件</p>

                        </div>
                     </Upload>
                  </FormItem>
               </Col>
               <Col :span="24">
                  <FormItem label="上传私钥：" prop="remark" v-if="configForm.id==null&&configForm.type==2">
                     <Upload
                             ref="configUpload"
                             multiple
                             type="drag"
                             :format="['key']"
                             :headers="headers"
                             :action="getUploadUrl()"
                             :max-size="2048"
                             :on-success="uploadKeySuccess">
                        <div style="padding: 20px 0">
                           <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                           <p>点击或拖拽上传文件</p>

                        </div>
                     </Upload>
                  </FormItem>
               </Col>
            </Row>
         </Form>
         <div slot="footer">
            <Button type="primary" size="large" :loading="saveLoading" @click="saveEdit">保存</Button>
         </div>
      </Modal>
      <!-- 编辑配置 end -->
   </div>
</template>
<script>
   import {
      getConfig,
      delConfig,
      addConfig,
      updateConfig
   } from '@/api/kube/config'
   import appUtils from '@/utils/appUtils';
   import Setting from "@/setting";
   import dataUtils from "@/utils/dataUtils";

   export default {
      props: {
         //扩展数据
         extend:{
            type: Object
         }
      },
      data() {

         return {
            // 配置表单
            configForm: {
               id: null,
               namespace: null,
               namespaceId: null,
               name: null,
               type: '0',
               content: '',
               version: null,
               remark: null,
            }
            //保存密等
            , saveLoading: false
            //参数验证
            , configValidate: {
               name: [
                  {required: true, message: '名称不能为空', trigger: 'blur'},
                  {message: '只能字母或数字', trigger: 'blur', pattern: /^[a-zA-Z0-9]{1,20}$/}
               ],
               type:[
                  { max: 10, message: '类型最大长度10', trigger: 'blur' },
               ],
               remark:[
                  { max: 255, message: '描述最大长度255', trigger: 'blur' },
               ]
            }
            //是否显示编辑
            ,showEdit:false
            //编辑类型 true=编辑 false=添加
            ,editType:true
            ,headers: {//上传header
               'token': dataUtils.getData(Setting.key.token),
            },
            configContent: [],//配置文件内容
         }
      },
      computed: {},
      methods: {
         getUploadUrl() {
            return Setting.configUploadUrl
         },
         //上传成功
         uploadSuccess(response, file, fileList) {
            this.configForm.content = response.data
         },
         //上传成功
         uploadCertSuccess(res, file, fileList) {
            if (res.state) {
               if (this.configForm.content == null) {
                  this.configForm.content = {}
               }
               this.configForm.content.cert = res.data.content
               this.configForm.content.version = res.data.version
               this.configForm.content.serial = res.data.serial
               this.configForm.content.issue = res.data.issue
               this.configForm.content.expire = res.data.expire
               this.configForm.content.domain = res.data.domain
               this.configForm.content.issuer = res.data.issuer
               this.configForm.content.algorithm = res.data.algorithm
            } else {
               appUtils.showError(res.msg)
            }

         },
         //上传成功
         uploadKeySuccess(response, file, fileList) {
            if (this.configForm.content == null) {
               this.configForm.content = {}
            }
            this.configForm.content.key = response.data
         },
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
         handleUpdate (configForm) {
            updateConfig(configForm).then(res => {
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
         handleSave(configForm) {
            addConfig(configForm).then(res => {
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
            if (this.$refs['configForm'] != null) {
               this.$refs['configForm'].resetFields();
            }

            if (row != null) {
               let loading = appUtils.showLoading()
               getConfig(row.id).then(res=>{
                  appUtils.hideLoading(loading)
                  if(res.state){
                     let model = res.data
                     this.configForm = model
                     this.showEdit = true
                  }else{
                     appUtils.showError(res.msg)
                  }
               }).catch(err=>{
                  appUtils.hideLoading(loading)
               })
            } else {
               this.configForm = {
                  id: null,
                  namespace: null,
                  namespaceId: null,
                  name: null,
                  type: '0',
                  content: null,
                  version: null,
                  remark: null,
               }
               if (this.configForm.type == 0) {
                  this.configContent = [
                     {
                        key: null,
                        value: null
                     }
                  ]
               }
               this.showEdit = true
            }
         },
         /**
          * 保存数据
          */
         saveEdit() {
            this.$refs['configForm'].validate((valid) => {
               if (!valid) {
                  return false
               }
               let configForm = JSON.parse(JSON.stringify(this.configForm))

               if (configForm.id == null && configForm.type == 1) {
                  if (configForm.content == null || configForm.content.length <= 0) {
                     appUtils.showError('请上传配置文件！')
                     return false
                  }
               }
               if (configForm.id == null && configForm.type == 0) {
                  configForm.content = JSON.stringify(this.configContent)
                  for (let i = 0; i < this.configContent.length; i++) {
                     if (this.configContent[i].key == null || this.configContent[i].value == null) {
                        appUtils.showError('请输入正确的键值！')
                        return false
                     }
                  }
               }
               if (configForm.id == null && configForm.type == 2) {
                  if (configForm.content == null || configForm.content.cert == null || configForm.content.cert.length <= 0) {
                     appUtils.showError('请上传证书！')
                     return false
                  }
                  if (configForm.content.key == null || configForm.content.key <= 0) {
                     appUtils.showError('请上传私钥！')
                     return false
                  }
                  configForm.content = JSON.stringify(configForm.content)
               }


               this.saveLoading = true
               if (configForm.id != null) {
                  this.handleUpdate(configForm)
               } else {
                  this.handleSave(configForm)
               }
            })
         },

         /**
          * 删除数据
          */
         rowDel(row) {
            let loading = appUtils.showLoading('正在删除...')
            delConfig(row.id).then((res) => {
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
         //添加键值对
         addConfigContent() {
            this.configContent.push({
               key: null,
               value: null
            })
         },
         //删除键值对
         deleteConfigContent(index) {
            this.configContent.splice(index, 1);
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
