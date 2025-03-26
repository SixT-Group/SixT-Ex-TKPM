package com.example.sixt.validators.status;

import com.example.sixt.enums.StudentStatus;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidStatusTransitionValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Mock
    private ValidStatusTransition annotation;

    private ValidStatusTransitionValidator validator;
    private Map<StudentStatus, Set<StudentStatus>> statusTransitionRules;

    @BeforeEach
    void setUp() {
        statusTransitionRules = new HashMap<>();
        statusTransitionRules.put(StudentStatus.STUDYING, new HashSet<>(Arrays.asList(
            StudentStatus.TEMPORARILY_SUSPENDED,
            StudentStatus.GRADUATED,
            StudentStatus.DROPPED_OUT
        )));
        statusTransitionRules.put(StudentStatus.TEMPORARILY_SUSPENDED, new HashSet<>(Arrays.asList(
            StudentStatus.STUDYING,
            StudentStatus.DROPPED_OUT
        )));
        statusTransitionRules.put(StudentStatus.DROPPED_OUT, new HashSet<>());
        statusTransitionRules.put(StudentStatus.GRADUATED, new HashSet<>());

        validator = new ValidStatusTransitionValidator(statusTransitionRules);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    @Test
    @DisplayName("Should validate valid status transition")
    void isValid_ValidTransition_ReturnsTrue() {
        // Arrange
        when(annotation.currentStatus()).thenReturn("STUDYING");
        validator.initialize(annotation);

        // Act & Assert
        assertTrue(validator.isValid("TEMPORARILY_SUSPENDED", context));
        assertTrue(validator.isValid("GRADUATED", context));
        assertTrue(validator.isValid("DROPPED_OUT", context));
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should reject invalid status transition")
    void isValid_InvalidTransition_ReturnsFalse() {
        // Arrange
        when(annotation.currentStatus()).thenReturn("GRADUATED");
        validator.initialize(annotation);

        // Act & Assert
        assertFalse(validator.isValid("STUDYING", context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(
            "Cannot transition from GRADUATED to STUDYING"
        );
        verify(builder).addConstraintViolation();
    }

    @Test
    @DisplayName("Should reject null status")
    void isValid_NullStatus_ReturnsFalse() {
        // Arrange
        when(annotation.currentStatus()).thenReturn("STUDYING");
        validator.initialize(annotation);

        // Act & Assert
        assertFalse(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Should reject non-existent current status")
    void isValid_NonExistentCurrentStatus_ReturnsFalse() {
        // Arrange
        when(annotation.currentStatus()).thenReturn("NON_EXISTENT");
        validator.initialize(annotation);

        // Act & Assert
        assertFalse(validator.isValid("STUDYING", context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(
            "Invalid status: STUDYING"
        );
        verify(builder).addConstraintViolation();
    }
} 