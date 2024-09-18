package com.xiaoke.model.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.system.entity.Error;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异常 Mapper
 *
 * @author xiaoke
 * @date 2020-08-24 21:37:54
 */
@Mapper
public interface ErrorMapper extends BaseMapper<Error> {

}
