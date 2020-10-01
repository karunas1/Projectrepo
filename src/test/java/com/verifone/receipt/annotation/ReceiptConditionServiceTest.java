package com.verifone.receipt.annotation;

import com.verifone.receipt.annotation.impl.ReceiptConditionServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collections;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ReceiptConditionServiceTest extends AbstractTestNGSpringContextTests {

    @Mock
    private ConditionContext conditionContext;

    private MockEnvironment environment;

    private ReceiptConditionService receiptConditionService;

    @Mock
    private AnnotatedTypeMetadata metadata;

    @BeforeTest
    public void init() {
        MockitoAnnotations.initMocks(this);
        environment = new MockEnvironment();
        receiptConditionService = new ReceiptConditionService();
    }
    @Test
    public void testMatches() {
        environment.setProperty("receipt.services.enable", "nameDummyValue");
        environment.setProperty("DummyValue", "DummyValue");
        when(conditionContext.getEnvironment()).thenReturn(environment);
        MultiValueMap<String, Object> attributes = new LinkedMultiValueMap<>();
        attributes.put("name", Collections.singletonList("DummyValue"));
        when(metadata.getAllAnnotationAttributes(ReceiptConditionServiceImpl.class.getName())).thenReturn(attributes);
        boolean result = receiptConditionService.matches(conditionContext, metadata);
        assertTrue(result);
        verify(conditionContext).getEnvironment();
        verify(metadata).getAllAnnotationAttributes(ReceiptConditionServiceImpl.class.getName());
    }
    @Test
    public void testMatches_ForFalse() {
        environment.setProperty("receipt.services.enable", "");
        when(conditionContext.getEnvironment()).thenReturn(environment);
        when(metadata.getAllAnnotationAttributes(ReceiptConditionServiceImpl.class.getName())).thenReturn(null);
        boolean result = receiptConditionService.matches(conditionContext, metadata);
        assertFalse(result);
        verify(conditionContext, times(2)).getEnvironment();
        verify(metadata, times(2)).getAllAnnotationAttributes(ReceiptConditionServiceImpl.class.getName());
    }

}