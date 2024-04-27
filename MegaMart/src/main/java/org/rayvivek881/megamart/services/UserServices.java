package org.rayvivek881.megamart.services;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.implementations.CustomUserDetails;
import org.rayvivek881.megamart.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return CustomUserDetails.builder().user(user).build();
  }

  public User save(User user) {
    User preUser = userRepository.findByUsername(user.getUsername()).orElse(null);
    if (preUser != null) {
      throw new RuntimeException("User already exists");
    }
    return userRepository.save(user);
  }

  public User updateRoles(String username, List<String> roles) throws RuntimeException {
    String adminUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userRepository.findByUsername(adminUsername).orElse(null);

    if (user == null || !user.getRoles().contains("ADMIN")) {
      throw new RuntimeException("You Are Not Authorise to Do this operation");
    }
    user = userRepository.findByUsername(username).
        orElseThrow(() -> new UsernameNotFoundException("User not found"));

    user.setRoles(roles);
    return userRepository.save(user);
  }

  public User getUserByUserId(String userId) {
    return userRepository.findById(userId).orElse(null);
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }
}
