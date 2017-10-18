package com.lming.sell.service;

import com.lming.sell.model.ProductCategory;

import java.util.List;

public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    ProductCategory save(ProductCategory productCategory);

    public List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);


}
