package com.lming.sell.service;

import com.lming.sell.dto.OrderDTO;

public interface PushMessageService {

    void orderStatus(OrderDTO orderDTO);
}
