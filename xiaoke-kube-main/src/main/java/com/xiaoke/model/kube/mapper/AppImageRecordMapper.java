package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.AppImageRecord;


/**
* 应用镜像更新记录 Mapper
*
*  @author xiaoke
*  @date 2024-08-14 00:03:31
*/
@Mapper
public interface AppImageRecordMapper extends BaseMapper<AppImageRecord> {

}
