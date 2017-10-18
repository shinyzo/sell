package com.lming.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 微信开放平台配置
 */
@Component
public class WechatOpenConfig {


    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    /**
     * 构造sdk需要的微信支付bean
     * @return
     */
    @Bean
    public WxMpService wxOpenService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxOpenConfigStorage());

        return wxMpService;
    }


    @Bean
    public WxMpConfigStorage wxOpenConfigStorage(){
        WxMpInMemoryConfigStorage wxOpenConfigStorage = new WxMpInMemoryConfigStorage();
        wxOpenConfigStorage.setAppId(wechatAccountConfig.getOpenAppId());
        wxOpenConfigStorage.setSecret(wechatAccountConfig.getOpenAppId());
        return wxOpenConfigStorage;
    }


}
