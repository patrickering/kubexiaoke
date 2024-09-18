package com.xiaoke.model.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.system.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * 登录日志 Mapper
 *
 * @author xiaoke
 * @date 2022-01-23 15:30:21
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}
