package com.example.sixt.validators.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.config.validate.ValidationException;

@Component
public class RestrictPhoneFormatValidator implements
    ConstraintValidator<RestrictPhoneFormat, String> {

  private final PhoneFormatProperties phoneFormatProperties;
  private String selectedCountry;

  @Autowired
  public RestrictPhoneFormatValidator(PhoneFormatProperties phoneFormatProperties) {
    this.phoneFormatProperties = phoneFormatProperties;
  }

  @Override
  public void initialize(RestrictPhoneFormat annotation) {
    this.selectedCountry = annotation.country();
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    try {
      if (phoneNumber == null) {
        return true;
      }

      PhoneFormatProperties.PhoneFormatConfig config = phoneFormatProperties.getFormats().get(selectedCountry);
      if (config == null) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Unsupported country: " + selectedCountry)
            .addConstraintViolation();
        return false;
      }

      String normalizedPhone = phoneNumber.replaceAll("[\\s-]", "");

      if (config.getRegex() != null && !config.getRegex().isEmpty()) {
        return normalizedPhone.matches(config.getRegex());
      }

      boolean startsWithCountryCode = normalizedPhone.startsWith(config.getCode());
      boolean startsWithZero = normalizedPhone.startsWith("0");

      if (!startsWithCountryCode && !startsWithZero) {
        return false;
      }

      String internationalFormat;
      if (startsWithZero && config.getCode().startsWith("+")) {
        internationalFormat = config.getCode() + normalizedPhone.substring(1);
      } else {
        internationalFormat = normalizedPhone;
      }

      int expectedLength = config.getMaxLength();
      return internationalFormat.length() == expectedLength &&
          internationalFormat.substring(config.getCode().length()).matches("\\d+");
    } catch (Exception e) {
      System.err.println("Error during phone number validation: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }
}
