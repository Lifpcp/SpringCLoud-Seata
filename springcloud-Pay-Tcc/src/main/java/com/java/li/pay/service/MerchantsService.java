package com.java.li.pay.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(
        value = "spring-cloud-merchants-service"
)
public interface MerchantsService {

    @RequestMapping("/business/businessHal")
    boolean businessHal(@RequestBody Map<String, Object> params);
}
