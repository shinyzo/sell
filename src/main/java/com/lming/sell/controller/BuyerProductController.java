package com.lming.sell.controller;

import com.lming.sell.model.ProductCategory;
import com.lming.sell.model.ProductInfo;
import com.lming.sell.service.impl.CategoryServiceImpl;
import com.lming.sell.service.impl.ProductServiceImpl;
import com.lming.sell.util.ResultVOUtil;
import com.lming.sell.vo.ProductInfoVO;
import com.lming.sell.vo.ProductVO;
import com.lming.sell.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {


    @Autowired
    private ProductServiceImpl productInfoService;
    @Autowired
    private CategoryServiceImpl categoryService;


    @GetMapping(value = "/list")
    public ResultVO list(){
        List<ProductVO> productVOList = new ArrayList<>();

        // 查询所有在架商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        // java8 lambda
        List<Integer> categoryTypeList  = productInfoList.stream().map(e->e.getCategoryType())
                                        .collect(Collectors.toList());
        // 查询所有类目
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        // 拼装数据
        for(ProductCategory productCategory:productCategoryList)
        {

            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(productCategory,productVO);

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for(ProductInfo productInfo:productInfoList)
            {
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType()))
                {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }




}
