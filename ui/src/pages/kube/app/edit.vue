<template>
    <div v-if="type=='StatefulSet'||type=='Deployment'">
        <Card ref="card" v-loading="loading" :bordered="false" :dis-hover="true" :padding="32" style="margin-top: 30px;border-radius: 8px">
            <div style="height: 50px">
                <Steps style="width: 800px" :current="step">
                    <Step>
                        <div slot="title" @click="select(0)"
                             :style="{'color':(stepList[0].error)?'#ed4014':'#1F8ECF'}"
                             style="cursor: pointer">基本信息
                        </div>
                    </Step>
                    <Step>
                        <div slot="title" @click="select(1)"
                             :style="{'color':(stepList[1].error)?'#ed4014':'#1F8ECF'}"
                             style="cursor: pointer;color: #2d8cf0">容器配置
                        </div>
                    </Step>
                    <Step>
                        <div slot="title" @click="select(2)"
                             :style="{'color':(stepList[2].error)?'#ed4014':'#1F8ECF'}"
                             style="cursor: pointer">访问配置
                        </div>
                    </Step>
                    <Step>
                        <div slot="title" @click="select(3)"
                             :style="{'color':(stepList[3].error)?'#ed4014':'#1F8ECF'}"
                             style="cursor: pointer">存储配置
                        </div>
                    </Step>
                    <Step>
                        <div slot="title" @click="select(4)"
                             :style="{'color':(stepList[4].error)?'#ed4014':'#1F8ECF'}"
                             style="cursor: pointer">高级
                        </div>
                    </Step>
                </Steps>
            </div>
            <div >
                <Form ref="modelForm" :model="modelForm" :rules="modelFormValidate" @on-validate="onValidate"
                      :label-width="120"
                      style="width: 700px">
                    <div v-show="step==0">
                        <div style="width: 700px">
                            <FormItem label="名称" prop="name">
                                <Input v-model.trim="modelForm.name" size="large" placeholder="应用名称"></Input>
                                <span style="color: #929292">支持中文和特殊符号，长度限制 2 - 200 个字符</span>
                            </FormItem>
                            <FormItem label="标识" prop="sign">
                                <Input :disabled="modelForm.id!=null" v-model.trim="modelForm.sign"
                                       size="large"
                                       placeholder="应用标识"></Input>
                                <span style="color: #929292">k8s服务名称、Pod名称前缀，2 - 200 个字符（支持英文字母，“_”,“-”）</span>
                            </FormItem>
                            <FormItem label="实例个数">
                                <el-slider
                                        style="float: left;width: 430px"
                                        v-model="modelForm.count"
                                        :min="1"
                                        :max="100"
                                        :step="1">
                                </el-slider>
                                <Input readonly style="float: left;margin-left: 15px;width: 50px"
                                       v-model="modelForm.count"></Input>
                            </FormItem>
                            <FormItem label="描述" prop="remark">
                                <Input v-model.trim="modelForm.remark" type="textarea" :rows="4" placeholder="应用描述"/>
                                <span style="color: #929292">应用描述信息，字符控制在 300 个字符以内</span>

                            </FormItem>
                        </div>
                    </div>
                    <div v-show="step==1">
                        <div style="color: #202224;font-size: 20px;font-weight: 600">
                            容器基本信息
                        </div>
                        <div style="width: 700px;margin-top: 30px">
                            <FormItem label="镜像名称" prop="image">
                                <Input size="large"  v-model="modelForm.image" placeholder="镜像名称"></Input>
                                <span style="color: #929292">长度限制 2 - 200 个字符</span>
                            </FormItem>
                            <FormItem label="镜像版本" prop="imageVersion">
                                <Input v-model.trim="modelForm.imageVersion" size="large"  placeholder="镜像版本"></Input>
                                <span style="color: #929292">长度限制 1 - 20 个字符</span>
                            </FormItem>
                            <FormItem label="配额">
                                <Row>
                                    <Col span="12">
                                        <FormItem prop="cpuRequest" label="CPU：  请求">
                                            <Input v-model="modelForm.cpuRequest" size="large"  number placeholder="请求"
                                                   style="width: 80px"/>
                                            Core
                                            <div style="color: #929292">可分配<span v-text="getUseCpuRequest()"></span>Core</div>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem prop="cpuLimit" label="上限">
                                            <Input v-model="modelForm.cpuLimit" size="large"  number placeholder="上限"
                                                   style="width: 80px"/>
                                            Core
                                            <div style="color: #929292">可分配<span v-text="getUseCpuLimit()"></span>Core</div>
                                        </FormItem>
                                    </Col>
                                </Row>
                                <Row style="margin-top: 10px">
                                    <Col span="12">
                                        <FormItem prop="memoryRequest" label=" 内存：  请求">
                                            <Input v-model="modelForm.memoryRequest" size="large"  number placeholder="请求"
                                                   style="width: 130px">
                                                <Select v-model="modelForm.memoryRequestCompany" slot="append"
                                                        style="width: 60px">
                                                    <Option value="Mi">MiB</Option>
                                                    <Option value="Gi">GiB</Option>
                                                    <Option value="Ti">TiB</Option>
                                                </Select>
                                            </Input>
                                            <div style="color: #929292">可分配<span v-text="getUseMemoryRequest()"></span>GB</div>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem prop="memoryLimit" label="上限">
                                            <Input v-model="modelForm.memoryLimit" size="large"  number placeholder="上限"
                                                   style="width: 130px">
                                                <Select v-model="modelForm.memoryLimitCompany" slot="append"
                                                        style="width: 60px">
                                                    <Option value="Mi">MiB</Option>
                                                    <Option value="Gi">GiB</Option>
                                                    <Option value="Ti">TiB</Option>
                                                </Select>
                                            </Input>
                                            <div style="color: #929292">可分配<span v-text="getUseMemoryLimit()"></span>GB</div>
                                        </FormItem>
                                    </Col>
                                </Row>
                            </FormItem>
                        </div>
                        <div style="color: #202224;font-size: 20px;font-weight: 600">
                            环境变量
                        </div>
                        <div style="width: 700px;margin-top: 30px">
                            <FormItem label="配置文件">
                                <Switch v-model="modelForm.openEnvConfig" @on-change="changeOpenConfig"/>
                                <div v-if="modelForm.openEnvConfig">
                                    <Row v-if="modelForm.envConfigList!=null"
                                         v-for="(item,index) in modelForm.envConfigList">
                                        <Col span="22">
                                            <FormItem :prop="'envConfigList.' + index + '.versionId'"
                                                      :rules="{validator:versionEnvValidator, trigger: 'blur'}">
                                                <Cascader v-model="item.versionId" size="large"  :data="envConfigList"
                                                          :load-data="loadConfig"
                                                          style="margin-top: 10px"></Cascader>
                                            </FormItem>
                                        </Col>
                                        <Col span="2">
                                            <Icon @click="deleteEnvConfigList(index)"
                                                  v-if="modelForm.envConfigList.length>1"
                                                  type="md-close"
                                                  style="font-size: 24px;margin-top: 13px;cursor: pointer;color: #ed4014;margin-left: 10px"/>
                                        </Col>
                                    </Row>

                                    <Button type="dashed" icon="md-add" style="margin-top: 10px" @click="addEnvConfig">
                                        添加配置文件
                                    </Button>
                                </div>
                            </FormItem>

                            <FormItem label="自定义">
                                <Switch v-model="modelForm.openCustomEnv"/>
                                <div v-if="modelForm.openCustomEnv">
                                    <el-table
                                            class="form-table"
                                            :data="modelForm.customEnvList"
                                            border
                                            size="mini"
                                            style="width: 100%;margin-top: 10px">
                                        <el-table-column
                                                prop="envKey"
                                                label="键">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'customEnvList.' + scope.$index + '.envKey'"
                                                          :rules="{validator:customEnvValidator, trigger: 'blur'}">
                                                    <Input v-model.trim="scope.row.envKey"   placeholder="键"/>
                                                </FormItem>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                prop="envValue"
                                                label="值">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'customEnvList.' + scope.$index + '.envValue'"
                                                          :rules="{validator:customEnvValidator, trigger: 'blur'}">
                                                    <Input style="margin-left: 5px" v-model.trim="scope.row.envValue"
                                                           placeholder="值"/>
                                                </FormItem>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                width="50"
                                                prop="address"
                                                align="center"
                                                label="">
                                            <template slot-scope="scope">
                                                <i v-if="modelForm.customEnvList.length>1"  @click="deleteCustomEnv(scope.$index)" class="el-icon-delete" style="color: #FF4242;cursor: pointer"></i>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                    <el-button type="text" @click="addCustomEnv">添加环境变量</el-button>
                                </div>
                            </FormItem>
                        </div>
                        <div style="color: #202224;font-size: 20px;font-weight: 600">
                            配置文件
                        </div>
                        <div style="width: 700px;margin-top: 30px">
                            <Alert type="warning" show-icon>提示：挂载目录不能为跟目录</Alert>
                            <FormItem label="挂载配置文件">
                                <Switch v-model="modelForm.openConfig" @on-change="changeOpenConfig"/>
                                <div v-if="modelForm.openConfig">

                                    <el-table
                                            class="form-table"
                                            :data="modelForm.configList"
                                            border
                                            size="mini"
                                            style="width: 100%;margin-top: 10px">
                                        <el-table-column
                                                prop="versionId"
                                                label="配置文件">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'configList.' + scope.$index + '.versionId'"
                                                          :rules="{validator:versionValidator, trigger: 'blur'}">
                                                    <Cascader v-model="scope.row.versionId"  :data="configList"
                                                              :load-data="loadConfig"></Cascader>
                                                </FormItem>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                prop="catalog"
                                                label="挂载目录">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'configList.' + scope.$index + '.catalog'"
                                                          :rules="{validator:catalogValidator, trigger: 'blur'}">
                                                    <Input v-model.trim="scope.row.catalog"   placeholder="挂载目录,不能为根目录"/>
                                                </FormItem>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                width="30"
                                                align="center"
                                                prop="envValue"
                                                label="/">
                                            <template slot-scope="scope">
                                                <spam>/</spam>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                prop="envValue"
                                                label="挂载文件名">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'configList.' + scope.$index + '.fileName'"
                                                          :rules="{validator:fileNameValidator, trigger: 'blur'}">
                                                    <Input v-model.trim="scope.row.fileName"   placeholder="挂载文件名"/>
                                                </FormItem>
                                            </template>
                                        </el-table-column>

                                        <el-table-column
                                                width="50"
                                                prop="address"
                                                align="center"
                                                label="">
                                            <template slot-scope="scope">
                                                <i v-if="modelForm.configList.length>1"  @click="deleteConfigList(scope.$index)" class="el-icon-delete" style="color: #FF4242;cursor: pointer"></i>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                    <el-button type="text" @click="addConfig">添加配置文件</el-button>
                                </div>
                            </FormItem>
                        </div>
                    </div>
                    <div v-show="step==2">
                        <div v-if="type=='StatefulSet'" style="color: #202224;font-size: 20px;font-weight: 600">
                            Headless Service
                        </div>
                        <div v-if="type=='StatefulSet'" style="width: 700px;margin-top: 30px">
                            <div v-for="(item,index) in modelForm.headlessList">
                                <FormItem label="名称" :prop="'headlessList.'+index+'.sign'"
                                          :rules="{validator:serviceNameValidator, trigger: 'blur'}">
                                    <Input v-model.trim="item.sign" size="large"  placeholder="名称"></Input>
                                    <span>长度 1 - 63 个字符</span>
                                </FormItem>
                                <FormItem label="端口">
                                    <FormItem v-for="(portItem,portIndex) in item.portList"
                                              :prop="'headlessList.' + index + '.portList.'+portIndex+'.container'"
                                              :rules="{validator:containerValidator, trigger: 'blur'}"
                                              style="margin-bottom: 10px">
                                        <Row>
                                            <Col span="23">
                                                <Input v-model="portItem.container" size="large"  placeholder="容器端口1-65535"/>
                                            </Col>
                                            <Col span="1" v-if="item.portList.length>1">
                                                <Icon @click="deleteHeadless(item,index)"
                                                      type="md-close"
                                                      style="font-size: 24px;margin-top: 5px;cursor: pointer;color: #ed4014"/>
                                            </Col>
                                        </Row>
                                    </FormItem>
                                    <Button type="dashed" icon="md-add" @click="addHeadless(item)">添加端口
                                    </Button>
                                </FormItem>
                            </div>
                        </div>
                        <div style="color: #202224;font-size: 20px;font-weight: 600">
                            Service
                        </div>
                        <div style="width: 800px;margin-top: 30px">
                            <Card :bordered="false" :dis-hover="true"  v-for="(item,index) in  modelForm.serviceList"
                                  style="margin-bottom: 20px">
                                <div style="text-align: right;margin-bottom: 10px">
                                    <el-button style="color: #F45236" v-if="modelForm.serviceList.length>1" type="text" icon="el-icon-delete"
                                               @click="deleteService(index)">删除
                                    </el-button>
                                </div>
                                <FormItem label="名称" :prop="'serviceList.'+index+'.name'"
                                          :rules="{validator:serviceNameValidator, trigger: 'blur'}">
                                    <Input v-model.trim="item.name" size="large"  placeholder="名称"></Input>
                                    <span style="color: #929292">长度 1 - 63 个字符</span>
                                </FormItem>
                                <FormItem label="标签" :prop="'serviceList.'+index+'.sign'"
                                          :rules="{validator:serviceNameValidator, trigger: 'blur'}">
                                    <Input v-model.trim="item.sign" size="large"  placeholder="标签"></Input>
                                    <span style="color: #929292">长度 1 - 63 个字符</span>
                                </FormItem>
                                <FormItem label="访问类型">
                                    <RadioGroup v-model="item.visitType">
                                        <Radio label="ClusterIP">集群内访问（ClusterIP）</Radio>
                                        <Radio label="NodePort">集群外访问（NodePort）</Radio>
                                    </RadioGroup>
                                </FormItem>

                                <FormItem label="访问端口">

                                    <el-table
                                            class="form-table"
                                            :data="item.portList"
                                            border
                                            size="mini"
                                            style="width: 100%;margin-top: 10px">
                                        <el-table-column
                                                prop="agreement"
                                                label="协议">
                                            <template slot-scope="scope">
                                                <FormItem>
                                                    <Select v-model="scope.row.agreement">
                                                        <Option value="HTTP">HTTP</Option>
                                                        <Option value="HTTPS">HTTPS</Option>
                                                        <Option value="TCP">TCP</Option>
                                                        <Option value="UDP">UDP</Option>
                                                    </Select>
                                                </FormItem>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                prop="container"
                                                label="容器端口">
                                            <template slot-scope="scope">
                                                <FormItem
                                                        :prop="'serviceList.'+index+'.portList.' + scope.$index + '.container'"
                                                        :rules="{validator:containerValidator, trigger: 'blur'}">
                                                    <Input v-model.trim="scope.row.container"  placeholder="容器端口1-65535"/>
                                                </FormItem>


                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                v-if="item.visitType=='NodePort'"
                                                prop="node"
                                                label="节点端口">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'serviceList.'+index+'.portList.' + scope.$index + '.node'"
                                                          :rules="{validator:nodeValidator, trigger: 'blur'}">
                                                    <Poptip placement="bottom" width="400"
                                                            @on-popper-show="portShow(scope.row)">
                                                        <Input v-if="item.visitType=='NodePort'" readonly
                                                               v-model="scope.row.node"
                                                               placeholder="节点端口30000-32767"/>
                                                        <div class="api" slot="content">
                                                            <div style="height: 200px;overflow: auto">
                                                                <div v-for="(item,index) in scope.row.portList"
                                                                     style="float: left;margin: 5px">
                                                                    <Button size="small"
                                                                            :disabled="item.have"
                                                                            @click="scope.row.node = item.port"
                                                                            v-text="item.port"></Button>
                                                                </div>
                                                                <div style="clear: both"></div>
                                                                <Button size="small" style="width: 87%" long
                                                                        @click="loadPort(scope.row)">加载更多
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </Poptip>
                                                </FormItem>

                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                width="50"
                                                prop="address"
                                                align="center"
                                                label="">
                                            <template slot-scope="scope">
                                                <i @click="deletePort(item,scope.$index)" class="el-icon-delete" style="color: #FF4242;cursor: pointer"></i>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                    <el-button type="text" @click="addPort(item)">添加端口</el-button>
                                </FormItem>
                            </Card>
                            <Button type="primary" ghost @click="addService">添加Service</Button>
                        </div>
                    </div>
                    <div v-show="step==3">
                        <div id="storage">
                            <Button size="small" icon="md-add" @click="addStorage" style="margin-bottom: 10px">增加存储
                            </Button>
                            <el-collapse v-model="activeNames">
                                <el-collapse-item v-for="(tab,index) in modelForm.storageList" :title="'存储'+(index+1)"
                                                  :name="tab.index">
                                    <template slot="title">
                                        <div style="width: 100%">
                                            <span>存储{{ index + 1 }}</span>

                                            <el-button style="color: #F45236;float: right;margin-right: 20px;margin-top: 10px" type="text" icon="el-icon-delete"
                                                       @click="removeStorage(index)">删除
                                            </el-button>

                                            <div style="clear: both"></div>
                                        </div>
                                    </template>

                                    <FormItem v-if="type!='StatefulSet'" label="选择存储"
                                              :prop="'storageList.' + index + '.storageId'"
                                              :rules="{validator:storageValidator, trigger: 'blur'}">
                                        <Select v-model="tab.storageId" size="large"  transfer>
                                            <Option v-for="item in storageList" :value="item.id" :label="item.name"
                                                    :key="item.id">
                                                <span v-text="item.name"></span>
                                                <span v-text="item.capacity+'GB'" style="float:right;color:#ccc"></span>
                                            </Option>
                                        </Select>
                                        <el-button type="text" @click="showStorageClick">创建存储</el-button>
                                    </FormItem>
                                    <FormItem v-if="type=='StatefulSet'" label="名称"
                                              :prop="'storageList.' + index + '.name'"
                                              :rules="{validator:storageNameValidator, trigger: 'blur'}">
                                        <Input v-model.trim="tab.name" size="large"  placeholder="名称"></Input>
                                    </FormItem>
                                    <FormItem v-if="type=='StatefulSet'" label="容量"
                                              :prop="'storageList.' + index + '.capacity'"
                                              :rules="{validator:storageCapacityValidator, trigger: 'blur'}">
                                        <Input v-model="tab.capacity" size="large"  number placeholder="容量">
                                            <span slot="append">GB</span>
                                        </Input>
                                    </FormItem>
                                    <FormItem label="挂载目录" :prop="'storageList.' + index + '.path'"
                                              :rules="{validator:storagePathValidator, trigger: 'blur'}">
                                        <Input v-model.trim="tab.path" size="large"  placeholder="挂载目录"></Input>
                                    </FormItem>
                                </el-collapse-item>
                            </el-collapse>
                        </div>
                    </div>
                    <div v-show="step==4">
                        <div>
                            <div style="color: #202224;font-size: 20px;font-weight: 600">
                                更新策略
                            </div>
                            <div style="width: 700px;margin-top: 30px">
                                <FormItem label="策略类型">
                                    <RadioGroup v-model="modelForm.updateType">
                                        <Radio label="update">滚动更新</Radio>
                                        <Radio label="create">重新创建</Radio>
                                    </RadioGroup>
                                </FormItem>
                                <FormItem label="最大不可用" v-if="modelForm.updateType == 'update'">
                                    <Input v-model="modelForm.unavailable" size="large"  placeholder="最大不可用" style="width: 300px"/>
                                </FormItem>
                                <FormItem label="最大超量" v-if="modelForm.updateType == 'update'">
                                    <Input v-model="modelForm.excess" size="large"  placeholder="最大超量" style="width: 300px"/>
                                </FormItem>
                            </div>
                            <div style="color: #202224;font-size: 20px;font-weight: 600">
                                容器组网络
                            </div>
                            <div style="width: 700px;margin-top: 30px">
                                <FormItem label="DNS 策略">
                                    <RadioGroup v-model="modelForm.dnsType">
                                        <Radio label="ClusterFirst">ClusterFirst</Radio>
                                        <Radio label="Default">Default</Radio>
                                    </RadioGroup>
                                </FormItem>
                                <FormItem label="Host 文件配置">
                                    <el-table
                                            class="form-table"
                                            :data="modelForm.hostList"
                                            border
                                            size="mini"
                                            style="width: 100%;margin-top: 10px">
                                        <el-table-column
                                                prop="agreement"
                                                label="IP">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'hostList.' + scope.$index + '.ip'"
                                                          :rules="{required: true, message: ' ', trigger: 'blur'}">
                                                    <Input v-model.trim="scope.row.ip"  placeholder="IP"/>
                                                </FormItem>
                                            </template>
                                        </el-table-column>
                                        <el-table-column
                                                prop="container"
                                                label="域名">
                                            <template slot-scope="scope">
                                                <FormItem :prop="'hostList.' + scope.$index + '.domain'"
                                                          :rules="{required: true, message: ' ', trigger: 'blur'}">
                                                    <Input v-model.trim="scope.row.domain"  placeholder="域名"
                                                           />
                                                </FormItem>
                                            </template>
                                        </el-table-column>

                                        <el-table-column
                                                width="50"
                                                prop="address"
                                                align="center"
                                                label="">
                                            <template slot-scope="scope">
                                                <i @click="deleteHost(scope.$index)" class="el-icon-delete" style="color: #FF4242;cursor: pointer"></i>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                    <el-button type="text" @click="addHost">添加Host</el-button>
                                </FormItem>
                            </div>


                            <div style="color: #202224;font-size: 20px;font-weight: 600">
                                健康检查
                            </div>
                            <div style="width: 700px;margin-top: 30px">
                                <FormItem label="是否启用">
                                    <Switch v-model="modelForm.openHealth"/>
                                </FormItem>

                                <Row v-if="modelForm.openHealth">
                                    <Col span="12">
                                        <FormItem  label="协议">
                                            <Select v-model="modelForm.healthScheme" size="large"  placeholder="请选择协议" style="width:100%">
                                                <Option value="HTTP">HTTP</Option>
                                                <Option value="HTTPS">HTTPS</Option>
                                            </Select>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="端口">
                                            <Input v-model="modelForm.healthPort" size="large"  placeholder="端口" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="路径">
                                            <Input v-model="modelForm.healthPath" size="large"  placeholder="路径" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="失败次数">
                                            <Input v-model="modelForm.failureThreshold" size="large"  placeholder="失败次数" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="首次检查时间">
                                            <Input v-model="modelForm.initialDelay" size="large"  placeholder="首次检查等待时间（秒）" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="探测周期">
                                            <Input v-model="modelForm.periodSeconds" size="large"  placeholder="探测周期（秒）" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="成功阈值">
                                            <Input v-model="modelForm.successThreshold" size="large"  placeholder="成功阈值" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                    <Col span="12">
                                        <FormItem label="超时时间">
                                            <Input v-model="modelForm.timeoutSeconds" size="large"  placeholder="超时时间（秒）" style="width: 100%"/>
                                        </FormItem>
                                    </Col>
                                </Row>
                            </div>


                        </div>
                    </div>
                </Form>
                <div style="margin-top: 10px;text-align: right">
                    <Button type="primary" size="large" ghost @click="cancel" style="width: 140px">取消</Button>
                    <Button type="primary" size="large" ghost @click="top" style="width: 140px;margin-left: 10px" v-if="step>0">上一步</Button>
                    <Button type="primary" size="large" @click="next" style="width: 140px;margin-left: 10px" v-if="step<4">下一步</Button>
                    <Button type="primary" size="large" @click="save" style="width: 140px;margin-left: 10px" :loading="loading">完成</Button>
                </div>
            </div>
        </Card>
        <Modal v-model="showImage"
               width="1000"
               :footer-hide="true"
               title="选择镜像">
            <el-table
                    :data="imageList"
                    :header-row-style="()=>{return 'color: #202224'}"
                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                    style="width: 100%">
                <el-table-column
                        prop="name"
                        :show-overflow-tooltip="true"
                        label="镜像">
                </el-table-column>
                <el-table-column
                        prop="tagsCount"
                        :show-overflow-tooltip="true"
                        label="版本数量"
                        align="center"
                        width="80">
                </el-table-column>
                <el-table-column
                        width="80"
                        align="center"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="mini" @click="selectImage(scope.row)">选择</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </Modal>
        <!-- 创建存储 start -->
        <Modal
                v-model="showStorage"
                :mask-closable="false"
                title="创建存储"
                width="500">
            <Form ref="storageModel" :model="storageModel"
                  :rules="storageValidate" :label-width="100">
                <FormItem label="名称：" prop="name">
                    <Input :disabled="storageModel.id!=null" size="large" v-model.trim="storageModel.name"/>
                </FormItem>
                <FormItem label="容量：" prop="capacity">
                    <Input v-model="storageModel.capacity" size="large" number placeholder="容量">
                        <span slot="append">GB</span>
                    </Input>
                </FormItem>
                <FormItem label="读写策略：" prop="strategy">
                    <RadioGroup v-model="storageModel.strategy" size="large">
                        <Radio label="ReadWriteMany">多节点读写</Radio>
                        <Radio label="ReadOnlyMany">多节点只读</Radio>
                        <Radio label="ReadWriteOnce">单节点读写</Radio>
                    </RadioGroup>
                </FormItem>
                <FormItem label="描述：" prop="remark">
                    <Input v-model.trim="storageModel.remark" type="textarea"/>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large" :loading="storageLoading" @click="saveStorage">保存</Button>
            </div>
        </Modal>
        <!-- 编辑存储 end -->
    </div>
</template>
<script>
    import {getApp, addApp, updateApp, usePort} from '@/api/kube/app'
    import {listStorage, addStorage} from '@/api/kube/storage'
    import {listConfig} from '@/api/kube/config'
    import {getByConfig} from '@/api/kube/configVersion'
    import {currentNamespace} from "@/api/kube/namespace";
    import dataUtils from '@/utils/dataUtils'
    import appUtils from '@/utils/appUtils'
    import Setting from '@/setting';


    export default {
        name: 'app-deployment-edit',
        props: {
            id: {
                type: String,
                required: true
            },
            appType: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                loading:false,
                type: null,
                step: 0//当前步骤
                , stepList: [
                    {error: false},
                    {error: false},
                    {error: false},
                    {error: false},
                    {error: false}
                ]
                , modelForm: {
                    type: 'Deployment',
                    count: 0,
                    memoryRequestCompany: 'Mi',
                    memoryLimitCompany: 'Mi',
                    memoryLimit: '',
                    openConfig: false,
                    openEnvConfig: false,
                    openCustomEnv: false,
                    serviceList: [{
                        name: null,
                        type: 'Service',
                        sign: null,
                        visitType: 'ClusterIP',
                        portList: []
                    }],
                    headlessList: [],
                    configList: [
                        {versionId: [], catalog: null, fileName: null}
                    ],
                    envConfigList: [
                        {versionId: []}
                    ],
                    customEnvList: [
                        {envKey: null, envValue: null}
                    ],
                    updateType: 'update',
                    unavailable: 0,
                    excess: 1,
                    dnsType: 'ClusterFirst',
                    hostList: [],
                    storageList: [],
                    openHealth:false,
                    healthScheme:'HTTP',
                    healthPort:null,
                    healthPath:'/',
                    failureThreshold:5,
                    initialDelay:120,
                    periodSeconds:10,
                    successThreshold:1,
                    timeoutSeconds:10,
                }
                , memoryRequest: 0
                , memoryLimit: 0
                , cpuRequest: 0
                , cpuLimit: 0
                , modelFormValidate: {
                    name: [
                        {required: true, message: ' ', trigger: 'blur'},
                        {type: 'string', min: 2, max: 200, message: ' ', trigger: 'blur'}
                    ],
                    sign: [
                        {required: true, message: ' ', trigger: 'blur'},
                        {type: 'string', min: 2, max: 200, message: ' ', trigger: 'blur'},
                        {message: ' ', trigger: 'blur', pattern: /^[A-Za-z0-9-]+$/}

                    ],
                    remark: [
                        {type: 'string', max: 300, message: ' ', trigger: 'blur'}
                    ],
                    image: [
                        {required: true, message: ' ', trigger: 'blur'},
                        {type: 'string', min: 2, max: 200, message: ' ', trigger: 'blur'}
                    ],
                    imageVersion: [
                        {required: true, message: ' ', trigger: 'blur'},
                        {type: 'string', min: 1, max: 50, message: ' ', trigger: 'blur'}
                    ],
                    cpuRequest: [
                        {type: 'number', required: true, message: ' ', trigger: 'blur'},
                        {message: ' ', trigger: 'blur', pattern: /^(([1-9]\d{0,5})|0)(\.\d{0,2})?$/}
                    ],
                    cpuLimit: [
                        {type: 'number', required: true, message: ' ', trigger: 'blur'},
                        {message: ' ', trigger: 'blur', pattern: /^(([1-9]\d{0,5})|0)(\.\d{0,2})?$/}
                    ],
                    memoryRequest: [
                        {type: 'number', required: true, message: ' ', trigger: 'blur'},
                        {message: ' ', trigger: 'blur', pattern: /^(([1-9]\d{0,5})|0)(\.\d{0,2})?$/}
                    ],
                    memoryLimit: [
                        {type: 'number', required: true, message: ' ', trigger: 'blur'},
                        {message: ' ', trigger: 'blur', pattern: /^(([1-9]\d{0,5})|0)(\.\d{0,2})?$/}
                    ]
                }
                , portIndex: 0
                , hostIndex: 0
                , errorList: []
                , configList: []//配置文件列表
                , envConfigList: []
                , storageList: []//待选存储列表
                , activeNames: []//展开的存储
                , storageIndex: 0
                , usePortList: []//已使用端口列表
                , showImage: false//是否显示选择镜像
                , imageList: []//镜像列表
                , imageVersionList: []//镜像版本列表
                , showStorage: false//是否显示添加存储
                , storageModel: {// 存储
                    name: '',
                    capacity: null,
                    strategy: 'ReadWriteMany',
                    remark: ''
                }
                , storageValidate: {
                    name: [
                        {required: true, message: '名称不能为空', trigger: 'blur'},
                        {message: '只能字母或数字', trigger: 'blur', pattern: /^[a-zA-Z0-9]{1,20}$/}
                    ],
                    capacity: [
                        {type: 'number', min: 1, required: true, message: '请输入大于0的正整数', trigger: 'blur'}
                    ],
                    strategy: [
                        {required: true, message: '请选择读写策略', trigger: 'blur'},
                    ],
                },
                namespaceInfo: null,//命名空间
                storageLoading:false
            }
        },
        methods: {
            //初始化应用
            initApp(res) {
                this.cpuRequest = res.data.cpuRequest
                this.cpuLimit = res.data.cpuLimit
                if (res.data.memoryRequestCompany == 'Mi') {
                    this.memoryRequest = res.data.memoryRequest / 1024
                } else if (res.data.memoryRequestCompany == 'Gi') {
                    this.memoryRequest = res.data.memoryRequest
                } else if (res.data.memoryRequestCompany == 'Ti') {
                    this.memoryRequest = res.data.memoryRequest * 1024
                }

                if (res.data.memoryLimitCompany == 'Mi') {
                    this.memoryLimit = res.data.memoryLimit / 1024
                } else if (res.data.memoryLimitCompany == 'Gi') {
                    this.memoryLimit = res.data.memoryLimit
                } else if (res.data.memoryLimitCompany == 'Ti') {
                    this.memoryLimit = res.data.memoryLimit * 1024
                }

                this.imageVersionList = [{
                    value: res.data.imageVersion,
                    label: res.data.imageVersion
                }]


                if (res.data.configList == null) {
                    res.data.configList = []
                    res.data.envConfigList = []
                } else {
                    res.data.envConfigList = []
                    var configList = []
                    dataUtils.each(res.data.configList, (key, obj) => {
                        if (obj.config.type == 0) {
                            res.data.envConfigList.push({
                                versionId: [obj.version.configId, obj.versionId]
                            })
                        } else if (obj.config.type == 1) {
                            obj.versionId = [obj.version.configId, obj.versionId]
                            configList.push(obj)
                        }
                    })
                    res.data.configList = configList
                }


                if (res.data.storageList == null) {
                    res.data.storageList = []
                } else {
                    dataUtils.each(res.data.storageList, (key, obj) => {
                        res.data.storageList[key].index = key
                        this.activeNames.push(key)
                    })
                    this.storageIndex = res.data.storageList.length
                }

                if (res.data.serviceList != null) {
                    dataUtils.each(res.data.serviceList, (key, obj) => {
                        if (res.data.serviceList[key].portList != null) {
                            dataUtils.each(res.data.serviceList[key].portList, (k, o) => {
                                if (res.data.serviceList[key].portList[k].portList == null) {
                                    res.data.serviceList[key].portList[k].portList = []
                                }
                            })
                        }
                    })
                }


                if (this.type == 'StatefulSet') {
                    let headlessList = []
                    let serviceList = []
                    dataUtils.each(res.data.serviceList, (key, obj) => {
                        if (obj.type == 'Service') {
                            serviceList.push(obj)
                        } else {
                            headlessList.push(obj)
                        }
                    })
                    res.data.serviceList = serviceList
                    res.data.headlessList = headlessList
                    if (headlessList.length <= 0) {
                        res.data.headlessList = [{
                            type: 'Headless',
                            sign: null,
                            portList: [{
                                agreement: 'TCP',
                                container: null
                            }]
                        }]
                    }
                }
                this.modelForm = res.data
            },
            select(index) {
                if (index == 3 && this.step != index) {
                    this.getStorage()
                }
                this.step = index
            },
            cancel() {
                this.$router.push(dataUtils.getData(Setting.topUrl));
            },
            top() {
                this.step--
                //加载存储
                if (this.step == 3) {
                    this.getStorage()
                }
            },
            next() {
                this.step++
                //加载存储
                if (this.step == 3) {
                    this.getStorage()
                }
            },
            //添加端口
            addPort(item) {
                item.portList.push({agreement: null, container: null, node: null, portList: []})
            },
            //添加Service
            addService() {
                this.modelForm.serviceList.push({
                    name: null,
                    sign: null,
                    type: 'Service',
                    visitType: 'ClusterIP',
                    portList: []
                })
            },
            //添加Headless
            addHeadless(item) {
                item.portList.push({
                    agreement: "TCP",
                    container: null,
                    portList: []
                })
            },
            //删除端口
            deletePort(item, index) {
                item.portList.splice(index, 1);
            },
            //删除Service
            deleteService(index) {
                this.modelForm.serviceList.splice(index, 1);
            },
            deleteHeadless(item, index) {
                item.portList.splice(index, 1);
            },
            //删除配置文件
            deleteConfigList(index) {
                this.modelForm.configList.splice(index, 1);
            },
            //删除自定义环境变量
            deleteCustomEnv(index) {
                this.modelForm.customEnvList.splice(index, 1);
            },
            //删除配置文件
            deleteEnvConfigList(index) {
                this.modelForm.envConfigList.splice(index, 1);
            },

            //容器端口验证
            containerValidator(rule, value, callback) {
                if (!value) {
                    return callback(new Error(' '));
                }
                var reg = /^[1-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(' '));
                } else {
                    if (value <= 0 || value > 65565) {
                        callback(new Error(' '));
                    } else {
                        callback();
                    }
                }
            },
            //容器端口验证
            agreementValidator(rule, value, callback) {
                if (!value) {
                    return callback(new Error(' '));
                }
            },
            //service名称验证
            serviceNameValidator(rule, value, callback) {
                if (!value) {
                    return callback(new Error(' '));
                }
                if (value.length > 63) {
                    return callback(new Error(' '));
                }
                callback();
            },
            //节点端口验证
            nodeValidator(rule, value, callback) {
                if (!value) {
                    return callback();
                }
                var reg = /^[1-9]\d*$/;
                if (!reg.test(value)) {
                    callback(new Error(' '));
                } else {
                    if (value < 30000 || value > 32767) {
                        callback(new Error(' '));
                    } else {
                        callback();
                    }
                }
            },
            //存储验证
            storageValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error('请选择存储'));
                } else {
                    return callback();
                }
            },
            //挂载目录验证
            storagePathValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error('请输入挂载目录'));
                } else if (value == '/') {
                    callback(new Error('挂载目录不能为根目录'));
                } else {
                    return callback();
                }
            },
            //挂载名称验证
            storageNameValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error('请输入存储名称'));
                } else {
                    var reg = /^[a-zA-Z0-9]{1,20}$/;
                    if (!reg.test(value)) {
                        callback(new Error('只能字母或数字'));
                    } else {
                        return callback();
                    }
                }
            },
            //挂载容量验证
            storageCapacityValidator(rule, value, callback) {
                if (value == null || value <= 0) {
                    callback(new Error('请输入大于0的正整数'));
                } else {
                    var reg = /^(([1-9]\d{0,5})|0)?$/;
                    if (!reg.test(value)) {
                        callback(new Error('请输入大于0的正整数'));
                    } else {
                        return callback();
                    }
                }
            },

            //配置文件版本验证
            versionValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error(' '));
                } else {
                    return callback();
                }
            },
            //自定义环境变量验证
            customEnvValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error(' '));
                } else {
                    return callback();
                }
            },
            //环境变量配置文件版本验证
            versionEnvValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error(' '));
                } else {
                    return callback();
                }
            },
            //配置文件挂载目录验证
            catalogValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error(' '));
                } else if (value == '/') {
                    callback(new Error(' '));
                } else {
                    return callback();
                }
            },
            //配置文件挂载文件名验证
            fileNameValidator(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error(' '));
                } else {
                    return callback();
                }
            },
            //添加host
            addHost() {
                this.hostIndex++;
                if (this.modelForm.hostList == null) {
                    this.modelForm.hostList = []
                }
                this.modelForm.hostList.push({ip: null, domain: null, index: this.hostIndex})
            },
            //删除host
            deleteHost(index) {
                this.modelForm.hostList.splice(index, 1);
            },
            onValidate(prop, status, error) {
                if (!status) {
                    this.errorList.push(prop)
                }
            },
            save() {
                var that = this
                that.errorList = []
                that.stepList = [
                    {error: false},
                    {error: false},
                    {error: false},
                    {error: false},
                    {error: false}
                ]
                that.$refs['modelForm'].validate((valid) => {
                    if (valid) {
                        if (that.getUseCpuRequest() < 0) {
                            appUtils.showError('CPU请求超过可分配数值！')
                            return
                        }

                        if (that.getUseCpuLimit() < 0) {
                            appUtils.showError('CPU上限超过可分配数值！')
                            return
                        }


                        if (that.getUseMemoryRequest() < 0) {
                            appUtils.showError('内存请求超过可分配数值！')
                            return
                        }
                        if (that.getUseMemoryLimit() < 0) {
                            appUtils.showError('内存上限超过可分配数值！')
                            return
                        }


                        let modelForm = JSON.parse(JSON.stringify(that.modelForm))
                        if (modelForm.openConfig) {
                            dataUtils.each(modelForm.configList, (key, obj) => {
                                let versionId = modelForm.configList[key].versionId
                                let length = modelForm.configList[key].versionId.length
                                modelForm.configList[key].versionId = versionId[length - 1]
                            })
                        } else {
                            modelForm.configList = []
                        }

                        if (modelForm.openEnvConfig) {
                            dataUtils.each(modelForm.envConfigList, (key, obj) => {
                                let versionId = modelForm.envConfigList[key].versionId
                                let length = modelForm.envConfigList[key].versionId.length
                                modelForm.envConfigList[key].versionId = versionId[length - 1]
                            })
                        } else {
                            modelForm.envConfigList = []
                        }

                        modelForm.configList = modelForm.configList.concat(modelForm.envConfigList)

                        if (modelForm.headlessList != null && modelForm.headlessList.length > 0) {
                            modelForm.serviceList = modelForm.serviceList.concat(modelForm.headlessList)
                        }

                        delete modelForm.pods
                        if (modelForm.id == null) {
                            addApp(modelForm).then((res) => {
                                if (res.state) {
                                    appUtils.showSuccess(res.msg)
                                    this.$router.push(dataUtils.getData(Setting.topUrl));
                                } else {
                                    appUtils.showError(res.msg)
                                }
                            }).catch(err => {
                                appUtils.showError('保存失败！')
                            })
                        } else {
                            updateApp(modelForm).then((res) => {
                                if (res.state) {
                                    appUtils.showSuccess(res.msg)
                                    this.$router.push(dataUtils.getData(Setting.topUrl));
                                } else {
                                    appUtils.showError(res.msg)
                                }
                            }).catch(err => {
                                appUtils.showError('保存失败！')
                            })
                        }
                    } else {
                        setTimeout(() => {
                            dataUtils.each(that.errorList, (key, obj) => {
                                if (obj == 'name' || obj == 'sign' || obj == 'remark') {
                                    that.stepList[0].error = true
                                }
                                if (obj == 'image' || obj == 'imageVersion' || obj == 'cpuRequest' || obj == 'cpuLimit' || obj == 'memoryRequest' || obj == 'memoryLimit') {
                                    that.stepList[1].error = true
                                }
                                if (obj.indexOf('configList') >= 0 || obj.indexOf('envConfigList') >= 0) {
                                    that.stepList[1].error = true
                                }
                                if (obj.indexOf('serviceList') >= 0) {
                                    that.stepList[2].error = true
                                }

                                if (obj.indexOf('storageList') >= 0) {
                                    that.stepList[3].error = true
                                }

                                if (obj.indexOf('hostList') >= 0) {
                                    that.stepList[4].error = true
                                }
                            })
                        }, 100)
                    }
                })
            },
            //是否挂载配置文件切换事件
            changeOpenConfig(val) {
                if (val) {
                    this.getConfig()
                }
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
            //添加配置文件
            addConfig() {
                if (this.modelForm.configList == null) {
                    this.modelForm.configList = []
                }
                this.modelForm.configList.push({versionId: [], catalog: null, fileName: null})
            },
            //添加环境变量配置文件
            addEnvConfig() {
                if (this.modelForm.envConfigList == null) {
                    this.modelForm.envConfigList = []
                }
                this.modelForm.envConfigList.push({versionId: []})
            },
            //添加自定义环境变量
            addCustomEnv() {
                if (this.modelForm.customEnvList == null) {
                    this.modelForm.customEnvList = []
                }
                this.modelForm.customEnvList.push({envKey: null, envValue: null})
            },
            //获取存储
            getStorage() {
                listStorage().then(res => {
                    appUtils.res(res, () => {
                        this.storageList = res.data
                    })
                })
            },
            //增加存储
            addStorage() {
                let item = {storageId: null, path: null, show: true, index: this.storageIndex}
                item = JSON.parse(JSON.stringify(item))
                this.modelForm.storageList.push(item)
                this.activeNames.push(item.index)
                this.storageIndex++
            },
            //移除存储
            removeStorage(index) {
                this.modelForm.storageList.splice(index, 1);

            },
            //选择镜像
            selectImage(item) {
                this.modelForm.image = item.name
                this.showImage = false
            },
            //是否包含镜像
            containImage(imageName) {
                if (this.imageList != null) {
                    for (let i = 0; i < this.imageList.length; i++) {
                        if (this.imageList[i].name == imageName) {
                            return true
                        }
                    }
                }
                return false
            },
            handleCreateVersion(val) {
                this.imageVersionList.push({
                    value: val,
                    label: val
                });
            },
            //显示选择端口  30000---32767
            portShow(item) {
                let start = 30000
                let end = 32767
                if (item.portList == null || item.portList.length <= 0) {
                    item.portList = []
                    end = start + 100
                    if (end >= 32768) {
                        end = 32768
                    }
                    for (let i = start; i < end; i++) {
                        let have = false
                        if (this.usePortList.indexOf(i) >= 0) {
                            have = true
                        }
                        item.portList.push({
                            port: i,
                            have: have
                        })
                    }
                }
            },
            //加载更多端口
            loadPort(item) {
                let start = 30000
                let end = 32767
                if (item.portList == null || item.portList.length <= 0) {
                    item.portList = []
                } else {
                    start = item.portList[item.portList.length - 1].port + 1
                }
                end = start + 100
                if (end >= 32768) {
                    end = 32768
                }
                for (let i = start; i < end; i++) {
                    let have = false
                    if (this.usePortList.indexOf(i) >= 0) {
                        have = true
                    }
                    item.portList.push({
                        port: i,
                        have: have
                    })
                }
            },
            //获取应用市场应用
            getShopApp(id) {
                getShopApp(id).then(res => {
                    if (res.state) {
                        if (res.data != null) {
                            let app = JSON.parse(res.data.content)
                            this.type = app.type
                            let appData = {
                                data: app
                            }
                            this.initApp(appData)
                        } else {
                            appUtils.showError('应用不存在！')
                        }
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            //创建存储点击
            showStorageClick() {
                this.$refs['storageModel'].resetFields();
                this.showStorage = true
            },
            //保存存储
            saveStorage() {
                this.$refs['storageModel'].validate((valid) => {
                    if (valid) {
                        this.storageLoading = true
                        addStorage(this.storageModel).then(res => {
                            this.storageLoading = false
                            if (res.state) {
                                this.showStorage = false
                                this.getStorage()
                                appUtils.showSuccess(res.msg)
                            } else {
                                appUtils.showError(res.msg)
                            }
                        }).catch(err=>{
                            this.storageLoading = true
                        })
                    }
                })
            },
            //获取可用CPU请求
            getUseCpuRequest() {
                if(this.namespaceInfo==null){
                    return 0
                }
                let cpuRequest = this.modelForm.cpuRequest == null ? 0 : this.modelForm.cpuRequest
                return (this.namespaceInfo.cpuRequest - this.namespaceInfo.nowCpuRequest - (cpuRequest - this.cpuRequest)).toFixed(2)
            },
            //获取可用CPU上限
            getUseCpuLimit() {
                if(this.namespaceInfo==null){
                    return 0
                }
                let cpuLimit = this.modelForm.cpuLimit == null ? 0 : this.modelForm.cpuLimit
                return (this.namespaceInfo.cpuLimit - this.namespaceInfo.nowCpuLimit - (cpuLimit - this.cpuLimit)).toFixed(2)
            },
            //获取可用内存请求
            getUseMemoryRequest() {
                if(this.namespaceInfo==null){
                    return 0
                }
                let memoryRequest = 0
                if (this.modelForm.memoryRequest != null) {
                    if (this.modelForm.memoryRequestCompany == 'Mi') {
                        memoryRequest = this.modelForm.memoryRequest / 1024
                    } else if (this.modelForm.memoryRequestCompany == 'Gi') {
                        memoryRequest = this.modelForm.memoryRequest
                    } else if (this.modelForm.memoryRequestCompany == 'Ti') {
                        memoryRequest = this.modelForm.memoryRequest * 1024
                    }
                }
                return (this.namespaceInfo.memoryRequest - this.namespaceInfo.nowMemoryRequest - (memoryRequest - this.memoryRequest)).toFixed(2)
            },
            //获取可用内存上限
            getUseMemoryLimit() {
                if(this.namespaceInfo==null){
                    return 0
                }
                let memoryLimit = 0
                if (this.modelForm.memoryLimit != null) {
                    if (this.modelForm.memoryLimitCompany == 'Mi') {
                        memoryLimit = this.modelForm.memoryLimit / 1024
                    } else if (this.modelForm.memoryLimitCompany == 'Gi') {
                        memoryLimit = this.modelForm.memoryLimit
                    } else if (this.modelForm.memoryLimitCompany == 'Ti') {
                        memoryLimit = this.modelForm.memoryLimit * 1024
                    }
                }
                return (this.namespaceInfo.memoryLimit - this.namespaceInfo.nowMemoryLimit - (memoryLimit - this.memoryLimit)).toFixed(2)
            },
            //获取命名空间信息
            getNameSpaceData(callback) {
                currentNamespace().then(res=>{
                    this.namespaceInfo = res.data
                    callback()
                })
            },
        },

        mounted() {
            this.loading = true
            this.type = this.appType
            this.getNameSpaceData(() => {
                console.log(this.type)
                if (this.type != 'StatefulSet' && this.type != 'Deployment' && this.type != 'shop') {
                    appUtils.showError('地址有误，请检查地址！')
                    return
                }
                usePort().then(res => {
                    this.usePortList = res.data
                })
                if (this.id != null && this.id != 'add') {
                    this.getConfig(() => {
                        if (this.type == 'shop') {
                            this.getShopApp(this.id)
                        } else {
                            getApp(this.id).then(res => {
                                this.loading =false
                                if (res.state) {
                                    this.initApp(res)
                                } else {
                                    appUtils.showError(res.msg)
                                }
                            }).catch(err=>{
                                this.loading =false
                            })
                        }
                    })
                } else {
                    this.loading =false
                    this.modelForm.type = this.type
                    if (this.type == 'StatefulSet') {
                        this.modelForm.headlessList.push({
                            type: 'Headless',
                            sign: null,
                            portList: [{
                                agreement: 'TCP',
                                container: null
                            }]
                        })
                    }
                }
            })
        },
        beforeRouteEnter(to, from, next) {
            // 在导航完成前获取数据
            dataUtils.setData(Setting.topUrl, from.fullPath)
            next()

        }
    }
</script>
<style>
    #storage .el-collapse-item__arrow {
        display: none;
    }
    .form-table .ivu-input{
        border: 1px solid transparent;
    }
    .form-table .ivu-form-item-error .ivu-input {
        border: 1px solid #ed4014;
    }
    .form-table .ivu-input:focus {
        border-color: #4ca5d9;
        outline: 0;
        box-shadow: none;
    }
</style>
