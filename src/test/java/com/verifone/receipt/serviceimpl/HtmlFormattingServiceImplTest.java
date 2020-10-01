package com.verifone.receipt.serviceimpl;

import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.api.receipt.model.SendReceipt;
import com.verifone.api.receipt.model.Transaction;
import com.verifone.api.receipt.model.TransactionType;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.model.Mail;
import com.verifone.receipt.util.MockData;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Properties;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(locations = "/templates/application-test.properties")
public class HtmlFormattingServiceImplTest extends AbstractTestNGSpringContextTests {

    @Configuration
    static class HtmlFormattingServiceImplTestContextConfiguration {
        @Bean
        public HtmlFormattingServiceImpl htmlFormattingService() {
            return new HtmlFormattingServiceImpl();
        }
    }

    @Autowired
    HtmlFormattingServiceImpl htmlFormattingServiceImpl;

    ReceiptTransaction receiptTransaction;
    SendReceipt sendReceipt;
    Mail mail;
    Properties props = System.getProperties();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        receiptTransaction = MockData.prepareReceiptTransactionObject();
        sendReceipt = MockData.prepareSendReceiptObject();
        mail = MockData.prepareMailObject();
        //props.setProperty("receipt.file.location", "/opt/vcs/cegp/svc/receipt-service/");
    }


    @Test
    public void testFormatReceipt() throws Exception {

        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testFormatReceiptNew() throws Exception {
        Transaction txn = new Transaction();
        txn.setTransactionType(TransactionType.SALE);
        receiptTransaction.setTransaction(txn);
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }


    @Test
    public void testFormatReceiptNewWithNoAttach() throws Exception {
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }

    @Test
    public void testFormatReceiptNewWithAttach() throws Exception {

        props.setProperty("emailAttachment", "yes");
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }

    @Test
    public void testFormatReceiptWithNoAttach() throws Exception {
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test
    public void testFormatReceiptWithNoAttach1() throws Exception {
        props.setProperty("receipt.file.location", "/opt/vcs/cegp/svc/receipt-service");
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test
    public void testFormatReceiptWithAttach() throws Exception {
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test
    public void testFormatReceiptWithNoAttach4False() throws Exception {
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test(expectedExceptions = DateTimeParseException.class)
    public void testFormatReceiptWithNoAttach4False1() throws Exception {
        Transaction txn = new Transaction();
        txn.setCreatedDateTime(OffsetDateTime.parse("2020-06-19T25:04:47Z"));
        receiptTransaction.setTransaction(txn);
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test
    public void testFormatReceiptWithAttach4False() throws Exception {
        props.setProperty("preFormatAttachment", "yes");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test
    public void testFormatReceiptWithNoAttach4True1() throws Exception {
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testFormatReceiptNewTemplateNull() throws Exception {
        receiptTransaction.setTemplateId(null);
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }

    @Test
    public void testFormatReceiptWithNoExc() throws Exception {
        props.setProperty("preFormatAttachment", "no");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testFormatReceiptWithNoAttach4Exc() throws Exception {
        receiptTransaction.setTemplateId(null);
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, new Mail("no-reply@verifone.com", "rajeshwarim1@verifone.com", "rs", "text"));
    }

    @Test
    public void testFormatReceipt1() throws Exception {
        ReflectionTestUtils.setField(htmlFormattingServiceImpl, "preFormatAttachment", "yes");
        htmlFormattingServiceImpl.formatPreformatted(sendReceipt, mail);
    }
    @Test
    public void testFormatReceipt4() throws Exception {
        ReflectionTestUtils.setField(htmlFormattingServiceImpl, "emailAttachment", "yes");
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }
    @Test
    public void testFormatReceipt5() throws Exception {
        ReflectionTestUtils.setField(htmlFormattingServiceImpl, "emailAttachment", "yes");
        receiptTransaction.setSubject(null);
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }
    @Test
    public void testFormatReceipt6() throws Exception {
        ReflectionTestUtils.setField(htmlFormattingServiceImpl, "emailAttachment", "no");
        htmlFormattingServiceImpl.formatReceipt(receiptTransaction, mail);
    }
}

