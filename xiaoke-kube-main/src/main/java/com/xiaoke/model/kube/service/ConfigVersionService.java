package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.ConfigVersion;
import com.xiaoke.entity.kube.vo.ConfigVersionVO;

import java.util.List;

/**
 * 配置版本 Service
 *
 * @author xiaoke
 * @date 2024-08-03 16:25:16
 */
public interface ConfigVersionService extends IService<ConfigVersion> {

    /**
     * 分页查询配置版本
     *
     * @param page
     * @param configVersion
     * @return
     */
    R<IPage> pageConfigVersion(Page page, ConfigVersion configVersion);


    /**
     * 获取配置版本列表
     *
     * @param configVersion
     * @return
     */
    List<ConfigVersionVO> listConfigVersion(ConfigVersion configVersion);


    /**
     * 通过ID查询配置版本
     *
     * @param id
     * @return
     */
    ConfigVersionVO getConfigVersionById(Integer id);


    /**
     * 添加配置版本
     *
     * @param configVersion
     * @return
     */
    R<Boolean> saveConfigVersion(ConfigVersion configVersion,String namespace) throws Exception;


    /**
     * 修改配置版本
     *
     * @param configVersion
     * @return
     */
    R<Boolean> updateConfigVersion(ConfigVersion configVersion);


    /**
     * 删除配置版本
     *
     * @param id
     * @return
     */
    R<Boolean> removeConfigVersionById(Integer id);


    /**
     * 创建ConfigMap
     *
     * @param configVersion
     * @return
     */
    Boolean createConfigMap(ConfigVersion configVersion, String namespace);
}