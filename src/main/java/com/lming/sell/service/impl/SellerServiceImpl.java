package com.lming.sell.service.impl;

import com.lming.sell.dao.SellerInfoRepository;
import com.lming.sell.model.SellerInfo;
import com.lming.sell.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo save(SellerInfo sellerInfo) {
        return repository.save(sellerInfo);
    }

    @Override
    public SellerInfo findByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
