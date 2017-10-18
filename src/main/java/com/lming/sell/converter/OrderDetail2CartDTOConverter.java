package com.lming.sell.converter;

import com.lming.sell.dto.CartDTO;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.model.OrderDetail;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单明细转成购物车DTO
 */
public class OrderDetail2CartDTOConverter {

    public static CartDTO convert(OrderDetail orderDetail)
    {
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(orderDetail,cartDTO);
        return cartDTO;
    }

    public static List<CartDTO> convert(List<OrderDetail> orderDetailList)
    {
        return orderDetailList.stream().map(e->
                convert(e)
        ).collect(Collectors.toList());
    }
}
