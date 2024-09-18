package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.PushUser;


/**
* 推送接收人 Mapper
*
*  @author xiaoke
*  @date 2024-08-28 00:33:59
*/
@Mapper
public interface PushUserMapper extends BaseMapper<PushUser> {

}
