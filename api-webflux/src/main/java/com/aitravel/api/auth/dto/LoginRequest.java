package com.aitravel.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter

public class LoginRequest {

  @Email
  private String email;

  @NotBlank
  private String password;
}
