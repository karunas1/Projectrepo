package com.verifone.receipt.annotation.impl;

import com.verifone.receipt.annotation.ReceiptConditionService;
import org.springframework.context.annotation.Conditional;

@Conditional(ReceiptConditionService.class)
public @interface ReceiptConditionServiceImpl {

    String name();

}
