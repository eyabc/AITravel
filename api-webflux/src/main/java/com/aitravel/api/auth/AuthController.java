package com.aitravel.api.auth;


import com.aitravel.api.auth.dto.LoginRequest;
import com.aitravel.api.auth.dto.LoginResponse;
import com.aitravel.api.auth.service.AuthService;
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
  public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
    String token = authService.login(request.getEmail(), request.getPassword());
    return Mono.just(ResponseEntity.ok(new LoginResponse(token)));
  }
}
