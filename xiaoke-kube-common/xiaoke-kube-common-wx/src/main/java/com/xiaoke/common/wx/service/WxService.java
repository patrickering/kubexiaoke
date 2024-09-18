package com.xiaoke.common.wx.service;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaMsgServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.*;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.util.StrUtil;
import com.xiaoke.common.wx.WxProperties;
import com.xiaoke.common.wx.constant.WxConstant;
import com.xiaoke.common.wx.entity.WxTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 调用微信服务
 *
 * @author xiaoke
 */
@Slf4j
@RequiredArgsConstructor
public class WxService {
    private final WxProperties wxProperties;
    private final RedisTemplate redisTemplate;


    /**
     * 微信用户手机号
     *
     * @param code
     * @throws WxErrorException
     */
    public String getPhone(String code) {
        try {
            WxMaService wxMaService = createWxMaService();
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(code);
            return phoneNoInfo.getPhoneNumber();
        } catch (Exception e) {
            log.error("获取微信用户手机号异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 创建WxMpService
     */
    private WxMpService createWxMpService() {
        if (WxConstant.wxMpService == null) {
            log.info("==========> 初始化WxMpService：{}", wxProperties);
            WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
            config.setAppId(wxProperties.getWxAppId());
            config.setSecret(wxProperties.getMiniAppSecret());
            WxMpService wxService = new WxMpServiceImpl();
            wxService.setWxMpConfigStorage(config);
            WxConstant.wxMpService = wxService;
        }
        return WxConstant.wxMpService;
    }


    /**
     * 获取openid
     *
     * @param code
     * @return
     * @throws WxErrorException
     */
    public WxOAuth2UserInfo getOpenId(String code) {
        try {
            WxMpService wxMpService = createWxMpService();
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
            return userInfo;
        } catch (WxErrorException e) {
            log.error("获取openId异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取微信用户信息
     *
     * @param code
     * @return
     * @throws WxErrorException
     */
    public WxOAuth2UserInfo getWxMpUser(String code) {
        try {
            WxMpService wxMpService = createWxMpService();
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取微信用户信息失败");
            return null;
        }
    }

    /**
     * 发送模板消息
     *
     * @param wxTemplate
     * @return
     */
    public Boolean sendOneTemplate(WxTemplate wxTemplate) {
        try {
            if (StrUtil.isEmpty(wxTemplate.getOpenId())) {
                log.error("未获取用户openId无法发送模板消息");
                return Boolean.FALSE;
            }
            log.info("===============>开始发送模板消息<===============");
            WxMpService wxMpService = createWxMpService();
            //计数器
            log.info("===============>发送模板消息,openId：{}<===============", wxTemplate.getOpenId());
            //构造模板消息对象
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(wxTemplate.getOpenId())
                    .templateId(wxTemplate.getTemplateId())
                    .url(wxTemplate.getUrl())
                    .build();
            Map<String, String> data = wxTemplate.getData();
            if (data != null) {
                log.info("===============>拼接模板数据<===============");
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    log.info("{}:{}", entry.getKey(), entry.getValue());
                    templateMessage.addData(new WxMpTemplateData(entry.getKey(), entry.getValue()));
                }
            }
            log.info("===============>调取微信发送模板消息<===============", wxTemplate.getTemplateId());
            String result = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            log.info("发送结果：{}", result);
            log.info("===============>发送模板消息完成<===============");
        } catch (Exception e) {
            log.error("===============>发送模板消息失败<===============");
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 创建微信jssdk
     *
     * @param url
     * @throws WxErrorException
     */
    public WxJsapiSignature createJsApiSignature(String url) {
        try {
            WxMpService wxMpService = createWxMpService();
            return wxMpService.createJsapiSignature(url);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建微信jssdk失败");
            return null;
        }
    }


    /**
     * 创建WxMaService
     */
    private WxMaService createWxMaService() {
        if (WxConstant.wxMaService == null) {
            log.info("初始化WxMpService");
            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setAppid(wxProperties.getMiniAppId());
            config.setSecret(wxProperties.getMiniAppSecret());
            log.info("wx小程序AppId{}", wxProperties.getMiniAppId());
            log.info("wx小程序AppSecret{}", wxProperties.getMiniAppSecret());
            WxMaService wxService = new WxMaServiceImpl();
            wxService.setWxMaConfig(config);
            WxConstant.wxMaService = wxService;
        }
        return WxConstant.wxMaService;
    }

    /**
     * 小程序登录
     *
     * @param code
     * @throws WxErrorException
     */
    public String getWxMiniOpenId(String code) {
        try {
            WxMaService wxMaService = createWxMaService();
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            if (session == null) {
                log.error("获取微信小程序OpenId失败！");
                return null;
            }
            String sessionKey = session.getSessionKey();
            String openid = session.getOpenid();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.opsForValue().set("WX-MINI-SESSION-KEY:" + openid, sessionKey, 24, TimeUnit.HOURS);
            return openid;
        } catch (Exception e) {
            log.error("获取openId异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取微信小程序用户信息
     *
     * @param openid
     * @param encryptedData
     * @param ivStr
     * @return
     */
    public WxMaUserInfo getWxMiniUser(String openid, String encryptedData, String ivStr) {
        try {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            Object sessionKey = redisTemplate.opsForValue().get("WX-MINI-SESSION-KEY:" + openid);
            if (sessionKey != null) {
                WxMaService wxMaService = createWxMaService();
                WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey.toString(), encryptedData, ivStr);
                return userInfo;
            }
        } catch (Exception e) {
            log.error("解析微信小程序用户信息异常：{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * params 参数
     * ｛
     * openId  用户小程序openid
     * templateId  模板消息ID
     * url  跳转网页url
     * path 跳转小程序路径
     * ｝
     * data 模板消息内容
     *
     * @param params
     * @return
     */
    public Boolean sendTemplate(Map<String, String> params, Map<String, String> data) {
        try {
            WxMaService wxMaService = createWxMaService();
            WxMaMsgService wxMaMsgService = new WxMaMsgServiceImpl(wxMaService);
            WxMaUniformMessage uniformMessage = new WxMaUniformMessage();
            uniformMessage.setToUser(params.get("openId"));
            uniformMessage.setMpTemplateMsg(Boolean.TRUE);

            uniformMessage.setAppid(wxProperties.getWxAppId());
            log.info("wxAppId{}", wxProperties.getWxAppSecret());
            uniformMessage.setTemplateId(params.get("templateId"));

            //跳转连接
            if (StrUtil.isNotEmpty(params.get("url"))) {
                uniformMessage.setUrl(params.get("url"));
            }

            //跳转小程序
            if (StrUtil.isNotEmpty(params.get("path"))) {
                WxMaUniformMessage.MiniProgram miniProgram = new WxMaUniformMessage.MiniProgram();
                log.info("小程序跳转{}", wxProperties.getMiniAppId());
                miniProgram.setAppid(wxProperties.getMiniAppSecret());
                miniProgram.setPagePath(params.get("path"));
                uniformMessage.setMiniProgram(miniProgram);
            }

            List<WxMaTemplateData> wxMaTemplateDataList = new ArrayList<>();
            data.forEach((key, value) -> {
                WxMaTemplateData wxMaTemplateData = new WxMaTemplateData();
                wxMaTemplateData.setName(key);
                wxMaTemplateData.setValue(value);
                wxMaTemplateDataList.add(wxMaTemplateData);
            });
            uniformMessage.setData(wxMaTemplateDataList);
            wxMaMsgService.sendUniformMsg(uniformMessage);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送模板消息异常");
            return Boolean.FALSE;
        }
    }
}

