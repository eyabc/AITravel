package com.aitravel.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/protected")
public class AuthProtectedController {

  @GetMapping
  public String protectedHello() {
    return "Hello, you are authenticated!";
  }
}
