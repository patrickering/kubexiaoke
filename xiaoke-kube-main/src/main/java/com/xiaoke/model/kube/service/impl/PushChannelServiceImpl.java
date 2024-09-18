package com.xiaoke.model.kube.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.entity.kube.entity.PushChannel;
import com.xiaoke.model.kube.mapper.PushChannelMapper;
import com.xiaoke.model.kube.service.PushChannelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 推送渠道 Service
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
@Slf4j
@AllArgsConstructor
@Service("pushChannelService")
public class PushChannelServiceImpl extends ServiceImpl<PushChannelMapper, PushChannel> implements PushChannelService {

}