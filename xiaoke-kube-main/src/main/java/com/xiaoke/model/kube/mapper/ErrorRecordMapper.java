package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.ErrorRecord;


/**
* 异常记录 Mapper
*
*  @author xiaoke
*  @date 2024-08-22 23:06:02
*/
@Mapper
public interface ErrorRecordMapper extends BaseMapper<ErrorRecord> {

}
