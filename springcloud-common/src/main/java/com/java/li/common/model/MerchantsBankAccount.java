package com.java.li.common.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 招商银行表
 */
@Entity(name = "merchants_bank_account")
@Data
public class MerchantsBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int merchantsId;

    //账户余额
    private int merchantsAmount;

    //冻结金额
    private int freezedAmount;

}
