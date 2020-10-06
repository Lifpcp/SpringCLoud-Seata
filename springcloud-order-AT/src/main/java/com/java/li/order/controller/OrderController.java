package com.java.li.order.controller;

import com.alibaba.fastjson.JSON;
import com.java.li.common.model.Order;
import com.java.li.common.model.Product;
import com.java.li.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    //下单--fegin的调用方式
    @RequestMapping(value = "/order/save", produces = "application/json; charset=utf-8", method = {
            RequestMethod.POST, RequestMethod.GET})
    public Order orderSave(@RequestBody Product product) {
        log.info("接收到{}号商品的下单请求,接下来调用商品微服务查询此商品信息", product);

        //下单(创建订单)
        Order order = new Order();
        order.setUid(1);
        order.setUsername("测试用户");
        order.setPid(product.getPid());
        order.setPname(product.getPname());
        order.setPprice(product.getPprice());
        order.setNumber(1);
        //mysql-order库新增一条数据
        orderService.createOrder(order);
        log.info("创建订单成功,订单信息为{}", JSON.toJSONString(order));
        return order;
    }
}
