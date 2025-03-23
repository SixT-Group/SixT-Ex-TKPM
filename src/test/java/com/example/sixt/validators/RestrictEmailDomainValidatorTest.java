package com.example.sixt.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.sixt.validators.email.RestrictEmailDomain;
import com.example.sixt.validators.email.RestrictEmailDomainValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RestrictEmailDomainValidatorTest {

  @Mock
  private RestrictEmailDomain annotation;

  @Mock
  private ConstraintValidatorContext context;

  @InjectMocks
  private RestrictEmailDomainValidator validator;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(validator, "allowedDomain", "@student.university.edu.vn");
    when(annotation.allowedDomain()).thenReturn("@student.university.edu.vn");
    validator.initialize(annotation);
  }

  @Test
  void testValidEmail() {
    assertTrue(validator.isValid("user@student.university.edu.vn", context));
  }

  @Test
  void testInvalidEmail() {
    assertFalse(validator.isValid("gay@gay.com,", context));
  }
}