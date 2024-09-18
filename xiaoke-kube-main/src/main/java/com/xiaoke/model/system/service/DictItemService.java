
package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.DictItem;

import java.util.List;

/**
 * 字典项
 *
 * @author xiaoke
 * @date 2019/03/19
 */
public interface DictItemService extends IService<DictItem> {

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    R removeDictItem(Integer id);

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    R updateDictItem(DictItem item);

    /**
     * 根据类型获取字典项
     *
     * @param type
     * @return
     */
    List<DictItem> getDictByType(String type);


}
