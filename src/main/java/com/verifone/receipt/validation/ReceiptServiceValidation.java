/**********************************************************************
 * The ReceiptServiceValidation class is related to configure all the  *
 * data as well as bussiness validation.                               *
 *                                                                     *
 * @author Snehasish Das                                              *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                 *
 **********************************************************************/
package com.verifone.receipt.validation;

import com.verifone.api.receipt.model.*;
import com.verifone.cegp.security.StringUtil;
import com.verifone.receipt.constant.ReceiptConstants;
import com.verifone.receipt.enums.CountryCodeEnum;
import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.exception.InvalidTemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.verifone.receipt.constant.ReceiptConstants.*;

@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public class ReceiptServiceValidation {

    @Value("${receipt.file.location}")
    private String fileLocation;

    public void validateRequireField(ReceiptTransaction receiptTransaction, SendReceipt sendReceipt,
                                     boolean isProcessReceipt) throws InvalidTemplateDataException, InvalidTemplateException {
        if (isProcessReceipt) {
            vaidateReceiptTransaction(receiptTransaction);
        } else {
            validateSendReceipt(sendReceipt);
        }
    }

    private void validateSendReceipt(SendReceipt sendReceipt) throws InvalidTemplateDataException {
        if (StringUtil.isNullOrEmpty(sendReceipt.getTo())) {
            throw new InvalidTemplateDataException("Email Address Can't be empty");
        }
        if (StringUtil.isNullOrEmpty(sendReceipt.getContent())) {
            throw new InvalidTemplateDataException("Content Can't be empty");
        }
        if (StringUtil.isNullOrEmpty(sendReceipt.getSubject())) {
            throw new InvalidTemplateDataException("Subject Can't be empty");
        }
        if (StringUtil.isNullOrEmpty(sendReceipt.getFromName())) {
            throw new InvalidTemplateDataException("From Name Can't be empty");
        }
    }

    private void vaidateReceiptTransaction(ReceiptTransaction receiptTransaction) throws InvalidTemplateDataException, InvalidTemplateException {
        if (StringUtil.isNullOrEmpty(receiptTransaction.getTo())) {
            throw new InvalidTemplateDataException("Email Address Can't be empty");
        }
        if (StringUtil.isNullOrEmpty(receiptTransaction.getTemplateId())) {
            throw new InvalidTemplateException("Template Id Can't be empty");
        }
        if (StringUtil.isNullOrEmpty(receiptTransaction.getFromName())) {
            throw new InvalidTemplateDataException("From Name Can't be empty");
        }
        if (receiptTransaction.getTransaction() == null) {
            throw new InvalidTemplateDataException("Transaction object Can't be empty");
        }
        if (receiptTransaction.getMerchant() == null) {
            throw new InvalidTemplateDataException("Merchant object Can't be empty");
        }
        if (receiptTransaction.getMetadata() == null) {
            throw new InvalidTemplateDataException("Meta Data object Can't be empty");
        }
    }

    public void validateFields(ReceiptTransaction receiptTransaction, SendReceipt sendReceipt, boolean isProcessReceipt)
            throws InvalidTemplateDataException, InvalidTemplateException {
        String emailAddress = isProcessReceipt ? receiptTransaction.getTo() : sendReceipt.getTo();
        if (!isValidEmailAddress(emailAddress)) {
            throw new InvalidTemplateDataException(emailAddress + " invalid Email Address");
        }
        if (isProcessReceipt) {
            processValidation(receiptTransaction);
        }
    }

    private boolean isValidEmailAddress(String address) {
        int pos = address.indexOf('@');
        String domain = null;
        boolean valid = true;
        try {
            // If the address does not contain an '@', it's not valid
            if (pos == -1) {
                throw new EmailServiceException("Address is not valid!");
            }
            /*
            if ("yes".equalsIgnoreCase(domainValidation)) {
                domain = address.substring(++pos);
                InetAddress.getByName(domain);
            }
            if ("yes".equalsIgnoreCase(accountValidation)) {
                domain = address.substring(++pos);
                ArrayList mxList = null;
                mxList = checkDomain(domain);
                // Just because we can send mail to the domain, doesn't mean that the
                // address is valid, but if we can't, it's a sure sign that it isn't
                if (mxList.isEmpty()) {
                    return false;
                }
                // Now, do the SMTP validation, try each mail exchanger until we get
                // a positive acceptance. It *MAY* be possible for one MX to allow
                // a message [store and forwarder for example] and another [like
                // the actual mail server] to reject it. This is why we REALLY ought
                // to take the preference into account.
                for (int mx = 0; mx < mxList.size(); mx++) {
                    int res;
                    try (Socket skt = new Socket((String) mxList.get(mx), TWO_FIVE)) {
                        BufferedReader rdr = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                        BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));
                        res = hear(rdr);
                        validateSenderAddress(address, res, rdr, wtr);
                        rdr.close();
                        wtr.close();
                    }
                }
            }

             */
        } catch (Exception ex) {
            valid = false;
        }
        return valid;
    }

    /*
    private void validateSenderAddress(String address, int res, BufferedReader rdr, BufferedWriter wtr) throws EmailServiceException, IOException {
        if (res != TWO_TWENTY) {
            throw new EmailServiceException("Invalid header");
        }
        say(wtr, "EHLO orbaker.com");
        res = hear(rdr);
        if (res != TWO_FIFTY) {
            throw new EmailServiceException("Not ESMTP");
        }
        // validate the sender address
        say(wtr, "MAIL FROM: <tim@orbaker.com>");
        res = hear(rdr);
        if (res != TWO_FIFTY) {
            throw new EmailServiceException("Sender rejected");
        }
        say(wtr, "RCPT TO: <" + address + ">");
        res = hear(rdr);
        // be polite
        say(wtr, "RSET");
        hear(rdr);
        say(wtr, "QUIT");
        hear(rdr);
        if (res != TWO_FIFTY) {
            throw new EmailServiceException("Address is not valid!");
        }
    }

    private static ArrayList checkDomain(String hostName) throws NamingException {
        // Perform a DNS lookup for MX records in the domain
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes(hostName, new String[]{"MX"});
        Attribute attr = attrs.get("MX");
        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attrs = ictx.getAttributes(hostName, new String[]{"A"});
            attr = attrs.get("A");
            if (attr == null)
                throw new NamingException("No match for name '" + hostName + "'");
        }
        // Huzzah! we have machines to try. Return them as an array list
        // NOTE: We SHOULD take the preference into account to be absolutely
        // correct. This is left as an exercise for anyone who cares.
        ArrayList res = new ArrayList();
        NamingEnumeration en = attr.getAll();
        while (en.hasMore()) {
            String x = (String) en.next();
            String[] f = x.split(" ");
            if (f[1].endsWith("."))
                f[1] = f[1].substring(0, (f[1].length() - 1));
            res.add(f[1]);
        }
        return res;
    }

    private static int hear(BufferedReader in) throws IOException {
        String line = null;
        int res = 0;
        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, THREE);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(THREE) != '-')
                break;
        }
        return res;
    }

    private static void say(BufferedWriter wr, String text) throws IOException {
        wr.write(text + "\r\n");
        wr.flush();
    }

     */
    private void processValidation(ReceiptTransaction receiptTransaction)
            throws InvalidTemplateException, InvalidTemplateDataException {
        Merchant merchant = receiptTransaction.getMerchant();
        Transaction transaction = receiptTransaction.getTransaction();
        Metadata metadata = receiptTransaction.getMetadata();
        if (!new File(fileLocation + "templates/" + receiptTransaction.getTemplateId() + ".ftl").exists()) {
            throw new InvalidTemplateException("Template Id Doesn't Exist");
        }
        // Merchant related field Validation
        String phoneNumber = null;
        if (merchant != null) {
            if (Objects.nonNull(merchant.getPhoneNumbers())) {
                phoneNumber = merchant.getPhoneNumbers().replaceAll("\\s+", "");
            }
            String merchantName = merchant.getName();
            String address = merchant.getAddress();
            String postCode = merchant.getPostCode();
            String country = null;
            if (Objects.nonNull(merchant.getCountry())) {
                country = merchant.getCountry().name();
            }
            String poiId = merchant.getPoiId();
            String merchantId = merchant.getMerchantId();
            String merchantCity = merchant.getCity();
            String url = merchant.getURL();
            String regex = "^[a-zA-Z0-9]+$"; // Accept only alha-numeric
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher;
            if (!StringUtil.isNullOrEmpty(phoneNumber) && (!(phoneNumber.length() >= ReceiptConstants.RECEIPT_TEXT_MIN_PHONE_WIDTH
                    && phoneNumber.length() <= ReceiptConstants.RECEIPT_TEXT_MAX_PHONE_WIDTH))) {

                throw new InvalidTemplateDataException(phoneNumber + SizeLimitValidation.SIZE_IN_BETWEEN
                        + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MIN_PHONE_WIDTH
                        + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_PHONE_WIDTH);

            }

            if (!StringUtil.isNullOrEmpty(merchantName)
                    && merchantName.length() > ReceiptConstants.RECEIPT_TEXT_MAX_NAME) {
                throw new InvalidTemplateDataException(
                        merchantName + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_NAME);
            }

            if (!StringUtil.isNullOrEmpty(address) && address.length() > ReceiptConstants.RECEIPT_TEXT_MAX_NAME) {
                throw new InvalidTemplateDataException(
                        address + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_NAME);
            }

            if (!StringUtil.isNullOrEmpty(merchantCity) && (merchantCity.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                    || merchantCity.length() > ReceiptConstants.RECEIPT_TEXT_MAX_CITY_LENGTH)) {
                throw new InvalidTemplateDataException(
                        merchantCity + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_CITY_LENGTH);
            }

            if (!StringUtil.isNullOrEmpty(postCode)) {
                if (!(postCode.length() >= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                        && postCode.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MAX_SIZE)) {
                    throw new InvalidTemplateDataException(
                            postCode + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MAX_SIZE);
                }
                matcher = pattern.matcher(postCode);
                if ((postCode.length() >= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                        && postCode.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MAX_SIZE)
                        && !matcher.matches()) {
                    throw new InvalidTemplateDataException(
                            postCode + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }
            }

            if (!StringUtil.isNullOrEmpty(country) && CountryCodeEnum.fromValue(country) == null) {
                throw new InvalidTemplateDataException(
                        country + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
            }

            if (!StringUtil.isNullOrEmpty(poiId)) {
                if ((poiId.length() < ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                        || poiId.length() > ReceiptConstants.RECEIPT_TEXT_MAX_POIID)) {
                    throw new InvalidTemplateDataException(
                            poiId + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_POIID);

                } else if (!StringUtil.isNullOrEmpty(poiId)) {
                    matcher = pattern.matcher(poiId);
                    if (!matcher.matches()) {
                        throw new InvalidTemplateDataException(
                                poiId + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                    }

                }
            }

            if (StringUtil.isNullOrEmpty(merchantId) || (!StringUtil.isNullOrEmpty(merchantId)
                    && (merchantId.length() < ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                    || merchantId.length() > ReceiptConstants.RECEIPT_TEXT_MID_SIZE))) {
                throw new InvalidTemplateDataException(
                        merchantId + SizeLimitValidation.SIZE_IN_BETWEEN
                                + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MID_SIZE);
            } else if (!StringUtil.isNullOrEmpty(merchantId)) {
                matcher = pattern.matcher(merchantId);
                if (!matcher.matches()) {
                    throw new InvalidTemplateDataException(
                            merchantId + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }
            }

            if (!StringUtil.isNullOrEmpty(url) && (url.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                    || url.length() > ReceiptConstants.RECEIPT_TEXT_MAX_NAME)) {
                throw new InvalidTemplateDataException(
                        url + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_NAME);
            }

            if (metadata != null) {
                String cryptoType = metadata.getCryptoType();
                String txnApprovalText = metadata.getTxnApprovalText();
                String receiptCopyType = metadata.getReceiptCopyType();
                if (!StringUtil.isNullOrEmpty(cryptoType) && (cryptoType.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                        || cryptoType.length() > ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH)) {
                    throw new InvalidTemplateDataException(
                            cryptoType + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH);
                }

                if (!StringUtil.isNullOrEmpty(txnApprovalText) && (txnApprovalText.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                        || txnApprovalText.length() > ReceiptConstants.RECEIPT_TEXT_MAX_LINE_WIDTH)) {
                    throw new InvalidTemplateDataException(
                            txnApprovalText + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_LINE_WIDTH);
                }

                if (!StringUtil.isNullOrEmpty(receiptCopyType) && (receiptCopyType.length() <= ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                        || receiptCopyType.length() > ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH)) {
                    throw new InvalidTemplateDataException(
                            receiptCopyType + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MIN_SIZE
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH);
                }
            }

            if (transaction != null) {
                Double maxValue = Double.valueOf("99999999999999999999999999999999");
                String accountType = String.valueOf(transaction.getAccountType());
                String acquirerName = transaction.getAcquirerName();
                Double amount = Double.valueOf(transaction.getAmount());
                String applicationPreferredName = transaction.getApplicationPreferredName();
                String authorisationCode = transaction.getAuthorisationCode();
                String cardBrand = transaction.getCardBrand();
                String fundingSource = null;
                if (Objects.nonNull(transaction.getFundingSource())) {
                    fundingSource = transaction.getFundingSource().toString();
                }
                Double cashbackAmount = null;
                if (Objects.nonNull(transaction.getCashbackAmount()) && !transaction.getCashbackAmount().isEmpty()) {
                    cashbackAmount = Double.valueOf(transaction.getCashbackAmount());
                }
                String entryMode = null;
                if (Objects.nonNull(transaction.getEntryMode())) {
                    entryMode = String.valueOf(transaction.getEntryMode());
                }
                Double feeAmount = null;
                if (Objects.nonNull(transaction.getFeeAmount()) && !transaction.getFeeAmount().isEmpty()) {
                    feeAmount = Double.valueOf(transaction.getFeeAmount());
                }
                Double gratuityAmount = null;
                if (Objects.nonNull(transaction.getGratuityAmount()) && !transaction.getGratuityAmount().isEmpty()) {
                    gratuityAmount = Double.valueOf(transaction.getGratuityAmount());
                }
                String initiatorTraceId = transaction.getInitiatorTraceId();
                String maskedCardNumber = transaction.getMaskedCardNumber();
                String responseCode = null;
                if (Objects.nonNull(transaction.getResponseCode())) {
                    responseCode = transaction.getResponseCode();
                }
                Double totalAmount = null;
                if (Objects.nonNull(transaction.getTotalAmount()) && !transaction.getTotalAmount().isEmpty()) {
                    totalAmount = Double.valueOf(transaction.getTotalAmount());
                }

                String currencyCode = null;
                if (Objects.nonNull(transaction.getCurrencyCode())) {
                    currencyCode = String.valueOf(transaction.getCurrencyCode());
                }

                String merchantReference = transaction.getMerchantReference();
                ICC icc = transaction.getIcc();

                if (StringUtil.isNullOrEmpty(accountType)
                        || (!StringUtil.isNullOrEmpty(accountType) && AccountType.fromValue(accountType) == null)) {
                    throw new InvalidTemplateDataException(
                            accountType + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (!StringUtil.isNullOrEmpty(acquirerName)
                        && acquirerName.length() > ReceiptConstants.RECEIPT_TEXT_MAX_LINE_WIDTH) {
                    throw new InvalidTemplateDataException(
                            acquirerName + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_LINE_WIDTH);
                }

                if (amount == null || (amount != null && (amount < 0.0 || amount > maxValue
                        || transaction.getAmount().length() > ReceiptConstants.RECEIPT_TEXT_AMOUNT_SIZE))) {
                    throw new InvalidTemplateDataException(
                            transaction.getAmount() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (!StringUtil.isNullOrEmpty(applicationPreferredName) && (applicationPreferredName.length() > ReceiptConstants.RECEIPT_TEXT_POST_CODE_MAX_SIZE)) {

                    throw new InvalidTemplateDataException(applicationPreferredName + SizeLimitValidation.SPACE
                            + SizeLimitValidation.SIZE_GREATER_THAN + ReceiptConstants.RECEIPT_TEXT_POST_CODE_MAX_SIZE);

                }

                if (!StringUtil.isNullOrEmpty(authorisationCode)
                        && authorisationCode.length() > ReceiptConstants.RECEIPT_TEXT_AUTH_CODE_LENGTH) {
                    throw new InvalidTemplateDataException(
                            authorisationCode + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_AUTH_CODE_LENGTH);
                }

                if (!StringUtil.isNullOrEmpty(cardBrand)
                        && cardBrand.length() > ReceiptConstants.RECEIPT_TEXT_MAX_LINE_WIDTH) {
                    throw new InvalidTemplateDataException(
                            cardBrand + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_LINE_WIDTH);
                }

                if (!StringUtil.isNullOrEmpty(fundingSource) && FundingSource.fromValue(fundingSource) == null) {
                    throw new InvalidTemplateDataException(
                            fundingSource + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (cashbackAmount != null && (cashbackAmount < 0.0 || cashbackAmount > maxValue
                        || transaction.getCashbackAmount().length() > ReceiptConstants.RECEIPT_TEXT_AMOUNT_SIZE)) {
                    throw new InvalidTemplateDataException(
                            transaction.getCashbackAmount() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (StringUtil.isNullOrEmpty(entryMode) || CardDetailsEntryModeEnum.fromValue(entryMode) == null) {
                    throw new InvalidTemplateDataException(
                            entryMode + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (feeAmount != null && (feeAmount < 0.0 || feeAmount > maxValue
                        || transaction.getFeeAmount().length() > ReceiptConstants.RECEIPT_TEXT_AMOUNT_SIZE)) {
                    throw new InvalidTemplateDataException(
                            transaction.getFeeAmount() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (gratuityAmount != null && (gratuityAmount < 0.0 || gratuityAmount > maxValue
                        || transaction.getGratuityAmount().length() > ReceiptConstants.RECEIPT_TEXT_AMOUNT_SIZE)) {
                    throw new InvalidTemplateDataException(
                            transaction.getGratuityAmount() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (StringUtil.isNullOrEmpty(initiatorTraceId)
                        || initiatorTraceId.length() > ReceiptConstants.RECEIPT_TEXT_MAX_STAN) {
                    throw new InvalidTemplateDataException(
                            initiatorTraceId + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_STAN);
                }

                if (StringUtil.isNullOrEmpty(maskedCardNumber) || (!StringUtil.isNullOrEmpty(maskedCardNumber)
                        && (maskedCardNumber.length() < ReceiptConstants.RECEIPT_TEXT_MIN_LINE_WIDTH
                        || maskedCardNumber.length() > ReceiptConstants.RECEIPT_TEXT_MAX_CARD_LENGTH))) {
                    throw new InvalidTemplateDataException(
                            maskedCardNumber + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MIN_LINE_WIDTH
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_CARD_LENGTH);
                }

                if (StringUtil.isNullOrEmpty(responseCode) || responseCode.length() < RECEIPT_TEXT_RESPONSE_CODE_SIZE
                || responseCode.length() > RECEIPT_TEXT_MIN_LINE_WIDTH ) {
                    throw new InvalidTemplateDataException(
                            responseCode + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_IN_BETWEEN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_RESPONSE_CODE_SIZE
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MIN_LINE_WIDTH);
                }

                if (totalAmount == null || (totalAmount < 0.0 || totalAmount > maxValue
                        || transaction.getTotalAmount().length() > ReceiptConstants.RECEIPT_TEXT_AMOUNT_SIZE)) {
                    throw new InvalidTemplateDataException(
                            transaction.getTotalAmount() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (!StringUtil.isNullOrEmpty(currencyCode) && CurrencyCodeEnum.fromValue(currencyCode) == null) {
                    throw new InvalidTemplateDataException(
                            currencyCode + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }

                if (!StringUtil.isNullOrEmpty(merchantReference)
                        && merchantReference.length() > ReceiptConstants.RECEIPT_TEXT_MAX_MER_SIZE) {
                    throw new InvalidTemplateDataException(
                            merchantReference + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                    + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_MER_SIZE);
                }

                if (icc != null) {
                    String applicationId = icc.getApplicationId();
                    String cryptogramInformationData = icc.getCryptogramInformationData();
                    String debugData = icc.getDebugData();
                    String expectedApplicationTransactionCounter = icc.getExpectedApplicationTransactionCounter();
                    String sequenceNumber = icc.getSequenceNumber();
                    String terminalVerificationResults = icc.getTerminalVerificationResults();

                    if (!StringUtil.isNullOrEmpty(applicationId)
                            && applicationId.length() > ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH) {
                        throw new InvalidTemplateDataException(
                                applicationId + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                        + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH);
                    }

                    if (!StringUtil.isNullOrEmpty(cryptogramInformationData)
                            && cryptogramInformationData.length() > ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH) {
                        throw new InvalidTemplateDataException(cryptogramInformationData + SizeLimitValidation.SPACE
                                + SizeLimitValidation.SIZE_GREATER_THAN + SizeLimitValidation.SPACE
                                + ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH);
                    }

                    if (!StringUtil.isNullOrEmpty(debugData)
                            && debugData.length() > ReceiptConstants.RECEIPT_TEXT_MAX_BUFFER_SIZE) {
                        throw new InvalidTemplateDataException(
                                debugData + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                        + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_MAX_BUFFER_SIZE);
                    }

                    if (!StringUtil.isNullOrEmpty(expectedApplicationTransactionCounter)
                            && expectedApplicationTransactionCounter
                            .length() > ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH) {
                        throw new InvalidTemplateDataException(expectedApplicationTransactionCounter
                                + SizeLimitValidation.SPACE + SizeLimitValidation.SIZE_GREATER_THAN
                                + SizeLimitValidation.SPACE + ReceiptConstants.RECEIPT_TEXT_ERROR_MSG_LENGTH);
                    }

                    if (!StringUtil.isNullOrEmpty(sequenceNumber)
                            && sequenceNumber.length() > ReceiptConstants.RECEIPT_TEXT_SEQUENCE_NUMBER_SIZE) {
                        throw new InvalidTemplateDataException(sequenceNumber + SizeLimitValidation.SPACE
                                + SizeLimitValidation.SIZE_GREATER_THAN + SizeLimitValidation.SPACE
                                + ReceiptConstants.RECEIPT_TEXT_SEQUENCE_NUMBER_SIZE);
                    }

                    if (!StringUtil.isNullOrEmpty(terminalVerificationResults)
                            && terminalVerificationResults.length() != ReceiptConstants.RECEIPT_TEXT_TRML_LENGTH) {
                        throw new InvalidTemplateDataException(terminalVerificationResults + SizeLimitValidation.SPACE
                                + SizeLimitValidation.SIZE_EQUAL_TO + SizeLimitValidation.SPACE
                                + ReceiptConstants.RECEIPT_TEXT_TRML_LENGTH);
                    }
                }
            }
        }
    }
}