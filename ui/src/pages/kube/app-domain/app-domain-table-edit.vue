<!--
   应用域名
   author： xiaoke
   date： 2024-08-19 00:39:43
-->

<template>
    <div>
        <!-- 编辑应用域名 start -->
        <Modal
                v-model="showEdit"
                :title="editType?'编辑域名':'添加域名'"
                width="600">
            <Form ref="appDomainForm" :model="appDomainForm"
                  :rules="appDomainValidate" :label-width="100">
                <FormItem label="域名：" prop="domainPrefix">
                    <Input v-model.trim="appDomainForm.domainPrefix" size="large" :disabled="appDomainForm.id!=null">
                        <Select v-model="appDomainForm.protocol" :disabled="appDomainForm.id!=null" slot="prepend"
                                style="width: 80px">
                            <Option value="http">http://</Option>
                            <Option value="https">https://</Option>
                        </Select>
                        <Select v-model="appDomainForm.domainId" :disabled="appDomainForm.id!=null" slot="append"
                                style="width: 260px">
                            <Option v-for="item in extend.domainList" :value="item.id">{{ item.domain }}</Option>
                        </Select>
                    </Input>
                    <span>1 - 20 个字符（支持英文字母，数字，“-”，“@”）</span>
                    <br/>
                    <span>使用 “@” 解析顶级域名</span>
                </FormItem>

                <FormItem label="注解配置：">
                    <Row v-for="(item,index) in appDomainForm.annotations" style="margin-bottom: 5px">
                        <Col span="12">
                            <Input v-model="item.key" size="large" placeholder="key"/>
                        </Col>
                        <Col span="1" style="text-align: center">
                            <span>:</span>
                        </Col>
                        <Col span="9">
                            <Input v-model="item.value" size="large" placeholder="value"/>
                        </Col>
                        <Col span="2" style="text-align: center">
                            <el-button
                                    style="color: #F45236"
                                    type="text"
                                    size="mini"
                                    @click="deleteAnnotation(index)">删除
                            </el-button>
                        </Col>
                    </Row>
                    <el-button type="text" @click="showAnnotations = true">添加配置</el-button>
                </FormItem>

                <FormItem v-if="appDomainForm.protocol=='https'" label="证书：" prop="versionId">
                    <Cascader v-model="appDomainForm.versionId" size="large" :data="configList"
                              :load-data="loadConfig"></Cascader>
                </FormItem>
                <Tabs class="domain-edit-tab">
                    <TabPane v-for="(item,index) in appDomainForm.appDomainRuleList" :key="index" :label="'规则' + (index+1)">
                        <FormItem label="规则：" :prop="'appDomainRuleList.'+index+'.path'"
                                  :rules="{validator:validatePath, trigger: 'blur'}">
                            <Input v-model.trim="item.path" size="large" clearable/>
                            <span>1 - 20 个字符 以“/”开头（支持英文字母，数字，“-”）</span>
                        </FormItem>
                        <FormItem label="应用类型：" prop="path">
                            <RadioGroup v-model="item.appType" size="large" @on-change="appTypeChange(item)">
                                <Radio label="Deployment" border>无状态应用</Radio>
                                <Radio label="StatefulSet" border>有状态应用</Radio>
                            </RadioGroup>
                        </FormItem>
                        <FormItem label="应用：" :prop="'appDomainRuleList.'+index+'.appId'"
                                  :rules="{type:'number',required: true, message: '请选择应用', trigger: 'blur'}">
                            <Select transfer v-model="item.appId" size="large" @on-change="appChange(item)">
                                <Option v-for="item in item.appList" :value="item.id" :key="item.id">{{ item.name }}</Option>
                            </Select>
                        </FormItem>
                        <FormItem label="Service：" :prop="'appDomainRuleList.'+index+'.serviceSign'"
                                  :rules="{required: true, message: '请选择Service', trigger: 'blur'}">
                            <Select transfer v-model="item.serviceSign" size="large" @on-change="serviceChange(item)">
                                <Option v-for="item in item.serviceList" :value="item.sign" :key="item.id">{{ item.sign }}</Option>
                            </Select>
                        </FormItem>
                        <FormItem label="端口：" :prop="'appDomainRuleList.'+index+'.port'"
                                  :rules="{type:'number',required: true, message: '请选择端口', trigger: 'blur'}">
                            <Select transfer v-model="item.port" size="large">
                                <Option v-for="item in item.portList" :value="item.container" :key="item.id">{{
                                    item.container
                                    }}
                                </Option>
                            </Select>
                        </FormItem>
                    </TabPane>
                    <Button icon="md-add" type="primary" ghost @click="handleTabsAdd" size="small" slot="extra" style="margin-top: 5px">添加规则</Button>
                </Tabs>
            </Form>
            <div slot="footer">
                <Button type="primary" size="large"  :loading="saveLoading" @click="saveEdit">保存</Button>
            </div>
        </Modal>
        <!-- 编辑应用域名 end -->

        <Modal
                v-model="showAnnotations"
                :footer-hide="true"
                :heignt="800"
                title="选择注解配置"
                width="1200">
            <Row>
                <Col span="4">
                    <Anchor show-ink container="#anchorBox">
                        <AnchorLink v-for="item in annotationList" :href="'#'+item.anchor" :title="item.title"/>
                    </Anchor>
                </Col>
                <Col span="20">
                    <div id="anchorBox" style="height: 500px;overflow: auto">
                        <div v-for="item in annotationList">
                            <Alert v-text="item.title" :id="item.anchor" style="margin-top: 10px"></Alert>
                            <el-table
                                    :data="item.data"
                                    :header-row-style="()=>{return 'color: #202224'}"
                                    :header-cell-style="()=>{return 'background: #F1F4F9;'}"
                                    style="width: 100%">
                                <el-table-column
                                        prop="key"
                                        width="340"
                                        :show-overflow-tooltip="true"
                                        label="注解">
                                </el-table-column>
                                <el-table-column
                                        prop="type"
                                        label="类型"
                                        width="120">
                                </el-table-column>
                                <el-table-column
                                        :show-overflow-tooltip="true"
                                        prop="remark"
                                        label="描述">
                                </el-table-column>
                                <el-table-column
                                        label="操作"
                                        align="center"
                                        width="80">
                                    <template slot-scope="scope">
                                        <el-button
                                                type="text"
                                                size="mini"
                                                @click="selectAnnotation(scope.row)">选择
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </Col>
            </Row>
        </Modal>
    </div>
</template>
<script>
    import {
        getAppDomain,
        delAppDomain,
        addAppDomain,
        updateAppDomain
    } from '@/api/kube/appDomain'
    import {listApp} from '@/api/kube/app'
    import {listConfig} from '@/api/kube/config'
    import {getByConfig} from "@api/kube/configVersion";
    import appUtils from '@/utils/appUtils';
    import dataUtils from '@/utils/dataUtils';

    export default {
        props: {
            //扩展数据
            extend:{
                type: Object
            }
        },
        data() {
            return {
                // 应用域名表单
                appDomainForm: {
                    id: null,
                    protocol: null,
                    domainPrefix: null,
                    domainId: null,
                    versionId: null,
                    annotations: null,
                }
                //保存密等
                , saveLoading: false
                //参数验证
                , appDomainValidate: {
                    protocol:[
                        { required: true, message: '协议不能为空', trigger: 'blur'},
                        { max: 20, message: '协议最大长度20', trigger: 'blur' },
                    ],
                    domainPrefix:[
                        { required: true, message: '域名前缀不能为空', trigger: 'blur'},
                        { max: 50, message: '域名前缀最大长度50', trigger: 'blur' },
                        {message: ' ', trigger: 'blur', pattern: /^[A-Za-z0-9-@]+$/}
                    ],
                    domainId:[
                        {type: 'number', required: true, message: '域名不能为空', trigger: 'blur'},
                    ],
                    versionId: [
                        {type: 'array', required: true, message: '请选择证书', trigger: 'blur'}
                    ]
                }
                //是否显示编辑
                ,showEdit:false
                //编辑类型 true=编辑 false=添加
                ,editType:true
                ,showAnnotations: false
                , annotationList: [
                    {
                        anchor: 'nginx',
                        title: 'Nginx原生配置指令',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/server-snippet',
                                type: 'string',
                                remark: '在 server 指令域添加 Nginx 配置指令'
                            },{
                                key: 'nginx.ingress.kubernetes.io/configuration-snippet',
                                type: 'string',
                                remark: '在 location 指令域添加 Nginx 配置指令'
                            }
                        ]
                    },{
                        anchor: 'currency',
                        title: '通用配置',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/enable-access-log',
                                type: 'Boolean',
                                remark: '对当前虚拟主机设置是否启用访问日志，默认为真'
                            },{
                                key: 'nginx.ingress.kubernetes.io/server-alias',
                                type: 'string',
                                remark: '为 Nginx 添加更多的主机名，同 Nginx 配置指令 server name'
                            },{
                                key: 'nginx.ingress.kubernetes.io/app-root',
                                type: 'string',
                                remark: '将当前虚拟主机根目录的访问 302 跳转到当前指定的路径'
                            },{
                                key: 'nginx.ingress.kubernetes.io/client-body-buffer-size',
                                type: 'string',
                                remark: '同 Nginx 配置指令 client_body_buffer_size'
                            },{
                                key: 'nginx.ingress.kubernetes.io/use-regex',
                                type: 'Boolean',
                                remark: '是否对当前虚拟主机的 Nginx 指令 location 使用正则方式进行路径匹配，默认值为 false'
                            },{
                                key: 'nginx.ingress.kubernetes.io/custom-http-errors',
                                type: '[]int',
                                remark: '根据响应码状态定义为错误状态并跳转到设置的默认后端'
                            },{
                                key: 'nginx.ingress.kubernetes.io/default-backend',
                                type: 'string',
                                remark: '自定义默认后端的资源对象 Service 名称，当客户端的请求没有匹配的 Nginx 规则或响应错误时，将被转发到默认后端'
                            },{
                                key: 'nginx.ingress.kubernetes.io/whitelist-source-range',
                                type: 'CIDR',
                                remark: '功能同 ConfigMap 配置键 whitelist-source-range'
                            },{
                                key: 'nginx.ingress.kubernetes.io/permanent-redirect',
                                type: 'string',
                                remark: '设置永久重定向的目标地址'
                            },{
                                key: 'nginx.ingress.kubernetes.io/permanent-redirect-code',
                                type: 'number',
                                remark: '自定义永久重定向的响应码，默认为 301'
                            },{
                                key: 'nginx.ingress.kubernetes.io/temporal-redirect',
                                type: 'string',
                                remark: '设置临时重定向的目标地址'
                            },{
                                key: 'nginx.ingress.kubernetes.io/from-to-www-redirect',
                                type: 'Boolean',
                                remark: '设置是否将当前虚拟主机子域名为 www 的请求跳转到当前主机域名'
                            },{
                                key: 'nginx.ingress.kubernetes.io/rewrite-target',
                                type: 'URI',
                                remark: '同 Nginx 配置指令 rewrite'
                            },{
                                key: 'nginx.ingress.kubernetes.io/enable-rewrite-log',
                                type: 'Boolean',
                                remark: '同 Nginx 配置指令 rewrite log，默认为 false'
                            },{
                                key: 'nginx.ingress.kubernetes.io/mirror-uri',
                                type: 'string',
                                remark: '同 Nginx 配置指令 mirro'
                            },{
                                key: 'nginx.ingress.kubernetes.io/mirror-request-body',
                                type: 'Boolean',
                                remark: '同 Nginx 配置指令 mirror_request_body，默认为 true'
                            }
                        ]
                    },
                    {
                        anchor: 'visit',
                        title: '访问控制',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/limit-rate',
                                type: 'number',
                                remark: '访问流量速度限制，同 Nginx 配置指令 limit_rate'
                            },{
                                key: 'nginx.ingress.kubernetes.io/limit-rate-after',
                                type: 'number',
                                remark: '启用访问流量速度限制的最大值，同 Nginx 配置指令 limit_rate_after'
                            },{
                                key: 'nginx.ingress.kubernetes.io/limit-connections',
                                type: 'number',
                                remark: '节并发连接数限制，同 Nginx 配置指令 limit_conn'
                            },{
                                key: 'nginx.ingress.kubernetes.io/limit-rps',
                                type: 'number',
                                remark: '每秒请求频率限制，burst 参数为给定值的 5 倍，响应状态码由 ConfigMap 的 limit-req-status-code 设定'
                            },{
                                key: 'nginx.ingress.kubernetes.io/limit-rpm',
                                type: 'number',
                                remark: '每分钟请求频率限制，burst 参数为给定值的 5 倍，响应状态码由 ConfigMap 的 limit-req-status-code 设定'
                            },{
                                key: 'nginx.ingress.kubernetes.io/limit-whitelist',
                                type: 'CIDR',
                                remark: '对以上限制设置基于 IP 的白名单'
                            }
                        ]
                    },
                    {
                        anchor: 'authentication',
                        title: '认证管理',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/enable-global-auth',
                                type: 'Boolean',
                                remark: '如果 ConfigMap 的 global-auth-url 被设置，Nginx 会将所有的请求重定向到提供身份验证的 URL，默认为 true'
                            },{
                                key: 'nginx.ingress.kubernetes.io/satisfy',
                                type: 'string',
                                remark: '同 Nginx 配置指令 satisfy'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-type',
                                type: 'basic 或 digest',
                                remark: '设置 HTTP 认证类型，支持基本和摘要两种类型'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-secret',
                                type: 'string',
                                remark: '指定关联资源对象 secret 的名称'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-realm',
                                type: 'string',
                                remark: '设置基本认证的提示信息 auth_basic'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-url',
                                type: 'string',
                                remark: '设置提供外部身份认证的 URL，由 Nginx 配置指令 auth_request 提供该功能'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-signin',
                                type: 'string',
                                remark: '设置当外部认证返回 401 时跳转的 URL，通常为提示输入用户名和密码的 URL'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-method',
                                type: 'string',
                                remark: '指定访问外部认证 URL 的 HTTP 方法，由 Nginx 配置指令 proxy_method 提供该功能'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-request-redirect',
                                type: 'string',
                                remark: '设置发送给认证服务器请求头中 X-Auth-Request-Redirect 的值'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-cache-key',
                                type: 'string',
                                remark: '启用认证缓存，并设置认证缓存的关键字'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-cache-duration',
                                type: 'string',
                                remark: '基于响应码设置认证缓存的有效时间'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-response-headers',
                                type: 'string',
                                remark: '设置认证请求完成后传递到真实后端的头信息'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-snippet',
                                type: 'string',
                                remark: '可以自定义在外部认证指令区域添加 Nginx 配置指令'
                            }
                        ]
                    },
                    {
                        anchor: 'cross',
                        title: '跨域访问',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/enable-cors',
                                type: 'Boolean',
                                remark: '是否启用跨域访问支持，默认为 false令'
                            },{
                                key: 'nginx.ingress.kubernetes.io/cors-allow-origin',
                                type: 'string',
                                remark: '允许跨域访问的域名，默认为 *，表示接受任意域名的访问'
                            },{
                                key: 'nginx.ingress.kubernetes.io/cors-allow-methods',
                                type: 'string',
                                remark: '允许跨域访问方法，默认为 GET、PUT、POST、DELETE、PATCH、OPTIONS'
                            },{
                                key: 'nginx.ingress.kubernetes.io/cors-allow-headers',
                                type: 'string',
                                remark: '允许跨域访问的请求头，默认为 DNT，X-CustomHeader、Keep-Alive、User-Agent、X-Requested-With、If-Modified-Since、Cache-Control、Content-Type、Authorization'
                            },{
                                key: 'nginx.ingress.kubernetes.io/cors-allow-credentials',
                                type: 'Boolean',
                                remark: '设置在响应头中 Access-Control-Allow-Credentials 的值，设置是否允许客户端携带验证信息，如 cookie 等，默认为 true'
                            },{
                                key: 'nginx.ingress.kubernetes.io/cors-max-age',
                                type: 'number',
                                remark: '设置响应头中 Access-Control-Max-Age 的值，设置返回结果可以用于缓存的最长时间，默认为 1728000 秒'
                            }
                        ]
                    },
                    {
                        anchor: 'agent',
                        title: '代理配置',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/service-upstream',
                                type: 'Boolean',
                                remark: '默认 Nginx 以 Service 中 Pod 的 IP 和端口为 Upstream 中的成员列表，该参数为 true 时，将以 Service 的 ClusterIP 和端口为被代理入口，该功能避免了因 Pod 漂移带来的 Upstream 的配置变化'
                            },{
                                key: 'nginx.ingress.kubernetes.io/backend-protocol',
                                type: 'НТТР 或 HTTPS 或 GRPC 或 GRPCS 或 AJP 或 FCGI',
                                remark: '设置代理后端服务器的代理协议类型，默认为 HTTP'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-body-size',
                                type: 'string',
                                remark: '同 Nginx 配置指令 client_max_body-size，默认为 1m'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-cookie-do-main',
                                type: 'string',
                                remark: '同 Nginx 配置指令 proxy_cookie_domain'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-cookie-path',
                                type: 'string',
                                remark: '同 Nginx 配置指令 proxy_cookie_path'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-connect-timeout',
                                type: 'number',
                                remark: '同 Nginx 配置指令 proxy_connect_timeout'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-send-time-out',
                                type: 'number',
                                remark: '同 Nginx 配置指令 proxy_send_timeout'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-read-time-out',
                                type: 'number',
                                remark: '同 Nginx 配置指令 proxy_read_timeout'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-next-up-stream',
                                type: 'string',
                                remark: '同 Nginx 配置指令 proxy_next_upstream'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-next-up-stream-timeout',
                                type: 'number',
                                remark: '同 Nginx 配置指令 proxy_next_upstream_timeout'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-next-up-stream-tries',
                                type: 'number',
                                remark: '同 Nginx 配置指令 proxy_next_upstream_tries'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-buffering',
                                type: 'string',
                                remark: '同 Nginx 配置指令 proxy_buffering'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-buffers-number',
                                type: 'number',
                                remark: '同 Nginx 配置指令 proxy_buffers'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-buffer-size',
                                type: 'string',
                                remark: '同 Nginx 配置指令 proxy_buffer_size'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-request-buffering',
                                type: 'string',
                                remark: '同 Nginx 配置指令 proxy_request_buffering'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-http-ver-sion',
                                type: '1.0 或 1.1',
                                remark: '同 Nginx 配置指令 proxy_http_version，默认为 1.1'
                            },{
                                key: 'nginx.ingress.kubernetes.io/upstream-vhost',
                                type: 'string',
                                remark: '自定义发送到上游服务器的信息头字段中 Host 的内容，相当于 Nginx 配置指令 proxy_set_header Host $host 的设置'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-redirect-from',
                                type: 'string',
                                remark: '设置要替换的源文本，同 Nginx 配置指令 proxy_redirect'
                            },{
                                key: 'nginx.ingress.kubernetes.io/proxy-redirect-to',
                                type: 'string',
                                remark: '设置要替换的目标文本，同 Nginx 配置指 proxy redirect'
                            },{
                                key: 'nginx.ingress.kubernetes.io/connection-proxy-header',
                                type: 'string',
                                remark: '设置发送到被代理服务器请求头中字段属性 connection 的值，相当于 Nginx 配置指令 proxy_set_header Connection 的状态为 Keep-Alive'
                            },{
                                key: 'nginx.ingress.kubernetes.io/x-forwarded-prefix',
                                type: 'string',
                                remark: '创建并设置代理请求头属性字段 X-Forwarded-Prefix 属性，用以向后端传递请求路径'
                            },{
                                key: 'nginx.ingress.kubernetes.io/http2-push-pre-load',
                                type: 'Boolean',
                                remark: '同 Nginx 配置指令 http2-push-preload，默认值为 false'
                            }
                        ]
                    },
                    {
                        anchor: 'load',
                        title: '负载均衡',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/upstream-hash-by',
                                type: 'string',
                                remark: '同 Nginx 配置指令 hash，此处默认为一致性哈希负载算法，允许除了客户端 IP 或 cookie 之外的会话粘连'
                            },{
                                key: 'nginx.ingress.kubernetes.io/upstream-hash-by-subset',
                                type: 'Boolean',
                                remark: '设置是否使用子集模式的一致性哈希负载算法，默认为 false'
                            },{
                                key: 'nginx.ingress.kubernetes.io/upstream-hash-by-subset-size',
                                type: 'int',
                                remark: '设置子集模式中上游服务器分组的大小，默认为 3'
                            },{
                                key: 'nginx.ingress.kubernetes.io/load-balance',
                                type: 'round_robin 或 ewma',
                                remark: '设置负载均衡算法，基于 balancer_by_lua 模块实现，支持轮询和 Peak EWMA 两种负载算法'
                            }
                        ]
                    },
                    {
                        anchor: 'session',
                        title: '会话保持配置',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/affinity',
                                type: 'cookie',
                                remark: '设置会话保持类型，目前只有 cookie 类型'
                            },{
                                key: 'nginx.ingress.kubernetes.io/session-cookie-name',
                                type: 'string',
                                remark: '设置 cookie 名称，默认为 INGRESSCOOKIE'
                            },{
                                key: 'nginx.ingress.kubernetes.io/session-cookie-path',
                                type: 'string',
                                remark: '设置 cookie 字段 path 的值，默认值为当前资源实例 path 的设置。如果启用 use-regex 功能，使用正则匹配时，必须单独指定，不能使用默认值'
                            },{
                                key: 'nginx.ingress.kubernetes.io/session-cookie-max-age',
                                type: '--',
                                remark: '设置 cookie 字段 max-age 的值，表示 cookie 过期时间'
                            },{
                                key: 'nginx.ingress.kubernetes.io/session-cookie-expires',
                                type: '--',
                                remark: '为兼容旧的浏览器，设置 cookie 字段 expires 的值，表示 cookie 过期时间'
                            },{
                                key: 'nginx.ingress.kubernetes.io/session-cookie-change-on-failure',
                                type: 'Boolean',
                                remark: '当会话保持的被代理服务器请求失败时，如果设置值为 true，则将下次请求更改为向另一台被代理服务器转发，否则继续向当前被代理服务器转发请求'
                            }
                        ]
                    },
                    {
                        anchor: 'https',
                        title: 'HTTPS配置',
                        data: [
                            {
                                key: 'Boolean',
                                remark: '当客户端的 HTTPS 被外部集群进行 SSL 卸载（SSL offloading）时，仍将 HTTP 的请求强制跳转到 HTTPS 端口'
                            },{
                                key: 'nginx.ingress.kubernetes.io/ssl-redirect',
                                type: 'Boolean',
                                remark: '设置当前虚拟主机支持 HTTPS 请求时，是否将 HTTP 的请求强制跳转到 HTTPS 端口，全局默认为 true'
                            },{
                                key: 'nginx.ingress.kubernetes.io/ssl-passthrough',
                                type: 'Boolean',
                                remark: '设置是否启用 SSL 透传'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-tls-secret',
                                type: 'string',
                                remark: '设置客户端证书的资源对象名称'
                            },{
                                key: 'nginx.ingress.kubernetes.io/ssl-ciphers',
                                type: 'string',
                                remark: '设置 TLS 用于协商使用的加密算法组合，同 Nginx 配置指令 ssl_ciphers'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-tls-verify-client',
                                type: 'string',
                                remark: '是否启用客户端证书验证，同 Nginx 配置指令 ssl_verify_client'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-tls-verify-depth',
                                type: 'number',
                                remark: '客户端证书链的验证深度同 Nginx 配置指令 ssl_verify_depth'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-tls-error-page',
                                type: 'string',
                                remark: '设置客户端证书验证错误时的跳转页面'
                            },{
                                key: 'nginx.ingress.kubernetes.io/auth-tls-pass-certificate-to-upstream',
                                type: 'Boolean',
                                remark: '指定证书是否传递到上游服务器'
                            },{
                                key: 'nginx.ingress.kubernetes.io/secure-verify-ca-secret',
                                type: 'string',
                                remark: '设置是否启用对被代理服务器的 SSL 证书验证功能'
                            }
                        ]
                    },
                    {
                        anchor: 'canary',
                        title: '“金丝雀”发布',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/canary',
                                type: 'Boolean',
                                remark: '启用“金丝雀”发布功能'
                            },{
                                key: 'nginx.ingress.kubernetes.io/canary-by-header',
                                type: 'string',
                                remark: '设置请求头属性字段的名称，用于根据该字段的值判断是否将请求路由到“金丝雀”服务器组，该字段值为 always 时则该请求被路由到“金丝雀”服务器组；该字段值为 never 时则不路由到“金丝雀”服务器组'
                            },{
                                key: 'nginx.ingress.kubernetes.io/canary-by-header-value',
                                type: 'string',
                                remark: '自定义用于判断是否路由到“金丝雀”服务器组的请求头字段值，默认为 always，必须与 canary-by-header同时使用'
                            },{
                                key: 'nginx.ingress.kubernetes.io/canary-by-cookie',
                                type: 'string',
                                remark: '设置 cookie 的字段名称，用于根据该字段的值判断是否将请求路由到“金丝雀”服务器组。always 则路由到“金丝雀”服务器组；never 则永远不路由到“金丝雀”服务器组'
                            },{
                                key: 'nginx.ingress.kubernetes.io/canary-weight',
                                type: 'number',
                                remark: '将请求基于整数（0~100）的请求百分比随机路由到“金丝雀”服务器组；100 表示所有请求都路由到“金丝雀”服务器组；0 表示不路由任何请求到“金丝雀”服务器组'
                            }
                        ]
                    },
                    {
                        anchor: 'lua',
                        title: 'lua-resty-waf模块',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf',
                                type: 'string',
                                remark: '设置 WAF 防火墙的工作模式，inactive 表示不执行任何操作；active 表示启用：simulate 模式下，如果给定请求有匹配的规则，它将记录一条警告消息而不进行处理。这有助于在完全部署规则之前调试规则并消除可能的误报'
                            },{
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf-debug',
                                type: 'Boolean',
                                remark: '设置是否启用调试功能，默认值为 false'
                            },{
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf-ignore-rulesets',
                                type: 'string',
                                remark: '设置忽略规则集的名称，当某些规则集（如 sqli 或 xss crs 规则集）太容易误报或不适用时，可通过该设置进行忽略处理'
                            },{
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf-extra-rules',
                                type: 'string',
                                remark: '设置自定义的规则理'
                            },{
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf-allow-unknown-content-types',
                                type: 'Boolean',
                                remark: '设置在发送了不在允许内容类型表中的内容类型头时是否继续处理请求。默认允许的为 text/html、text/json、application/json 的文档类型，默认值为 false'
                            },{
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf-score-threshold',
                                type: 'number',
                                remark: '设置请求异常评分的阈值，如果超过这个阈值，则拒绝该请求，默认值为 5'
                            },{
                                key: 'nginx.ingress.kubernetes.io/lua-resty-waf-process-multipart-body',
                                type: 'Boolean',
                                remark: '设置是否使用 lua-resty-upload 模块对 multipart/form-data 类型请求体的处理，默认为 true'
                            }
                        ]
                    },
                    {
                        anchor: 'ModSecurity',
                        title: 'ModSecurity模块配置',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/enable-mod-security',
                                type: 'Boolean',
                                remark: '设置是否启用 ModSecurity 过滤，启用时应用推荐的规则以仅检测（Detection-Only）模式运行'
                            },{
                                key: 'nginx.ingress.kubernetes.io/enable-owasp-core-rules',
                                type: 'Boolean',
                                remark: '设置是否使用 OWASP 核心规则进行请求检测'
                            },{
                                key: 'nginx.ingress.kubernetes.io/modsecurity-transaction-id',
                                type: 'string',
                                remark: '设置从 Nginx 传递事务 ID，而不是在库中自动生成，有利于在 ModSecurity 中跟踪查看检测的请求，对应模块配置指令为 modsecurity_transaction_id'
                            },{
                                key: 'nginx.ingress.kubernetes.io/modsecurity-snippet',
                                type: 'string',
                                remark: '添加模块配置指令 modsecurity_rules 的内容'
                            }
                        ]
                    },
                    {
                        anchor: 'Influxdb模块配置',
                        title: 'Influxdb模块配置',
                        data: [
                            {
                                key: 'nginx.ingress.kubernetes.io/enable-influxdb',
                                type: 'Boolean',
                                remark: '是否启用 Influxdb 输出功能'
                            },{
                                key: 'nginx.ingress.kubernetes.io/influxdb-measurement',
                                type: 'string',
                                remark: '指定 Influxdb 中的 measurement 名称'
                            },{
                                key: 'nginx.ingress.kubernetes.io/influxdb-port',
                                type: 'string',
                                remark: '指定 Influxdb 的端口'
                            },{
                                key: 'nginx.ingress.kubernetes.io/influxdb-host',
                                type: 'string',
                                remark: '指定 Influxdb 的 IP 地址'
                            },{
                                key: 'nginx.ingress.kubernetes.io/influxdb-server-name',
                                type: 'string',
                                remark: '设置自己的应用标识'
                            }
                        ]
                    }
                ]
                //配置文件列表
                ,configList: []
            }
        },
        computed: {},
        methods: {
            //验证规则
            validatePath(rule, value, callback) {
                if (value == null || value.length <= 0) {
                    callback(new Error(' '));
                } else if (value.length > 20) {
                    callback(new Error(' '));
                    return
                } else if (value.substring(0, 1) != '/') {
                    callback(new Error(' '));
                } else {
                    callback();
                }
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
            handleUpdate (appDomainForm) {
                updateAppDomain(appDomainForm).then(res => {
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
            handleSave(appDomainForm) {
                addAppDomain(appDomainForm).then(res => {
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
                if (this.$refs['appDomainForm'] != null) {
                    this.$refs['appDomainForm'].resetFields();
                }
                this.getConfig()
                if (row != null) {
                    let loading = appUtils.showLoading()
                    getAppDomain(row.id).then(res=>{
                        appUtils.hideLoading(loading)
                        if(res.state){
                            let model = res.data
                            if(model.annotations!=null){
                                model.annotations = JSON.parse(model.annotations)
                            }else {
                                model.annotations = []
                            }
                            if(model.appDomainRuleList==null){
                                model.appDomainRuleList = []
                            }
                            if(model.configVersion!=null&&model.configVersion.configId!=null){
                                model.versionId = [model.configVersion.configId, model.versionId]
                            }
                            this.appDomainForm = model
                            dataUtils.each(this.appDomainForm.appDomainRuleList, (key, obj) => {
                                this.getApp(this.appDomainForm.appDomainRuleList[key])
                            })
                            this.showEdit = true
                        }else{
                            appUtils.showError(res.msg)
                        }
                    }).catch(err=>{
                        appUtils.hideLoading(loading)
                    })
                } else {
                    this.appDomainForm = {
                        id: null,
                        protocol: null,
                        domainPrefix: null,
                        domainId: null,
                        versionId: null,
                        annotations: [],
                        appDomainRuleList:[{
                            path: '/',
                            appId: null,
                            appType: 'Deployment',
                            serviceSign: null,
                            port: null,
                            appList: []
                        }]
                    }
                    if (this.domainList != null && this.domainList.length > 0) {
                        this.appDomainForm.domainId = this.domainList[0].id
                    }
                    this.getApp(this.appDomainForm.appDomainRuleList[0])
                    this.showEdit = true
                }
            },
            //获取应用
            getApp(item) {
                listApp({type: item.appType}).then(res => {
                    if (res.state) {
                        item.appList = res.data
                        if (item.appList != null && item.appList.length == 1) {
                            item.appId = item.appList[0].id
                        } else {
                            item.serviceList = []
                            item.serviceSign = null
                        }
                        this.appChange(item)
                    } else {
                        appUtils.showError(res.msg)
                    }
                })
            },
            //切换应用
            appChange(item) {
                if (item.appId != null) {
                    let app = item.appList.filter(appItem => appItem.id == item.appId);
                    item.serviceList = app[0].serviceList
                    if (item.serviceList != null && item.serviceList.length > 0) {
                        item.serviceSign = item.serviceList[0].sign
                        this.serviceChange(item)
                    }
                }
            },
            //切换服务
            serviceChange(item) {
                if (item.serviceSign != null) {
                    let service = item.serviceList.filter(portItem => portItem.sign == item.serviceSign);
                    item.portList = service[0].portList
                    if (item.portList != null && item.portList.length > 0) {
                        item.port = item.portList[0].container
                    }
                }
            },
            /**
             * 保存数据
             */
            saveEdit() {
                this.$refs['appDomainForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                    let appDomainForm = JSON.parse(JSON.stringify(this.appDomainForm))
                    if(appDomainForm.versionId!=null){
                        appDomainForm.versionId = appDomainForm.versionId[1]
                    }

                    if(appDomainForm.annotations!=null){
                        appDomainForm.annotations = JSON.stringify(appDomainForm.annotations)
                    }
                    this.saveLoading = true
                    if (appDomainForm.id != null) {
                        this.handleUpdate(appDomainForm)
                    } else {
                        this.handleSave(appDomainForm)
                    }
                })
            },

            /**
             * 删除数据
             */
            rowDel(row) {
                let loading = appUtils.showLoading('正在删除...')
                delAppDomain(row.id).then((res) => {
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
            //选择注解
            selectAnnotation(row){
                this.appDomainForm.annotations.push(row)
                this.showAnnotations = false
            },
            //删除注解
            deleteAnnotation(index){
                this.appDomainForm.annotations.splice(index, 1);
            },
            //添加规则
            handleTabsAdd() {
                this.appDomainForm.appDomainRuleList.push({
                    appDomainId: this.appDomainForm.id,
                    path: '/',
                    appId: null,
                    appType: 'Deployment',
                    serviceSign: null,
                    port: null,
                    appList: []
                })
                this.getApp(this.appDomainForm.appDomainRuleList[this.appDomainForm.appDomainRuleList.length - 1])
            },
            //获取配置文件
            getConfig() {
                listConfig({type: 2}).then(res => {
                    if (res.state) {
                        this.configList = []
                        dataUtils.each(res.data, (key, obj) => {
                            let item = {
                                value: obj.id,
                                label: obj.name,
                                children: [],
                                loading: false
                            }
                            this.configList.push(item)
                        })
                    } else {
                        appUtils.showError(res.msg)
                    }
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
            //加载配置文件版本
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
            //切换应用类型
            appTypeChange(item) {
                item.appId = null
                item.port = null
                this.getApp(item)
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
<style>
    .domain-edit-tab .ivu-tabs-nav .ivu-tabs-tab {
        font-weight: 400 !important;
        font-size: 18px !important;
        color: #929292;
    }

    .domain-edit-tab .ivu-tabs-nav .ivu-tabs-tab-active {
        color: #202224;
        font-weight: 600 !important;
    }
</style>
