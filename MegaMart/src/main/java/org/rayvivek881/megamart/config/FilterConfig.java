package org.rayvivek881.megamart.config;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.filters.Customfilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

  private final Customfilter customfilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
        .addFilterAt(customfilter, BasicAuthenticationFilter.class);
    return  httpSecurity.build();
  }
}