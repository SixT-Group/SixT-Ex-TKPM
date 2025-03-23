package com.example.sixt.validators.email;


import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = RestrictEmailDomainValidator.class)
public @interface RestrictEmailDomain {

  String allowedDomain() default "@student.university.edu.vn";

  String message() default "Email must belong to the allowed domain";

  Class<?>[] groups() default {};

  Class<? extends jakarta.validation.Payload>[] payload() default {};
}
