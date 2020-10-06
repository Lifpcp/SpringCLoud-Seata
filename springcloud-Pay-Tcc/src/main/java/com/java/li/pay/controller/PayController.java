package com.java.li.pay.controller;

import com.java.li.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PayController {

    @Autowired
    private PayService payService;


    /**
     * @param merchantsId 转入账号
     * @param businessId  转出账号
     * @param amount      金额
     * @return
     */
    @RequestMapping(value = "/seata/tcc", produces = "application/json; charset=utf-8", method = {
            RequestMethod.POST, RequestMethod.GET})
    public boolean seataProduct(@RequestParam("merchantsId") String merchantsId,
                                @RequestParam("businessId") String businessId,
                                @RequestParam("amount") int amount) {
        try {
            return payService.transfer(merchantsId, businessId, amount);
        } catch (Exception e) {
            return false;
        }

    }
}
