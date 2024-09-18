<template>
  <div style="width: 1200px;margin: 0 auto">
    <Card :bordered="false" dis-hover >
      <Row :gutter="24">
        <Col :span="8">
          <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
            <div style="color: #202224;font-size: 24px;font-weight: 600">菜单管理</div>
            <div :style="menuListStyle" style="margin-top: 10px">
              <div style="display: flex;align-items: center;">
                <Input v-model="query.name" search placeholder="输入菜单名称搜索"
                       @on-change="handleGetMenuList"/>
                <Dropdown v-permission="{'value':'sys_menu_add'}" style="margin-left: 5px">
                  <Button type="primary" ghost>
                    添加菜单
                    <Icon type="ios-arrow-down"></Icon>
                  </Button>
                  <DropdownMenu slot="list">
                    <DropdownItem @click.native="handleOpenCreateHeader">添加顶栏菜单</DropdownItem>
                    <DropdownItem :disabled="menuInfo==null||menuInfo.menuId==null" @click.native="handleOpenCreateSider">
                      添加子菜单
                    </DropdownItem>
                  </DropdownMenu>
                </Dropdown>
              </div>
              <el-tree
                      style="margin-top: 10px"
                      :data="treeData"
                      :default-expanded-keys="aExpandedKeys"
                      :expand-on-click-node="false"
                      :props="defaultProps"
                      class="filter-tree"
                      node-key="id"
                      highlight-current
                      @node-click="handleSelectNode">
              </el-tree>
            </div>
          </Card>
        </Col>
        <Col :span="16">
          <Card :bordered="false" dis-hover :padding="32" style="border-radius: 8px;box-shadow: 6px 6px 54px 0px rgba(0,0,0,0.05);">
            <div style="color: #202224;font-size: 24px;font-weight: 600" v-text="menuInfo.menuId==null?'添加菜单':'编辑菜单'"></div>
            <div style="margin-top: 10px" :style="menuListStyle" >
              <Form
                      v-if="menuInfo!=null"
                      ref="menuForm"
                      :model="menuInfo"
                      :rules="menuRules"
                      label-position="right"
                      :label-width="100"
                      label-colon
                      hide-required-mark>
                <FormItem prop="name" label="标题">
                  <Input v-model.trim="menuInfo.name" size="large" placeholder="请输入标题，必填"/>
                </FormItem>
                <FormItem prop="type" label="类型">
                  <RadioGroup v-model="menuInfo.type" size="large">
                    <Radio label="0" border>菜单</Radio>
                    <Radio label="1" border>权限</Radio>
                  </RadioGroup>
                </FormItem>
                <FormItem prop="path" label="地址" v-if="menuInfo.type==0">
                  <Input v-model.trim="menuInfo.path" size="large" placeholder="请输入地址，必填"/>
                </FormItem>
                <FormItem prop="icon" label="图标" v-if="menuInfo.type==0">
                  <Input v-model.trim="menuInfo.icon" size="large" placeholder="请选择图标"/>
                  <Alert style="margin-top: 5px">以“i-”开头为图标，其他为图片</Alert>
                </FormItem>
                <FormItem prop="sort" label="排序">
                  <InputNumber v-model="menuInfo.sort" size="large" style="width: 100%;" :min="0" placeholder="请输入排序"></InputNumber>
                </FormItem>
                <FormItem prop="keepAlive" label="路由缓冲" v-if="menuInfo.type==0">
                  <Switch v-model="menuInfo.keepAlive" true-value="0" false-value="1"/>
                </FormItem>
                <FormItem prop="permission" v-if="menuInfo.type==1">
                  <div slot="label">
                    权限标识
                  </div>
                  <Input v-model.trim="menuInfo.permission" size="large" placeholder="权限标识"/>
                </FormItem>
                <FormItem style="text-align: right">
                  <Button size="large" v-if="menuInfo.menuId==null" v-permission="{'value':'sys_menu_add'}" type="primary" @click="handleSubmit" :loading="submitting">保存</Button>
                  <Button size="large" v-else v-permission="{'value':'sys_menu_edit'}" type="primary" @click="handleSubmit" :loading="submitting">修改</Button>
                  <Button size="large" v-if="menuInfo.menuId!=null" v-permission="{'value':'sys_menu_del'}" type="error" @click="handleMenuDelete" class="ivu-ml">删除</Button>
                </FormItem>
              </Form>
            </div>
          </Card>
        </Col>
      </Row>
    </Card>
  </div>
</template>
<script>
  import { addObj, delObj, fetchMenuTree, getObj, putObj } from '@/api/system/menu'
  import appUtils from "@/utils/appUtils";

  export default {
    name: 'system-menu',
    data() {
      return {
        treeData: [],//菜单数据
        query: {//查询条件
          name: null
        },
        aExpandedKeys: [],
        defaultProps: {//树自定义key
          children: 'children',
          label: 'name'
        },
        menuInfo: {//菜单实体
          menuId: null,
          name: null,
          sort: null,
          keepAlive: '0',
          parentId: -1,
          path: null,
          permission: null,
          type: '0',
          icon: null,
        },
        submitting: false,//保存密等
        menuRules: {
          name: [
            {required: true, message: '请输入标题', trigger: 'blur'}
          ],
          sort: [
            {type:'number',required: true, message: '请输入排序', trigger: 'blur'}
          ],
          path: [
            {required: true, message: '请输入路径', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      menuListStyle() {
        return {
          height: document.body.clientHeight - 280 + 'px',
          overflow: 'auto'
        }
      }
    },
    methods: {
      // 获取菜单数据
      handleGetMenuList() {
        let loading = appUtils.showLoading()
        fetchMenuTree(this.query).then(res => {
          if(res.state){
            this.treeData = res.data
          }else {
            appUtils.showError('加载菜单失败！')
          }
          appUtils.hideLoading(loading)
        }).catch(err => {
          appUtils.hideLoading(loading)
        })
      },
      //选择菜单
      handleSelectNode(item) {
        getObj(item.id).then(res => {
          if(res.state){
            this.menuInfo = res.data
          }else {
            appUtils.showError('加载菜单失败！')
          }
        })
      },
      //添加顶级菜单
      handleOpenCreateHeader() {
        this.menuInfo = {
          menuId: null,
          name: null,
          sort: null,
          keepAlive: '0',
          parentId: -1,
          path: null,
          permission: null,
          type: '0',
          icon: null,
        }
        this.handleReset()
      },
      //添加子菜单
      handleOpenCreateSider() {
        let parentId = this.menuInfo.menuId
        this.menuInfo = {
          menuId: null,
          name: null,
          sort: null,
          keepAlive: '0',
          parentId: parentId,
          path: null,
          permission: null,
          type: '0',
          icon: null,
        }
        this.handleReset()
      },
      //保存
      handleSubmit() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {
            this.submitting = true;
            if (this.menuInfo.menuId == null) {
              addObj(this.menuInfo).then(res => {
                if (res.state) {
                  this.submitting = false;
                  this.menuInfo = {
                    menuId: null,
                    name: null,
                    sort: null,
                    keepAlive: '0',
                    parentId: -1,
                    path: null,
                    permission: null,
                    type: '0',
                    icon: null,
                  }
                  this.handleReset()
                  appUtils.showSuccess('添加菜单成功！')
                  // 完成后刷新数据
                  this.handleGetMenuList();
                } else {
                  appUtils.showError('添加菜单失败！')
                  this.submitting = false;
                }
              }).catch(err => {
                appUtils.showError('添加菜单失败！')
                this.submitting = false;
              });
            } else {
              putObj(this.menuInfo).then(res => {
                if (res.state) {
                  this.submitting = false;
                  this.menuInfo = {
                    menuId: null,
                    name: null,
                    sort: null,
                    keepAlive: '0',
                    parentId: -1,
                    path: null,
                    permission: null,
                    type: '0',
                    icon: null,
                  }
                  appUtils.showSuccess('修改菜单成功！')
                  // 完成后刷新数据
                  this.handleGetMenuList();
                } else {
                  appUtils.showError('修改菜单失败！')
                  this.submitting = false;
                }
              }).catch(err => {
                appUtils.showError('修改菜单失败！')
                this.submitting = false;
              });
            }

          }
        });
      },
      //删除菜单
      handleMenuDelete() {
        this.$Modal.confirm({
          title: '删除确认',
          content: `确认要删除菜单<strong>${this.menuInfo.name}</strong>吗？`,
          loading: true,
          onOk: () => {
            delObj(this.menuInfo.menuId).then(res => {
              this.$Modal.remove();
              if (res.state) {
                appUtils.showSuccess('删除成功！')
                this.menuInfo = {
                  menuId: null,
                  name: null,
                  sort: null,
                  keepAlive: '0',
                  parentId: -1,
                  path: null,
                  permission: null,
                  type: '0',
                  icon: null,
                }
                // 完成后刷新数据
                this.handleGetMenuList();
              } else {
                appUtils.showSuccess('删除失败！')
              }
            }).catch(err => {
              this.$Modal.remove();
              appUtils.showSuccess('删除失败！')
            });
          }
        });
      },
      //重置表单
      handleReset() {
        if (this.$refs['menuForm'] != null) {
          this.$refs['menuForm'].resetFields();
        }
      }
    },
    mounted() {
      this.handleGetMenuList();
    }
  }
</script>
<style lang="less">
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
