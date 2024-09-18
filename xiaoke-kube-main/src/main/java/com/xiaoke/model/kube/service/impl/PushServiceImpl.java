package com.xiaoke.model.kube.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.model.kube.mapper.PushMapper;
import com.xiaoke.model.kube.service.PushService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 推送 Service
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
@Slf4j
@AllArgsConstructor
@Service("pushService")
public class PushServiceImpl extends ServiceImpl<PushMapper, Push> implements PushService {

}