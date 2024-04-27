package org.rayvivek881.megamart.controllers;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.services.UserServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

  private final UserServices userServices;

  @GetMapping
  public String Home() {
    return "Welcome to the Basic Security home page";
  }

  @PostMapping
  public User create(@RequestBody User user) {
    return userServices.save(user);
  }

  @GetMapping("/all")
  public List<User> findAll() {
    return userServices.findAll();
  }
}
