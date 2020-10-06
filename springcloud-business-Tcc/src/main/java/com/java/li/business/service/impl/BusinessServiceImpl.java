package com.java.li.business.service.impl;


import com.java.li.business.dao.BusinessDao;
import com.java.li.business.service.BusinessService;
import com.java.li.common.model.BusinessBankAccount;
import com.java.li.common.util.ResultHolder;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessDao businessDao;

    @Override
    public boolean businessTransfer(BusinessActionContext businessActionContext, Map<String, Object> params) throws Exception {
        log.info("======>工商银行开始校验转账账号.......");
        // 分布式事务ID
        final String xid = businessActionContext.getXid();
        int amount = Integer.parseInt(params.get("amount").toString());
        int businessId = Integer.parseInt(params.get("businessId").toString());
        try {
            //校验账户余额是否充足
            BusinessBankAccount businessBankAccount = businessDao.findById(businessId).get();
            if (businessBankAccount == null) {
                log.info(
                        "======>工商银行: 账户[" + businessId + "]不存在, txId:" + xid);
                return false;
            }
            //账号现有金额大于等于转出金额
            if (businessBankAccount.getBusinessAmount() <
                    amount) {
                log.info("======>工商银行: 账户[" + businessId +
                        "]余额不足账号余额:[" + businessBankAccount.getBusinessAmount() + "], txId:" + xid);
                return false;
            }
            // 待转入资金作为不可用金额,划入冻结金额中
            int freezedAmount = businessBankAccount.getFreezedAmount() + amount;
            businessBankAccount.setFreezedAmount(freezedAmount);
            //修改账号冻结金额
            businessDao.save(businessBankAccount);
            log.info("工商银行转账校验结束: account:" + businessId + " amount:" + amount + ", dtx transaction id: " + xid);

            //事务成功，保存一个标识，供第二阶段进行判断
            ResultHolder.setResult(getClass(), xid, "p");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("businessTransfer业务操作异常回滚！");
            return false;
        }
    }

    @Override
    @Transactional
    public boolean commitBusinessTcc(BusinessActionContext context) throws Exception {
        log.info("======>into commitBusinessTcc.commit() method");
        // 分布式事务ID
        final String xid = context.getXid();
        Map<String, Object> params = (Map<String, Object>) context.getActionContext("params");
        // 账户ID
        int businessId = Integer.parseInt(params.get("businessId").toString());
        // 转出金额
        int amount = Integer.parseInt(params.get("amount").toString());
        //    try {
        // 防止幂等性，如果commit阶段重复执行则直接返回
        if (ResultHolder.getResult(getClass(), xid) == null) {
            return true;
        }
        BusinessBankAccount businessBankAccount = businessDao.findById(businessId).get();
        // 扣除账户余额
        int newAmount = businessBankAccount.getBusinessAmount() - amount;
        if (newAmount < 0) {
            throw new RuntimeException("转账失败；余额不足账号：" + businessId);
        }
        businessBankAccount.setBusinessAmount(newAmount);
        // 释放账户 冻结金额
        businessBankAccount.setFreezedAmount(businessBankAccount.getFreezedAmount() - amount);

        businessDao.save(businessBankAccount);
        log.info("commit======>工商银行转账成功 account:" + businessId + " amount:" + amount + ", dtx transaction id: " + xid);
        //提交成功是删除标识
        ResultHolder.removeResult(getClass(), xid);
        return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("commit操作异常！");
//            //出现异常删除标识
//            ResultHolder.removeResult(getClass(), xid);
//            return ResultHolder.FAILURE;
//        }
    }

    @Override
    public boolean rollbackBusinessTcc(BusinessActionContext context) throws Exception {
        log.info("======>into rollbackBusinessTcc.rollback() method");
        // 分布式事务ID
        final String xid = context.getXid();
        Map<String, Object> params = (Map<String, Object>) context.getActionContext("params");
        // 账户ID
        int businessId = Integer.parseInt(params.get("businessId").toString());
        // 转出金额
        int amount = Integer.parseInt(params.get("amount").toString());
        try {
            // 防止幂等性，如果rollback阶段重复执行则直接返回
            if (ResultHolder.getResult(getClass(), xid) == null) {
                return true;
            }
            BusinessBankAccount businessBankAccount = businessDao.findById(businessId).get();
            if (businessBankAccount == null) {
                // 账户不存在，回滚什么都不做
                return true;
            }
            // 释放冻结金额
            if (businessBankAccount.getFreezedAmount() >= amount) {
                businessBankAccount.setFreezedAmount(businessBankAccount.getFreezedAmount() - amount);
                businessDao.save(businessBankAccount);
            }
            log.info("======>Undo rollback account:" + businessId + " amount:" + amount + ", dtx transaction id: " + xid);
            ResultHolder.removeResult(getClass(), xid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("rollback操作异常！");
            ResultHolder.removeResult(getClass(), xid);
            return false;
        }
    }
}
