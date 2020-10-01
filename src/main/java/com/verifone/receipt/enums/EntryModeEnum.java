package com.verifone.receipt.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EntryModeEnum {
	
	ICC_CONTACTLESS("ICC_CONTACTLESS"),
	ACCOUNT_DATA("ACCOUNT_DATA"),
	ICC("ICC"),
	BARCODE("BARCODE"),
	CARD_ON_FILE("CARD_ON_FILE"),
	MAG_STRIPE_CONTACTLESS("MAG_STRIPE_CONTACTLESS"),
	MANUAL("MANUAL"),
	TAG("TAG"),
	UNKNOWN("UNKNOWN"),
	MICR("MICR"),
	OCR("OCR"),
	MAG_STRIPE("MAG_STRIPE"),
	STORED_CREDENTIAL("STORED_CREDENTIAL");

	private String value;

	EntryModeEnum(String value) {
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
	   public static EntryModeEnum fromValue(String text) {
	      for (EntryModeEnum b : EntryModeEnum.values()) {
	         if (String.valueOf(b.value).equals(text)) {
	            return b;
	         }
	      }
	      return null;
	   }
}
