package com.lming.sell.service;

import com.lming.sell.dto.OrderDTO;

public interface BuyerService {

     OrderDTO findOrderOne(String openId,String orderId);

    OrderDTO cancelOrder(String openId,String orderId);

}
