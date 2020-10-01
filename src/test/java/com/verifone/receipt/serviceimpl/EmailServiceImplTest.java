package com.verifone.receipt.serviceimpl;

import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.model.Mail;
import com.verifone.receipt.util.MockData;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Properties;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(locations = "/templates/application-test.properties")
public class EmailServiceImplTest extends AbstractTestNGSpringContextTests {

    @InjectMocks
    EmailServiceImpl emailServiceImpl;

    Mail mail;
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mail = MockData.prepareMailObject();

    }

    @Test(expectedExceptions = EmailServiceException.class)
    public void testSendEmail() throws EmailServiceException {
        String result = emailServiceImpl.sendEmail(mail);
        Assert.assertNotNull(result);
    }

    @Test(expectedExceptions = EmailServiceException.class)
    public void testSendEmail1() throws Exception {

        mail.setProcessEmail(false);
        String result = emailServiceImpl.sendEmail(mail);
        Assert.assertNotNull(result);
    }

    @Test(expectedExceptions = EmailServiceException.class)
    public void testsendEmailSimple() throws EmailServiceException {
        String result = emailServiceImpl.sendEmailSimple(mail);
        Assert.assertNotNull(result);
    }

    @Test(expectedExceptions = EmailServiceException.class)
    public void testsendEmailSimple1() throws Exception {

        mail.setProcessEmail(false);
        String result = emailServiceImpl.sendEmailSimple(mail);
        Assert.assertNotNull(result);
    }
}

