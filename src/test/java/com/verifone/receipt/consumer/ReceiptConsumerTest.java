package com.verifone.receipt.consumer;

import com.verifone.receipt.config.ApplicationProperties;
import com.verifone.receipt.config.KafkaProperties;
import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.exception.InvalidTemplateException;
import com.verifone.receipt.exception.KafkaImporterException;
import com.verifone.receipt.producer.Sender;
import com.verifone.receipt.service.IReceiptService;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class ReceiptConsumerTest extends AbstractTestNGSpringContextTests {

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @MockBean
    @Qualifier("property")
    private ApplicationProperties applicationProperties;

    @Autowired
    private IReceiptService receiptService;

    @Autowired
    private ReceiptConsumer receiptConsumer;

    @Mock
    private Acknowledgment acknowledgment;

    @MockBean
    private RebalanceListener rebalanceListener;

    @MockBean
    private Sender sender;

    @TestConfiguration
    static class ReceiverTestConfiguration {
        @MockBean
        private IReceiptService receiptService;

        @Bean
        ReceiptConsumer receiver() {
            return new ReceiptConsumer(receiptService);
        }
    }

    @Test
    public void receiverTest_success() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doNothing().when(receiptService).processReceipt(any()) ;
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("insertTopic-failure");
        when(applicationProperties.getKafka()).thenReturn(kafkaProperties);
        receiptConsumer.receive("insertTopic", 1, 1, acknowledgment,data());
        verify(applicationProperties, times(6)).getKafka();
        verify(receiptService, times(5)).processReceipt(any());
    }

    @Test
    public void receiverTestFailure() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("insertTopic-failure");
        when(applicationProperties.getKafka()).thenReturn(kafkaProperties);
        receiptConsumer.receive("insertTopic", 1, 1, acknowledgment,invalidData());
        verify(applicationProperties).getKafka();
    }

    @Test
    public void receiverTestFailureForInvalidTempletData() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(InvalidTemplateDataException.class).when(receiptService).processReceipt(any());
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("insertTopic-failure");
        when(applicationProperties.getKafka()).thenReturn(kafkaProperties);
        receiptConsumer.receive("insertTopic", 1, 1, acknowledgment,data());
        verify(applicationProperties, times(4)).getKafka();
        verify(receiptService, times(3)).processReceipt(any());
    }

    @Test
    public void receiverTestFailureForInvalidTemplate() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(InvalidTemplateException.class).when(receiptService).processReceipt(any());
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("insertTopic-failure");
        when(applicationProperties.getKafka()).thenReturn(kafkaProperties);
        receiptConsumer.receive("insertTopic", 1, 1, acknowledgment,data());
        verify(applicationProperties, times(3)).getKafka();
        verify(receiptService, times(2)).processReceipt(any());
    }

    @Test
    public void receiverTestFailureForInvalidEmail() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(EmailServiceException.class).when(receiptService).processReceipt(any());
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("insertTopic-failure");
        when(applicationProperties.getKafka()).thenReturn(kafkaProperties);
        receiptConsumer.receive("insertTopic", 1, 1, acknowledgment,data());
        verify(applicationProperties,times(2)).getKafka();
        verify(receiptService).processReceipt(any());
    }

    @Test
    public void receiverTestFailureForKafka() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(KafkaImporterException.class).when(receiptService).processReceipt(any());
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("insertTopic-failure");
        when(applicationProperties.getKafka()).thenReturn(kafkaProperties);
        receiptConsumer.receive("insertTopic", 1, 1, acknowledgment,data());
        verify(applicationProperties, times(5)).getKafka();
        verify(receiptService, times(4)).processReceipt(any());
    }

    private String data() {
        return "{\"to\":\"gayatrib1@verifone.com\",\"fromName\":\"CarteBancaire\",\"subject\":\"Intermarche?Votreticketdepaiement\",\"templateId\":\"payment_cb_sale\",\"metadata\":{\"logoContentType\":\"LITERAL111\",\"logoContent\":\"image\",\"businessInfoContentType\":\"\",\"businessInfoContent\":\"\",\"greetingsContentType\":\"\",\"greetingsContent\":\"CBAAustrailia\",\"signature\":\"\",\"receiptCopyType\":\"\",\"footerContentType\":\"\",\"footerContent\":\"\",\"receiptHtmlStyle\":\"\",\"printEmvParametersReceiptEnabled\":\"\",\"customerCopy\":\"false\",\"psnAvailable\":false,\"cryptoType\":\"ARQC\",\"cashOnly\":\"\",\"txnApprovalText\":\"APPROVED1\",\"copyType\":\"\",\"emvDataAvailable\":\"\"},\"transaction\":{\"cardProduct\":\"\",\"initiatorTraceId\":\"123456789\",\"maskedCardNumber\":\"5500000000000000\",\"accountType\":\"CREDIT\",\"entryMode\":\"MAG_STRIPE\",\"applicationPreferredName\":\"Verifone_CBA\",\"transactionType\":\"SALE\",\"amount\":\"20\",\"responseCode\":\"00\",\"errorMessage\":\"\",\"transmittedDateTime\":\"2002-10-02T15:00:00Z\",\"authorisationCode\":\"AP123456\",\"cashbackAmount\":\"0\",\"gratuityAmount\":\"0\",\"feeAmount\":\"0\",\"totalAmount\":\"20\",\"dedicatedFileName\":\"A00000000300037561\",\"acquirerName\":\"CB\",\"icc\":{\"debugData\":\"\",\"sequenceNumber\":\"0\",\"applicationId\":\"appID01\",\"terminalVerificationResults\":\"terminal01\",\"expectedApplicationTransactionCounter\":\"expTransCount01\",\"cryptogramInformationData\":\"cryptoInfo01\"}},\"merchant\":{\"name\":\"CarteBancaire\",\"address\":\"maddress01\",\"postCode\":\"12345678\",\"phoneNumbers\":\"+61253456793\",\"country\":\"UZB\",\"poiId\":\"aaccvv23\",\"merchantId\":\"222229999999999\"}}";
    }

    private String invalidData() {
        return "{{{\"to\":\"gayatrib1@verifone.com\",\"fromName\":\"CarteBancaire\",\"subject\":\"Intermarche?Votreticketdepaiement\",\"templateId\":\"\",\"metadata\":{\"logoContentType\":\"LITERAL111\",\"logoContent\":\"image\",\"businessInfoContentType\":\"\",\"businessInfoContent\":\"\",\"greetingsContentType\":\"\",\"greetingsContent\":\"CBAAustrailia\",\"signature\":\"\",\"receiptCopyType\":\"\",\"footerContentType\":\"\",\"footerContent\":\"\",\"receiptHtmlStyle\":\"\",\"printEmvParametersReceiptEnabled\":\"\",\"customerCopy\":\"false\",\"psnAvailable\":false,\"cryptoType\":\"ARQC\",\"cashOnly\":\"\",\"txnApprovalText\":\"APPROVED1\",\"copyType\":\"\",\"emvDataAvailable\":\"\"},\"transaction\":{\"cardProduct\":\"\",\"initiatorTraceId\":\"123456789\",\"maskedCardNumber\":\"5500000000000000\",\"accountType\":\"CREDIT\",\"entryMode\":\"MAG_STRIPE\",\"applicationPreferredName\":\"Verifone_CBA\",\"transactionType\":\"SALE\",\"amount\":\"20\",\"responseCode\":\"00\",\"errorMessage\":\"\",\"transmittedDateTime\":\"2002-10-02T15:00:00Z\",\"authorisationCode\":\"AP123456\",\"cashbackAmount\":\"0\",\"gratuityAmount\":\"0\",\"feeAmount\":\"0\",\"totalAmount\":\"20\",\"dedicatedFileName\":\"A00000000300037561\",\"acquirerName\":\"CB\",\"icc\":{\"debugData\":\"\",\"sequenceNumber\":\"0\",\"applicationId\":\"appID01\",\"terminalVerificationResults\":\"terminal01\",\"expectedApplicationTransactionCounter\":\"expTransCount01\",\"cryptogramInformationData\":\"cryptoInfo01\"}},\"merchant\":{\"name\":\"CarteBancaire\",\"address\":\"maddress01\",\"postCode\":\"12345678\",\"phoneNumbers\":\"+61253456793\",\"country\":\"UZB\",\"poiId\":\"aaccvv23\",\"merchantId\":\"222229999999999\"}}";
    }

}