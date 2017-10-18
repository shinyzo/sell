package com.lming.sell.controller;

import com.lming.sell.exception.SellException;
import com.lming.sell.config.ProjectUrlConfig;
import com.lming.sell.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

/**
 * 微信支付
 */
@Controller
@Slf4j
@RequestMapping("/wechat")
public class WechatController {


    @Autowired
    private WxMpService wxMpService;


    @Autowired
    private WxMpService wxOpenService;


    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * 微信登录授权
     */
    @GetMapping(value = "/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){

//        String url = "http://qimoom.com/sell/wechat/userInfo";
        String url = projectUrlConfig.getWechatMpCodeSkipUrl();
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));

        return "redirect:"+redirectUrl;
    }

    /**
     * 微信登录会跳
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping(value = "/userInfo")
    public String userInfo(@RequestParam("code") String code,
                            @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();

        try {
            wxMpOAuth2AccessToken =  wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【网页微信授权】 错误");
            throw new SellException(ResultEnum.WECHAT_OAUTH_ERROR.getCode(),e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:"+returnUrl+"?openid="+openId;
    }


    /**
     * 网页扫码登录授权
     */
    @GetMapping(value = "/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl){

//        String url = "http://qimoom.com/sell/wechat/qrUserInfo";
        String url = projectUrlConfig.getWechatOpenCodeSkipUrl();
        String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, URLEncoder.encode(returnUrl));

        return "redirect:"+redirectUrl;
    }

    /**
     * 网页扫码登录回调
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping(value = "/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();

        try {
            wxMpOAuth2AccessToken =  wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【网页扫码授权】 错误");
            throw new SellException(ResultEnum.WECHAT_OAUTH_ERROR.getCode(),e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:"+returnUrl+"?openid="+openId;
    }

}
