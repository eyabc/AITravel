package com.aitravel.api.user;

import com.aitravel.api.user.dto.UserRegisterRequest;
import com.aitravel.api.user.dto.UserResponse;
import com.aitravel.user.entity.User;
import com.aitravel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public Mono<ResponseEntity<UserResponse>> register(@Valid @RequestBody UserRegisterRequest request) {
    // 비동기 처리는 나중에 Mono.fromCallable 등으로 감쌀 수 있음
    User user = userService.register(request.getEmail(), request.getPassword(), request.getLanguage());
    return Mono.just(ResponseEntity.ok(UserResponse.from(user)));
  }
}
