package com.verifone.receipt.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionTypeEnum {

	PREAUTH("PREAUTH"),
	EXTEND_TRANSACTION("EXTEND_TRANSACTION"),
	SALE("SALE"),
	NON_FINANCIAL("NON_FINANCIAL"),
	REFUND("REFUND"),
	PREAUTH_INCREMENT("PREAUTH_INCREMENT"),
	LOAD("LOAD"),
	UNLOAD("UNLOAD"),
	NO_SHOW("NO_SHOW"),
	BALANCE("BALANCE"),
	CARD_DEACTIVATION("CARD_DEACTIVATION"),
	CASH_ADVANCE("CASH_ADVANCE"),
	CARD_ACTIVATION("CARD_ACTIVATION"),
	CASH_DEPOSIT("CASH_DEPOSIT"),
	AUTHORISATION("AUTHORISATION"),
	CANCEL("CANCEL"),
	COMPLETION("COMPLETION"),
	DELAYED_CHARGE("DELAYED_CHARGE"),
	VOID("VOID"),
	CARD_VERIFICATION("CARD_VERIFICATION");
	
	private String value;

	TransactionTypeEnum(String value) {
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
	   public static TransactionTypeEnum fromValue(String text) {
	      for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
	         if (String.valueOf(b.value).equals(text)) {
	            return b;
	         }
	      }
	      return null;
	   }
}
