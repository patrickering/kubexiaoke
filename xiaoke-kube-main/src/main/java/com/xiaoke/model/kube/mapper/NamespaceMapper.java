package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.Namespace;


/**
* 命名空间 Mapper
*
*  @author xiaoke
*  @date 2024-08-03 15:00:35
*/
@Mapper
public interface NamespaceMapper extends BaseMapper<Namespace> {

}
