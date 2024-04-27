package org.rayvivek881.megamart.controllers;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.services.UserServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final UserServices userServices;

  @PostMapping("/register")
  public User register(@RequestBody User user) {
    return userServices.save(user);
  }

  @PostMapping("/login")
  public String login(@RequestBody User user) {
    return null;
  }
}
