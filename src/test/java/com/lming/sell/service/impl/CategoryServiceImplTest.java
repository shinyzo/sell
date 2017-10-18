package com.lming.sell.service.impl;

import com.lming.sell.model.ProductCategory;
import com.lming.sell.service.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {


    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void findOne() throws Exception {

       ProductCategory productCategory = categoryService.findOne(1   );
       Assert.assertNotNull(productCategory);

    }

    @Test
    public void save() throws Exception {
        ProductCategory productCategory = new ProductCategory("小吃",3);
        productCategory = categoryService.save(productCategory    );
        Assert.assertNotNull(productCategory);

    }

    @Test
    public void findAll() throws Exception {
       List<ProductCategory> categoryList =  categoryService.findAll();
       Assert.assertNotEquals(0,categoryList.size());
    }

    @Test
    public void findByCategoryIn() throws Exception {

        List<Integer> productTypeList = Arrays.asList(1,2,3);
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(productTypeList);
        Assert.assertNotEquals(0,productCategoryList.size());
    }

}