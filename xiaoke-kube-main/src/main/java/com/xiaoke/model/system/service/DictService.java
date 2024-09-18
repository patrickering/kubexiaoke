
package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Dict;

/**
 * 字典表
 *
 * @author xiaoke
 * @date 2019/03/19
 */
public interface DictService extends IService<Dict> {

    /**
     * 根据ID 删除字典
     *
     * @param id
     * @return
     */
    R removeDict(Integer id);

    /**
     * 更新字典
     *
     * @param dict 字典
     * @return
     */
    R updateDict(Dict dict);
}
