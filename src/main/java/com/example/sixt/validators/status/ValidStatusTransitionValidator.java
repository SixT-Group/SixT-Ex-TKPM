package com.example.sixt.validators.status;

import com.example.sixt.enums.StudentStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@Component
public class ValidStatusTransitionValidator implements 
    ConstraintValidator<ValidStatusTransition, String> {

    private final Map<StudentStatus, Set<StudentStatus>> statusTransitionRules;
    private StudentStatus currentStatus;

    @Autowired
    public ValidStatusTransitionValidator(Map<StudentStatus, Set<StudentStatus>> statusTransitionRules) {
        this.statusTransitionRules = statusTransitionRules;
    }

    @Override
    public void initialize(ValidStatusTransition constraintAnnotation) {
        this.currentStatus = StudentStatus.valueOf(constraintAnnotation.currentStatus());
    }

    @Override
    public boolean isValid(String newStatus, ConstraintValidatorContext context) {
        if (newStatus == null) {
            return false;
        }

        try {
            StudentStatus newStatusEnum = StudentStatus.valueOf(newStatus);
            Set<StudentStatus> allowedTransitions = statusTransitionRules.get(currentStatus);
            
            if (allowedTransitions == null || !allowedTransitions.contains(newStatusEnum)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    String.format("Cannot transition from %s to %s", currentStatus, newStatus)
                ).addConstraintViolation();
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("Invalid status: %s", newStatus)
            ).addConstraintViolation();
            return false;
        }
    }
}