package com.lming.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lming.sell.exception.SellException;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.form.OrderForm;
import com.lming.sell.model.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm)
    {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getBuyerName());
        orderDTO.setBuyerPhone(orderForm.getBuyerPhone());
        orderDTO.setBuyerAddress(orderForm.getBuyerPhone());
        orderDTO.setBuyerOpenid(orderForm.getBuyerOpenid());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try{
            orderDetailList =  gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        }
        catch (Exception e)
        {
            log.error("【数据转换】错误，items={}",orderForm.getItems());
            throw new SellException(ResultEnum.DATA_TRANS_ERROR);

        }

        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;

    }

}
