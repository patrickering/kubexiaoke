package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.kube.entity.Domain;
import org.apache.ibatis.annotations.Mapper;


/**
 * 集群域名 Mapper
 *
 * @author xiaoke
 * @date 2024-08-18 02:17:05
 */
@Mapper
public interface DomainMapper extends BaseMapper<Domain> {

}
