package com.java.li.consumer.service;

import com.java.li.common.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//value用于指定调用nacos下哪个微服务
//fallback 指定当调用出现问题之后,要进入到哪个类中的同名方法之下执行备用逻辑
@FeignClient(
        value = "spring-cloud-product-service"//,
        //fallback = ProductServiceFallback.class,
        //fallbackFactory = ProductServiceFallbackFactory.class
)
public interface ProductService {
    //@FeignClient的value +  @RequestMapping的value值  其实就是完成的请求地址  "http://service-product/product/" + pid
    //指定请求的URI部分
    @RequestMapping("/product/{pid}")
    Product findByPid(@PathVariable Integer pid);

    //扣减库存
    //参数一: 商品标识
    //参数二:扣减数量
    @RequestMapping("/product/reduceInventory")
    void reduceInventory(@RequestParam("pid") Integer pid,
                         @RequestParam("number") Integer number);
}
