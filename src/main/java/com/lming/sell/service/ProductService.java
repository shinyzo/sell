package com.lming.sell.service;

import com.lming.sell.dto.CartDTO;
import com.lming.sell.model.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    /**
     * 通过商品id查询商品
     * @param productId
     * @return
     */
    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 分页查询所有商品
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    /**
     * 所有商品集合
     * @return
     */
    List<ProductInfo> findAll();

    /**
     * 添加商品
     * @param ProductInfo
     * @return
     */
    ProductInfo save(ProductInfo ProductInfo);

    /**
     * 通过产品状态查询商品
     * @param productStatus
     * @return
     */
    List<ProductInfo>  findByProductStatus(Integer productStatus);

    /**
     * 增加库存
     */
    void increaseStock(List<CartDTO> cartDTOList);

    /**
     * 减少库存
     */
    void decreaseStock(List<CartDTO> cartDTOList);

}
