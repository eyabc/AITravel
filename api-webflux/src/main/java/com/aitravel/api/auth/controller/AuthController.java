package com.aitravel.api.auth.controller;

import com.aitravel.api.auth.dto.RefreshTokenRequest;
import com.aitravel.api.auth.dto.TokenResponse;
import com.aitravel.api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
    TokenResponse tokenResponse = authService.reissue(request);
    return ResponseEntity.ok(tokenResponse);
  }
}
