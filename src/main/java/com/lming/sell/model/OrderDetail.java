package com.lming.sell.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@DynamicUpdate
public class OrderDetail {

    /**
     * 明细id
     */
    @Id
    private String detailId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品价格
     */
    private BigDecimal productPrice;
    /**
     * 购买数量
     */
    private Integer productQuantity;
    /**
     * 产品图标
     */
    private String productIcon;

}
