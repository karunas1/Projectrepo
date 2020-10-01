/**********************************************************************
 * The Mail class is related to handle  	                           *
 * the model data from the request.                                    *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 26/04/2019                                                   *
 **********************************************************************/
package com.verifone.receipt.model;

public class Mail {

    private String from;
    private String to;
    private String subject;
    private String content;
    private String imageName;
    private boolean isProcessEmail;

    public Mail() {
    }

    public Mail(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isProcessEmail() {
        return isProcessEmail;
    }

    public void setProcessEmail(boolean isProcessEmail) {
        this.isProcessEmail = isProcessEmail;
    }
}