package com.verifone.receipt.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseCodeEnum {

	SUCCESS("0000"),
    TXN_REFUSED_BEFORE_SENDING_TO_ACQ("9200"),
	TXN_REFUSED_AFTER_SENDING_TO_ACQ("9201");

	private String value;

	ResponseCodeEnum(String value) {
	      this.value = value;
	   }

	   @JsonValue
	   public String getValue() {
	      return value;
	   }

	   @Override
	   public String toString() {
	      return String.valueOf(value);
	   }

	   @JsonCreator
	   public static ResponseCodeEnum fromValue(String text) {
	      for (ResponseCodeEnum b : ResponseCodeEnum.values()) {
	         if (String.valueOf(b.value).equals(text)) {
	            return b;
	         }
	      }
	      return null;
	   }
}
