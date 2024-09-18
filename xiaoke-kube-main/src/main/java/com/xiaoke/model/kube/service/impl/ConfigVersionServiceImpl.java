package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.entity.kube.vo.ConfigVersionVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.extend.kube.ConfigMapService;
import com.xiaoke.extend.kube.SecretService;
import com.xiaoke.model.kube.mapper.ConfigMapper;
import com.xiaoke.model.kube.mapper.ConfigVersionMapper;
import com.xiaoke.model.kube.service.ConfigVersionService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Secret;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置版本 Service
 *
 * @author xiaoke
 * @date 2024-08-03 16:25:16
 */
@Service
@AllArgsConstructor
@Slf4j
public class ConfigVersionServiceImpl extends ServiceImpl<ConfigVersionMapper, ConfigVersion> implements ConfigVersionService {
    private final ConfigVersionMapper configVersionMapper;
    private final ConfigMapper configMapper;
    private final SecretService secretService;
    private final ConfigMapService configMapService;

    /**
     * 封装共用查询条件
     *
     * @param configVersion
     * @return
     */
    private QueryWrapper<ConfigVersion> baseQuery(ConfigVersion configVersion) {
        QueryWrapper<ConfigVersion> query = Wrappers.query();
        if (configVersion.getConfigId() != null) {
            query.lambda().eq(ConfigVersion::getConfigId, configVersion.getConfigId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询配置版本
     *
     * @param page
     * @param configVersion
     * @return
     */
    @Override
    public R<IPage> pageConfigVersion(Page page, ConfigVersion configVersion) {
        QueryWrapper<ConfigVersion> query = this.baseQuery(configVersion);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), ConfigVersionVO.class));
    }

    /**
     * 获取配置版本列表
     *
     * @param configVersion
     * @return
     */
    @Override
    public List<ConfigVersionVO> listConfigVersion(ConfigVersion configVersion) {
        QueryWrapper<ConfigVersion> query = this.baseQuery(configVersion);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), ConfigVersionVO.class);
    }


    /**
     * 通过ID查询配置版本
     *
     * @param id
     * @return
     */
    @Override
    public ConfigVersionVO getConfigVersionById(Integer id) {
        return JoinUtil.entity(this.getById(id), ConfigVersionVO.class);
    }

    /**
     * 添加配置版本
     *
     * @param configVersion
     * @return
     */
    @Override
    public R<Boolean> saveConfigVersion(ConfigVersion configVersion,String namespace) throws Exception{

        ConfigVersion queryVersion = new ConfigVersion();
        queryVersion.setConfigId(configVersion.getConfigId());
        QueryWrapper<ConfigVersion> query = Wrappers.query(queryVersion);
        query.select("MAX(version) AS version");
        ConfigVersion version = this.getOne(query);
        configVersion.setId(null);
        configVersion.setVersion(version.getVersion() + 1);
        if(StrUtil.isNotEmpty(namespace)){
            //k8s创建ConfigMap
            Boolean configMap = this.createConfigMap(configVersion, namespace);
            if(!configMap){
                throw new Exception("新增配置文件版本失败！");
            }
        }

        //保存数据
        this.save(configVersion);
        //保存版本
        Config config = new Config();
        config.setId(configVersion.getConfigId());
        config.setVersion(configVersion.getVersion());
        config.updateById();
        return R.ok(Boolean.TRUE, "编辑成功！");
    }

    /**
     * 修改配置版本
     *
     * @param configVersion
     * @return
     */
    @Override
    public R<Boolean> updateConfigVersion(ConfigVersion configVersion) {
        log.info("修改配置版本！id:{}", configVersion.getId());
        UpdateWrapper<ConfigVersion> updateWrapper = new UpdateWrapper<ConfigVersion>();
        updateWrapper.lambda().set(ConfigVersion::getConfigId, configVersion.getConfigId());
        updateWrapper.lambda().set(ConfigVersion::getVersion, configVersion.getVersion());
        updateWrapper.lambda().eq(ConfigVersion::getId, configVersion.getId());
        Boolean updateFlag = this.update(configVersion, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改配置版本成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改配置版本失败！");
        }

    }

    /**
     * 删除配置版本
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeConfigVersionById(Integer id) {
        log.info("删除配置版本！id:{}", id);
        return R.ok(this.removeById(id), "删除配置版本成功！");
    }

    /**
     * 创建ConfigMap
     *
     * @param configVersion
     * @return
     */
    @Override
    public Boolean createConfigMap(ConfigVersion configVersion, String namespace) {
        Config config = configMapper.selectById(configVersion.getConfigId());
        //证书
        if ("2".equals(config.getType())) {
            V1Secret secret = new V1Secret();
            V1ObjectMeta secretMetadata = new V1ObjectMeta();
            secretMetadata.setName(config.getName() + ".v" + configVersion.getVersion());
            secret.setMetadata(secretMetadata);
            Map<String, byte[]> secretData = new HashMap<>();

            Map<String, Object> content = JsonUtils.jsonToMap(configVersion.getContent());
            secretData.put("tls.crt", Base64.encode(content.get("cert").toString()).getBytes());
            secretData.put("tls.key", Base64.encode(content.get("key").toString()).getBytes());

            secret.setData(secretData);
            return secretService.createSecret(secret, namespace);
        } else {
            V1ConfigMap configMap = new V1ConfigMap();


            V1ObjectMeta configMapMetadata = new V1ObjectMeta();
            configMapMetadata.setName(config.getName() + ".v" + configVersion.getVersion());
            configMap.setMetadata(configMapMetadata);
            Map<String, String> configMapData = new HashMap<>();
            if ("0".equals(config.getType())) {
                List<Map<String, String>> contentList = JsonUtils.jSONArrayConvertList(configVersion.getContent());
                for (Map<String, String> content : contentList) {
                    configMapData.put(content.get("key"), content.get("value"));
                }
            } else if ("1".equals(config.getType())) {
                configMapData.put(config.getName(), configVersion.getContent());
            }
            configMap.setData(configMapData);
            return configMapService.createConfigMap(configMap, namespace);
        }
    }


}