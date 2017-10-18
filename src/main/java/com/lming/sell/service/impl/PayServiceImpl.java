package com.lming.sell.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lming.sell.exception.SellException;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.service.OrderService;
import com.lming.sell.service.PayService;
import com.lming.sell.util.JsonUtil;
import com.lming.sell.util.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    public static final String ORDER_NAME ="微信点餐系统";


    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {

        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("【微信公众号支付】- 发起请求，payRequest={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse =  bestPayService.pay(payRequest);
        log.info("【微信公众号支付】- 发起请求，payResponse={}",JsonUtil.toJson(payResponse));
        return payResponse;
    }

    @Override
    public PayResponse notify(String notifyData) {
        // 1.验证签名
        // 2.验证支付结果
        // 3.验证金额
        // 4.付款人==订单人

        // 接受微信异步通知
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());
        if(!MoneyUtil.equals(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue()))
        {
            log.error("【微信公众号支付】-异步通知，异步通知金额与订单金额不一致，orderId={},通知金额={},订单金额={}",
                    orderDTO.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount()
            );
            throw new SellException(ResultEnum.NOTIFY_AMOUNT_ERROR);

        }

        // 修改订单状态
        orderService.paidOrder(orderDTO);

        return payResponse;
    }

    /**
     * 退款
     * @param orderDTO
     * @return
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();

        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】 -发起请求，request={}",JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】 -发起请求，response={}",JsonUtil.toJson(refundResponse));
        return refundResponse;
    }


}
