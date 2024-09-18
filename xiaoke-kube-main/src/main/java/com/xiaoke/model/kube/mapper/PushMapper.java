package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.kube.entity.Push;
import org.apache.ibatis.annotations.Mapper;

/**
 * 推送 Dao
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
@Mapper
public interface PushMapper extends BaseMapper<Push> {

}