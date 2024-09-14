package com.demo.sqlite.utils;

import java.util.Arrays;

public enum Roles {
   ADMIN("ADMIN"), EMPLOYEE("EMPLOYEE"), CLIENT("CLIENT");

   private final String role;

   Roles(String role) {
      this.role = role;
   }

   public String getRole() {
      return role;
   }

   public String getRoleWithPrefix() {
      return "ROLE_" + role;
   }

   public static boolean isValid(String role) {
      for (Roles current : Roles.values()) {
         if (current.getRole().equalsIgnoreCase(role)) {
            return true;
         }
      }
      return false;
   }

}
