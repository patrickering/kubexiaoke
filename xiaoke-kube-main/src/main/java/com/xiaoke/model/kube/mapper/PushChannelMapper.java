package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.kube.entity.PushChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 推送渠道 Dao
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
@Mapper
public interface PushChannelMapper extends BaseMapper<PushChannel> {

}