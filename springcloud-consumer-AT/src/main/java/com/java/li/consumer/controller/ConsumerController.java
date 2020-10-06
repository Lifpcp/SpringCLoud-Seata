package com.java.li.consumer.controller;

import com.java.li.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    //商品下单；商品库存减数量
    @RequestMapping(value = "/product/order/{pid}", produces = "application/json; charset=utf-8", method = {
            RequestMethod.POST, RequestMethod.GET})
    public String seataProduct(@PathVariable("pid") Integer pid) {
        try {
            return consumerService.seataProduct(pid);
        }catch (Exception e){
            return "处理结束；结果进行事务回滚了！";
        }

    }
}
