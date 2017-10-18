package com.lming.sell.service;

import com.lming.sell.model.SellerInfo;

public interface SellerService {

    public SellerInfo save(SellerInfo sellerInfo);


    public SellerInfo findByOpenid(String openid);
}
