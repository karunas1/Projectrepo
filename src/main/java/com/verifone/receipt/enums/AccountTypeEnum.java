package com.verifone.receipt.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountTypeEnum {
	
	DEFAULT("DEFAULT"),
	CREDIT("CREDIT"),
	GIFT("GIFT"),
	DEBIT("DEBIT"),
	SAVING("SAVING"),
	EBT("EBT"),
	CHEQUE("CHEQUE"),
	CURRENT("CURRENT"),
	PRIVATE_LABEL("PRIVATE_LABEL");
	

	private String value;

	AccountTypeEnum(String value) {
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
	   public static AccountTypeEnum fromValue(String text) {
	      for (AccountTypeEnum b : AccountTypeEnum.values()) {
	         if (String.valueOf(b.value).equals(text)) {
	            return b;
	         }
	      }
	      return null;
	   }
}
