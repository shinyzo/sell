package com.lming.sell.service.impl;

import com.lming.sell.exception.SellException;
import com.lming.sell.converter.OrderDetail2CartDTOConverter;
import com.lming.sell.converter.OrderMaster2OrderDTOConverter;
import com.lming.sell.dao.OrderDetailRepository;
import com.lming.sell.dao.OrderMasterRepository;
import com.lming.sell.dto.CartDTO;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.enums.OrderStatusEnum;
import com.lming.sell.enums.PayStatusEnum;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.model.OrderDetail;
import com.lming.sell.model.OrderMaster;
import com.lming.sell.model.ProductInfo;
import com.lming.sell.service.*;
import com.lming.sell.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMaterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private PayService payService;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        BigDecimal orderAmount = BigDecimal.ZERO;
        String orderId = KeyUtil.genUniqueKey();
        for(OrderDetail orderDetail :orderDetailList)
        {
            // 查询商品数量，价格
            // 产品校验
           ProductInfo productInfo =  productService.findOne(orderDetail.getProductId());
           if(null==productInfo)
           {
               log.error("【创建订单】-产品不存在，productId={}",orderDetail.getProductId());
               throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
           }

           // 计算总金额
            orderAmount  = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);

            //  写入订单详情(orderdetail)
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);
        }

        // 写入订单主表
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setBuyerName(orderDTO.getBuyerName());
        orderMaster.setBuyerPhone(orderDTO.getBuyerPhone());
        orderMaster.setBuyerAddress(orderDTO.getBuyerAddress());
        orderMaster.setBuyerOpenid(orderDTO.getBuyerOpenid());
        orderMaterRepository.save(orderMaster);

        // 扣减库存
        // java8 lambda 进行数据的复制
       List<CartDTO> cartDTOList =  orderDetailList.stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity()))
                                    .collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        orderDTO.setOrderId(orderMaster.getOrderId());
        // 发送消息至PC端
        webSocket.sendMessage("您有新的订单，订单号："+orderId);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster =  orderMaterRepository.findOne(orderId);
        if(null == orderMaster)
        {
            log.error("【单订单查询】-订单号不存在,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
        if(CollectionUtils.isEmpty(orderDetailList))
        {
            log.error("【单订单查询】-订单中无订单详情,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findBuyerOrderList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMaterRepository.findByBuyerOpenid(buyerOpenid,pageable);

        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(
                OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent()),
                pageable,orderMasterPage.getTotalElements());

        return orderDTOPage;
    }

    @Override
    @Cacheable(cacheNames = "product",key="123")
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMaterRepository.findAll(pageable);

        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(
                OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent()),
                pageable,orderMasterPage.getTotalElements());

        return orderDTOPage;
    }

    @Override
    public OrderDTO finishOrder(OrderDTO orderDTO) {

        // 更改的订单状态
        String orderId = orderDTO.getOrderId();
        OrderMaster orderMaster = orderMaterRepository.findOne(orderId);
        if(null==orderMaster)
        {
            log.error("【完结订单】-订单不存在,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        if(!OrderStatusEnum.NEW.getCode().equals(orderMaster.getOrderStatus()))
        {
            log.error("【完结订单】-订单状态不正确,orderId={},orderStatus={}",orderId,orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster updateResult = orderMaterRepository.save(orderMaster);
        if(null == updateResult)
        {
            log.error("【完结订单】-更新订单状态失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());

        // 推送微信模板消息
        pushMessageService.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(OrderDTO orderDTO) {
        // 更改的订单状态
        String orderId = orderDTO.getOrderId();
        OrderMaster orderMaster = orderMaterRepository.findOne(orderId);
        if(null==orderMaster)
        {
            log.error("【取消订单】-订单不存在,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        if(!OrderStatusEnum.NEW.getCode().equals(orderMaster.getOrderStatus()))
        {
            log.error("【取消订单】-订单状态不正确,orderId={},orderStatus={}",orderId,orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster updateResult = orderMaterRepository.save(orderMaster);
        if(null == updateResult)
        {
            log.error("【取消订单】-更新订单状态失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());

        //  返还库存
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList))
        {
            log.error("【取消订单】-订单中无订单详情,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }

        List<CartDTO> cartDTOList = OrderDetail2CartDTOConverter.convert(orderDetailList);
        productService.increaseStock(cartDTOList);

        // 若已支付，退款
        if(PayStatusEnum.PAID.getCode().equals(orderMaster.getPayStatus()))
        {
            // todo 退款
            payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    public OrderDTO paidOrder(OrderDTO orderDTO) {
        String orderId = orderDTO.getOrderId();
        OrderMaster orderMaster = orderMaterRepository.findOne(orderId);
        if(null==orderMaster)
        {
            log.error("【订单支付】-订单不存在,orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        if(!PayStatusEnum.WAIT.getCode().equals(orderMaster.getPayStatus()))
        {
            log.error("【订单支付】-订单支付状态不正确,orderId={},payStatus={}",orderId,orderMaster.getPayStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMaster.setPayStatus(PayStatusEnum.PAID.getCode());
        OrderMaster updateResult = orderMaterRepository.save(orderMaster);
        if(null == updateResult)
        {
            log.error("【订单支付】-更新订单支付状态失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        orderDTO.setPayStatus(PayStatusEnum.PAID.getCode());
        return orderDTO;
    }

}
