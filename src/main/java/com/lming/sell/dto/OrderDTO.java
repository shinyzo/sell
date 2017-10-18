package com.lming.sell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lming.sell.enums.OrderStatusEnum;
import com.lming.sell.enums.PayStatusEnum;
import com.lming.sell.model.OrderDetail;
import com.lming.sell.util.EnumUtil;
import com.lming.sell.util.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO implements Serializable{


    private static final long serialVersionUID = 7322563929692112854L;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 购买者姓名
     */
    private String buyerName;
    /**
     * 购买者电话
     */
    private String buyerPhone;
    /**
     * 购买者地址
     */
    private String buyerAddress;
    /**
     * 购买者微信openid
     */
    private String buyerOpenid;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;
    /**
     * 订单详情
     */
    private List<OrderDetail> orderDetailList;


    /**
     * 用于前台数据展现，json忽略
     * @return
     */
    @JsonIgnore
    public PayStatusEnum getPayStatusEnum()
    {
        return EnumUtil.getCodeEnum(payStatus,PayStatusEnum.class);
    }

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum()
    {
        return EnumUtil.getCodeEnum(orderStatus,OrderStatusEnum.class);
    }

}
