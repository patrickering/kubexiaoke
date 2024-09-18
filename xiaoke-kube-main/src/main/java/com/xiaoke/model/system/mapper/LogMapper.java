

package com.xiaoke.model.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke.entity.system.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author xiaoke
 * @since 2017-11-20
 */
@Mapper
public interface LogMapper extends BaseMapper<SysLog> {
}
