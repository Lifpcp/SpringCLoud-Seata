package com.java.li.product.service.impl;

import com.java.li.common.model.Product;
import com.java.li.product.dao.ProductDao;
import com.java.li.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product findByPid(Integer pid) {
        return productDao.findById(pid).get();
    }

    @Override
    public void reduceInventory(Integer pid, Integer number) {
        //查询
        Product product = productDao.findById(pid).get();
        //省略校验

        //内存中扣减
        product.setStock(product.getStock() - number);

        //模拟异常
        int i = 1 / 0;

        //保存
        productDao.save(product);
    }


}
