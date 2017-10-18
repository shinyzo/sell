package com.lming.sell.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class OrderForm {

    /**
     * 购买者姓名
     */
    @NotEmpty(message = "姓名不能为空")
    private String buyerName;
    /**
     * 购买者联系电话
     */
    @NotEmpty(message = "联系电话不能为空")
    private String buyerPhone;
    /**
     * 购买者地址
     */
    @NotEmpty(message = "收货地址不能为空")
    private String buyerAddress;
    /**
     * 购买者微信openid
     */
    @NotEmpty(message = "openid不能为空")
    private String buyerOpenid;

    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;

}
