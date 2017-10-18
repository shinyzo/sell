package com.lming.sell.service.impl;

import com.lming.sell.dto.OrderDTO;
import com.lming.sell.enums.OrderStatusEnum;
import com.lming.sell.enums.PayStatusEnum;
import com.lming.sell.model.OrderDetail;
import com.lming.sell.service.OrderService;
import com.lming.sell.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {


    private static final String OPENID = "111111111111";

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void create() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(KeyUtil.genUniqueKey());
        orderDTO.setBuyerName("张良明");
        orderDTO.setBuyerPhone("15501700742");
        orderDTO.setBuyerAddress("海南省海口市蓝天路金棕苑C#1108");
        orderDTO.setBuyerOpenid("111111111111");
        List<OrderDetail> orderDetailList = new ArrayList<>();

        OrderDetail o1 = new OrderDetail();
        o1.setProductId("11111111");
        o1.setProductQuantity(5);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("22222222");
        o2.setProductQuantity(5);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result =  orderService.create(orderDTO  );

        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() throws Exception {
      OrderDTO orderDTO =   orderService.findOne("1507975433075286501");
      log.info("orderDTO={}",orderDTO);
      Assert.assertNotNull(orderDTO);
    }

    @Test
    public void findBuyerOrderList() throws Exception {

        PageRequest pageRequest = new PageRequest(1,2);

        Page<OrderDTO> result = orderService.findBuyerOrderList(OPENID,pageRequest);
        log.info("【订单列表】-result={}",result.getContent());

        Assert.assertNotEquals(0,result.getTotalElements());


    }

    @Test
    public void finishOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("1507978280577355807");
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        OrderDTO result = orderService.finishOrder(orderDTO);

        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    public void cancelOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("1507978280577355807");
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        OrderDTO result = orderService.cancelOrder(orderDTO);

        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());

    }

    @Test
    public void paidOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("1507978280577355807");
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        OrderDTO result = orderService.paidOrder(orderDTO);

        Assert.assertEquals(PayStatusEnum.PAID.getCode(),result.getPayStatus());
    }

}