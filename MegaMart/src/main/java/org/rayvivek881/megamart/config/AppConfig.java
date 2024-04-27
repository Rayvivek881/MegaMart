package org.rayvivek881.megamart.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(CharSequence charSequence) {
        return charSequence.toString();
      }
      @Override
      public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
      }
    };

  }

  @Bean
  public Cloudinary cloudinary() {
    String cloudinary_url = "cloudinary://414231947285516:cXKCkatIk00-VpFAitPSYlwrMXw@dvclzi87c";
    return new Cloudinary(cloudinary_url);
  }
}