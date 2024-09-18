package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.App;


/**
* 应用 Mapper
*
*  @author xiaoke
*  @date 2024-08-03 15:34:01
*/
@Mapper
public interface AppMapper extends BaseMapper<App> {

}
