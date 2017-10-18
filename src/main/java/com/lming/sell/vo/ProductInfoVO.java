package com.lming.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductInfoVO implements Serializable{


    private static final long serialVersionUID = 2336420045080615739L;
    /**
     * 产品id
     */
    @JsonProperty("id")
    private String productId;
    /**
     * 产品名称
     */
    @JsonProperty("name")
    private String productName;
    /**
     * 产品价格
     */
    @JsonProperty("price")
    private BigDecimal productPrice;

    /**
     * 产品描述
     */
    @JsonProperty("description")
    private String productDescription;
    /**
     * 产品小图
     */
    @JsonProperty("icon")
    private String productIcon;


}
