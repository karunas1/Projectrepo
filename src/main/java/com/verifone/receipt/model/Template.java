package com.verifone.receipt.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.InputStream;


@Document
public class Template {
    private String title;
    private InputStream inputStream;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getTitle() {
        return title;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}


