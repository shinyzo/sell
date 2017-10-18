package com.lming.sell.service.impl;

import com.lming.sell.exception.SellException;
import com.lming.sell.dao.ProductInfoRepository;
import com.lming.sell.dto.CartDTO;
import com.lming.sell.enums.ProductStatusEnum;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.model.ProductInfo;
import com.lming.sell.service.ProductService;
import com.lming.sell.service.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;

    @Autowired
    private RedisLock redisLock;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<ProductInfo> findAll() {
        return repository.findAll();
    }

    @Override
    @CachePut(cacheNames = "product",key="123")
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public List<ProductInfo> findByProductStatus(Integer productStatus) {
        return repository.findByProductStatus(productStatus);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO :cartDTOList)
        {
            ProductInfo productInfo =  repository.findOne(cartDTO.getProductId());

            if(null==productInfo)
            {

                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer number = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(number);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {

        for(CartDTO cartDTO :cartDTOList)
        {
//            ProductInfo productInfo =  repository.findOne(cartDTO.getProductId());
//
//            if(null==productInfo)
//            {
//                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
//            }
//
//
//            Integer number = productInfo.getProductStock() - cartDTO.getProductQuantity();
//            if(number<0)
//            {
//                log.error("【扣减库存】- 库存不足，ProductId={},currentProductStock={},buyProductStock={}",
//                        cartDTO.getProductId(), productInfo.getProductStock(),cartDTO.getProductQuantity());
//                throw new SellException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
//            }
//            productInfo.setProductStock(number);
//            repository.save(productInfo);

            decreaseStockByLock(cartDTO.getProductId(),cartDTO.getProductQuantity());

        }
    }

    /**
     * 方式一：添加synchronized 也可以，但是无法做到多台机器分布式处理 2：性能较低
     * 采用redis分布式锁，可以支持分布式部署，支持高并发秒杀抢购类业务同时在线访问 10w+
     * 进行库存扣减
     * @param productId
     * @param productQuantity
     */
    public void  decreaseStockByLock(String productId,Integer productQuantity)
    {

        long time = System.currentTimeMillis() + RedisLock.TIMEOUT ;
        // 加锁
        if(!redisLock.lock(productId,String.valueOf(time)))
        {
            throw new SellException(101,"当前下单人数较多，请稍后再试");
        }

        ProductInfo productInfo =  repository.findOne(productId);

        if(null==productInfo)
        {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        Integer number = productInfo.getProductStock() - productQuantity;
        if(number<0)
        {
            log.error("【扣减库存】- 库存不足，ProductId={},currentProductStock={},buyProductStock={}",
                    productId, productInfo.getProductStock(),productQuantity);
            throw new SellException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
        }
        productInfo.setProductStock(number);
        repository.save(productInfo);
        // 解锁
        redisLock.unlock(productId,String.valueOf(time));
    }
}
