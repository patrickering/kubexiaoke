package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.Config;


/**
* 配置 Mapper
*
*  @author xiaoke
*  @date 2024-08-03 16:10:50
*/
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

}
