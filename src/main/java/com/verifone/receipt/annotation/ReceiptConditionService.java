package com.verifone.receipt.annotation;

import com.verifone.receipt.annotation.impl.ReceiptConditionServiceImpl;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class ReceiptConditionService implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        final String enableReceiptService = conditionContext.getEnvironment().getProperty("receipt.services.enable", "");
        MultiValueMap<String, Object> params = annotatedTypeMetadata.getAllAnnotationAttributes(ReceiptConditionServiceImpl.class.getName());
        return !StringUtils.isEmpty(enableReceiptService) && Objects.nonNull(params) &&
                enableReceiptService.contains((String)params.getFirst("name"));
    }

}
