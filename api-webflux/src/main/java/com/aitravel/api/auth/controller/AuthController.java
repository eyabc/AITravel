package com.aitravel.api.auth.controller;

import com.aitravel.api.auth.dto.RefreshTokenRequest;
import com.aitravel.api.auth.dto.TokenResponse;
import com.aitravel.api.auth.service.AuthService;
import com.aitravel.api.dto.LoginRequest;
import com.aitravel.api.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
    String token = authService.login(request.getEmail(), request.getPassword());
    return Mono.just(ResponseEntity.ok(new LoginResponse(token)));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
    TokenResponse tokenResponse = authService.reissue(request);
    return ResponseEntity.ok(tokenResponse);
  }
}
