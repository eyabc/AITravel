package com.aitravel.api.auth;

import com.aitravel.api.dto.LoginRequest;
import com.aitravel.user.entity.User;
import com.aitravel.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    userRepository.save(User.register(
      com.aitravel.user.entity.value.Email.of("test@example.com"),
      com.aitravel.user.entity.value.Password.fromRaw("password123"),
      com.aitravel.user.entity.value.PreferredLanguage.from("en")
    ));
  }

  @Nested
  @DisplayName("로그인 및 인증 API 통합 테스트")
  class LoginAndAuthTests {

    @Test
    @DisplayName("성공: 로그인 → 토큰 발급 → 인증 API 호출 성공")
    void 로그인_성공_후_인증_성공() {
      // 로그인 요청 → 토큰 추출
      AtomicReference<String> tokenRef = new AtomicReference<>();

      webTestClient.post()
        .uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new LoginRequest("test@example.com", "password123"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.token").value(token -> {
          assertThat(token).isInstanceOf(String.class);
          tokenRef.set((String) token);
        });

      String token = tokenRef.get();

      // 토큰으로 보호된 API 호출
      webTestClient.get()
        .uri("/api/v1/protected")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .exchange()
        .expectStatus().isOk();
    }

    @Test
    @DisplayName("실패: 잘못된 토큰 → 인증 실패")
    void 잘못된_토큰_인증_실패() {
      webTestClient.get()
        .uri("/api/v1/protected")
        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token.here")
        .exchange()
        .expectStatus().isUnauthorized();
    }
  }

}
