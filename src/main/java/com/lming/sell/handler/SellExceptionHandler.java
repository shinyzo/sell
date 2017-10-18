package com.lming.sell.handler;

import com.lming.sell.exception.SellException;
import com.lming.sell.exception.SellerAuthorizeException;
import com.lming.sell.config.ProjectUrlConfig;
import com.lming.sell.util.ResultVOUtil;
import com.lming.sell.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常捕获处理返回错误页面
 */

@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * 用户信息鉴权异常跳转至登录页面
     * @return
     */
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException(){

        return new ModelAndView("redirect:"
                .concat( projectUrlConfig.getWechatOpenAuthorizeUrl())
                .concat("?returnUrl=").concat(projectUrlConfig.getSell())
                .concat("/seller/login")
        );
    }

    /**
     * 异常捕获 并按统一格式返回给前端
     * @param e
     * @return
     */
    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVO handlerSellException(SellException e){

        return ResultVOUtil.error(e.getCode(),e.getMessage());

    }



}
