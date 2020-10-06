package com.java.li.consumer.service;

import com.java.li.common.model.Order;
import com.java.li.common.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(
        value = "spring-cloud-order-service"
)
public interface OrderService {
    //创建商品订单
    @RequestMapping("/order/save")
    Order orderSave(@RequestBody Product product);
}
