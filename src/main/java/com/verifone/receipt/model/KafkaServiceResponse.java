package com.verifone.receipt.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"statusCode", "successMessageCount", "failureMessageCount", "status", "invalidRequest"})
public class KafkaServiceResponse {

    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("successMessageCount")
    private Integer successMessageCount;
    @JsonProperty("failureMessageCount")
    private Integer failureMessageCount;
    @JsonProperty("status")
    private String status;

    public KafkaServiceResponse() {
        //This constructor is used for creating object.
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

