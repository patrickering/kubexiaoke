

package com.xiaoke.model.kube.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.entity.kube.entity.PushChannel;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.model.kube.service.PushService;
import com.xiaoke.utils.sms.HuaWeiSms;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoke
 * @date 2018/11/18
 */
@Slf4j
@Component("sms")
@AllArgsConstructor
public class SmsPushHandler extends AbstractPushHandler {
    private final PushService pushService;

    /**
     * 发送短信
     *
     * @param contentType
     * @param push
     * @param user
     * @param namespace
     * @param app
     * @return
     */
    @Override
    public Boolean send(Integer contentType, Push push, User user, String namespace, App app) {
        log.info("==========>发送短信推送");
        if (StrUtil.isNotEmpty(user.getPhone())) {
            //组装内容
            Map<String, String> content = new LinkedHashMap<>();
            content.put("namespace", namespace);
            content.put("app", app.getName());


            //获取推送渠道
            PushChannel pushChannel = new PushChannel();
            pushChannel.setPushId(push.getId());
            pushChannel.setOpen(Boolean.TRUE);
            QueryWrapper<PushChannel> pushChannelQuery = Wrappers.query(pushChannel);
            pushChannelQuery.orderByDesc("weight");
            List<PushChannel> pushChannelList = pushChannel.selectList(pushChannelQuery);
            if (pushChannelList != null && pushChannelList.size() > 0) {
                for (PushChannel channel : pushChannelList) {
                    Boolean send = Boolean.FALSE;
                    String params = channel.getParams();
                    Map<String, Object> paramMap = JsonUtils.jsonToMap(params);
                    if (contentType == 0) {
                        paramMap.put("templateId", paramMap.get("appTemplateId"));
                    } else if (contentType == 1) {
                        paramMap.put("templateId", paramMap.get("handleTemplateId"));
                    }
                    //华为
                    if ("sms-huawei".equals(channel.getType())) {
                        send = HuaWeiSms.send(paramMap, user.getPhone(), content);
                    }
                    if (send) {
                        return Boolean.TRUE;
                    }
                }
            }
        }
        return Boolean.FALSE;
    }
}
