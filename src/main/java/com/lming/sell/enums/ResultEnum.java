package com.lming.sell.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    PRODUCT_NOT_EXIST(101,"产品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(102,"产品库存不足"),
    ORDER_NOT_EXIST(103,"订单不存在"),
    ORDER_DETAIL_EMPTY(104,"订单详情为空"),
    ORDER_STATUS_ERROR(105,"订单状态不正确"),
    ORDER_UPDATE_FAIL(106,"订单更新失败"),
    ORDER_PAY_STATUS_ERROR(107,"订单支付状态不正确"),
    CART_EMPTY(108,"购物车不能为空"),
    ORDER_OWNER_ERROR(109,"非当前用户订单"),
    LOGIN_FAIL(110,"登录失败，登录信息不正确"),
    LOGOUT_SUCCESS(111,"退出成功"),

    WECHAT_OAUTH_ERROR(201,"微信网页授权错误"),
    NOTIFY_AMOUNT_ERROR(202,"微信支付异步通知金额与订单金额不一致"),


    DATA_TRANS_ERROR(901,"数据格式转换错误"),

    PARAM_ERROR(902,"参数不合法"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
