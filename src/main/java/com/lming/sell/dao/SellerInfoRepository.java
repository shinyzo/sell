package com.lming.sell.dao;

import com.lming.sell.model.ProductInfo;
import com.lming.sell.model.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo,String> {

    public SellerInfo findByOpenid(String openid);

}
