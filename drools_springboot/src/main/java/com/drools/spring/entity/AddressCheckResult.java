package com.drools.spring.entity;

import lombok.Data;

/**
 * Created by lcc on 2018/9/27.
 */
@Data
public class AddressCheckResult {

    private boolean postCodeResult = false; // true:通过校验；false：未通过校验
}