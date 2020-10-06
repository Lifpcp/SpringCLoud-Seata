package com.java.li.pay.service;

public interface PayService {

    public boolean transfer(String merchantsId,String businessId, int amount) throws Exception;
}
