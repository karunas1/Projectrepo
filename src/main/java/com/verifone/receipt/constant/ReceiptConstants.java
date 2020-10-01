package com.verifone.receipt.constant;

public final class ReceiptConstants {

    private ReceiptConstants() {

    }

    public static final int RECEIPT_TEXT_POST_CODE_MIN_SIZE = 1;
    public static final int RECEIPT_TEXT_RESPONSE_CODE_SIZE = 2;
    public static final int RECEIPT_TEXT_COUNTRY_SIZE = 3;
    public static final int RECEIPT_TEXT_SEQUENCE_NUMBER_SIZE = 3;
    public static final int RECEIPT_TEXT_MIN_LINE_WIDTH = 4;
    public static final int RECEIPT_TEXT_AUTH_CODE_LENGTH = 8;
    public static final int RECEIPT_TEXT_TRML_LENGTH = 10;
    public static final int RECEIPT_TEXT_MAX_POIID = 12;
    public static final int RECEIPT_TEXT_MID_SIZE = 15;
    public static final int RECEIPT_TEXT_POST_CODE_MAX_SIZE = 16;
    public static final int RECEIPT_DATE_MIN_SIZE = 20;
    public static final int RECEIPT_TEXT_MAX_CARD_LENGTH = 21;
    public static final int RECEIPT_TEXT_MAX_LINE_WIDTH = 24;
    public static final int RECEIPT_TEXT_MAX_CITY_LENGTH = 28;
    public static final int RECEIPT_TEXT_ERROR_MSG_LENGTH = 32;
    public static final int RECEIPT_TEXT_AMOUNT_SIZE = 32;
    public static final int RECEIPT_TEXT_MAX_STAN = 40;
    public static final int RECEIPT_TEXT_MAX_MER_SIZE = 50;
    public static final int RECEIPT_DATE_MAX_SIZE = 64;
    public static final int RECEIPT_TEXT_MAX_BUFFER_SIZE = 1024;
    public static final int RECEIPT_TEXT_MIN_PHONE_WIDTH = 6;
    public static final int RECEIPT_TEXT_MAX_PHONE_WIDTH = 13;
    public static final int RECEIPT_TEXT_MAX_NAME = 100;
    public static final int SINC_RATE = 100;
    public static final int TWO_TWENTY = 220;
    public static final int TWO_FIFTY = 250;
    public static final float MIDDLE_VALUE = .5f;
    public static final int TWO_FIVE = 25;
    public static final int TWO_THOUSAND = 2000;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int SIXTEEN = 16;
    public static final String COMMA_DELIM = ",";
    public static final String KAFKA_ERR_MSG = "Error while sending message to kafka topic : ";
    public static final String HTML_FORMAT_ERR = "Error while formatting the HTML receipt ";
    public static final String TEMPLATE_FILE_TYPE = "templatefile";
    public static final String RECEIPT_FILE_TYPE = "receiptfile";
    public static final String FILE_TYPE_KEY = "fileType";
    public static final String FILE_NOT_EXIST = "File not exist in DB";
    public static final String FILE_NAME = "filename";
    public static final String TEMPLATES = "//templates//";
    public static final String CONTENT_TYPE = "text/plain";
    public static final String SLASH = "/";
    public static final String FALSE = "FALSE";
    public static final String UTF_CONTENT_TYPE ="text/html; charset=UTF-8";
}