package com.lming.sell.controller;

import com.lming.sell.exception.SellException;
import com.lming.sell.converter.OrderForm2OrderDTOConverter;
import com.lming.sell.dto.OrderDTO;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.form.OrderForm;
import com.lming.sell.service.impl.BuyerServiceImpl;
import com.lming.sell.service.impl.OrderServiceImpl;
import com.lming.sell.util.ResultVOUtil;
import com.lming.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private BuyerServiceImpl buyerService;

    /**
     * 生成订单
     * @return
     */
    @PostMapping(value = "/create")
    public ResultVO create(@Valid  OrderForm orderForm, BindingResult bindingResult){

        if(bindingResult.hasErrors())
        {
            log.error("【创建订单】 参数不正确，orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());

        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList()))
        {
            log.error("【创建订单】 购物车不能为空，orderCart={}",orderDTO.getOrderDetailList());
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO result = orderService.create(orderDTO);
        Map<String,String> map = new HashMap<String,String>();
        map.put("orderId",result.getOrderId());

        return ResultVOUtil.success(map);
    }

    /**
     * 订单列表
     * @return
     */
    @PostMapping(value = "/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size){

        if(StringUtils.isEmpty(openid))
        {
            log.error("【订单列表查询】-openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest pageRequest = new PageRequest(page,size);

        Page<OrderDTO> orderDTOPage =  orderService.findBuyerOrderList(openid,pageRequest);

        return ResultVOUtil.success(orderDTOPage.getContent());

    }


    /**
     * 订单详情
     * @return
     */
    @GetMapping(value = "/detail")
    public ResultVO detail(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId){


        if(StringUtils.isEmpty(openid))
        {
            log.error("【订单详情查询】-openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(orderId))
        {
            log.error("【订单详情查询】-orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        // todo 不安全做法 校验买家openid和登录用户的openid是否一致
        OrderDTO orderDTO =   buyerService.findOrderOne(openid,orderId);


        return ResultVOUtil.success(orderDTO);
    }

    /**
     * 取消订单
     * @return
     */
    @GetMapping(value = "/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(openid))
        {
            log.error("【取消订单】-openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(orderId))
        {
            log.error("【取消订单】-orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        // todo 不安全做法 校验买家openid和登录用户的openid是否一致
        OrderDTO orderDTO = buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }







}
