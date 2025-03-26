package com.example.sixt.validators.phone;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "app.phone")
@Getter
@Setter
public class PhoneFormatProperties {

  private Map<String, PhoneFormatConfig> formats;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class PhoneFormatConfig {

    private String code;
    private int minLength;
    private int maxLength;
    private String regex;
  }
}
