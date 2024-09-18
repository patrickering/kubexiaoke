
package com.xiaoke.model.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.enums.DictTypeEnum;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Dict;
import com.xiaoke.entity.system.entity.DictItem;
import com.xiaoke.model.system.mapper.DictItemMapper;
import com.xiaoke.model.system.service.DictItemService;
import com.xiaoke.model.system.service.DictService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典项
 *
 * @author xiaoke
 * @date 2019/03/19
 */
@Service
@AllArgsConstructor
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {
    private final DictService dictService;

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R removeDictItem(Integer id) {
        //根据ID查询字典ID
        DictItem dictItem = this.getById(id);
        Dict dict = dictService.getById(dictItem.getDictId());
        // 系统内置
        if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystem())) {
            return R.failed("系统内置字典项目不能删除");
        }
        return R.ok(this.removeById(id));
    }

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R updateDictItem(DictItem item) {
        //查询字典
        Dict dict = dictService.getById(item.getDictId());
        // 系统内置
        if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystem())) {
            return R.failed("系统内置字典项目不能删除");
        }
        return R.ok(this.updateById(item));
    }

    @Override
    @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.isEmpty()")
    public List<DictItem> getDictByType(String type) {
        return this.list(Wrappers.<DictItem>query().lambda().eq(DictItem::getType, type));
    }
}
