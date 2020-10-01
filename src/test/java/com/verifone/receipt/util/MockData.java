package com.verifone.receipt.util;

import com.verifone.api.receipt.model.*;
import com.verifone.receipt.model.Mail;

import java.time.OffsetDateTime;

public class MockData {

    private MockData() {

    }

    private static String emailAddress = "rajeshwarim1@verifone.com";

    public static ReceiptTransaction prepareReceiptTransactionObject() {
        ReceiptTransaction receiptTransaction = new ReceiptTransaction();
        receiptTransaction.setFromName("CBA");
        receiptTransaction.setTemplateId("payment_cb");
        receiptTransaction.setTo(emailAddress);
        receiptTransaction.setSubject("CBA receipt for Merchant 1234");

        Merchant merchant = new Merchant();
        Metadata metadata = new Metadata();
        Transaction transaction = new Transaction();

        merchant.setMerchantId("123020000000000");
        merchant.setPoiId("aaccvv23");
        merchant.setAddress("maddress01");
        merchant.setCity("Bangalore");
        merchant.setName("CB");
        merchant.setPhoneNumbers("+61253456793");
        merchant.setPostCode("123456");
        merchant.setURL("http://example.com");
        receiptTransaction.setMerchant(merchant);


        metadata.setCryptoType("ARQC");
        metadata.setPsnAvailable(true);
        metadata.setReceiptCopyType("copy");
        metadata.setTxnApprovalText("APPROVED");
        metadata.setHeaderText("Header");
        metadata.setFooterText("Footer");
        receiptTransaction.setMetadata(metadata);

        transaction.setAccountType(AccountType.DEFAULT);
        transaction.setAmount("20000");
        transaction.setAcquirerName("CB");
        transaction.setAcquirerReference("CB Sale");
        transaction.setAuthorisationCode("auth01");
        transaction.setCardBrand("VISA");
        transaction.setCurrencyCode(CurrencyCodeEnum.EUR);
        transaction.setTransmittedDateTime(OffsetDateTime.parse("2020-06-16T14:04:47Z"));
        transaction.setEntryMode(CardDetailsEntryModeEnum.MAG_STRIPE);
        transaction.setErrorMessage("Error");
        transaction.setFundingSource(FundingSource.PREPAID);
        transaction.setCreatedDateTime(OffsetDateTime.parse("2020-06-16T14:04:47Z"));
        transaction.setTransactionType(TransactionType.SALE);
        transaction.setResponseCode("0000");
        transaction.setTotalAmount("8648");
        transaction.setGratuityAmount("78");
        transaction.setMaskedCardNumber("5500000000000000");
        transaction.setMerchantReference("7a1db7a8-6f24-4bc5-a51b-cef33fc05140");
        ICC icc = new ICC();
        icc.setApplicationId("appID01");
        icc.setTerminalVerificationResults("terminal01");
        icc.setCryptogramInformationData("cryptoInfo01");
        transaction.setIcc(icc);
        transaction.setInitiatorTraceId("123456789");
        receiptTransaction.setTransaction(transaction);

        return receiptTransaction;
    }

    public static SendReceipt prepareSendReceiptObject() {
        SendReceipt sendReceipt = new SendReceipt();
        sendReceipt.setFromName("CBA");
        sendReceipt.setTo(emailAddress);
        sendReceipt.setContent("PCFET0NUWVBFIGh0bWw+CjxodG1sPgogICA8aGVhZD4KICAgICAgPHN0eWxlPgogICAgICAgICAqIHsKICAgICAgICAgZm9udDoyMHB4IGFyaWFsLCBzYW5zLXNlcmlmOwogICAgICAgICB9CiAgICAgICAgIGJvZHkgewoJCQl3aWR0aDogMTc5cHg7CiAgICAgICAgIH0KICAgICAgICAgdGFibGUgewogICAgICAgICB3aWR0aDogMTAwJTsKICAgICAgICAgYm9yZGVyOiBub25lOwogICAgICAgICBib3JkZXItc3BhY2luZzogMDsKICAgICAgICAgfQogICAgICAgICAuZW12X3RhYmxlIHsKICAgICAgICAgdGFibGUtbGF5b3V0OiBmaXhlZDsKICAgICAgICAgfQogICAgICAgICB0ZCwgdGggewogICAgICAgICBib3JkZXI6IDBweCBzb2xpZCAjZGRkZGRkOwogICAgICAgICB2ZXJ0aWNhbC1hbGlnbjogdGV4dC10b3A7CiAgICAgICAgIH0KICAgICAgICAgLmVtdl9rZXkgewogICAgICAgICB3b3JkLXdyYXA6IGJyZWFrLXdvcmQ7CiAgICAgICAgIHdpZHRoOiAzMCU7CiAgICAgICAgIHRleHQtYWxpZ246IGxlZnQ7CiAgICAgICAgIH0KICAgICAgICAgLmVtdl92YWx1ZSB7CiAgICAgICAgIHdvcmQtd3JhcDogYnJlYWstd29yZDsKICAgICAgICAgdGV4dC1hbGlnbjogcmlnaHQ7CiAgICAgICAgIH0KICAgICAgICAgLnN1YnRpdGxlIHsKICAgICAgICAgZm9udDogMjhweDsKICAgICAgICAgdGV4dC1hbGlnbjogY2VudGVyOwogICAgICAgICB9CiAgICAgICAgIC5sZWZ0IHsKICAgICAgICAgd2lkdGg6IDUwJQogICAgICAgICB0ZXh0LWFsaWduOiBsZWZ0OwogICAgICAgICB3aGl0ZS1zcGFjZTogbm93cmFwOwogICAgICAgICB9CiAgICAgICAgIC5yaWdodCB7CiAgICAgICAgIHdpZHRoOiA1MCU7CiAgICAgICAgIHRleHQtYWxpZ246IHJpZ2h0OwogICAgICAgICB3b3JkLWJyZWFrOiBicmVhay1hbGw7CiAgICAgICAgIH0KICAgICAgICAgLmxlZnROb1dpZHRoIHsKICAgICAgICAgdGV4dC1hbGlnbjogbGVmdDsKICAgICAgICAgd2hpdGUtc3BhY2U6IG5vd3JhcDsKICAgICAgICAgfQogICAgICAgICAucmlnaHROb1dpZHRoIHsKICAgICAgICAgdGV4dC1hbGlnbjogcmlnaHQ7CiAgICAgICAgIHdvcmQtYnJlYWs6IGJyZWFrLWFsbDsKICAgICAgICAgfQogICAgICAgICAuY2VudGVyIHsKICAgICAgICAgdGV4dC1hbGlnbjogY2VudGVyOwogICAgICAgICB9CiAgICAgIDwvc3R5bGU+CiAgIDwvaGVhZD4KICAgPGJvZHk+CiAgICAgIDwhLS0gQnVzaW5lc3MgaW5mb3JtYXRpb24gc3RhcnQgLS0+CiAgICAgIDxkaXYgaWQ9ImJ1c2luZXNzSW5mbyI+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIEJ1c2luZXNzIGluZm9ybWF0aW9uIGVuZCAtLT48IS0tIEJlZm9yZSBncmVldGluZyBzdGFydCAtLT48IS0tIEVtcHR5IC0tPjwhLS0gQmVmb3JlIGdyZWV0aW5nIGVuZCAtLT48IS0tIEdyZWV0aW5nIHN0YXJ0IC0tPgogICAgICA8ZGl2IGlkPSJjdXN0b21HcmVldGluZyI+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIEdyZWV0aW5nIGVuZCAtLT48IS0tIEFmdGVyIGdyZWV0aW5nIHN0YXJ0IC0tPjwhLS0gRW1wdHkgLS0+PCEtLSBBZnRlciBncmVldGluZyBlbmQgLS0+PCEtLSBDYXNoaWVyIG5hbWUgc3RhcnQgLS0+PCEtLSBFbXB0eSAtLT48IS0tIENhc2hpZXIgbmFtZSBlbmQgLS0+PCEtLSBCZWZvcmUgdHJhbnNhY3Rpb24gc3RhcnQgLS0+PCEtLSBFbXB0eSAtLT48IS0tIEJlZm9yZSB0cmFuc2FjdGlvbiBlbmQgLS0+PCEtLSBUcmFuc2FjdGlvbiBpbmZvcm1hdGlvbiBzdGFydCAtLT4KICAgICAgPGRpdiBpZD0idHJhbnNhY3Rpb25JbmZvcm1hdGlvbiI+CiAgICAgIDxkaXYgaWQ9IlRSQU5TQUNUSU9OX1NFQ1RJT04iPgogICAgICA8IS0tIFRJVExFIC0tPgogICAgICA8ZGl2IGlkPSJzdWJ0aXRsZSIgY2xhc3M9ImNlbnRlciI+CiAgICAgICAgIENhcmRob2xkZXIncyByZWNlaXB0CiAgICAgIDwvZGl2PgogICAgICA8IS0tICBUUkFOU0FDVElPTiBJTkZPIC0tPgogICAgICA8ZGl2IGlkPSJ0cmFuc2FjdGlvbl90eXBlIiBjbGFzcz0ic3VidGl0bGUiPgogICAgICAgICA8c3Ryb25nPgogICAgICAgICBQVVJDSEFTRQogICAgICAgICA8L3N0cm9uZz4KICAgICAgPC9kaXY+CiAgICAgIDxoci8+CiAgICAgIDxici8+CiAgICAgIDwhLS0gTUVSQ0hBTlQgQlVTSU5FU1MgSU5GTyAtLT4KICAgICAgPHRhYmxlIGlkPSJtZXJjaGFudF9idXNpbmVzc19pbmZvIj4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMiI+VmVyaWZvbmU8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjb2xzcGFuPSIyIj4zMDAgUyBXYWNrZXI8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD4KICAgICAgICAgICAgICAgNjA2MDYKICAgICAgICAgICAgICAgQ2hpY2FnbwogICAgICAgICAgICA8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjb2xzcGFuPSIyIj5JbGxpbm9pczwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNvbHNwYW49IjIiPlVuaXRlZCBTdGF0ZXM8L3RkPgogICAgICAgICA8L3RyPgogICAgICA8L3RhYmxlPgogICAgICA8YnIvPgogICAgICA8dGFibGU+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5QaG9uZTo8L3RkPgogICAgICAgICAgICA8dGQgY2xhc3M9InJpZ2h0Ij4xLTU1NS01NTUtNTU1NTwvdGQ+CiAgICAgICAgIDx0ci8+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5CdXMuUmVnLk5vOjwvdGQ+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0icmlnaHQiPjwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDxici8+CiAgICAgIDx0YWJsZT4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY2xhc3M9ImxlZnQiPjMxLzAzLzIwMjA8L3RkPgogICAgICAgICAgICA8dGQgY2xhc3M9InJpZ2h0Ij4xMjozNjwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDxici8+CiAgICAgIDwhLS0gQU1PVU5UIFRBQkxFIC0tPgogICAgICA8dGFibGUgaWQ9ImFtb3VudCI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5QVVJDSEFTRTwvdGQ+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0icmlnaHQiPkdCUCAxLjIzPC90ZD4KICAgICAgICAgPC90cj4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMiI+CiAgICAgICAgICAgICAgIDxoci8+CiAgICAgICAgICAgIDwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij48c3Ryb25nPlRPVEFMPC9zdHJvbmc+PC90ZD4KICAgICAgICAgICAgPHRkIGNsYXNzPSJyaWdodCI+PHN0cm9uZz5HQlAgMS4yMzwvc3Ryb25nPjwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDxici8+CiAgICAgIDwhLS0gQ1ZNIC0tPgogICAgICA8dGFibGUgaWQ9ImN2bSI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkPgogICAgICAgICAgICAgICBDVk06CiAgICAgICAgICAgICAgIE5vQ1ZNCiAgICAgICAgICAgIDwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDwhLS0gQ0FSRCBOQU1FIC0tPgogICAgICA8dGFibGUgaWQ9ImNhcmRfbmFtZSI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5EZWJpdCBNYXN0ZXJDYXJkPC90ZD4KICAgICAgICAgICAgPHRkIGNsYXNzPSJyaWdodCI+UFNOOjAwPC90ZD4KICAgICAgICAgPHRyPgogICAgICA8L3RhYmxlPgogICAgICA8ZGl2IGlkPSJjYXJkX3RlY2giIGNsYXNzPSJsZWZ0Ij4KICAgICAgICAgQ09OVEFDVExFU1MgQ0hJUAogICAgICA8L2Rpdj4KICAgICAgPGRpdiBpZD0icGFuIiBjbGFzcz0ibGVmdCI+CiAgICAgICAgICoqKioqKioqKioqKjAwNTIKICAgICAgPC9kaXY+CiAgICAgIDx0YWJsZT4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMiI+VC1TL046NDAxLTg5Mi00OTM8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD4KICAgICAgICAgICAgICAgVElEOjYxNDAwOTM0CiAgICAgICAgICAgIDwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDwhLS0gQUNRVUlSRVIgTkFNRSAtLT4KICAgICAgPGRpdiBpZD0iYWNxdWlyZXJfbmFtZSI+CiAgICAgICAgIGFwaWd3CiAgICAgIDwvZGl2PgogICAgICA8ZGl2IGlkPSJtaWQiPgogICAgICAgICBNSUQ6MjYwNDU3CiAgICAgIDwvZGl2PgogICAgICA8dGFibGUgaWQ9ImF0Y19hZWQiPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0ibGVmdCI+QVRDOjBBQkE8L3RkPgogICAgICAgICAgICA8dGQgY2xhc3M9InJpZ2h0Ij5BRUQ6MTcwMjAxPC90ZD4KICAgICAgICAgPC90cj4KICAgICAgPC90YWJsZT4KICAgICAgPHRhYmxlIGlkPSJhaWQiPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD5BSUQ6QTAwMDAwMDAwNDEwMTA8L3RkPgogICAgICAgICA8L3RyPgogICAgICA8L3RhYmxlPgogICAgICA8ZGl2IGlkPSJ0YyI+CiAgICAgICAgIFRDOkFENjVFMjgyOUFBMTFGMTcKICAgICAgPC9kaXY+CiAgICAgIDx0YWJsZSBpZD0iYXJjX2F1dGhfY29kZSI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Tm9XaWR0aCI+QVJDOjwvdGQ+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0icmlnaHROb1dpZHRoIj5BVVRIIENPREU6MDA3OTAyPC90ZD4KICAgICAgICAgPC90cj4KICAgICAgPC90YWJsZT4KICAgICAgPHRhYmxlIGlkPSJyZWYiPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0ibGVmdCI+UkVGOjQ5PC90ZD4KICAgICAgICAgPC90cj4KICAgICAgPC90YWJsZT4KICAgICAgPHRhYmxlIGlkPSJ0dnJfdHNpIj4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY2xhc3M9ImxlZnQiPlRWUjowMDQwMDAwMDAxPC90ZD4KICAgICAgICAgICAgPHRkIGNsYXNzPSJyaWdodCI+VFNJOkU4MDA8L3RkPgogICAgICAgICA8L3RyPgogICAgICA8L3RhYmxlPgogICAgICA8YnIvPgogICAgICA8L3RhYmxlPgogICAgICA8IS0tIFRSQU5TQUNUSU9OIFJFU1VMVCAtLT4KICAgICAgPGRpdiBpZD0ibWVzc2FnZV9hcHByb3ZhbCIgY2xhc3M9InN1YnRpdGxlIj4KICAgICAgICAgPHN0cm9uZz4KICAgICAgICAgT05MSU5FIEFVVEhPUklaRUQKICAgICAgICAgMDAKICAgICAgICAgPC9zdHJvbmc+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIEFERElUSU9OQUwgTUVTU0FHRSAtLT4KICAgICAgPGRpdiBpZD0ibWVzc2FnZV9zdGF0dXMiIGNsYXNzPSJjZW50ZXIiPgogICAgICA8L2Rpdj4KICAgICAgPGJyLz4KICAgICAgPCEtLSBDVk0gU0lHTkFUVVJFIC0tPgogICAgICA8ZGl2IGlkPSJuZmkiIGNsYXNzPSJzdWJ0aXRsZSI+CiAgICAgICAgIDxkaXY+CiAgICAgICAgICAgIDxkaXYgaWQ9Im1lc3NhZ2VfcmV0YWluIiBjbGFzcz0iY2VudGVyIj5QTEVBU0UgUkVUQUlOIFJFQ0VJUFQ8L2Rpdj4KICAgICAgICAgPC9kaXY+CiAgICAgICAgIDwhLS0gRW1wdHkgLS0+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIFRyYW5zYWN0aW9uIGluZm9ybWF0aW9uIGVuZCAtLT48IS0tIEFmdGVyIHRyYW5zYWN0aW9uIHN0YXJ0IC0tPjwhLS0gRW1wdHkgLS0+PCEtLSBBZnRlciB0cmFuc2FjdGlvbiBlbmQgLS0+PCEtLSBCZWZvcmUgY3VzdG9tIGZvb3RlciBzdGFydCAtLT48IS0tIEVtcHR5IC0tPjwhLS0gQmVmb3JlIGN1c3RvbSBmb290ZXIgZW5kIC0tPjwhLS0gQ3VzdG9tIGZvb3RlciBzdGFydCAtLT48IS0tIEVtcHR5IC0tPjwhLS0gQ3VzdG9tIGZvb3RlciBlbmQgLS0+PCEtLSBBZnRlciBjdXN0b20gZm9vdGVyIHN0YXJ0IC0tPjwhLS0gRW1wdHkgLS0+PCEtLSBBZnRlciBjdXN0b20gZm9vdGVyIGVuZCAtLT48IS0tIEZvb3RlciBzdGFydCAtLT4KICAgICAgPGRpdiBpZD0iZm9vdGVyIj4KICAgICAgICAgPGRpdiBjbGFzcz0iZm9vdGVyIj4gRm9yIG1vcmUgaW5mb3JtYXRpb24gcmVmZXIgdG8gdGhlIEV4Y2hhbmdlcyBhbmQgUmVmdW5kcyBpbiBvdXIgPGI+UHVyY2hhc2UgQ29uZGl0aW9ucy48L2I+IFRoaXMgcmVjZWlwdCBpcyBlc3NlbnRpYWwgZm9yIHJldHVybmluZyBvciBleGNoYW5naW5nIGl0ZW1zLiBZb3UgY2FuIHNob3cgaXQgb24gdGhlIHNjcmVlbiBvZiB5b3VyIG1vYmlsZSBkZXZpY2Ugb3IgcHJpbnQgaXQuIDwvZGl2PgogICAgICA8L2Rpdj4KICAgICAgPCEtLSBGb290ZXIgZW5kIC0tPgogICA8L2JvZHk+CjwvaHRtbD4=");
        sendReceipt.setSubject("CB Receipt");

        return sendReceipt;
    }

    public static Mail prepareMailObject() {
        Mail mail = new Mail();
        mail.setContent("text/html");
        mail.setFrom("no-reply@verifone.com");
        mail.setTo(emailAddress);
        mail.setSubject("ABCD");
        mail.setProcessEmail(true);
        mail.setImageName("message");
        return mail;
    }

    public static Transaction prepareTransactionObject(){
        Transaction transaction = new Transaction();
        transaction.setAccountType(AccountType.DEFAULT);
        transaction.setAmount("20000");
        transaction.setAcquirerName("CB");
        transaction.setAcquirerReference("CB Sale");
        transaction.setAuthorisationCode("auth01");
        transaction.setCardBrand("VISA");
        transaction.setCurrencyCode(CurrencyCodeEnum.EUR);
        transaction.setTransmittedDateTime(OffsetDateTime.parse("2020-06-16T14:04:47Z"));
        transaction.setEntryMode(CardDetailsEntryModeEnum.MAG_STRIPE);
        transaction.setErrorMessage("Error");
        transaction.setFundingSource(FundingSource.PREPAID);
        transaction.setCreatedDateTime(OffsetDateTime.parse("2020-06-16T14:04:47Z"));
        transaction.setTransactionType(TransactionType.SALE);
        transaction.setResponseCode("0000");
        transaction.setTotalAmount("8648");
        transaction.setGratuityAmount("78");
        transaction.setMaskedCardNumber("5500000000000000");
        transaction.setMerchantReference("7a1db7a8-6f24-4bc5-a51b-cef33fc05140");
        ICC icc = new ICC();
        icc.setApplicationId("appID01");
        icc.setTerminalVerificationResults("terminal01");
        icc.setCryptogramInformationData("cryptoInfo01");
        icc.setSequenceNumber("123");
        transaction.setIcc(icc);
        transaction.setInitiatorTraceId("123456789");
        return transaction;
    }
}
