package com.verifone.receipt.handler;

import com.verifone.receipt.dao.SMTPConfigDAO;
import com.verifone.receipt.exception.DaoException;
import com.verifone.receipt.model.SMTPConfig;
import com.verifone.svc.framework.endpoint.server.SvcEndpointProperties;
import com.verifone.svc.framework.endpoint.server.lb.ELBStatus;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.when;

public class SvcLBStatusHandlerTest {
    @Mock
    SMTPConfigDAO smtpConfigDAO;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    SvcEndpointProperties endpointProperties;

    @InjectMocks
    SvcLBStatusHandler svcLBStatusHandler;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandle() throws DaoException, IOException {
        when(smtpConfigDAO.getSMTPConfDetails()).thenReturn(new SMTPConfig());
        svcLBStatusHandler.checkSocketAlive("receipt.verifone.com", 20);
        ELBStatus result = svcLBStatusHandler.handle(request, response, endpointProperties);
        Assert.assertEquals(ELBStatus.DOWN, result);
    }

    @Test
    public void testCheckSocketAlive1() throws IOException {
        boolean result = svcLBStatusHandler.checkSocketAlive("test.com", 20);
        Assert.assertEquals(false, result);
    }
}

