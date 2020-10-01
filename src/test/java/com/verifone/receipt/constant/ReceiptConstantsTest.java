package com.verifone.receipt.constant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RunWith(SpringRunner.class)
public class ReceiptConstantsTest {

    @Test
    public void receiptTestConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ReceiptConstants> receiptConstantsConstructor = ReceiptConstants.class.getDeclaredConstructor();
        receiptConstantsConstructor.setAccessible(true);
        receiptConstantsConstructor.newInstance();

    }
}

