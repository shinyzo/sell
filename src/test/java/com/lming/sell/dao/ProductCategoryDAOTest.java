package com.lming.sell.dao;

import com.lming.sell.model.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryDAOTest {

    public CategoryRepository getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(CategoryRepository productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    @Autowired
    private CategoryRepository productCategoryDAO;
    @Test
    public void findOneTest()
    {
        ProductCategory productCategory = productCategoryDAO.findOne(1);
        System.out.print(productCategory.toString());

    }


    @Test
    public void saveTest()
    {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("主食");
        productCategory.setCategoryType(2);
        productCategory = productCategoryDAO.save(productCategory);
        Assert.assertNotNull(productCategory);
    }



}