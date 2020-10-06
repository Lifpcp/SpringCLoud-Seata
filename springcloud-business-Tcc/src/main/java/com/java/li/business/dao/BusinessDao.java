package com.java.li.business.dao;

import com.java.li.common.model.BusinessBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessDao extends JpaRepository<BusinessBankAccount, Integer> {
}
