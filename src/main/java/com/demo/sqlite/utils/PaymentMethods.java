package com.demo.sqlite.utils;

public enum PaymentMethods {
   CASH("CASH"), VISA("VISA"), MASTERCARD("MASTERCARD"), PAYPAL("PAYPAL");

   private final String value;

   PaymentMethods(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }

   public static boolean isValid(String value) {
      for (PaymentMethods current : PaymentMethods.values()) {
         if (current.getValue().equalsIgnoreCase(value)) {
            return true;
         }
      }
      return false;
   }

}
