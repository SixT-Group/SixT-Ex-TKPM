package com.example.sixt.validators.phone;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = RestrictPhoneFormatValidator.class)
public @interface RestrictPhoneFormat {

  String country() default "VN";

  String message() default "Invalid phone number format for the specified country";

  Class<?>[] groups() default {};

  Class<? extends jakarta.validation.Payload>[] payload() default {};
}