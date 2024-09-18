

package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.PublicParam;

/**
 * 公共参数配置
 *
 * @author Lucky
 * @date 2019-04-29
 */
public interface PublicParamService extends IService<PublicParam> {

    /**
     * 通过key查询公共参数指定值
     *
     * @param publicKey
     * @return
     */
    String getSysPublicParamKeyToValue(String publicKey);

    /**
     * 通过key查询公共参数
     *
     * @param publicKey
     * @return
     */
    PublicParam getPublicParamByKey(String publicKey);

    /**
     * 更新参数
     *
     * @param publicParam
     * @return
     */
    R updateParam(PublicParam publicParam);

    /**
     * 删除参数
     *
     * @param publicId
     * @return
     */
    R removeParam(Long publicId);
}
