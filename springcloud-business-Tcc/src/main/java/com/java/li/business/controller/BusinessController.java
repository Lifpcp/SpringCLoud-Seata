package com.java.li.business.controller;

import com.alibaba.fastjson.JSON;
import com.java.li.business.service.BusinessService;
import com.java.li.common.util.ResultHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 工商银行
 */
@RestController
@Slf4j
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/business/transfer", produces = "application/json; charset=utf-8", method = {
            RequestMethod.POST, RequestMethod.GET})
    public boolean businessHal(@RequestBody Map<String, Object> params) throws Exception {
        try {
            return businessService.businessTransfer(null,params);
        } catch (Exception e) {
            return false;
        }
    }
}
