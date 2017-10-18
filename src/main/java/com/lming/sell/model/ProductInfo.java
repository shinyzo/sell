package com.lming.sell.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@DynamicUpdate
public class ProductInfo {
    /**
     * 产品id
     */
    @Id
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
     * 产品库存
     */
    private Integer productStock;
    /**
     * 产品描述
     */
    private String productDescription;
    /**
     * 产品小图
     */
    private String productIcon;
    /**
     * 产品状态
     */
    private Integer productStatus;
    /**
     * 产品类目
     */
    private Integer categoryType;


}
