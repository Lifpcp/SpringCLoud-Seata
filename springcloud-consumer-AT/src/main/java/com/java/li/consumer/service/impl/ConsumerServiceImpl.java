package com.java.li.consumer.service.impl;

import com.alibaba.fastjson.JSON;
import com.java.li.common.model.Order;
import com.java.li.common.model.Product;
import com.java.li.consumer.service.ConsumerService;
import com.java.li.consumer.service.OrderService;
import com.java.li.consumer.service.ProductService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {


    //订单服务
    @Autowired
    private OrderService orderService;

    //商品库存服务
    @Autowired
    private ProductService productService;


    /**
     * rollbackFor回滚异常级别
     * @param pid
     * @return
     */
    @Override
    @GlobalTransactional(name = "spring-cloud-consumer-service", rollbackFor = Exception.class)
    public String seataProduct(Integer pid) {
        log.info("接下来要进行{}号商品信息的查询", pid);
        Product product = productService.findByPid(pid);
        log.info("商品信息查询成功,内容为{}", JSON.toJSONString(product));
        //创建订单
        Order order = orderService.orderSave(product);
        log.info("创建订单", JSON.toJSONString(order));
        //商品库存减数量
        /**
         * 真实场景下：并发处理时，其实这里要先查一遍，看库存是否足够，如果不足则业务失败回滚
         */
        productService.reduceInventory(pid, order.getNumber());
        log.info("商品库存减数量", order.getNumber());

        return "操作结束";
    }
}
