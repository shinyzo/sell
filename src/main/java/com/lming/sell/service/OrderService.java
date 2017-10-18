package com.lming.sell.service;

import com.lming.sell.dto.OrderDTO;
import com.lming.sell.model.OrderMaster;
import com.lming.sell.model.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 查询单个订单
     * @param orderId
     * @return
     */
    OrderDTO findOne(String orderId);

    /**
     * 查询买家订单列表
     * @param pageable
     * @return
     */
    Page<OrderDTO> findBuyerOrderList(String buyerOpenid,Pageable pageable);

    /**
     * 查找所有订单列表
     * @param pageable
     * @return
     */
    Page<OrderDTO> findList(Pageable pageable);
    /**
     * 完结订单
     * @param orderDTO
     * @return
     */
    OrderDTO finishOrder(OrderDTO orderDTO);

    /**
     * 取消订单
     * @param orderDTO
     * @return
     */
    OrderDTO cancelOrder(OrderDTO orderDTO);

    /**
     * 支付订单
     * @param orderDTO
     * @return
     */
    OrderDTO paidOrder(OrderDTO orderDTO);



}
