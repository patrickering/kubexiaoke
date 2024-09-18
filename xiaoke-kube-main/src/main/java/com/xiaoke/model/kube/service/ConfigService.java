package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.dto.ConfigDTO;
import com.xiaoke.entity.kube.entity.Config;
import java.util.List;
import com.xiaoke.entity.kube.vo.ConfigVO;

/**
* 配置 Service
*
*  @author xiaoke
*  @date 2024-08-03 16:10:50
*/
public interface ConfigService extends IService<Config> {

    /**
     * 分页查询配置
     * @param page
     * @param config
     * @return
     */
    R<IPage> pageConfig(Page page, Config config);


    /**
     * 获取配置列表
     * @param config
     * @return
     */
    List<ConfigVO> listConfig(Config config);




    /**
     * 通过ID查询配置
     * @param id
     * @return
     */
     ConfigVO getConfigById(Integer id);


    /**
     * 添加配置
     * @param config
     * @return
     */
    R<Boolean> saveConfig(ConfigDTO config, String namespace) throws Exception;


    /**
     * 修改配置
     * @param config
     * @return
     */
    R<Boolean> updateConfig(Config config);


    /**
     * 删除配置
     * @param id
     * @return
     */
    R<Boolean> removeConfigById(Integer id);





}