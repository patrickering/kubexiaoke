package com.xiaoke.model.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.xiaoke.entity.system.entity.Error;
import com.xiaoke.model.system.mapper.ErrorMapper;
import com.xiaoke.model.system.service.ErrorService;


/**
* 异常 Service
*
*  @author xiaoke
*  @date 2020-08-24 21:37:54
*/
@Service
@AllArgsConstructor
public class ErrorServiceImpl extends ServiceImpl<ErrorMapper,Error> implements ErrorService{
    private final ErrorMapper errorMapper;
}
