<template>
  <div v-if="config!=null">

    <Card ref="card" :bordered="false" :dis-hover="true" :padding="32" style="border-radius: 8px">
      <div id="config-version">
        <div style="white-space:nowrap;" :style="{width:config.versionList.length*(210+2)+'px'}">
          <Card v-for="(item,index) in config.versionList" :key="index" :bordered="false" :dis-hover="true" class="version-box"
                :style="{'border-color':selectVersion.id==item.id?'#1F8ECF':'#E8E8E8','background-color':selectVersion.id==item.id?'#1F8ECF0D':'#FCFCFC'}">
            <a href="javascript:void(0)" @click="checkVersion(item)">
              <Icon type="md-pricetag" :color="selectVersion.id==item.id?'#1F8ECF':'#969b9e'"/>
              <p style="font-size: 22px;font-weight: 600" :style="{color:selectVersion.id==item.id?'#1F8ECF':'#969b9e'}">v{{ item.version }}</p>
            </a>
          </Card>
          <div style="clear: both"></div>
        </div>
      </div>

      <div style="margin-top: 30px">
        <Row>
          <Col :span="12">
            <span v-text="config.name" style="color: #202224;font-size: 22px;font-weight: 600"></span>
          </Col>
          <Col :span="12" style="text-align: right">
            <Button type="primary" ghost @click="edit" style="width: 90px">更新</Button>
          </Col>
        </Row>
      </div>
      <div style="width: 50%;margin-top: 50px">
        <Row>
          <Col :span="12" style="margin-bottom: 30px">
            <span style="color: #8D8D8D;font-size: 18px">类型：</span>
            <span style="color: #393939;font-size: 18px" v-if="config.type=='0'">键值对</span>
            <span style="color: #393939;font-size: 18px" v-if="config.type=='1'">文件</span>
            <span style="color: #393939;font-size: 18px" v-if="config.type=='2'">SSL证书</span>
          </Col>
          <Col :span="12"  style="margin-bottom: 30px">
            <span style="color: #8D8D8D;font-size: 18px">版本：</span>
            <span style="color: #393939;font-size: 18px" v-text="'v'+selectVersion.version"></span>
          </Col>
          <Col :span="12"  style="margin-bottom: 30px">
            <span style="color: #8D8D8D;font-size: 18px">描述：</span>
            <span style="color: #393939;font-size: 18px" v-text="config.remark"></span>
          </Col>
          <Col :span="12"  style="margin-bottom: 30px">
            <span style="color: #8D8D8D;font-size: 18px">创建时间：</span>
            <span style="color: #393939;font-size: 18px" v-text="selectVersion.createTime">键值对</span>
          </Col>
        </Row>
      </div>

      <Tabs class="config-details-tab" size="small" :animated="false">
        <TabPane label="总览">
          <div v-if="config.type=='0'">
            <el-table
                    :header-row-style="()=>{return 'color: #202224'}"
                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                    size="mini"
                    style="width: 500px"
                    :data="selectVersion.content">
              <el-table-column
                      prop="key"
                      label="键">
              </el-table-column>
              <el-table-column
                      prop="value"
                      label="值">
              </el-table-column>
            </el-table>
          </div>
          <code-editor v-if="config.type=='1'" v-model="selectVersion.content" :read-only="true"></code-editor>

          <div v-if="config.type=='2'">
            <Form :label-width="120">
              <FormItem label="证书版本：">
                <span v-text="selectVersion.content.version"></span>
              </FormItem>
              <FormItem label="证书序列号：">
                <span v-text="selectVersion.content.serial"></span>
              </FormItem>
              <FormItem label="证书签发日期：">
                <span v-text="selectVersion.content.issue"></span>
              </FormItem>
              <FormItem label="证书到期日期：">
                <span v-text="selectVersion.content.expire"></span>
              </FormItem>
              <FormItem label="域名：">
                <span v-text="selectVersion.content.domain"></span>
              </FormItem>
              <FormItem label="颁发机构：">
                <span v-text="selectVersion.content.issuer"></span>
              </FormItem>
              <FormItem label="签名算法：">
                <span v-text="selectVersion.content.algorithm"></span>
              </FormItem>
            </Form>
          </div>
        </TabPane>
        <TabPane label="引用列表">
          <div v-if="appList!=null&&appList.length>0&&(config.type=='0'||config.type=='1')">
            <div v-for="item in appList" v-if="item.app!=null" style="background-color: #F9F9F9;border-radius: 8px;padding: 12px;margin-bottom: 25px">
              <div style="color: #202224;font-weight: 600;font-size: 16px">
                <span v-text="item.app.name"></span>
              </div>
              <Row style="margin-top: 20px">
                <Col span="8">
                  <span>标识：</span>
                  <span v-text="item.app.sign"></span>
                </Col>
                <Col span="8" v-if="config.type=='1'">
                  <span>挂载目录：</span>
                  <span v-text="item.catalog"></span>
                </Col>
                <Col span="8" v-if="config.type=='1'">
                  <span>文件名：</span>
                  <span v-text="item.fileName"></span>
                </Col>
              </Row>
            </div>
          </div>


          <el-timeline v-else-if="config.type=='2'" :colon="false">
            <el-timeline-item v-for="item in appList"
                              :timestamp="item.protocol+'://'+item.domainPrefix+'.'+item.domain.domain"
                              placement="top">
              <el-card v-for="rule in item.appDomainRuleList" style="margin-bottom: 10px">

                <el-descriptions :column="1">
                  <el-descriptions-item label="访问路径：">
                    <el-link type="primary"
                             v-text="item.protocol+'://'+item.domainPrefix+'.'+item.domain.domain+rule.path"
                             :href="item.protocol+'://'+item.domainPrefix+'.'+item.domain.domain+rule.path"
                             target="_blank"></el-link>
                  </el-descriptions-item>
                  <el-descriptions-item label="应用：">
                    <span v-text="rule.app.name"></span>
                  </el-descriptions-item>
                  <el-descriptions-item label="Service：">
                    <span v-text="rule.serviceSign"></span>
                  </el-descriptions-item>
                  <el-descriptions-item label="端口：">
                    <span v-text="rule.port"></span>
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else></el-empty>
        </TabPane>
      </Tabs>
    </Card>



    <!-- 编辑配置文件 start -->
    <Modal
        v-model="showEdit"
        title="编辑配置"
        :mask-closable="false"
        width="1000">
      <div v-if="config.type==0&&editVersion!=null">
        <Row v-for="(item,index) in configContent" style="margin-bottom: 10px">
          <Col span="8">
            <Input v-model="item.key" size="large" placeholder="键"/>
          </Col>
          <Col span="13">
            <Input style="margin-left: 5px" v-model="item.value" size="large" placeholder="值"/>
          </Col>
          <Col span="3">
            <Icon v-if="configContent.length>1" type="md-close" @click="deleteConfigContent(index)"
                  style="font-size: 24px;margin-top: 5px;cursor: pointer;color: #ed4014;margin-left: 10px"/>
          </Col>
        </Row>
        <div style="color: #1F8ECF;font-size: 12px;cursor: pointer;margin-top: 5px" @click="addConfigContent">添加</div>
      </div>

      <code-editor v-if="config.type=='1'&&editVersion!=null" v-model="editVersion.content" :height="800" :read-only="false"></code-editor>


      <div v-if="config.type==2&&editVersion!=null">
        <Form :label-width="80">
          <FormItem label="证书">
            <Input v-model="editVersion.content.cert" type="textarea" :autosize="{minRows: 2,maxRows: 10}"
                   placeholder="证书"/>
          </FormItem>
          <FormItem label="私钥">
            <Input v-model="editVersion.content.key" type="textarea" :autosize="{minRows: 2,maxRows: 10}"
                   placeholder="私钥"/>
          </FormItem>
        </Form>


      </div>
      <div slot="footer">
        <Button type="primary" size="large" @click="saveVersion">保存</Button>
      </div>
    </Modal>
    <!-- 编辑配置文件 end -->

    <!-- 更新关联应用 start -->
    <Modal
        v-model="showApp"
        title="更新关联"
        :mask-closable="false"
        width="350">
      <CheckboxGroup v-model="selectApp" v-if="oldAppList!=null&&oldAppList.length>0">
        <CellGroup>
          <template v-if="config.type==0||config.type==1" v-for="item in oldAppList">
            <Cell :title="item.app.name">
              <div slot="label" v-text="item.app.sign"></div>
              <Checkbox :label="item.id" slot="extra">选择</Checkbox>
            </Cell>
            <Divider/>
          </template>

          <template v-if="config.type==2" v-for="item in oldAppList">
            <Cell :title="item.protocol+'://'+item.domainPrefix+'.'+item.domain.domain">
              <Checkbox :label="item.id" slot="extra">选择</Checkbox>
            </Cell>
            <Divider/>
          </template>
        </CellGroup>
      </CheckboxGroup>
      <el-empty v-else></el-empty>
      <div slot="footer">
        <Button type="primary" size="large" long :disabled="oldAppList==null||oldAppList.length<=0" @click="syncApp">
          更新
        </Button>
      </div>
    </Modal>
    <!-- 更新关联应用 end -->
  </div>
</template>
<script>
import {codemirror} from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/addon/fold/foldgutter.css'
import {delConfig, getConfig} from '@/api/kube/config'
import {addConfigVersion} from '@/api/kube/configVersion'
import {getByVersion, updateAppVersion} from '@/api/kube/appConfig'
//import {updateAppDomainVersion} from '@/api/app/appDomain'

import dataUtils from '@/utils/dataUtils'
import appUtils from '@/utils/appUtils'

export default {
  name: 'config-details',
  components: {
    codemirror
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      config: null//配置
      , selectVersion: null//选择的版本
      , showEdit: false//显示编辑
      , editVersion: null//当前编辑版本
      , cmOptions: {
        mode: 'text/html',
        height: 500,
        lineNumbers: true,
        lineWrapping: false,
        foldGutter: true,
        gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter']
      }
      , showApp: false//是否显示同步关联应用
      , selectApp: []//选择的应用
      , appList: []//关联的应用
      , oldAppList: []//老的应用列表
      , configContent: []//配置文件内容
    }
  },
  computed: {
    codemirrorDom() {
      return this.$refs['myCm'].codemirror
    }
  },
  methods: {
    //获取数据
    getData() {
      getConfig(this.id).then(res => {
        appUtils.res(res, () => {
          if (res.data == null) {
            appUtils.showError('未查询到数据！')
            setTimeout(() => {
              this.$router.push('/config/config');
            }, 1000)
            return
          }
          this.config = res.data

          this.selectVersion = JSON.parse(JSON.stringify(res.data.versionList[0]));
          if (this.config.type == 0 || this.config.type == 2) {
            this.selectVersion.content = JSON.parse(res.data.versionList[0].content)
          }
          this.getApp(this.selectVersion.id)
        })
      })
    },
    //切换版本
    checkVersion(item) {
      if (this.selectVersion.id != item.id) {
        this.getApp(item.id)
        let flag = true
        if (item.content != this.selectVersion.content) {
          flag = false
        }
        this.selectVersion = JSON.parse(JSON.stringify(item));
        if (this.config.type == 0) {
          this.selectVersion.content = JSON.parse(item.content)
        }
        if (this.config.type == 2) {
          this.selectVersion.content = JSON.parse(item.content)
        }
        if (!flag) {
          if (this.config.type == 1) {
            this.initCode()
          }
        }
      }
    },

    //编辑配置
    edit() {
      this.editVersion = JSON.parse(JSON.stringify(this.selectVersion))
      if (this.config.type == 0) {
        this.configContent = JSON.parse(JSON.stringify(this.selectVersion)).content
      }
      if (this.config.type == 1) {
        setTimeout(() => {
          if (this.codemirrorDom != null) {
            this.codemirrorDom.setSize(null, 600)
          }
        }, 500)
      }
      this.showEdit = true;
    },
    //编辑器内容发生改变
    changes(val) {

      this.editVersion.content = this.codemirrorDom.getValue()
    },
    //保存版本
    saveVersion() {
      if (this.config.type == 0) {
        this.editVersion.content = JSON.stringify(this.configContent)
        for (let i = 0; i < this.configContent.length; i++) {
          if (this.configContent[i].key == null || this.configContent[i].value == null) {
            appUtils.showError('请输入正确的键值！')
            return false
          }
        }
      }

      if (this.config.type == 2) {
        if (this.editVersion.content.cert == null || this.editVersion.content.cert.length <= 0) {
          appUtils.showError('请输入证书！')
          return false
        }
        if (this.editVersion.content.key == null || this.editVersion.content.key.length <= 0) {
          appUtils.showError('请输入私钥！')
          return false
        }
        this.editVersion.content = JSON.stringify(this.editVersion.content)
      }

      addConfigVersion(this.editVersion).then(res => {
        appUtils.res(res, () => {
          appUtils.showSuccess(res.msg)
        })
        this.showEdit = false;
        this.oldAppList = JSON.parse(JSON.stringify(this.appList))
        this.getData()
        if (this.oldAppList != null && this.oldAppList.length > 0) {
          this.$Modal.confirm({
            title: '提示',
            content: '是否同步关联？',
            loading: true,
            onOk: () => {
              this.$Modal.remove()
              this.showApp = true
              this.selectApp = []
              dataUtils.each(this.oldAppList, (key, obj) => {
                this.selectApp.push(obj.id)
              })
            }
          })
        }
      })
    },
    //获取关联应用
    getApp(versionId) {
      getByVersion(versionId).then(res => {
        appUtils.res(res, () => {
          this.appList = res.data
        })
      })
    },
    // 删除
    del(row) {
      this.$Modal.confirm({
        title: '提示',
        content: '确定删除吗？',
        loading: true,
        onOk: () => {
          delConfig(this.config.id).then(res => {
            this.$Modal.remove()
            if (res.state) {
              appUtils.showSuccess(res.msg)
              this.$router.push('/config/config');
            } else {
              appUtils.showError(res.msg)
            }
          })
        }
      })
    },
    //同步应用
    syncApp() {
      if (this.selectApp == null || this.selectApp.length <= 0) {
        appUtils.showError('请选择关联！')
        return
      }
      if (this.config.type == 0 || this.config.type == 1) {
        updateAppVersion({versionId: this.selectVersion.id, idList: this.selectApp}).then(res => {
          appUtils.res(res, () => {
            this.showApp = false
            appUtils.showSuccess(res.msg)
            this.getData()
          })
        })
      } else if (this.config.type == 2) {
        updateAppDomainVersion({versionId: this.selectVersion.id, idList: this.selectApp,}).then(res => {
          appUtils.res(res, () => {
            this.showApp = false
            appUtils.showSuccess(res.msg)
            this.getData()
          })
        })
      }

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
    }

  },
  mounted() {
    this.getData()
  }
}
</script>
<style lang="less">
#config-version {
  overflow-x: scroll;
  text-align: center;

  .ivu-card-body {
    padding: 0;
  }

  i {
    font-size: 32px;
  }

  a {
    display: block;
    color: inherit;
    padding: 16px;
  }

  p {
    margin-top: 8px;
  }

  text-align: center;

  .ivu-card-body {
    padding: 0;
  }

  i {
    font-size: 32px;
  }

  a {
    display: block;
    color: inherit;
    padding: 16px;
  }

  p {
    margin-top: 8px;
  }
}

#config-version .version-box {
  width: 200px;
  float: left;
  margin-right: 10px;
  margin-bottom: 10px;
  background-color: #1F8ECF0D;
  border: 1px solid #1F8ECF;
}

.config-details-tab .ivu-tabs-nav .ivu-tabs-tab {
  font-weight: 400 !important;
  font-size: 18px !important;
  color: #929292;
}

.config-details-tab .ivu-tabs-nav .ivu-tabs-tab-active {
  color: #202224;
  font-weight: 600 !important;
}
</style>
