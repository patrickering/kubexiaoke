package com.xiaoke.utils;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.entity.kube.entity.PushChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 邮件工具类
 */
@Slf4j
public class MailUtils {

    /**
     * 发送邮件入口
     *
     * @param channel
     * @param mail
     * @param content
     * @return
     */
    public static Boolean send(PushChannel channel, String mail, String title, String content) {
        if ("163".equals(channel.getType())) {
            return MailUtils.send163(channel, mail, title, content);
        } else if ("qq".equals(channel.getType())) {
            return MailUtils.sendQQ(channel, mail, title, content);
        }
        return Boolean.FALSE;
    }

    /**
     * 发送163邮件
     *
     * @param channel
     * @param mail
     * @param content
     * @return
     */
    private static Boolean send163(PushChannel channel, String mail, String title, String content) {
        try {
            log.info("=========>发送163邮件到：{}", mail);
            MailAccount account = MailUtils.initMail(channel);
            account.setHost("smtp.163.com");
            MailUtil.send(account, mail, title, content, false);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=========>发送163邮件失败");
            return Boolean.FALSE;
        }

    }

    /**
     * 发送163邮件
     *
     * @param channel
     * @param mail
     * @param content
     * @return
     */
    private static Boolean sendQQ(PushChannel channel, String mail, String title, String content) {
        try {
            log.info("=========>发送QQ邮件到：{}", mail);
            MailAccount account = MailUtils.initMail(channel);
            account.setHost("smtp.qq.com");
            MailUtil.send(account, mail, title, content, false);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=========>发送163邮件失败");
            return Boolean.FALSE;
        }

    }

    /**
     * 初始化邮件
     *
     * @param channel
     * @return
     */
    private static MailAccount initMail(PushChannel channel) {
        String params = channel.getParams();
        Map<String, Object> paramMap = JsonUtils.jsonToMap(params);
        MailAccount account = new MailAccount();

        account.setPort(Integer.valueOf(paramMap.get("port").toString()));
        account.setAuth(true);
        account.setFrom(paramMap.get("from").toString());
        account.setUser(paramMap.get("user").toString());
        account.setPass(paramMap.get("pass").toString());
        return account;
    }

}
