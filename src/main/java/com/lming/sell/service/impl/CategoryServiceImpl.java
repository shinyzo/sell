package com.lming.sell.service.impl;

import com.lming.sell.dao.CategoryRepository;
import com.lming.sell.model.ProductCategory;
import com.lming.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findOne(categoryId);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {

        return repository.save(productCategory);
    }
    @Override
    public List<ProductCategory> findAll(){
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
       return repository.findByCategoryTypeIn(categoryTypeList);
    }

}
