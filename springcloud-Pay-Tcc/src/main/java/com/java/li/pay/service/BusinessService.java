package com.java.li.pay.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(
        value = "spring-cloud-business-service"
)
public interface BusinessService {

    @RequestMapping("/business/transfer")
    boolean businessHal(@RequestBody Map<String, Object> params);
}
