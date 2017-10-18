package com.lming.sell.service.impl;

import com.lming.sell.enums.ProductStatusEnum;
import com.lming.sell.model.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {


    private static final String PRODUCT_ID = "222222";

    @Autowired
    private ProductServiceImpl productInfoService;

    @Test
    public void findOne() throws Exception {

        ProductInfo result = productInfoService.findOne(PRODUCT_ID);
        Assert.assertNotNull(result);
    }

    @Test
    public void findAll() throws Exception {
        PageRequest pageRequest = new PageRequest(0,2  );
        Page<ProductInfo> productInfoList = productInfoService.findAll( pageRequest);
        Assert.assertNotEquals(0,productInfoList.getTotalElements());

    }

    @Test
    public void save() throws Exception {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("222222");
        productInfo.setProductName("金桔柠檬茶");
        productInfo.setProductPrice(new BigDecimal(4.2));
        productInfo.setProductStock(100);
        productInfo.setProductIcon("http://localhsot/sell/data/upload/pic/11111.png");
        productInfo.setProductDescription("美容养颜水果茶");
        productInfo.setCategoryType(3);
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());

        ProductInfo result =  productInfoService.save(productInfo);
        Assert.assertNotNull(result);

    }

    @Test
    public void findByProductStatus() throws Exception {

       List<ProductInfo> productInfoList =  productInfoService.findByProductStatus(ProductStatusEnum.UP.getCode());
       Assert.assertEquals(0,productInfoList.size());

    }

}