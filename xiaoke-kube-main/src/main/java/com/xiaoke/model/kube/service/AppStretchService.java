package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.AppStretch;
import java.util.List;

/**
* 弹性伸缩 Service
*
*  @author xiaoke
*  @date 2024-08-09 16:29:23
*/
public interface AppStretchService extends IService<AppStretch> {
    /**
     * 添加弹性伸缩
     * @param appStretch
     * @return
     */
    R<Boolean> saveAppStretch(AppStretch appStretch,String namespace) throws Exception;



    /**
     * 删除弹性伸缩
     * @param id
     * @return
     */
    R<Boolean> removeAppStretchById(Integer id) throws Exception;

}