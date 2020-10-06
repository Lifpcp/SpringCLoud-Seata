package com.java.li.merchants.service.impl;

import com.java.li.common.model.MerchantsBankAccount;
import com.java.li.common.util.ResultHolder;
import com.java.li.merchants.dao.MerchantsDao;
import com.java.li.merchants.service.MerchantsService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class MerchantsServiceImpl implements MerchantsService {

    @Autowired
    private MerchantsDao merchantsDao;

    @Override
    @Transactional
    public boolean merchantsTransfer(BusinessActionContext businessActionContext, Map<String, Object> params) throws Exception {
        log.info("======>招商银行开始校验转入账号.......");
        final String xid = businessActionContext.getXid();

        int amount = Integer.parseInt(params.get("amount").toString());
        int merchantsId = Integer.parseInt(params.get("merchantsId").toString());
        //校验转入账号
        MerchantsBankAccount merchantsBankAccount = merchantsDao.findById(merchantsId).get();
        if (merchantsBankAccount == null) {
            log.info(
                    "======>招商银行: 账户[" + merchantsId + "]不存在, txId:" + xid);
            return false;
        }
        // 待转入资金作为 不可用金额划入冻结资金中
        int freezedAmount = merchantsBankAccount.getFreezedAmount() + amount;
        merchantsBankAccount.setFreezedAmount(freezedAmount);
        merchantsDao.save(merchantsBankAccount);
        //这里模拟招商银行账号加钱操作的时候，抛出异常
        //模拟异常
        int i = 1 / 0;
        log.info("招商银行转账校验结束： account：" + merchantsId + " amount：" + amount + ", dtx transaction id:：" + xid);
        //事务成功，保存一个标识，供第二阶段进行判断
        ResultHolder.setResult(getClass(), xid, "p");
        return true;
    }

    @Override
    @Transactional
    public boolean commitMerchantsTcc(BusinessActionContext context) throws Exception {
        log.info("======>into commitMerchantsTcc.commit() method");
        // 分布式事务ID
        final String xid = context.getXid();
        Map<String, Object> params = (Map<String, Object>) context.getActionContext("params");
        // 账户ID
        int merchantsId = Integer.parseInt(params.get("merchantsId").toString());
        // 转出金额
        int amount = Integer.parseInt(params.get("amount").toString());
        try {
            // 防止幂等性，如果commit阶段重复执行则直接返回
            if (ResultHolder.getResult(getClass(), xid) == null) {
                return true;
            }
            //账户直接加钱
            MerchantsBankAccount merchantsBankAccount = merchantsDao.findById(merchantsId).get();
            int newAmount = merchantsBankAccount.getMerchantsAmount() + amount;
            //被冻结的金额清除
            merchantsBankAccount.setFreezedAmount(merchantsBankAccount.getFreezedAmount() - amount);
            merchantsBankAccount.setMerchantsAmount(newAmount);
            merchantsDao.save(merchantsBankAccount);
            log.info("commit======>招商银行收账成功： account：" + merchantsId + " amount：" + amount + ", dtx transaction id:：" + xid);
            //提交成功是删除标识
            ResultHolder.removeResult(getClass(), xid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("commit操作异常！");
            //提交成功是删除标识
            ResultHolder.removeResult(getClass(), xid);
            throw new Exception("commit操作异常！");
        }
    }

    @Override
    @Transactional
    public boolean rollbackMerchantsTcc(BusinessActionContext context) throws Exception {
        log.info("======>into rollbackMerchantsTcc.rollback() method");
        // 分布式事务ID
        final String xid = context.getXid();
        Map<String, Object> params = (Map<String, Object>) context.getActionContext("params");
        // 账户ID
        int merchantsId = Integer.parseInt(params.get("merchantsId").toString());
        // 转出金额
        int amount = Integer.parseInt(params.get("amount").toString());
        try {
            // 防止幂等性，如果rollback阶段重复执行则直接返回
            if (ResultHolder.getResult(getClass(), xid) == null) {
                return true;
            }

            MerchantsBankAccount merchantsBankAccount = merchantsDao.findById(merchantsId).get();
            //清除被冻结的金额
            if (merchantsBankAccount == null) {
                // 账户不存在, 无需回滚动作
                return true;
            }
            System.out.println(merchantsBankAccount.getFreezedAmount() + "----" + amount);
            // 冻结金额 清除
            if (merchantsBankAccount.getFreezedAmount() >= amount) {
                merchantsBankAccount.setFreezedAmount(merchantsBankAccount.getFreezedAmount() - amount);
                merchantsDao.save(merchantsBankAccount);
            }
            log.info(" rollback======>招商银行回滚： account：" + merchantsId + " amount：" + amount + ", dtx transaction id:：" + xid);
            ResultHolder.removeResult(getClass(), xid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("rollback操作异常！");
            ResultHolder.removeResult(getClass(), xid);
            throw new Exception("rollback操作异常！");
        }
    }
}
