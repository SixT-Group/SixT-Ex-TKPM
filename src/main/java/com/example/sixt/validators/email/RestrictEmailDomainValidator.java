package com.example.sixt.validators.emails;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestrictEmailDomainValidator implements
    ConstraintValidator<RestrictEmailDomain, String> {

  @Value("${app.allowed.email.domain:@student.university.edu.vn}")
  private String allowedDomain;

  @Override
  public void initialize(RestrictEmailDomain annotation) {
    if (!annotation.allowedDomain().equals("@student.university.edu.vn")) {
      this.allowedDomain = annotation.allowedDomain();
    }
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (email == null) {
      return false;
    }
    log.info("Got email: {}", email);
    log.info("Allowed domain: {}", allowedDomain);
    return email.endsWith(allowedDomain);
  }
}
