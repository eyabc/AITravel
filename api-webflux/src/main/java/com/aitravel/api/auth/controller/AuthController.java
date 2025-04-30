package com.aitravel.api.auth.controller;

import com.aitravel.api.auth.dto.RefreshTokenRequest;
import com.aitravel.api.auth.dto.TokenResponse;
import com.aitravel.api.auth.service.AuthService;
import com.aitravel.api.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public Mono<ResponseEntity<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
    TokenResponse tokenResponse = authService.login(request.getEmail(), request.getPassword());
    return Mono.just(ResponseEntity.ok(tokenResponse));
  }

  @PostMapping("/token/refresh")
  public Mono<ResponseEntity<TokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
    TokenResponse tokenResponse = authService.reissueToken(request.refreshToken());
    return Mono.just(ResponseEntity.ok(tokenResponse));
  }
}
