package com.lming.sell.controller;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.rest.type.Post;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.service.impl.OrderServiceImpl;
import com.lming.sell.service.impl.PayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 微信公众号支付
 */
@Controller
@RequestMapping("/pay")
public class WechatMpPayController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private PayServiceImpl payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl)
    {
        // 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        // 发起预支付
        PayResponse payResponse = payService.create(orderDTO);
        ModelMap modelMap = new ModelMap();
        modelMap.put("payResponse",payResponse);
        modelMap.put("returnUrl",returnUrl);

        return new ModelAndView("pay/create",modelMap);

    }

    /**
     * 微信后端通知请求
     * @param notifyData
     * @return
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody() String notifyData)
    {
        payService.notify(notifyData);

        // 同步告知微信处理结果
        return new ModelAndView("pay/success");
    }




}
