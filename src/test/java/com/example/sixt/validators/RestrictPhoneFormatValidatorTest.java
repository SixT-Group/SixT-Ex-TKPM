package com.example.sixt.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.example.sixt.validators.phone.PhoneFormatProperties;
import com.example.sixt.validators.phone.RestrictPhoneFormat;
import com.example.sixt.validators.phone.RestrictPhoneFormatValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RestrictPhoneFormatValidatorTest {

  @Mock
  private PhoneFormatProperties phoneFormatProperties;

  @Mock
  private ConstraintValidatorContext context;

  @InjectMocks
  private RestrictPhoneFormatValidator validator;

  @Mock
  private ConstraintViolationBuilder builder;

  @Mock
  private RestrictPhoneFormat annotation;

  @BeforeEach
  public void setUp() {
    Map<String, PhoneFormatProperties.PhoneFormatConfig> formats = Map.of("vn",
        new PhoneFormatProperties.PhoneFormatConfig("+84", 10, 12,
            "^(\\+84|0)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$"));

    when(phoneFormatProperties.getFormats()).thenReturn(formats);
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    when(builder.addConstraintViolation()).thenReturn(context);

    when(annotation.country()).thenReturn("vn");
    validator.initialize(annotation);
  }

  @Test
  public void testValidVNPhoneNumbeGlobally() {
    assertTrue(validator.isValid("+84858717878", context));
  }
  @Test
  public void testValidVNPhoneNumberLocally() {
    assertTrue(validator.isValid("0858717878", context));
  }

  @Test
  public void testInValidVNPhoneNumber_case01() {
    assertFalse(validator.isValid("031222", context));
  }
  @Test
  public void testInValidVNPhoneNumber_case02() {
    assertFalse(validator.isValid("085871787a", context));
  }
}
