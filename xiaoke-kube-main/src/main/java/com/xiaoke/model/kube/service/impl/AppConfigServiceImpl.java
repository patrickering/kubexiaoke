package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppConfigDTO;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppConfig;
import com.xiaoke.entity.kube.entity.AppDomain;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.entity.kube.vo.AppConfigVO;
import com.xiaoke.entity.kube.vo.AppDomainVO;
import com.xiaoke.entity.kube.vo.ConfigVersionVO;
import com.xiaoke.model.kube.mapper.AppConfigMapper;
import com.xiaoke.model.kube.mapper.AppMapper;
import com.xiaoke.model.kube.service.AppConfigService;
import com.xiaoke.model.kube.service.AppDomainService;
import com.xiaoke.model.kube.service.ConfigVersionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用配置 Service
 *
 * @author xiaoke
 * @date 2024-08-03 16:31:30
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppConfigServiceImpl extends ServiceImpl<AppConfigMapper, AppConfig> implements AppConfigService {
    private final AppConfigMapper appConfigMapper;
    private final ConfigVersionService configVersionService;
    private final AppMapper appMapper;
    private final AppDomainService appDomainService;

    /**
     * 封装共用查询条件
     *
     * @param appConfig
     * @return
     */
    private QueryWrapper<AppConfig> baseQuery(AppConfig appConfig) {
        QueryWrapper<AppConfig> query = Wrappers.query();
        if (appConfig.getAppId() != null) {
            query.lambda().eq(AppConfig::getAppId, appConfig.getAppId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用配置
     *
     * @param page
     * @param appConfig
     * @return
     */
    @Override
    public R<IPage> pageAppConfig(Page page, AppConfig appConfig) {
        QueryWrapper<AppConfig> query = this.baseQuery(appConfig);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用配置列表
     *
     * @param appConfig
     * @return
     */
    @Override
    public List<AppConfig> listAppConfig(AppConfig appConfig) {
        QueryWrapper<AppConfig> query = this.baseQuery(appConfig);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询应用配置
     *
     * @param id
     * @return
     */
    @Override
    public AppConfig getAppConfigById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加应用配置
     *
     * @param appConfig
     * @return
     */
    @Override
    public R<Boolean> saveAppConfig(AppConfig appConfig) {
        log.info("添加应用配置！应用配置:{}", appConfig);
        Boolean saveFlag = this.save(appConfig);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加应用配置成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用配置失败！");
        }
    }

    /**
     * 修改应用配置
     *
     * @param appConfig
     * @return
     */
    @Override
    public R<Boolean> updateAppConfig(AppConfig appConfig) {
        log.info("修改应用配置！id:{}", appConfig.getId());
        UpdateWrapper<AppConfig> updateWrapper = new UpdateWrapper<AppConfig>();
        updateWrapper.lambda().set(AppConfig::getAppId, appConfig.getAppId());
        updateWrapper.lambda().set(AppConfig::getVersionId, appConfig.getVersionId());
        updateWrapper.lambda().eq(AppConfig::getId, appConfig.getId());
        Boolean updateFlag = this.update(appConfig, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改应用配置成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用配置失败！");
        }

    }

    /**
     * 删除应用配置
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppConfigById(Integer id) {
        log.info("删除应用配置！id:{}", id);
        return R.ok(this.removeById(id), "删除应用配置成功！");
    }

    /**
     * 批量保存应用配置
     *
     * @param app 应用DTO对象，包含应用信息和配置信息
     */
    @Override
    public void batchSave(AppDTO app) {
        // 构建查询条件，用于删除特定应用和集群的所有配置
        QueryWrapper<AppConfig> queryRemove = Wrappers.query();
        queryRemove.lambda().eq(AppConfig::getAppId, app.getId());
        // 删除特定应用和集群的所有配置
        this.remove(queryRemove);

        // 检查是否需要保存新的配置
        // 只有在开放配置或环境配置打开时，才进行保存操作
        if ((app.getOpenConfig() != null && app.getOpenConfig()) || (app.getOpenEnvConfig() != null && app.getOpenEnvConfig())) {
            // 获取应用的配置列表
            List<AppConfig> configList = app.getConfigList();
            // 遍历配置列表，为每个配置设置应用ID和集群信息，并保存
            for (AppConfig configApp : configList) {
                configApp.setAppId(app.getId());
                this.save(configApp);
            }
        }
    }

    @Override
    public R getByVersion(Integer versionId) {
        //获取配置文件版本
        ConfigVersionVO versionConfig = configVersionService.getConfigVersionById(versionId);
        Config config = versionConfig.getConfig();
        if ("2".equals(config.getType())) {
            AppDomain appDomain = new AppDomain();
            appDomain.setVersionId(versionId);
            List<AppDomainVO> appDomainList = appDomainService.listAppDomain(appDomain);
            return R.ok(appDomainList);
        } else {
            QueryWrapper<AppConfig> queryAppConfig = Wrappers.query();
            queryAppConfig.lambda().eq(AppConfig::getVersionId, versionId);
            List<AppConfig> appConfigList = appConfigMapper.selectList(queryAppConfig);
            List<AppConfigVO> byVersion = new ArrayList<>();
            for (AppConfig appConfig : appConfigList) {
                AppConfigVO appConfigVO = Convert.convert(AppConfigVO.class, appConfig);
                appConfigVO.setApp(appMapper.selectById(appConfig.getAppId()));
                byVersion.add(appConfigVO);
            }
            return R.ok(byVersion);
        }
    }


    /**
     * 更新应用版本
     *
     * @param configApp
     */
    @Override
    public void updateAppVersion(AppConfigDTO configApp) throws Exception {
        QueryWrapper<AppConfig> query = Wrappers.query();
        query.lambda().in(AppConfig::getId, configApp.getIdList());
        AppConfig updateConfigApp = new AppConfig();
        updateConfigApp.setVersionId(configApp.getVersionId());
        this.update(updateConfigApp, query);
    }

}