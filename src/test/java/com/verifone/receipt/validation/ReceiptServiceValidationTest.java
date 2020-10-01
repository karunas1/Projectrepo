package com.verifone.receipt.validation;

import com.verifone.api.receipt.model.*;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.exception.InvalidTemplateException;
import com.verifone.receipt.util.MockData;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;

public class ReceiptServiceValidationTest {
    ReceiptServiceValidation receiptServiceValidation = new ReceiptServiceValidation();
    SendReceipt sendReceipt = MockData.prepareSendReceiptObject();
    public void fileLocation() {
        ReflectionTestUtils.setField(receiptServiceValidation, "fileLocation", "src/test/resources/");
    }

    public void accountLoc() {
        ReflectionTestUtils.setField(receiptServiceValidation, "accountValidation", "Yes");
    }

    public void domainLoc() {
        ReflectionTestUtils.setField(receiptServiceValidation, "domainValidation", "Yes");
    }


    @Test(expectedExceptions = InvalidTemplateException.class)
    public void testValidateRequireField1() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        receiptTxn.setTemplateId("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField2() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        receiptTxn.setFromName("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField3() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        receiptTxn.setFromName("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField4() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        receiptTxn.setTransaction(null);
        receiptServiceValidation.validateRequireField(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField5() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        receiptTxn.setMerchant(null);
        receiptServiceValidation.validateRequireField(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField6() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        receiptTxn.setMetadata(null);
        receiptServiceValidation.validateRequireField(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField7() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        sendRecpt.setTo("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendRecpt, false);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField8() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        sendRecpt.setSubject("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendRecpt, false);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField9() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        sendRecpt.setContent("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendRecpt, false);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateRequireField10() throws InvalidTemplateException, InvalidTemplateDataException {
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        sendRecpt.setFromName("");
        receiptServiceValidation.validateRequireField(receiptTxn, sendRecpt, false);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsCountryNonNull() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Merchant merchant = new Merchant();
        merchant.setCountry(CountryCode3Enum.BEN);
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerPhoneNonNull() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPhoneNumbers("1234567890");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerPhoneMaxLength() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPhoneNumbers("12345678901234");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsPhoneInvalid() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPhoneNumbers("12345678a1234");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantNameMaxLength() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setName("verifoneverifoneverifoneverifoneverifoneverifoneverifoneverifoneverifoneverifoneverifoneverifoneverifone");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantAddressLenMax() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setAddress("BangaloreBangaloreBangaloreBangalore");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantCityMaxLen() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setCity("BangaloreBangaloreBangaloreBangalore");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantPostCodeMaxLen() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPostCode("BangaloreBangalore");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantPoiMaxLen() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPoiId("123456789");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantPoiInvalid() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPoiId("12345@78");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantMaxLen() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setMerchantId("1234567812345678");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantRefMaxLen() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setMerchantId("123456781234567812345678901234567812345678123456789");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsPoiInvalid() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPoiId("12345@78");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsostCode() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPostCode("12345678123456789");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsostCodeInvalid() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setPostCode("1234567$81234567");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsEntryModeNN() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        transaction.setEntryMode(CardDetailsEntryModeEnum.MAG_STRIPE);
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsAppPrefNameNN() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        transaction.setApplicationPreferredName("jsadgajdafjagdjsagjfgsajdbvmfdsajg");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsAmount() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsCashBackAmount() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        transaction.setCashbackAmount("1000.90");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMerchantInvalid() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Merchant merchant = new Merchant();
        merchant.setMerchantId("1234@#78");
        receiptTxn.setMerchant(merchant);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsFeeAmount() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        transaction.feeAmount("250.90");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldscardBrand() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("2400.90");
        transaction.setCardBrand("viasamastervisamasretcardvisa");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsAcqMaxLen() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("2400.90");
        transaction.setAcquirerName("viasamastervisamasretcardvisa");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsEntryMode() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        transaction.setEntryMode(CardDetailsEntryModeEnum.fromValue(null));
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsMaskedCard() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("240.90");
        transaction.setMaskedCardNumber(null);
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFieldsAmountMax() throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        Transaction transaction = new Transaction();
        transaction.setAmount("9999999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendReceipt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.getIcc().setSequenceNumber("1234");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields2()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.getIcc().setTerminalVerificationResults("1234");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields3()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.getIcc().setExpectedApplicationTransactionCounter("123456789012345678901234567890123");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields4()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.getIcc().setDebugData("1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231" +
                "1234567890123456789012345678901231234567890123456789012345678901231234567890123456789012345678901231");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields5()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.getIcc().setCryptogramInformationData("123456789012345678901234567890123");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields6()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.getIcc().setApplicationId("123456789012345678901234567890123");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields9()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setTotalAmount("999999999999999999999999999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields10()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setResponseCode("12345");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields11()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setInitiatorTraceId("12345678901234567890123456789012345678901");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields12()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setGratuityAmount("999999999999999999999999999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields13()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setFeeAmount("999999999999999999999999999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields14()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setEntryMode(null);
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields15()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setCashbackAmount("999999999999999999999999999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields16()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setCardBrand("INVALID CARD BRAND TRANSACTION");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields17()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setAuthorisationCode("999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields18()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setApplicationPreferredName("Invalid ApplicationPreferredName");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields19()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setAmount("999999999999999999999999999999999");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields20()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setAcquirerName(" Invalid Acquirer Name   ");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields21()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        Transaction transaction = MockData.prepareTransactionObject();
        transaction.setMaskedCardNumber("1234567890123456789011");
        receiptTxn.setTransaction(transaction);
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields22()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.getMetadata().setCryptoType("Invalid crypto type value placed to test");
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields23()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.getMetadata().setTxnApprovalText("Invalid Txn type value placed to test");
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields24()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.getMetadata().setReceiptCopyType("Invalid Receipt type value placed to test");
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields25()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.getMerchant().setURL("Invalid URL value placed to test Invalid URL value placed to test " +
                "Invalid URL value placed to test Invalid URL value placed to test");
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields26()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.getMerchant().setPoiId("1234567890123");
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    @Test(expectedExceptions = InvalidTemplateDataException.class)
    public void testValidateFields27()throws InvalidTemplateException, InvalidTemplateDataException {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.getMerchant().setAddress("Invalid Address value placed to test Invalid URL value placed to test Invalid Address value placed to test Invalid URL value placed to test");
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    
    public void testValidateFieldsDomainValid() throws Exception {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.setTo("sourabh.kesharwani811@gmail.com");
        domainLoc();
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

    
    public void testValidateFieldsAccountValid() throws Exception {
        fileLocation();
        ReceiptTransaction receiptTxn= MockData.prepareReceiptTransactionObject();
        SendReceipt sendRecpt = MockData.prepareSendReceiptObject();
        receiptTxn.setTo("sourabh.kesharwani811@gmail.com");
        accountLoc();
        receiptServiceValidation.validateFields(receiptTxn, sendRecpt, true);
    }

}

