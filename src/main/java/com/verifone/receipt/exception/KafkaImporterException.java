package com.verifone.receipt.exception;

public class KafkaImporterException extends RuntimeException {

    private static final long serialVersionUID = 8579140106596183780L;

    public KafkaImporterException(final String message) {
        super(message);
    }

}
