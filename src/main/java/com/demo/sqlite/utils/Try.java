package com.demo.sqlite.utils;

import java.util.Optional;

public abstract class Try<T> {
   public abstract boolean isSuccess();

   public abstract boolean isFailure();

   public abstract T get();

   public abstract T getOrElse(T value);

   public abstract Optional<T> toOptional();

   public abstract Throwable getException();

   public static <T> Try<T> of(SupplierEx<T> supplier) {
      try {
         return new Success<>(supplier.get());
      } catch (Throwable t) {
         return new Failure<>(t);
      }
   }

   @FunctionalInterface
   public interface SupplierEx<T> {
      T get() throws Exception;
   }

   private static class Success<T> extends Try<T> {
      private final T value;

      public Success(T value) {
         this.value = value;
      }

      @Override
      public boolean isSuccess() {
         return true;
      }

      @Override
      public boolean isFailure() {
         return false;
      }

      @Override
      public T get() {
         return value;
      }

      @Override
      public T getOrElse(T newValue) {
         return this.value;
      }

      @Override
      public Optional<T> toOptional() {
         return Optional.ofNullable(value);
      }

      public Throwable getException() {
         throw new UnsupportedOperationException();
      }
   }

   private static class Failure<T> extends Try<T> {
      private final Throwable exception;

      public Failure(Throwable exception) {
         this.exception = exception;
      }

      @Override
      public boolean isSuccess() {
         return false;
      }

      @Override
      public boolean isFailure() {
         return true;
      }

      @Override
      public T get() {
         throw new UnsupportedOperationException();
      }

      @Override
      public T getOrElse(T newValue) {
         return newValue;
      }

      @Override
      public Optional<T> toOptional() {
         return Optional.empty();
      }

      @Override
      public Throwable getException() {
         return exception;
      }
   }
}
