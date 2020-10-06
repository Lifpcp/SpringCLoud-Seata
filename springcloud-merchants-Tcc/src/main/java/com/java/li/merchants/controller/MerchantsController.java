package com.java.li.merchants.controller;

import com.java.li.common.util.ResultHolder;
import com.java.li.merchants.service.MerchantsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 招商银行
 */
@RestController
@Slf4j
public class MerchantsController {

    @Autowired
    private MerchantsService merchantsService;

    @RequestMapping(value = "/business/businessHal", produces = "application/json; charset=utf-8", method = {
            RequestMethod.POST, RequestMethod.GET})
    public boolean businessHal(@RequestBody Map<String, Object> params) {
        try {
            return merchantsService.merchantsTransfer(null,params);
        } catch (Exception e) {
            return false;
        }
    }
}
