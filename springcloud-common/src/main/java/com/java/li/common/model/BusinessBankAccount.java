package com.java.li.common.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 工商银行表
 */
@Entity(name = "business_bank_account")
@Data
public class BusinessBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int businessId;

    //账户余额
    private int businessAmount;

    //冻结金额
    private int freezedAmount;

}
