package com.verifone.receipt.dao.impl;

import com.verifone.receipt.exception.DaoException;
import com.verifone.receipt.model.SMTPConfig;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.mockito.InjectMocks;
import org.testng.annotations.Test;
import org.mockito.MockitoAnnotations;

public class SMTPConfigDAOImplTest {

    @InjectMocks
    SMTPConfigDAOImpl sMTPConfigDAOImpl;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSMTPConfDetails() throws DaoException {
        SMTPConfig result = sMTPConfigDAOImpl.getSMTPConfDetails();
        Assert.assertNotNull(result);
    }
}

