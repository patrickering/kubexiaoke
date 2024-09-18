

package com.xiaoke.model.kube.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.entity.kube.entity.PushChannel;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.utils.MailUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaoke
 * @date 2018/11/18
 */
@Slf4j
@Component("email")
@AllArgsConstructor
public class EmailPushHandler extends AbstractPushHandler {

    /**
     * 发送邮件
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
        log.info("==========>发送邮件推送");
        if (StrUtil.isNotEmpty(user.getEmail())) {
            String title = "";
            String content = "";
            if (contentType == 0) {
                title = "应用异常通知";
                content = String.format("应用：%s，运行异常，请及时处理", app.getName());
            }
            //获取推送渠道
            PushChannel pushChannel = new PushChannel();
            pushChannel.setPushId(push.getId());
            pushChannel.setOpen(Boolean.TRUE);
            QueryWrapper<PushChannel> pushChannelQuery = Wrappers.query(pushChannel);
            pushChannelQuery.orderByDesc("weight");
            List<PushChannel> pushChannelList = pushChannel.selectList(pushChannelQuery);
            if (pushChannelList != null && pushChannelList.size() > 0) {
                for (PushChannel channel : pushChannelList) {
                    Boolean send = MailUtils.send(channel, user.getEmail(), title, content);
                    if (send) {
                        return Boolean.TRUE;
                    }
                }
            }
        }


        return Boolean.FALSE;
    }
}
