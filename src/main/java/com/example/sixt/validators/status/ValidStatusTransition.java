package com.example.sixt.validators.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidStatusTransitionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatusTransition {
    String message() default "Invalid status transition";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String currentStatus();
} 