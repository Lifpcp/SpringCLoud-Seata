package com.java.li.merchants.dao;

import com.java.li.common.model.MerchantsBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantsDao extends JpaRepository<MerchantsBankAccount, Integer> {
}
