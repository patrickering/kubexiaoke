package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.ConfigDTO;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.entity.kube.vo.ConfigVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.model.kube.mapper.ConfigMapper;
import com.xiaoke.model.kube.service.ConfigService;
import com.xiaoke.model.kube.service.ConfigVersionService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置 Service
 *
 * @author xiaoke
 * @date 2024-08-03 16:10:50
 */
@Service
@AllArgsConstructor
@Slf4j
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    private final ConfigMapper configMapper;
    private final ConfigVersionService configVersionService;

    /**
     * 封装共用查询条件
     *
     * @param config
     * @return
     */
    private QueryWrapper<Config> baseQuery(Config config) {
        QueryWrapper<Config> query = Wrappers.query();
        if (StrUtil.isNotEmpty(config.getNamespace())) {
            query.lambda().eq(Config::getNamespace, config.getNamespace());
        }
        if (StrUtil.isNotEmpty(config.getName())) {
            query.lambda().like(Config::getName, config.getName());
        }
        if (StrUtil.isNotEmpty(config.getType())) {
            query.lambda().eq(Config::getType, config.getType());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询配置
     *
     * @param page
     * @param config
     * @return
     */
    @Override
    public R<IPage> pageConfig(Page page, Config config) {
        QueryWrapper<Config> query = this.baseQuery(config);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), ConfigVO.class));
    }

    /**
     * 获取配置列表
     *
     * @param config
     * @return
     */
    @Override
    public List<ConfigVO> listConfig(Config config) {
        QueryWrapper<Config> query = this.baseQuery(config);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), ConfigVO.class);
    }


    /**
     * 通过ID查询配置
     *
     * @param id
     * @return
     */
    @Override
    public ConfigVO getConfigById(Integer id) {
        return JoinUtil.entity(this.getById(id), ConfigVO.class);
    }

    /**
     * 添加配置
     *
     * @param config    配置对象，包含配置的具体信息
     * @param namespace 命名空间，用于区分不同的配置环境（例如开发、测试、生产）
     * @return R<Boolean> 返回一个结果对象，包含操作是否成功的标志和提示信息
     * @throws Exception 如果添加配置过程中发生错误，可能会抛出异常
     */
    @Override
    public R<Boolean> saveConfig(ConfigDTO config, String namespace) throws Exception {

        // 名称重复验证
        QueryWrapper<Config> queryCount = Wrappers.query();
        queryCount.lambda().eq(Config::getNamespace, namespace);
        queryCount.lambda().eq(Config::getName, config.getName());
        long count = this.count(queryCount);
        if (count > 0) {
            return R.failed("配置名称已存在！");
        }
        SysUser user = SecurityUtils.getUser();

        // 设置配置的命名空间、集群标识、初始版本号和创建人信息
        config.setNamespace(namespace);
        config.setVersion(1);
        config.setCreateBy(user.getId());
        log.info("添加配置！配置:{}", config);
        Boolean saveFlag = this.save(config);
        if (saveFlag) {
            // 保存文件版本
            ConfigVersion configVersion = new ConfigVersion();
            configVersion.setConfigId(config.getId());
            configVersion.setVersion(config.getVersion());
            configVersion.setContent(config.getContent());
            configVersionService.save(configVersion);
            if (StrUtil.isNotEmpty(namespace)) {
                Boolean configMap = configVersionService.createConfigMap(configVersion, namespace);
                if (!configMap) {
                    throw new Exception("新增配置文件失败！");
                }
            }
            return R.ok(Boolean.TRUE, "添加配置成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加配置失败！");
        }
    }

    /**
     * 修改配置
     *
     * @param config
     * @return
     */
    @Override
    public R<Boolean> updateConfig(Config config) {
        SysUser user = SecurityUtils.getUser();
        config.setUpdateBy(user.getId());
        log.info("修改配置！id:{}", config.getId());
        UpdateWrapper<Config> updateWrapper = new UpdateWrapper<Config>();
        updateWrapper.lambda().set(Config::getRemark, config.getRemark());
        updateWrapper.lambda().eq(Config::getId, config.getId());
        Boolean updateFlag = this.update(updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改配置成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改配置失败！");
        }

    }

    /**
     * 删除配置
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeConfigById(Integer id) {
        log.info("删除配置！id:{}", id);
        return R.ok(this.removeById(id), "删除配置成功！");
    }


}