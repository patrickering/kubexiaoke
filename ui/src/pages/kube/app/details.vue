<template>
    <div  v-resize="handleResize">
        <Card  ref="card" :bordered="false" :dis-hover="true" :padding="30" style="border-radius: 8px;position: relative">
            <div v-if="app!=null">
                <div style="position: absolute;right: 30px;top: 30px">
                    <Button v-permission="{'value':'kube_app_edit'}" v-if="app.state!=30&&app.state!=40" size="large"
                            type="primary" style="width: 100px;margin-right: 10px" ghost @click="handleUpdate(app)">更新
                    </Button>
                    <Button v-permission="{'value':'kube_app_edit'}" v-if="app.state!=30&&app.state!=40" size="large"
                            type="primary" style="width: 100px;margin-right: 10px" ghost @click="telescopic()">伸缩
                    </Button>
                    <Button v-permission="{'value':'kube_app_stop'}" v-if="app.state!=30&&app.state!=40" size="large"
                            type="primary" style="width: 100px;margin-right: 10px" ghost @click="stop()">停止
                    </Button>
                    <Button v-permission="{'value':'kube_app_start'}" v-if="app.state==40" size="large" type="primary"
                            style="width: 100px;margin-right: 10px" ghost @click="start()">启动
                    </Button>
                    <Button v-permission="{'value':'kube_app_restart'}" v-if="app.state!=30&&app.state!=40" size="large"
                            type="primary" style="width: 100px;margin-right: 10px" ghost @click="restart()">重启
                    </Button>
                    <Poptip @on-ok="deleteApp()" transfer confirm title="确认删除?">
                        <Button v-permission="{'value':'kube_app_del'}" size="large" type="error" style="width: 100px"
                                ghost>删除
                        </Button>
                    </Poptip>
                </div>
                <el-page-header @back="back" content="详情页面">
                    <div slot="title"></div>
                    <div slot="content" style="width: 100%">
                        <div style="display: flex;align-items: center;">
                            <div v-text="app.name" style="color: #202224;font-size: 24px;font-weight: 600"></div>
                            <Tag style="width: 60px;border-radius: 28px;margin-left: 10px" v-if="app.stateDict!=null" size="medium"
                                 :color="app.stateDict.description"
                                 v-text="app.stateDict.label"></Tag>
                        </div>
                    </div>
                </el-page-header>


                <el-descriptions :column="4" :colon="false" style="margin-top: 30px">
                    <el-descriptions-item>
                        <div slot="label" style="color: #8D8D8D;font-size: 16px;">标识：</div>
                        <span style="color:#202224;font-size: 16px" v-text="app.sign"></span>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <div slot="label" style="color: #8D8D8D;font-size: 16px;">命名空间：</div>
                        <span style="color:#202224;font-size: 16px" v-text="app.namespace"></span>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <div slot="label" style="color: #8D8D8D;font-size: 16px;">创建人：</div>
                        <span style="color:#202224;font-size: 16px" v-text="app.createName"></span>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <div slot="label" style="color: #8D8D8D;font-size: 16px;">更新时间：</div>
                        <span style="color:#202224;font-size: 16px" v-text="app.updateTime"></span>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <div slot="label" style="color: #8D8D8D;font-size: 16px;">部署数量：</div>
                        <span style="color:#202224;font-size: 16px" v-text="app.nowCount+'/'+app.count"></span>
                    </el-descriptions-item>
                    <el-descriptions-item :span="4">
                        <div slot="label" style="color: #8D8D8D;font-size: 16px;">描述：</div>
                        <span style="color:#202224;font-size: 16px" v-text="app.remark"></span>
                    </el-descriptions-item>
                </el-descriptions>
            </div>
            <el-skeleton :loading="app==null" :rows="5" animated  />
        </Card>

        <Card  :bordered="false" dis-hover :padding="30" style="margin-top: 30px;border-radius: 8px">
            <Tabs v-if="app!=null" class="app-details-tab" size="small" :animated="false" @on-click="clickTabs">
                <TabPane label="实例列表">
                    <div slot="label"></div>
                    <el-table
                            size="small"
                            :data="podList"
                            :header-row-style="()=>{return 'color: #202224'}"
                            :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                            style="width: 100%">
                        <el-table-column type="expand">
                            <template slot-scope="props">
                                <Card :bordered="false" :dis-hover="true" :padding="32"
                                      style="border-radius: 8px;background-color: #F9F9F9"
                                      v-for="(item,index) in props.row.containers">
                                    <Form :label-width="90">
                                        <Row>
                                            <Col :span="3">
                                                <div v-if="props.row.conditions.status==null||props.row.conditions.status=='False'">
                                                    <FormItem label="运行状态：">
                                                        <Tooltip transfer
                                                                 max-width="200"
                                                                 :content="props.row.conditions.message"
                                                                 placement="top">
                                                            <div style="font-size: 16px;font-weight: 600;color: #ed4014">
                                                                异常
                                                            </div>
                                                        </Tooltip>
                                                    </FormItem>
                                                </div>
                                                <div :label-width="100" inline
                                                     v-if="props.row.conditions.status=='True'&&props.row.containerStatuses!=null&&props.row.containerStatuses.length>0">
                                                    <FormItem label="运行状态：">
                                                        <div v-if="props.row.containerStatuses[index].ready"
                                                             style="font-size: 16px;font-weight: 600;color: #19be6b">正常
                                                        </div>
                                                        <Tooltip
                                                                v-else-if="props.row.containerStatuses[index].state.waiting!=null"
                                                                transfer
                                                                max-width="200"
                                                                :content="props.row.containerStatuses[index].state.waiting.reason"
                                                                placement="top">
                                                            <div style="font-size: 16px;font-weight: 600;color: #ff9900">
                                                                等待中
                                                            </div>
                                                        </Tooltip>
                                                        <div v-else
                                                             style="font-size: 16px;font-weight: 600;color: #ed4014">异常
                                                        </div>
                                                    </FormItem>

                                                </div>


                                            </Col>
                                            <Col :span="3">
                                                <FormItem label="重启次数：">
                                                    {{ props.row.containerStatuses[index].restartCount }}
                                                </FormItem>
                                            </Col>
                                            <Col :span="6">
                                                <FormItem label="镜像：">
                                                    <Ellipsis transfer :text="item.image" :length="100"
                                                              tooltip/>
                                                </FormItem>
                                            </Col>
                                            <Col :span="6">
                                                <div :label-width="100" inline
                                                     v-if="props.row.containerStatuses!=null&&props.row.containerStatuses.length>0">
                                                    <FormItem label="运行时长：">
                                                        <span v-if="props.row.containerStatuses[index].state.running!=null"
                                                              v-text="getTimeToNow(props.row.containerStatuses[index].state.running.startedAt)"></span>
                                                    </FormItem>
                                                </div>
                                            </Col>
                                            <Col :span="6">
                                                <div :label-width="100" inline
                                                     v-if="props.row.containerStatuses!=null&&props.row.containerStatuses.length>0">
                                                    <FormItem label="创建时间：">
                                                        <span v-if="props.row.containerStatuses[index].state.running!=null"
                                                              v-text="$Date(new Date(props.row.containerStatuses[index].state.running.startedAt)).format('YYYY-MM-DD HH:mm:ss')"></span>
                                                    </FormItem>
                                                </div>
                                            </Col>
                                            <Col :span="24">
                                                <div style="color: #202224;font-size: 16px;font-weight: 600">配额</div>
                                                <div style="margin-top: 24px">
                                                    <Row>
                                                        <Col :span="12">
                                                            <Row>
                                                                <Col :span="4">
                                                                    <span style="color: #202224;font-size: 16px;font-weight: 600">CPU</span>
                                                                </Col>
                                                                <Col :span="8">
                                                                    <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">请求：</span>
                                                                    <span style="color: #202224;font-size: 16px;font-weight: 600">{{ item.resources.requests.cpu.number }}Core</span>
                                                                </Col>
                                                                <Col :span="8">
                                                                    <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">上限：</span>
                                                                    <span style="color: #202224;font-size: 16px;font-weight: 600">{{ item.resources.limits.cpu.number }}Core</span>
                                                                </Col>
                                                            </Row>
                                                        </Col>
                                                        <Col :span="12">
                                                            <Row>
                                                                <Col :span="4">
                                                                    <span style="color: #202224;font-size: 16px;font-weight: 600">内存</span>
                                                                </Col>
                                                                <Col :span="8">
                                                                    <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">请求：</span>
                                                                    <span style="color: #202224;font-size: 16px;font-weight: 600">{{ item.resources.requests.memory.number/1024/1024 }}Mi</span>
                                                                </Col>
                                                                <Col :span="8">
                                                                    <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">上限：</span>
                                                                    <span style="color: #202224;font-size: 16px;font-weight: 600">{{ item.resources.limits.memory.number/1024/1024 }}Mi</span>
                                                                </Col>
                                                            </Row>
                                                        </Col>
                                                    </Row>
                                                </div>
                                            </Col>
                                        </Row>
                                    </Form>
                                </Card>
                            </template>
                        </el-table-column>
                        <el-table-column
                                label="名称"
                                prop="name">
                            <template slot-scope="props">
                                <span v-text="props.row.name"></span>
                            </template>

                        </el-table-column>
                        <el-table-column
                                align="center"
                                label="状态"
                                prop="status">
                            <template slot-scope="props">
                                <Tag v-if="props.row.status=='Running'"  style="width: 60px;border-radius: 28px" color="#00B69B">运行中</Tag>
                                <Tag v-if="props.row.status=='Pending'"   style="width: 60px;border-radius: 28px"color="#ff9900">等待中</Tag>
                            </template>
                        </el-table-column>
                        <el-table-column
                                align="center"
                                label="节点 IP"
                                prop="hostIP">
                        </el-table-column>
                        <el-table-column
                                align="center"
                                label="实例 IP"
                                prop="podIP">
                        </el-table-column>
                        <el-table-column
                                width="100"
                                align="center"
                                label="操作">
                            <template slot-scope="scope">
                                <Poptip @on-ok="deletePod(scope.row)" transfer confirm title="确认删除?">
                                    <el-button v-permission="{'value':'kube_app_del'}" style="color: #F45236"
                                               type="text" size="small">删除
                                    </el-button>
                                </Poptip>
                            </template>
                        </el-table-column>
                    </el-table>

                </TabPane>
                <TabPane label="资源">
                    <div>
                        <Button :type="selectMenu=='visit'?'primary':'text'" @click="menuChange('visit')">访问</Button>
                        <Button style="margin-left: 10px" :type="selectMenu=='storage'?'primary':'text'" @click="menuChange('storage')">存储</Button>
                    </div>
                    <div v-if="selectMenu=='visit'" style="margin-top: 30px">
                        <el-table
                                :header-row-style="()=>{return 'color: #202224'}"
                                :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                                :data="app.serviceList"
                                size="small"
                                stripe
                                style="width: 100%">
                            <el-table-column
                                    prop="name"
                                    label="名称"
                                    width="180">
                            </el-table-column>
                            <el-table-column
                                    prop="visitType"
                                    label="访问方式"
                                    width="180">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.visitType=='NodePort'">集群外访问</span>
                                    <span v-else>集群内访问</span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="协议">
                                <template slot-scope="scope">
                                    <div v-for="item in scope.row.portList" v-text="item.agreement"></div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="节点端口">
                                <template slot-scope="scope">
                                    <div v-for="item in scope.row.portList">
                                        <span v-if="item.node!=null" v-text="item.node+'1'"></span>
                                        <span v-else>-</span>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="容器端口">
                                <template slot-scope="scope">
                                    <div v-for="item in scope.row.portList" v-text="item.container"></div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="访问地址">
                                <template slot-scope="scope">
                                    <div v-for="item in scope.row.portList">
                                        <template v-if="scope.row.visitType=='NodePort'&&item.node!=null">
                                            <span v-text="visitUrl+':'+item.node"></span>
                                            <Icon @click="handleCopy(visitUrl+':'+item.node)"
                                                  style="font-size: 24px;margin-left: 10px;cursor: pointer"
                                                  type="md-copy"/>
                                        </template>
                                        <template v-else>
                                            <span v-text="app.sign+':'+item.container"></span>
                                            <Icon @click="handleCopy(app.sign+':'+item.container)"
                                                  style="font-size: 24px;margin-left: 10px;cursor: pointer"
                                                  type="md-copy"/>
                                        </template>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div v-if="selectMenu=='storage'" style="margin-top: 30px">
                        <el-table
                                :header-row-style="()=>{return 'color: #202224'}"
                                :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                                :data="app.storageList"
                                size="small"
                                stripe
                                style="width: 100%">
                            <el-table-column
                                    prop="storage.name"
                                    label="名称"
                                    width="180">
                            </el-table-column>
                            <el-table-column
                                    prop="storage.capacity"
                                    label="容量">
                                <template slot-scope="scope">
                                    <span v-text="scope.row.storage.capacity"></span>GB
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="storage.strategy"
                                    label="类型">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.storage.strategy=='ReadWriteMany'">多节点读写</span>
                                    <span v-if="scope.row.storage.strategy=='ReadOnlyMany'">多节点只读</span>
                                    <span v-if="scope.row.storage.strategy=='ReadWriteOnce'">单节点读写</span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="path"
                                    label="容器挂载路径">
                                <template slot-scope="scope">
                                    <span v-text="scope.row.path"></span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </TabPane>
                <TabPane label="配置">
                    <Form>
                        <Collapse class="app-info" simple>
                            <Panel name="1">
                                基本信息
                                <div slot="content" style="padding-left: 35px">
                                    <Row>
                                        <Col span="6" style="margin-bottom: 20px">
                                            <span style="color: #8D8D8D;font-size: 16px">名称：</span>
                                            <span style="color: #202224;font-size: 16px" v-text="app.name"></span>
                                        </Col>
                                        <Col span="6" style="margin-bottom: 20px">
                                            <span style="color: #8D8D8D;font-size: 16px">标识：</span>
                                            <span style="color: #202224;font-size: 16px" v-text="app.sign"></span>
                                        </Col>
                                        <Col span="6" style="margin-bottom: 20px">
                                            <span style="color: #8D8D8D;font-size: 16px">镜像名称：</span>
                                            <span style="color: #202224;font-size: 16px" v-text="app.image"></span>
                                        </Col>
                                        <Col span="6" style="margin-bottom: 20px">
                                            <span style="color: #8D8D8D;font-size: 16px">镜像版本：</span>
                                            <span style="color: #202224;font-size: 16px" v-text="app.imageVersion"></span>
                                        </Col>
                                        <Col span="6" style="margin-bottom: 20px">
                                            <span style="color: #8D8D8D;font-size: 16px">部署数量：</span>
                                            <span style="color: #202224;font-size: 16px" v-text="app.count"></span>
                                        </Col>
                                        <Col span="18" style="margin-bottom: 20px">
                                            <span style="color: #8D8D8D;font-size: 16px">描述：</span>
                                            <span style="color: #202224;font-size: 16px" v-text="app.remark"></span>
                                        </Col>
                                    </Row>
                                </div>
                            </Panel>
                            <Panel name="2">
                                容器配置
                                <div slot="content" style="padding-left: 35px">
                                    <div style="margin-top: 20px">
                                        <div style="color: #202224;font-size: 16px;font-weight: 600;margin-top: 10px">配额</div>
                                        <div style="margin-top: 24px">
                                            <Row>
                                                <Col :span="12">
                                                    <Row>
                                                        <Col :span="4">
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600">CPU</span>
                                                        </Col>
                                                        <Col :span="8">
                                                            <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">请求：</span>
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600">{{ app.cpuRequest }}Core</span>
                                                        </Col>
                                                        <Col :span="8">
                                                            <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">上限：</span>
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600">{{ app.cpuLimit }}Core</span>
                                                        </Col>
                                                    </Row>
                                                </Col>
                                                <Col :span="12">
                                                    <Row>
                                                        <Col :span="4">
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600">内存</span>
                                                        </Col>
                                                        <Col :span="8">
                                                            <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">请求：</span>
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600" v-text="app.memoryRequest"></span>
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600" v-text="app.memoryRequestCompany"></span>
                                                        </Col>
                                                        <Col :span="8">
                                                            <span style="color: #8D8D8D;font-size: 16px;font-weight: 600">上限：</span>
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600" v-text="app.memoryLimit"></span>
                                                            <span style="color: #202224;font-size: 16px;font-weight: 600" v-text="app.memoryLimitCompany"></span>
                                                        </Col>
                                                    </Row>
                                                </Col>
                                            </Row>
                                        </div>
                                    </div>

                                    <div style="color: #202224;font-size: 16px;font-weight: 600;margin-top: 30px">环境变量</div>
                                    <div style="margin-top: 20px">
                                        <FormItem v-if="app.openEnvConfig">
                                            <Row v-if="app.envConfigList!=null"
                                                 v-for="(item,index) in app.envConfigList">
                                                <Col span="18">
                                                    <FormItem label="文件">
                                                        <Cascader v-model="item.versionId" :data="envConfigList"
                                                                  disabled
                                                                  :load-data="loadConfig"
                                                                  style="margin-top: 10px"></Cascader>
                                                    </FormItem>
                                                </Col>
                                            </Row>
                                        </FormItem>
                                        <Row>
                                            <Col span="2" style="margin-bottom: 20px">
                                                <span style="color: #202224;font-size: 16px;font-weight: 600">自定义</span>
                                            </Col>
                                            <Col span="22" style="margin-bottom: 20px">
                                                <Row>
                                                    <Col span="4" style="margin-bottom: 20px" v-for="(item,index) in app.customEnvList">
                                                        <span style="color: #8D8D8D;font-size: 16px;font-weight: 600" v-text="item.envKey+'：'"></span>
                                                        <span style="color: #202224;font-size: 16px;font-weight: 600" v-text="item.envValue"></span>
                                                    </Col>
                                                </Row>
                                            </Col>
                                        </Row>

                                    </div>
                                    <div style="color: #202224;font-size: 16px;font-weight: 600;margin-top: 30px">配置文件</div>
                                    <div style="width: 700px">
                                        <FormItem label="">
                                            <div v-if="app.openConfig">
                                                <Row>
                                                    <Col span="8">配置文件</Col>
                                                    <Col span="7">挂载目录</Col>
                                                    <Col span="2">
                                                        <div style="text-align: center">/</div>
                                                    </Col>
                                                    <Col span="7">挂载文件名</Col>
                                                </Row>
                                                <Row v-if="app.configList!=null" v-for="(item,index) in app.configList">

                                                    <Col span="8" style="padding-right: 5px">
                                                        <FormItem>
                                                            <Cascader v-model="item.versionId" :data="configList"
                                                                      disabled
                                                                      :load-data="loadConfig"></Cascader>
                                                        </FormItem>

                                                    </Col>
                                                    <Col span="7">
                                                        <span v-text="item.catalog"></span>
                                                    </Col>
                                                    <Col span="2">
                                                        <div style="text-align: center">/</div>
                                                    </Col>
                                                    <Col span="5">
                                                        <span v-text="item.fileName"></span>
                                                    </Col>
                                                </Row>
                                            </div>
                                        </FormItem>
                                    </div>
                                </div>
                            </Panel>
                            <Panel name="3">
                                访问配置
                                <div slot="content" style="padding-left: 35px">
                                    <el-alert v-if="app.type=='StatefulSet'"
                                              size="mini"
                                              title="Headless Service"
                                              type="info"
                                              style="margin-top: 10px;margin-bottom: 10px"
                                              :closable="false">
                                    </el-alert>

                                    <div v-if="app.type=='StatefulSet'" style="width: 700px">
                                        <div v-for="(item,index) in app.headlessList">
                                            <FormItem label="名称：">
                                                <span v-text="item.sign"></span>
                                            </FormItem>
                                            <FormItem label="端口：">
                                                <Row>
                                                    <Col span="2" v-for="(portItem,portIndex) in item.portList">
                                                        <Tag color="blue">{{ portItem.container }}</Tag>
                                                    </Col>
                                                </Row>
                                            </FormItem>
                                        </div>
                                    </div>
                                    <el-alert
                                            v-if="app.type=='StatefulSet'"
                                            size="mini"
                                            title="Service"
                                            type="info"
                                            style="margin-top: 10px;margin-bottom: 10px"
                                            :closable="false">
                                    </el-alert>
                                    <div v-for="(item,index) in  app.serviceList"
                                         style="background-color: #fafafa;margin-bottom: 20px">

                                        <Row>
                                            <Col span="6" style="margin-bottom: 20px">
                                                <span style="color: #8D8D8D;font-size: 16px">名称：</span>
                                                <span style="color: #202224;font-size: 16px" v-text="item.name"></span>
                                            </Col>
                                            <Col span="6" style="margin-bottom: 20px">
                                                <span style="color: #8D8D8D;font-size: 16px">标签：</span>
                                                <span style="color: #202224;font-size: 16px" v-text="item.sign"></span>
                                            </Col>
                                            <Col span="6" style="margin-bottom: 20px">
                                                <span style="color: #8D8D8D;font-size: 16px">访问类型：</span>
                                                <span style="color: #202224;font-size: 16px" v-if="item.visitType =='ClusterIP'">集群内访问（ClusterIP）</span>
                                                <span style="color: #202224;font-size: 16px" v-if="item.visitType =='NodePort'">集群外访问（NodePort）</span>
                                            </Col>

                                        </Row>

                                        <div style="color: #202224;font-size: 16px;font-weight: 600;margin-top: 30px">访问端口</div>

                                        <Row v-for="(portItem,portIndex) in item.portList" style="margin-top: 20px">
                                            <Col span="6" style="margin-bottom: 20px">
                                                <span style="color: #8D8D8D;font-size: 16px">协议：</span>
                                                <span style="color: #202224;font-size: 16px" v-text="portItem.agreement"></span>
                                            </Col>
                                            <Col span="6" style="margin-bottom: 20px">
                                                <span style="color: #8D8D8D;font-size: 16px">容器端口：</span>
                                                <span style="color: #202224;font-size: 16px" v-text="portItem.container"></span>
                                            </Col>
                                            <Col span="6" style="margin-bottom: 20px" v-if="item.visitType=='NodePort'">
                                                <span style="color: #8D8D8D;font-size: 16px">节点端口：</span>
                                                <span style="color: #202224;font-size: 16px" v-text="portItem.node"></span>
                                            </Col>

                                        </Row>
                                    </div>
                                </div>
                            </Panel>
                            <Panel name="4">
                                存储配置
                                <div slot="content">
                                    <Alert>存储配置</Alert>
                                    <div id="storage">
                                        <Card v-for="(tab,index) in app.storageList" dis-hover>
                                            <div style="width: 100%">
                                                <span>存储{{ index + 1 }}</span>
                                            </div>
                                            <FormItem label="存储" v-if="app.type!='StatefulSet'">
                                                <span v-text="tab.storage.name"></span>
                                            </FormItem>
                                            <FormItem label="存储名称" v-if="app.type=='StatefulSet'">
                                                <span v-text="tab.name"></span>
                                            </FormItem>
                                            <FormItem label="存储容量" v-if="app.type=='StatefulSet'">
                                                <span v-text="tab.capacity"></span>GB
                                            </FormItem>
                                            <FormItem label="挂载目录">
                                                <span v-text="tab.path"></span>
                                            </FormItem>

                                        </Card>
                                    </div>
                                </div>
                            </Panel>
                            <Panel name="5">
                                高级配置
                                <div slot="content">
                                    <Alert>高级配置</Alert>
                                    <el-alert
                                            size="mini"
                                            title="更新策略"
                                            type="info"
                                            style="margin-top: 10px;margin-bottom: 10px"
                                            :closable="false">
                                    </el-alert>

                                    <div style="width: 700px">
                                        <FormItem label="策略类型：">
                                            <span v-if="app.updateType=='update'">滚动更新</span>
                                            <span v-if="app.updateType=='create'">重新创建</span>
                                        </FormItem>
                                        <FormItem label="最大不可用：" v-if="app.updateType == 'update'">
                                            <span v-text="app.unavailable"></span>
                                        </FormItem>
                                        <FormItem label="最大超量：" v-if="app.updateType == 'update'">
                                            <span v-text="app.excess"></span>
                                        </FormItem>
                                    </div>
                                    <el-alert
                                            size="mini"
                                            title="容器组网络"
                                            type="info"
                                            style="margin-top: 10px;margin-bottom: 10px"
                                            :closable="false">
                                    </el-alert>
                                    <div style="width: 700px">
                                        <FormItem label="DNS 策略：">
                                            <span v-text="app.dnsType"></span>
                                        </FormItem>
                                        <FormItem label="">
                                            Hosts 文件配置
                                            <Row>
                                                <Col span="10">
                                                    <span>ip</span>

                                                </Col>
                                                <Col span="10">
                                                    <span>域名</span>
                                                </Col>
                                                <div style="margin-bottom: 20px"></div>
                                            </Row>

                                            <Row v-for="(item,index) in app.hostList">
                                                <Col span="10">
                                                    <FormItem>
                                                        <span v-text="item.ip"></span>
                                                    </FormItem>

                                                </Col>
                                                <Col span="10">
                                                    <FormItem>
                                                        <span v-text="item.domain"></span>
                                                    </FormItem>
                                                </Col>
                                            </Row>
                                        </FormItem>
                                    </div>
                                </div>
                            </Panel>
                        </Collapse>
                    </Form>

                </TabPane>
                <TabPane name="monitor" label="监控">
                    <Row :gutter="32">
                        <Col span="12">
                            <Card v-for="(item,index) in cpuList" :key="index" shadow :bordered="false"
                                  :dis-hover="true" :padding="16"
                                  style="background-color: #F9F9F9;margin-bottom: 30px;border-radius: 8px">
                                <div style="color: #202224;font-size: 20px;font-weight: 600">CPU使用量</div>
                                <div :id="'cpuChart'+index" style="height: 200px"></div>
                            </Card>
                        </Col>
                        <Col span="12">
                            <Card v-for="(item,index) in memoryList" :key="index" shadow :bordered="false"
                                  :dis-hover="true" :padding="16"
                                  style="background-color: #F9F9F9;margin-bottom: 30px;border-radius: 8px">
                                <div style="color: #202224;font-size: 20px;font-weight: 600">内存使用量</div>
                                <div :id="'memoryChart'+index" style="height: 200px"></div>
                            </Card>
                        </Col>
                    </Row>
                </TabPane>
                <TabPane name="log" label="日志">
                    <Row :gutter="24">
                        <Col span="6">
                            <CellGroup class="log-pod-list" @on-click="changePodLog">
                                <Cell v-for="item in podList" :title="item.name" :name="item.name"
                                      :selected="selectPodLog!=null && selectPodLog==item.name" :label="item.status">
                                    <div slot="extra">
                                        <Icon type="ios-arrow-forward"/>
                                    </div>
                                </Cell>
                            </CellGroup>
                        </Col>
                        <Col span="18">
                            <console-log v-model="logText"></console-log>
                        </Col>
                    </Row>



                </TabPane>
                <TabPane name="terminal" label="终端">

                    <Row :gutter="24">
                        <Col span="6">
                            <CellGroup class="log-pod-list" @on-click="changePodTerminal">
                                <Cell v-for="item in podList" :title="item.name" :name="item.name"
                                      :selected="selectTerminal!=null && selectTerminal==item.name" :label="item.status">
                                    <div slot="extra">
                                        <Icon type="ios-arrow-forward"/>
                                    </div>
                                </Cell>
                            </CellGroup>
                        </Col>
                        <Col span="18">
                            <iframe style="width: 100%;height: 750px;border: 0px" :src="terminalUrl">
                            </iframe>
                        </Col>
                    </Row>
                </TabPane>
                <TabPane name="stretch" label="弹性伸缩" v-if="app.type=='Deployment'">
                    <Alert show-icon>开启弹性伸缩，应用部署数量将会失效！</Alert>
                    <div v-if="app.appStretch!=null" style="margin-top: 30px">
                        <Row>
                            <Col span="12">
                                <div style="color: #202224;font-size: 18px;font-weight: 600">基本信息</div>
                            </Col>
                            <Col span="12">
                                <div style="text-align: right">
                                    <span v-permission="kube_appStretch_add"
                                          style="color: #1F8ECF;font-size: 16px;cursor: pointer" @click="createStretch">更新</span>
                                    <span v-permission="kube_appStretch_del"
                                          style="color: #1F8ECF;font-size: 16px;cursor: pointer;margin-left: 30px;margin-right: 30px"
                                          @click="delStretch">关闭</span>
                                </div>
                            </Col>
                        </Row>
                        <div style="margin-top: 30px">
                            <Row>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">最小副本数量：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-text="app.appStretch.minReplicas"></span>
                                </Col>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">实际副本数量：</span>
                                    <span style="color: #202224;font-size: 16px" v-text="app.nowCount"></span>
                                </Col>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">最大副本数量：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-text="app.appStretch.maxReplicas"></span>
                                </Col>
                                <Col span="6"
                                     v-if="app.appStretch.kubeJson!=null&&app.appStretch.kubeJson.status!=null">
                                    <span style="color: #8D8D8D;font-size: 16px">期望副本数量：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-text="app.appStretch.kubeJson.status.desiredReplicas"></span>
                                </Col>
                            </Row>
                        </div>

                        <div style="color: #202224;font-size: 18px;font-weight: 600;margin-top: 45px">指标</div>


                        <Card :bordered="false" :dis-hover="true" :padding="20"
                              style="border-radius: 8px;background-color: #F9F9F9;margin-top: 30px">
                            <div style="color: #202224;font-size: 16px;font-weight: 600">CPU</div>
                            <Row style="margin-top: 14px">
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">状态：</span>
                                    <Tag v-if="app.appStretch.cpu" color="#2BCC99">开启</Tag>
                                    <Tag v-else color="default">关闭</Tag>
                                </Col>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">指标类型：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.cpuValueType=='proportion'">比例</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.cpuValueType=='value'">数值</span>
                                </Col>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">指标数值：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-text="app.appStretch.cpuValue"></span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.cpuValueType=='proportion'">%</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.cpuValueType=='value'">Core</span>
                                </Col>
                                <Col span="6"
                                     v-if="app.appStretch.kubeJson!=null&&app.appStretch.kubeJson.status!=null">
                                    <span style="color: #8D8D8D;font-size: 16px">实际指标数值：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-for="item in app.appStretch.kubeJson.status.currentMetrics"
                                          v-if="item.resource.name == 'cpu'"
                                          v-text="getCpuValue(item.resource,app.appStretch.cpuValueType)"></span>
                                </Col>
                            </Row>
                        </Card>

                        <Card :bordered="false" :dis-hover="true" :padding="20"
                              style="border-radius: 8px;background-color: #F9F9F9;margin-top: 30px">
                            <div style="color: #202224;font-size: 16px;font-weight: 600">内存</div>
                            <Row style="margin-top: 14px">
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">状态：</span>
                                    <Tag v-if="app.appStretch.memory" color="#2BCC99">开启</Tag>
                                    <Tag v-else color="default">关闭</Tag>
                                </Col>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">指标类型：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.memoryValueType=='proportion'">比例</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.memoryValueType=='value'">数值</span>
                                </Col>
                                <Col span="6">
                                    <span style="color: #8D8D8D;font-size: 16px">指标数值：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-text="app.appStretch.memoryValue"></span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.memoryValueType=='proportion'">%</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-if="app.appStretch.memoryValueType=='value'"
                                          v-text="app.appStretch.memoryCompany"></span>
                                </Col>
                                <Col span="6"
                                     v-if="app.appStretch.kubeJson!=null&&app.appStretch.kubeJson.status!=null">
                                    <span style="color: #8D8D8D;font-size: 16px">实际指标数值：</span>
                                    <span style="color: #202224;font-size: 16px"
                                          v-for="item in app.appStretch.kubeJson.status.currentMetrics"
                                          v-if="item.resource.name == 'memory'"
                                          v-text="getMemoryValue(item.resource,app.appStretch.cpuValueType)">11</span>
                                </Col>
                            </Row>
                        </Card>
                    </div>
                    <el-empty v-else>
                        <el-button v-permission="{'value':'kube_appStretch_add'}" type="primary" @click="createStretch">
                            创建弹性伸缩
                        </el-button>
                    </el-empty>
                </TabPane>
                <TabPane name="image-update" label="镜像更新记录">
                    <el-table
                            :header-row-style="()=>{return 'color: #202224'}"
                            :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                            size="mini"
                            :data="imageRecordList"
                            style="width: 100%">
                        <el-table-column
                                prop="image"
                                label="镜像">
                        </el-table-column>
                        <el-table-column
                                prop="imageVersion"
                                label="版本"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                prop="createTime"
                                width="160"
                                align="center"
                                label="更新时间">
                        </el-table-column>
                        <el-table-column
                                prop="createName"
                                width="160"
                                label="操作人">
                        </el-table-column>
                        <el-table-column
                                label="操作"
                                align="center"
                                width="120">
                            <template slot-scope="scope">
                                <el-button type="text" size="small"
                                           v-if="app.image == scope.row.image && app.imageVersion == scope.row.imageVersion"
                                           style="color: #19be6b;cursor: default">当前版本
                                </el-button>
                                <el-button type="text" v-else size="small" @click="imageGoBack(scope.row.id)">回滚
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div style="text-align: right">
                        <Page v-if="imageRecordCount>0" style="margin-top: 10px;margin-bottom: 10px" transfer
                              @on-change="pageImageRecordChange"
                              @on-page-size-change="sizeImageRecordChange"
                              prev-text="上一页"
                              next-text="下一页" :total="imageRecordCount" show-elevator show-total show-sizer/>
                    </div>
                </TabPane>
                <TabPane name="history-log" label="历史日志">
                    <Row :gutter="24">
                        <Col span="6">
                            <el-date-picker
                                    size="large"
                                    style="margin-bottom: 27px;width: 100%"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    @change="loadHistoryLog(null)"
                                    v-model="podRecordTime"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                            <CellGroup class="log-pod-list" @on-click="loadHistoryLog">
                                <Cell v-for="item in podRecordList" :title="item.podName" :name="item.podName"
                                      :selected="historyLogQuery.podName==item.podName" :label="item.createTime">
                                    <div slot="extra">
                                        <Icon type="ios-arrow-forward"/>
                                    </div>
                                    <div slot="icon">
                                        <img v-if="podList.filter(pod=>pod.name == item.podName).length>0" src="@/assets/images/run-pod.png" width="30" height="30" alt="">
                                        <img v-else src="@/assets/images/stop-pod.png" width="30" height="30" alt="">
                                    </div>
                                </Cell>
                            </CellGroup>
                        </Col>
                        <Col span="18">
                            <div v-if="historyLogQuery.podName!=null" style="background-color: #F5F6FA;line-height: 40px;border-radius: 8px;border: 1px solid #D5D5D5;font-size: 18px;color: #5D5D5D;padding-left: 30px">
                                日志条数 <span v-text="historyLog.length+'/'+historyLogTotal"></span>
                            </div>
                            <div style="margin-top: 27px">
                                <console-log v-if="historyLogQuery.podName!=null" v-model="historyLog"
                                             @scroll-bottom="scrollBottom"></console-log>
                                <el-empty v-else></el-empty>
                            </div>
                        </Col>
                    </Row>


                </TabPane>
            </Tabs>

            <el-skeleton :loading="app==null"  animated  />
        </Card>

        <Modal v-model="showTelescopic"
               width="300"
               title="伸缩">

            <el-slider
                    v-if="telescopicApp!=null"
                    style="float: left"
                    v-width="200"
                    v-model="telescopicApp.count"
                    :min="1"
                    :max="100"
                    :step="1">
            </el-slider>
            <Input v-if="telescopicApp!=null" readonly style="float: left;margin-left: 15px" v-width="50"
                   v-model="telescopicApp.count"></Input>
            <div style="clear: both"></div>
            <div slot="footer">
                <Button type="primary" size="large" :loading="telescopicLoading" @click="handleTelescopic">伸缩</Button>
            </div>
        </Modal>

        <!-- 弹性伸缩 start-->
        <Modal v-model="showStretch"
               width="542"
               title="弹性伸缩">
            <Form :model="stretch" :label-width="100">
                <div style="color:#202224;font-size: 16px;font-weight: 600">副本设置</div>
                <Row style="margin-top: 30px">
                    <Col span="12">
                        <FormItem label="最小副本数：">
                            <InputNumber style="width: 100%" :precision="0" size="large" :max="100" :min="1"
                                         v-model="stretch.minReplicas" placeholder="最小副本数"></InputNumber>
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="最大副本数：">
                            <InputNumber style="width: 100%" :precision="0" size="large" :max="100" :min="1"
                                         v-model="stretch.maxReplicas" placeholder="最大副本数"></InputNumber>
                        </FormItem>
                    </Col>
                </Row>
                <div style="color:#202224;font-size: 16px;font-weight: 600;margin-top: 20px">指标设置</div>
                <Row style="margin-top: 20px">
                    <Col span="24">
                        <FormItem label="CPU：" :label-width="60">
                            <Switch v-model="stretch.cpu"/>
                        </FormItem>
                    </Col>
                    <Col span="10">
                        <FormItem label="指标类型：">
                            <Select v-model="stretch.cpuValueType" size="large" :disabled="!stretch.cpu">
                                <Option value="proportion">比例</Option>
                                <Option value="value">数值</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="10">
                        <FormItem label="指标数值：">
                            <InputNumber :disabled="!stretch.cpu" size="large" style="width: 100%" :min="0"
                                         :max="stretch.cpuValueType=='proportion'?100:9999999"
                                         v-model="stretch.cpuValue" placeholder="指标数值"></InputNumber>
                        </FormItem>
                    </Col>
                    <Col span="4">
                        <FormItem label="" :label-width="10">
                            <span style="margin-left: 5px" v-if="stretch.cpuValueType=='value'">Core</span>
                            <span style="margin-left: 5px" v-if="stretch.cpuValueType=='proportion'">%</span>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col span="24">
                        <FormItem label="内存：" :label-width="60">
                            <Switch v-model="stretch.memory"/>
                        </FormItem>
                    </Col>
                    <Col span="10">
                        <FormItem label="指标类型：">
                            <Select v-model="stretch.memoryValueType" size="large" :disabled="!stretch.memory">
                                <Option value="proportion">比例</Option>
                                <Option value="value">数值</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="10">
                        <FormItem label="指标数值：">
                            <Input v-model="stretch.memoryValue" size="large" placeholder="指标数值" :disabled="!stretch.memory"></Input>
                        </FormItem>
                    </Col>
                    <Col span="4">
                        <FormItem label="" :label-width="10">
                            <Select v-model="stretch.memoryCompany" size="large" v-if="stretch.memoryValueType=='value'"
                                    :disabled="!stretch.memory">
                                <Option value="Mi">Mi</Option>
                                <Option value="Gi">Gi</Option>
                                <Option value="Ti">Ti</Option>
                            </Select>
                            <span style="margin-left: 5px" v-if="stretch.memoryValueType=='proportion'">%</span>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" :loading="stretchLoafing" @click="saveStretch">保存</Button>
            </div>
        </Modal>
        <!-- 弹性伸缩 end-->
    </div>
</template>
<script>
    import {getApp,delApp, deletePod, getLog, restartApp, startApp, stopApp, telescopicApp} from '@/api/kube/app'
    import {addAppStretch, delAppStretch} from '@/api/kube/appStretch'
    import {goBackAppImageRecord, pageAppImageRecord} from '@/api/kube/appImageRecord'
    import {listAppPodRecord,appPodRecordLog} from '@/api/kube/appPodRecord'
    import {getCpu, getMemory} from '@/api/kube/monitor'
    import dataUtils from '@/utils/dataUtils'
    import dateUtils from '@/utils/dateUtils'
    import appUtils from '@/utils/appUtils'
    import Setting from '@/setting';
    import consoleLog from '@/components/console-log';
    import echarts from 'echarts';
    import 'echarts/theme/macarons.js'
    import {listConfig} from '@/api/kube/config'
    import {getByConfig} from '@/api/kube/configVersion'

    export default {
        name: 'app-deployment-details',
        components: {consoleLog},
        computed: {
        },
        props: {
            id: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                app:null,
                dictList: []//字典
                ,
                logText: []
                ,
                terminalUrl: ''//终端地址
                ,
                selectTerminal: ''//选择的终端
                ,
                selectTab: null //选择的选项卡
                ,
                selectMenu: 'visit'//菜单
                ,
                selectPodLog: null//选择的pod日志
                ,
                readLog: false//是否开始读取日志
                ,
                readMonitor: false//是否开始读取监控
                ,
                showTelescopic: false//是否显示伸缩
                ,
                telescopicApp: null//伸缩的app
                ,
                telescopicLoading: false//伸缩密等
                ,
                cpuChartList: []//cpu图表列表
                ,
                memoryChartList: []//内存图表列表
                ,
                cpuList: []//cpu列表
                ,
                memoryList: []//内存列表
                ,
                visitUrl: null//访问地址
                ,
                showStretch: false//是否显示弹性伸缩
                ,
                stretch: {//弹性伸缩对象

                },
                stretchLoafing: false//弹性伸缩密等
                ,
                envConfigList: []
                ,
                configList: []
                ,
                logTimestamp: null//日志时间戳
                ,
                imageRecordQuery: {
                    appId: null,
                    limit: 10,
                    page: 1
                }
                ,
                imageRecordList: []//镜像版把记录
                ,
                imageRecordCount: 0//镜像版本总是
                ,
                historyLog: []//历史日志
                ,
                podRecordList: []//pod记录
                ,
                podRecordTime: [this.$Date().subtract(1, 'hour').format('YYYY-MM-DD HH:mm:ss'), this.$Date().format('YYYY-MM-DD HH:mm:ss')]
                ,
                historyLogQuery: {
                    podName: null,
                    size: 50,
                    current: 1
                }
                ,
                historyLogTotal: 0
            }
        },
        watch: {
            selectTerminal: {
                handler(val) {
                    if (this.selectTab == 'terminal' && val != null && this.app != null && this.app.sign != null) {
                        this.checkTerminalUrl()
                    }
                },
                immediate: true
            }
        },
        methods: {
            //更新事件
            handleUpdate(item) {
                this.$router.push('/kube/app/edit/' + item.type + '/' + item.id);
            },
            //选项卡点击
            clickTabs(name) {
                this.selectTab = name
                this.readLog = false
                this.readMonitor = false
                if (name == 'log') {
                    if (this.podList != null && this.podList.length > 0) {
                        this.selectPodLog = this.podList[0].name
                        this.logText = []
                        this.readLog = true
                        this.initLog(1000)
                    }
                } else if (name == 'terminal') {
                    this.checkTerminalUrl()
                } else if (name == 'monitor') {
                    this.readMonitor = true
                    this.handleSetCpuChart()
                    this.handleSetMemoryChart()
                } else if (name == 'image-update') {
                    this.imageRecordQuery.page = 1
                    this.getAppImageRecord()
                } else if (name == 'history-log') {
                    this.getAppPodRecord()
                    this.historyLogQuery.podName = null
                    this.historyLog = []
                    this.podRecordTime = [this.$Date().subtract(1, 'hour').format('YYYY-MM-DD HH:mm:ss'), this.$Date().format('YYYY-MM-DD HH:mm:ss')]
                }
            },
            //格式化时间
            getTimeToNow(time) {
                return dateUtils.timesFun(this.$Date(new Date(time)).format('YYYY-MM-DD HH:mm:ss'))
            },
            //切换终端url
            checkTerminalUrl() {
                let namespace = dataUtils.getData(Setting.key.namespace)
                let startIndex = this.app.colonyUrl.indexOf(':')
                let endIndex = this.app.colonyUrl.lastIndexOf(':')
                let path = this.app.colonyUrl.substring(startIndex + 3, endIndex)
                this.terminalUrl = 'http://' + path + ':30100/container/terminal?name=' + this.selectTerminal + '&namespace=' + namespace + '&container=' + this.app.sign
            },
            //复制
            handleCopy(text) {
                this.$Copy({
                    text: text
                })
            },
            //删除应用
            deleteApp() {
                delApp(this.app.id).then(res => {
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                        this.getData()
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            //重启应用
            restart() {
                this.$Modal.confirm({
                    loading: true,
                    title: '重启应用',
                    content: '确定重启应用“' + this.app.name + '”吗？',
                    onOk: () => {
                        restartApp(this.app.id).then(res => {
                            this.$Modal.remove();
                            if (res.state) {
                                appUtils.showSuccess(res.msg)
                                this.getApp()
                            } else {
                                appUtils.showError(res.msg)
                            }
                        }).catch(err=>{
                            this.$Modal.remove();
                        })
                    }
                });
            },
            //获取应用
            getApp() {
                getApp(this.id).then(res=>{
                    if(res.state){
                        this.podList = []
                        dataUtils.each(res.data.pods, (key, obj) => {
                            let item = {}
                            item.name = obj.metadata.name
                            item.status = obj.status.phase
                            if(obj.status.conditions!=null&&obj.status.conditions.length>0){
                                item.conditions = obj.status.conditions[0]
                            }else {
                                item.conditions = obj.status
                            }
                            item.hostIP = obj.status.hostIP
                            item.podIP = obj.status.podIP
                            item.containers = obj.spec.containers
                            item.containerStatuses = obj.status.containerStatuses
                            this.podList.push(item)
                        })
                        if (this.podList != null && this.podList.length > 0) {
                            this.selectTerminal = this.podList[0].name
                        }

                        let startIndex =  res.data.colonyUrl.indexOf(':')
                        let endIndex = res.data.colonyUrl.lastIndexOf(':')
                        this.visitUrl = 'http://' + res.data.colonyUrl.substring(startIndex + 3, endIndex)
                        this.app = res.data
                    }
                })
            },
            //初始化pod日志
            initLog(limit) {
                if (this.readLog) {
                    getLog({
                        name: this.selectPodLog,
                        container: this.app.sign,
                        limit: limit,
                        timestamp: this.logTimestamp
                    }).then(res => {
                        if (res.data.podName == this.selectPodLog) {
                            dataUtils.each(res.data.logList, (key, obj) => {
                                if (this.logText.length > 3000) {
                                    this.logText.splice(1, 1);
                                }
                                this.logText.push(obj)
                            })
                        }
                        if (res.data.logList != null && res.data.logList.length > 0) {
                            this.logTimestamp = res.data.timestamp
                        }
                        setTimeout(() => {
                            this.initLog(limit)
                        }, 2000)
                    })
                }
            },
            //切换pod日志
            changePodLog(val) {
                this.selectPodLog = val
                this.logText = []
                this.readLog = false
                setTimeout(() => {
                    this.readLog = true
                    this.logTimestamp = null
                    this.initLog(1000)
                }, 2000)
            },
            //切换终端
            changePodTerminal(val){
                this.selectTerminal = val
            },
            //停止应用
            stop() {
                this.$Modal.confirm({
                    loading: true,
                    title: '停止应用',
                    content: '确定停止应用“' + this.app.name + '”吗？',
                    onOk: () => {
                        stopApp(this.app.id).then(res => {
                            this.$Modal.remove()
                            appUtils.res(res, () => {
                                this.getApp()
                                appUtils.showSuccess(res.msg)
                            })
                        }).catch(err => {
                            this.$Modal.remove()
                        })
                    }
                });
            },
            //启动应用
            start() {
                this.$Modal.confirm({
                    loading: true,
                    title: '启动应用',
                    content: '确定启动应用“' + this.app.name + '”吗？',
                    onOk: () => {
                        startApp(this.app.id).then(res => {
                            this.$Modal.remove()
                            appUtils.res(res, () => {
                                this.getApp()
                                appUtils.showSuccess(res.msg)
                            })
                        }).catch(err => {
                            this.$Modal.remove()
                        })
                    }
                });
            },
            //伸缩
            telescopic() {
                this.telescopicApp = JSON.parse(JSON.stringify(this.app))
                this.showTelescopic = true
                this.telescopicLoading = false
            },
            //处理伸缩
            handleTelescopic() {
                if (this.telescopicApp.count != this.app.count) {
                    this.telescopicLoading = true
                    telescopicApp(this.telescopicApp).then(res => {
                        appUtils.res(res, () => {
                            appUtils.showSuccess(res.msg)
                            this.showTelescopic = false
                        })
                        this.telescopicLoading = false
                        this.$store.dispatch('app/app/setAppListState', {appId: this.telescopicApp.id, state: '70'});
                    })
                } else {
                    this.showTelescopic = false
                }

            },
            //处理cpu使用量图表
            handleSetCpuChart() {
                getCpu({'container': this.app.sign}).then(res => {
                    this.cpuChartList = []
                    this.cpuList = res.data
                    this.$nextTick(() => {
                        dataUtils.each(res.data, (key, obj) => {
                            let podList = []
                            let seriesList = []
                            podList.push(obj.metric.pod_name)
                            this.cpuChartList.push({})
                            let time = []
                            let dataList = []
                            dataUtils.each(obj.values, (k, o) => {
                                time.push(this.getTime(o[0]))
                                dataList.push(Math.floor(o[1] * 100) / 100)
                            })
                            seriesList.push(
                                {
                                    name: obj.metric.pod_name,
                                    type: 'line',
                                    stack: 'Core',
                                    areaStyle: {
                                        normal: {
                                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                                offset: 0,
                                                color: 'rgba(31,142,207' + ', 0.3)'
                                            }, {
                                                offset: 0.8,
                                                color: 'rgba(31,142,207' + ', 0)'
                                            }], false),
                                            shadowColor: 'rgba(0, 0, 0, 0.1)',
                                            shadowBlur: 10
                                        }
                                    },
                                    data: dataList
                                }
                            )
                            this.cpuChartList[key] = echarts.init(document.getElementById('cpuChart' + key), 'macarons');
                            this.cpuChartList[key].setOption(
                                {
                                    tooltip: {
                                        trigger: 'axis',
                                        axisPointer: {
                                            type: 'cross',

                                        }
                                    },
                                    legend: {
                                        data: podList
                                    },
                                    grid: {
                                        left: '3%',
                                        right: '4%',
                                        bottom: '3%',
                                        containLabel: true
                                    },
                                    xAxis: [
                                        {
                                            type: 'category',
                                            boundaryGap: false,
                                            data: time
                                        }
                                    ],
                                    yAxis: [
                                        {
                                            type: 'value',
                                            min: 5,
                                            splitNumber: 5,
                                            axisLabel: {
                                                formatter: '{value} Core',
                                            }
                                        }
                                    ],
                                    series: seriesList
                                }
                            );

                        })
                    })
                })
                setTimeout(() => {
                    if (this.readMonitor) {
                        this.handleSetCpuChart()
                    }
                }, 15000)

            },
            //处理内存使用量图表
            handleSetMemoryChart() {
                getMemory({'container': this.app.sign}).then(res => {
                    this.memoryChartList = []
                    this.memoryList = res.data
                    this.$nextTick(() => {
                        dataUtils.each(res.data, (key, obj) => {
                            let podList = []
                            let seriesList = []
                            podList.push(obj.metric.pod_name)
                            this.memoryChartList.push({})
                            let time = []
                            let dataList = []
                            dataUtils.each(obj.values, (k, o) => {
                                time.push(this.getTime(o[0]))
                                dataList.push(Math.floor((o[1] / 1024 / 1024) * 100) / 100)
                            })
                            seriesList.push(
                                {
                                    name: obj.metric.pod_name,
                                    type: 'line',
                                    stack: 'Mib',
                                    areaStyle: {
                                        normal: {
                                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                                offset: 0,
                                                color: 'rgba(31,142,207,0.3)'
                                            }, {
                                                offset: 0.8,
                                                color: 'rgba(31,142,207' + ', 0)'
                                            }], false),
                                            shadowColor: 'rgba(0, 0, 0, 0.1)',
                                            shadowBlur: 10
                                        }
                                    },
                                    data: dataList
                                }
                            )
                            this.memoryChartList[key] = echarts.init(document.getElementById('memoryChart' + key), 'macarons');
                            this.memoryChartList[key].setOption(
                                {
                                    tooltip: {
                                        trigger: 'axis',
                                        axisPointer: {
                                            type: 'cross',

                                        }
                                    },
                                    legend: {
                                        data: podList
                                    },
                                    grid: {
                                        left: '3%',
                                        right: '4%',
                                        bottom: '3%',
                                        containLabel: true
                                    },
                                    xAxis: [
                                        {
                                            type: 'category',
                                            boundaryGap: false,
                                            data: time
                                        }
                                    ],
                                    yAxis: [
                                        {
                                            type: 'value',
                                            axisLabel: {
                                                formatter: '{value} Mib',
                                            }
                                        }
                                    ],
                                    series: seriesList
                                }
                            );

                        })
                    })
                })
                setTimeout(() => {
                    if (this.readMonitor) {
                        this.handleSetMemoryChart()
                    }
                }, 15000)
            },
            //页面刷新
            handleResize() {
                dataUtils.each(this.cpuChartList, (key, obj) => {
                    obj.resize();
                })
                dataUtils.each(this.memoryChartList, (key, obj) => {
                    obj.resize();
                })
            },
            //获取时间
            getTime(time) {
                let date = new Date(parseInt(time) * 1000);
                return date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
            },
            //创建弹性伸缩
            createStretch() {
                this.showStretch = true
                if (this.app.appStretch != null) {
                    this.stretch = JSON.parse(JSON.stringify(this.app.appStretch))
                } else {
                    this.stretch = {
                        cpu: false,
                        cpuValue: null,
                        cpuValueType: "value",
                        maxReplicas: null,
                        memory: false,
                        memoryCompany: "Mi",
                        memoryValue: null,
                        memoryValueType: "value",
                        minReplicas: null
                    }
                }
            },
            //保存弹性伸缩
            saveStretch() {
                //基础校验
                if (this.stretch.minReplicas == null || this.stretch.minReplicas.length <= 0) {
                    appUtils.showError('请输入最小副本数！')
                    return
                }
                if (this.stretch.maxReplicas == null || this.stretch.maxReplicas.length <= 0) {
                    appUtils.showError('请输入最大副本数！')
                    return
                }

                if (this.stretch.minReplicas > this.stretch.maxReplicas) {
                    appUtils.showError('最小副本数不能大于最大副本数！')
                    return
                }
                if (!this.stretch.cpu && !this.stretch.memory) {
                    appUtils.showError('至少开启一个指标类型！')
                    return
                }

                //cpu校验
                if (this.stretch.cpu) {
                    if (this.stretch.cpuValueType == null) {
                        appUtils.showError('请选择CPU指标类型！')
                        return
                    }
                    if (this.stretch.cpuValue == null || this.stretch.cpuValue.length <= 0) {
                        appUtils.showError('请输入CPU指标数值！')
                        return
                    }
                }
                //内存
                if (this.stretch.memory) {
                    if (this.stretch.memoryValueType == null) {
                        appUtils.showError('请选择内存指标类型！')
                        return
                    }

                    if (this.stretch.memoryValue == null || this.stretch.memoryValue.length <= 0) {
                        appUtils.showError('请输入内存指标数值！')
                        return
                    }

                    if (this.stretch.memoryValueType == 'value') {
                        if (this.stretch.memoryCompany == null) {
                            appUtils.showError('请输入内存指标数值单位！')
                            return
                        }
                    }
                }
                this.stretch.appId = this.app.id
                this.stretchLoafing = true
                addAppStretch(this.stretch).then(res => {
                    if (res.state) {
                        appUtils.showSuccess(res.msg)
                        this.showStretch = false
                        this.getApp()
                    } else {
                        appUtils.showError(res.msg)
                    }
                    this.stretchLoafing = false
                }).catch(err => {
                    this.stretchLoafing = false
                })
            },
            //获取cpu实际指标
            getCpuValue(value, type) {
                if (value != null) {
                    if (type == 'value') {
                        return Number(value.currentAverageValue.substring(0, value.currentAverageValue.length - 1)) / 1000 + "Core"
                    } else {
                        return value.currentAverageUtilization + "%"
                    }
                }
            },
            //获取内存实际指标
            getMemoryValue(value, type) {
                if (value != null) {
                    if (type == 'value') {
                        return Number(value.currentAverageValue / 1024 / 1024).toFixed(2) + "Mi"
                    } else {
                        return value.currentAverageUtilization + "%"
                    }
                }
            },
            //关闭弹性伸缩
            delStretch() {
                this.$Modal.confirm({
                    title: '关闭',
                    content: '确定关闭弹性伸缩？',
                    loading: true,
                    onOk: () => {
                        delAppStretch(this.app.id).then(res => {
                            if (res.state) {
                                appUtils.showSuccess(res.msg)
                                this.getApp()
                            } else {
                                appUtils.showError(res.msg)
                            }
                            this.$Modal.remove()
                        }).catch(res => {
                            this.$Modal.remove()
                        })
                    }
                });

            },
            menuChange(name) {
                this.selectMenu = name
            },
            //获取配置文件版本
            getVersion(configId, callback) {
                getByConfig(configId).then(res => {
                    appUtils.res(res, () => {
                        if (callback != null) {
                            callback(res)
                        }
                    })
                })
            },
            //异步加载配置文件版本
            loadConfig(item, callback) {
                item.loading = true;
                this.getVersion(item.value, res => {
                    let items = []
                    dataUtils.each(res.data, (key, obj) => {
                        let versionItem = {
                            value: obj.id,
                            label: 'v' + obj.version
                        }
                        items.push(versionItem)
                    })
                    item.children = items
                    item.loading = false;
                    callback();
                })
            },
            //获取配置文件
            getConfig(callback) {
                this.configList = []
                this.envConfigList = []
                listConfig().then(res => {
                    appUtils.res(res, () => {
                        dataUtils.each(res.data, (key, obj) => {
                            if (obj.type == 0) {
                                var item = {
                                    value: obj.id,
                                    label: obj.name,
                                    children: [],
                                    loading: false
                                }
                                this.envConfigList.push(item)
                            } else if (obj.type == 1) {
                                var item = {
                                    value: obj.id,
                                    label: obj.name,
                                    children: [],
                                    loading: false
                                }
                                this.configList.push(item)
                            }
                        })
                        if (callback != null) {
                            callback(res)
                        }

                    })
                })
            },
            //删除pod
            deletePod(row) {
                deletePod(row.name).then(res => {
                    if(res.state){
                        this.getApp()
                    }
                })
            },
            //获取镜像记录
            getAppImageRecord() {
                pageAppImageRecord(this.imageRecordQuery).then(res => {
                    appUtils.res(res, () => {
                        this.imageRecordList = res.data.records
                        this.imageRecordCount = res.data.total
                    })
                })
            },
            sizeImageRecordChange(val) {
                this.imageRecordQuery.limit = val
                this.imageRecordQuery.page = 1
                this.getAppImageRecord()
            },
            pageImageRecordChange(val) {
                this.imageRecordQuery.page = val
                this.getAppImageRecord()
            },
            //镜像回滚
            imageGoBack(id) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定回滚镜像版本吗？',
                    loading: true,
                    onOk: () => {
                        goBackAppImageRecord(id).then(res => {
                            this.$Modal.remove();
                            appUtils.res(res, () => {
                                this.getApp()
                                this.getAppImageRecord()
                            })
                        }).catch(res => {
                            this.$Modal.remove()
                        })
                    }
                })
            },
            //获取pod记录
            getAppPodRecord() {
                listAppPodRecord({appId: this.id}).then(res => {
                    appUtils.res(res, () => {
                        this.podRecordList = res.data
                    })
                })
            },
            //加载日志
            loadHistoryLog(podName) {
                if (podName != null) {
                    this.historyLogQuery.podName = podName
                }
                this.historyLogQuery.current = 1
                this.historyLogQuery.size = 50
                this.historyLog = []
                this.historyLogTotal = 0
                if (this.historyLogQuery.podName != null) {
                    this.getPodLog()
                }
            },
            //获取pod日志
            getPodLog() {
                if (this.podRecordTime == null || this.podRecordTime.length != 2) {
                    appUtils.showError('请选择时间范围')
                    return
                }
                this.historyLogQuery.startTime = this.podRecordTime[0]
                this.historyLogQuery.endTime = this.podRecordTime[1]
                appPodRecordLog(this.historyLogQuery).then(res => {
                    dataUtils.each(res.data.records, (key, obj) => {
                        this.historyLog.push(obj.message)
                    })
                    this.historyLogTotal = res.data.total
                })
            },
            //滚动到底部
            scrollBottom() {
                if (this.historyLogQuery.current * this.historyLogQuery.size < this.historyLogTotal) {
                    this.historyLogQuery.current++
                    this.getPodLog()
                }
            },
            //返回
            back(){
                history.go(-1)
            }

        },
        mounted() {
            if (this.id != null) {
                this.getConfig(() => {
                    this.getApp()
                })
                this.imageRecordQuery.appId = this.id
                this.$EventBus.$on('app_state', (data) => {
                    this.getApp()
                })
                this.$EventBus.$on('app_count', (data) =>{
                    this.getApp()
                })
            }
        },
        destroyed: function () {
            this.readLog = false
            this.readMonitor = false
            this.$EventBus.$off('app_state')
            this.$EventBus.$off('app_count')
        },
    }
</script>
<style>
    .app-details-tab .ivu-tabs-nav .ivu-tabs-tab {
        font-weight: 400 !important;
        font-size: 18px !important;
        color: #929292;
    }

    .app-details-tab .ivu-tabs-nav .ivu-tabs-tab-active {
        color: #202224;
        font-weight: 600 !important;
    }
    .log-pod-list .ivu-cell{
        font-weight: 600;
        font-size: 14px;
        color: #939393 !important;
        border-radius: 6px;
        margin-bottom: 10px;
        border: 1px solid transparent;
    }
    .log-pod-list .ivu-cell-label {
        font-size: 12px;
        color: #939393 !important;
    }
    .log-pod-list .ivu-cell-selected, .ivu-cell.ivu-cell-selected:hover {
        background: #1F8ECF1A;
        border: 1px solid #1F8ECF;
        border-radius: 6px;
        color: #1F8ECF !important;
        font-weight: 600;
        font-size: 14px;
    }
    .app-info{
        border: 0px;
    }
    .app-info .ivu-collapse-header{
        background: #F9F9F9;
    }
    .app-info .ivu-collapse-item {
        border-top: 0px !important;
        margin-bottom: 20px;
        background: #F9F9F9 !important;
    }
    .app-info .ivu-collapse-header{
        line-height: 63px !important;
        height: 63px !important;
        color: #202224 !important;
        font-size: 18px;
        font-weight: 600;
    }
    .app-info .ivu-collapse-content{
        background: #F9F9F9 !important;
    }
</style>
