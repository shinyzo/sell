package com.lming.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "projectUrl")
public class ProjectUrlConfig {
    /**
     * 微信公众平台授权Url
     */
    private String wechatMpAuthorizeUrl;
    /**
     * 微信公众平台授权获取code之后回跳项目地址
     */
    private String wechatMpCodeSkipUrl;

    /**
     * 微信开放平台授权url
     */
    private String wechatOpenAuthorizeUrl;

    /**
     * 微信开放平台授权获取code之后毁掉项目地址
     */
    private String wechatOpenCodeSkipUrl;
    /**
     * 项目url
     */
    private String sell;

}
