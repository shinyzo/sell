package com.lming.sell.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.lming.sell.dto.OrderDTO;

public interface PayService {

    public PayResponse create(OrderDTO orderDTO);

    public PayResponse notify(String notifyData);

    public RefundResponse refund(OrderDTO orderDTO);

}
