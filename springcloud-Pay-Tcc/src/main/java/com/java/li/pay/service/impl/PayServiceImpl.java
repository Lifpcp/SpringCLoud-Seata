package com.java.li.pay.service.impl;

import com.java.li.common.util.ResultHolder;
import com.java.li.pay.service.BusinessService;
import com.java.li.pay.service.MerchantsService;
import com.java.li.pay.service.PayService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    //工商银行
    @Autowired
    private BusinessService businessService;

    //招商银行
    @Autowired
    private MerchantsService merchantsService;

    @Override
    @GlobalTransactional(timeoutMills = 1000000, name = "spring-cloud-pay-service")
    public boolean transfer(String merchantsId, String businessId, int amount) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        //转出账号（工商账号）
        params.put("businessId", businessId);
        //转入账号（招商账号）
        params.put("merchantsId", merchantsId);
        boolean answer = businessService.businessHal(params);
        if (!answer) {
            // 扣钱参与者，一阶段失败; 回滚本地事务和分布式事务
            throw new Exception("账号:[" + businessId + "] 预扣款失败!");
        }
        // 加钱参与者，一阶段执行
        answer = merchantsService.businessHal(params);
        if (!answer) {
            throw new Exception("账号:[" + merchantsId + "] 预收款失败!");
        }
        return true;
    }
}
