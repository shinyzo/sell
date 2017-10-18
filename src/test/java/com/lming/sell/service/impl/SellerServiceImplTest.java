package com.lming.sell.service.impl;

import com.lming.sell.model.SellerInfo;
import com.lming.sell.service.SellerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerServiceImplTest {

    @Autowired
    private SellerService sellerService;

    @Test
    public void save(){

        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setOpenid("111111");
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("admin");
        sellerInfo.setSellerId("1");
        SellerInfo result =  sellerService.save(sellerInfo);
        Assert.assertEquals("admin",result.getUsername());

    }


    @Test
    public void findByOpenid(){
        String openid = "111111";

        SellerInfo result = sellerService.findByOpenid(openid);
        Assert.assertEquals(openid,result.getOpenid());
    }

}