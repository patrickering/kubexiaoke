package com.xiaoke.model.kube.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.xiaoke.entity.kube.entity.AppPodRecord;


/**
* 应用pod记录 Mapper
*
*  @author xiaoke
*  @date 2024-08-14 23:23:56
*/
@Mapper
public interface AppPodRecordMapper extends BaseMapper<AppPodRecord> {

}
