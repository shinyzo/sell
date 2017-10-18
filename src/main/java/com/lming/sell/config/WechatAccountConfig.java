package com.lming.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 微信支付参数账号相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    /**
     * 公众号appId
     */
    private String mpAppId;
    /**
     * 公众号appSecret
     */
    private String mpAppSecret;
    /**
     * 商户支付平台-商户id
     */
    private String mchId;
    /**
     * 商户支付平台-私钥
     */
    private String mchKey;
    /**
     * 商户支付平台-证书路径(下载)
     */
    private String keyPath;

    /**
     * 支付结果通知URL
     */
    private String notifyUrl;
    /**
     * 开放平台openid
     */
    private String openAppId;
    /**
     * 开放平台秘钥
     */
    private String openAppSecret;

    /**
     * 微信消息模板map
     */
    private Map<String,String> templateId;
}
