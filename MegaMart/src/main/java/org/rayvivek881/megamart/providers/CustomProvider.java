package org.rayvivek881.megamart.providers;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.services.UserServices;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomProvider implements AuthenticationProvider {
  private final UserServices userServices;
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails userDetails = userServices.loadUserByUsername(authentication.getName());
    if (userDetails.getPassword().equals(authentication.getCredentials().toString())) {
      return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }
    throw new AuthenticationException("Invalid credentials") { };
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.equals(authentication);
  }
}
