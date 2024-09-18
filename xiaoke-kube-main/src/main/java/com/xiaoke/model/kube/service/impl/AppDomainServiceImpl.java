package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDomainDTO;
import com.xiaoke.entity.kube.entity.AppDomain;
import com.xiaoke.entity.kube.entity.AppDomainRule;
import com.xiaoke.entity.kube.entity.Domain;
import com.xiaoke.entity.kube.vo.AppDomainVO;
import com.xiaoke.entity.kube.vo.ConfigVersionVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.extend.kube.IngressService;
import com.xiaoke.model.kube.mapper.AppDomainMapper;
import com.xiaoke.model.kube.service.AppDomainRuleService;
import com.xiaoke.model.kube.service.AppDomainService;
import com.xiaoke.model.kube.service.DomainService;
import com.xiaoke.model.kube.service.ConfigVersionService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用域名 Service
 *
 * @author xiaoke
 * @date 2024-08-19 00:39:43
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppDomainServiceImpl extends ServiceImpl<AppDomainMapper, AppDomain> implements AppDomainService {
    private final AppDomainMapper appDomainMapper;
    private final AppDomainRuleService appDomainRuleService;
    private final IngressService ingressService;
    private final DomainService domainService;
    private final ConfigVersionService configVersionService;

    /**
     * 封装共用查询条件
     *
     * @param appDomain
     * @return
     */
    private QueryWrapper<AppDomain> baseQuery(AppDomain appDomain) {
        QueryWrapper<AppDomain> query = Wrappers.query();
        if (StrUtil.isNotEmpty(appDomain.getNamespace())) {
            query.lambda().eq(AppDomain::getNamespace, appDomain.getNamespace());
        }
        if (appDomain.getVersionId() != null) {
            query.lambda().eq(AppDomain::getVersionId, appDomain.getVersionId());
        }
        if (appDomain.getDomainId() != null) {
            query.lambda().eq(AppDomain::getDomainId, appDomain.getDomainId());
        }
        if (StrUtil.isNotEmpty(appDomain.getDomainPrefix())) {
            query.lambda().like(AppDomain::getDomainPrefix, appDomain.getDomainPrefix());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用域名
     *
     * @param page
     * @param appDomain
     * @return
     */
    @Override
    public R<IPage> pageAppDomain(Page page, AppDomain appDomain) {
        QueryWrapper<AppDomain> query = this.baseQuery(appDomain);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), AppDomainVO.class));
    }

    /**
     * 获取应用域名列表
     *
     * @param appDomain
     * @return
     */
    @Override
    public List<AppDomainVO> listAppDomain(AppDomain appDomain) {
        QueryWrapper<AppDomain> query = this.baseQuery(appDomain);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), AppDomainVO.class);
    }


    /**
     * 通过ID查询应用域名
     *
     * @param id
     * @return
     */
    @Override
    public AppDomainVO getAppDomainById(Integer id) {
        return JoinUtil.entity(this.getById(id), AppDomainVO.class);
    }

    /**
     * 添加应用域名
     *
     * @param appDomain
     * @return
     */
    @Override
    public R<Boolean> saveAppDomain(AppDomainDTO appDomain) throws Exception {
        //验证是否存在
        QueryWrapper<AppDomain> checkQuery = Wrappers.query();
        checkQuery.lambda().eq(AppDomain::getProtocol, appDomain.getProtocol());
        checkQuery.lambda().eq(AppDomain::getDomainPrefix, appDomain.getDomainPrefix());
        checkQuery.lambda().eq(AppDomain::getDomainId, appDomain.getDomainId());
        long count = this.count(checkQuery);
        if (count > 0) {
            return R.failed("域名已存在，请更换！");
        }
        SysUser user = SecurityUtils.getUser();
        appDomain.setCreateBy(user.getId());
        log.info("添加应用域名！应用域名:{}", appDomain);
        Boolean saveFlag = this.save(appDomain);
        if (saveFlag) {
            List<AppDomainRule> appDomainRuleList = appDomain.getAppDomainRuleList();
            for (AppDomainRule appDomainRule : appDomainRuleList) {
                appDomainRule.setAppDomainId(appDomain.getId());
            }
            appDomainRuleService.saveBatch(appDomain.getAppDomainRuleList());
            Boolean ingressFlag = ingressService.createIngress(this.getExtensionsV1beta1Api(appDomain), appDomain.getNamespace());
            if (!ingressFlag) {
                throw new Exception("添加应用域名失败！");
            }
            return R.ok(Boolean.TRUE, "添加应用域名成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用域名失败！");
        }
    }

    /**
     * 修改应用域名
     *
     * @param appDomain
     * @return
     */
    @Override
    public R<Boolean> updateAppDomain(AppDomain appDomain) {
        SysUser user = SecurityUtils.getUser();
        appDomain.setUpdateBy(user.getId());
        log.info("修改应用域名！id:{}", appDomain.getId());
        UpdateWrapper<AppDomain> updateWrapper = new UpdateWrapper<AppDomain>();
        updateWrapper.lambda().set(AppDomain::getDomainId, appDomain.getDomainId());
        updateWrapper.lambda().set(AppDomain::getVersionId, appDomain.getVersionId());
        updateWrapper.lambda().eq(AppDomain::getId, appDomain.getId());
        Boolean updateFlag = this.update(appDomain, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改应用域名成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用域名失败！");
        }

    }

    /**
     * 删除应用域名
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppDomainById(Integer id) {
        log.info("删除应用域名！id:{}", id);
        return R.ok(this.removeById(id), "删除应用域名成功！");
    }

    /**
     * 获取创建ingress YAML
     *
     * @param appDomain
     * @return
     */
    private ExtensionsV1beta1Ingress getExtensionsV1beta1Api(AppDomainDTO appDomain) {
        Domain domainTemp = domainService.getById(appDomain.getDomainId());
        ExtensionsV1beta1Ingress ingress = new ExtensionsV1beta1Ingress();
        V1ObjectMeta ingressMetadata = new V1ObjectMeta();

        String name = "";
        if (StrUtil.equals(appDomain.getDomainPrefix(), StringPool.AT)) {
            name = appDomain.getProtocol() + "-" + appDomain.getDomainId() + "-ingress";
        } else {
            name = appDomain.getProtocol() + "-" + appDomain.getDomainPrefix() + "-" + appDomain.getDomainId() + "-ingress";
        }
        ingressMetadata.setName(name);
        ingressMetadata.setNamespace(appDomain.getNamespace());
        ingress.setMetadata(ingressMetadata);
        ExtensionsV1beta1IngressSpec ingressSpec = new ExtensionsV1beta1IngressSpec();

        if (StrUtil.isNotEmpty(appDomain.getAnnotations())) {
            Map<String, String> ingressMetadataAnnotations = new HashMap<>();
            List<Map<String, Object>> annotationList = JsonUtils.jSONArrayConvertList(appDomain.getAnnotations());
            for (Map<String, Object> annotation : annotationList) {
                ingressMetadataAnnotations.put(annotation.get("key").toString(), annotation.get("value").toString());
            }
            ingressMetadata.setAnnotations(ingressMetadataAnnotations);
        }

        String domain = "";
        if (StrUtil.equals(appDomain.getDomainPrefix(), StringPool.AT)) {
            domain = domainTemp.getDomain();
        } else {
            domain = appDomain.getDomainPrefix() + "." + domainTemp.getDomain();
        }

        if ("https".equals(appDomain.getProtocol())) {
            List<ExtensionsV1beta1IngressTLS> ingressSpecTlsList = new ArrayList<>();
            ExtensionsV1beta1IngressTLS ingressSpecTls = new ExtensionsV1beta1IngressTLS();
            List<String> ingressSpecTlsHosts = new ArrayList<>();

            ingressSpecTlsHosts.add(domain);
            ingressSpecTls.setHosts(ingressSpecTlsHosts);
            //获取证书版本
            ConfigVersionVO versionConfig = configVersionService.getConfigVersionById(appDomain.getVersionId());
            ingressSpecTls.setSecretName(versionConfig.getConfig().getName() + ".v" + versionConfig.getVersion());
            ingressSpecTlsList.add(ingressSpecTls);
            ingressSpec.setTls(ingressSpecTlsList);
        }

        List<ExtensionsV1beta1IngressRule> ingressSpecRulesList = new ArrayList<>();
        ExtensionsV1beta1IngressRule ingressSpecRules = new ExtensionsV1beta1IngressRule();

        ingressSpecRules.setHost(domain);
        ExtensionsV1beta1HTTPIngressRuleValue ingressSpecRulesHttp = new ExtensionsV1beta1HTTPIngressRuleValue();
        List<ExtensionsV1beta1HTTPIngressPath> ingressSpecRulesHostHttpPathsList = new ArrayList<>();
        List<AppDomainRule> appDomainRuleList = appDomain.getAppDomainRuleList();
        for (AppDomainRule appDomainRule : appDomainRuleList) {
            ExtensionsV1beta1HTTPIngressPath ingressSpecRulesHostHttpPaths = new ExtensionsV1beta1HTTPIngressPath();
            ingressSpecRulesHostHttpPaths.setPath(appDomainRule.getPath());
            ExtensionsV1beta1IngressBackend ingressSpecRulesHostHttpPathsBackend = new ExtensionsV1beta1IngressBackend();
            //设置Service

            V1IngressServiceBackend ingressSpecRulesHostHttpPathsBackendV1IngressServiceBackend = new V1IngressServiceBackend();
            ingressSpecRulesHostHttpPathsBackendV1IngressServiceBackend.setName(appDomainRule.getServiceSign());
            //设置端口
            V1ServiceBackendPort v1ServiceBackendPort = new V1ServiceBackendPort();
            v1ServiceBackendPort.setNumber(appDomainRule.getPort());
            ingressSpecRulesHostHttpPathsBackendV1IngressServiceBackend.setPort(v1ServiceBackendPort);

            ingressSpecRulesHostHttpPathsBackend.setServiceName(appDomainRule.getServiceSign());
            ingressSpecRulesHostHttpPathsBackend.setServicePort(new IntOrString(appDomainRule.getPort()));
            ingressSpecRulesHostHttpPaths.setBackend(ingressSpecRulesHostHttpPathsBackend);
            ingressSpecRulesHostHttpPathsList.add(ingressSpecRulesHostHttpPaths);
        }

        ingressSpecRulesHttp.setPaths(ingressSpecRulesHostHttpPathsList);
        ingressSpecRules.setHttp(ingressSpecRulesHttp);
        ingressSpecRulesList.add(ingressSpecRules);
        ingressSpec.setRules(ingressSpecRulesList);
        ingress.setSpec(ingressSpec);
        return ingress;
    }

}