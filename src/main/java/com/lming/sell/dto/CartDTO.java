package com.lming.sell.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 购物车
 */
@Data
public class CartDTO implements Serializable {

    private static final long serialVersionUID = 8742632405843817789L;
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品数量
     */
    private Integer productQuantity;

    public CartDTO() {
    }

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
