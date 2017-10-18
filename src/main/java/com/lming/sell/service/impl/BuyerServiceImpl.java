package com.lming.sell.service.impl;

import com.lming.sell.exception.SellException;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.service.BuyerService;
import com.lming.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;
    @Override
    public OrderDTO findOrderOne(String openId, String orderId) {

        return checkOrder(openId,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openId, String orderId) {

        OrderDTO orderDTO = checkOrder( openId, orderId);

        return orderService.cancelOrder(orderDTO);
    }


    /**
     * 校验当前订单与登录用户的openId是否一致
     * @param openId
     * @param orderId
     * @return
     */
    private OrderDTO checkOrder(String openId,String orderId)
    {
        OrderDTO orderDTO =  orderService.findOne(orderId);
        if(null == orderDTO)
        {
            log.error("【单订单查询】-订单号不存在,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if(!openId.equals(orderDTO.getBuyerOpenid()))
        {
            log.error("【单订单查询】-非法操作，openId不一致,orderId={},openId={},orderOpenId={}",orderId,openId,orderDTO.getBuyerOpenid());
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }


        return orderDTO;
    }
}
