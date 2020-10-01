package com.verifone.receipt.validation;

public class SizeLimitValidation {

    private SizeLimitValidation() {

    }

    public static final String INVALID_API = "Receipt Service Invalid Request or Data";
    public static final String INVALID_TEMPLATE = "Receipt Service Load Template Error";
    public static final String INVALID_EMAIL_PROCESS = "Receipt Service Unable to Send Email";
    public static final String UNKNOWN_ERROR = "Receipt Service Internal Error";
    public static final String SUCCESS_CODE = "Success";
    static final String SIZE_GREATER_THAN = "Characters should not be greater than ";
    public static final String INVALID = "Invalid";
    public static final String SPACE = " ";
    static final String SIZE_EQUAL_TO = " Characters should be equal to ";
    static final String SIZE_IN_BETWEEN = " Characters size in between ";
}