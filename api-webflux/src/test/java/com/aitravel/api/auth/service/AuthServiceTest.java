package com.aitravel.api.auth.service;

import com.aitravel.api.auth.dto.TokenResponse;
import com.aitravel.api.jwt.JwtTokenProvider;
import com.aitravel.auth.service.RefreshTokenService;
import com.aitravel.user.entity.User;
import com.aitravel.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DisplayName("AuthService 리프레시 토큰 재발급 테스트")
class AuthServiceTest {

  @Autowired
  AuthService authService;
  @Autowired
  UserService userService;
  @Autowired
  RefreshTokenService refreshTokenService;
  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @Nested
  @DisplayName("reissueToken()")
  class ReissueToken {

    @Test
    @DisplayName("성공: 유효한 RefreshToken으로 AccessToken 재발급")
    void 성공_재발급() {
      // given
      String email = "user@test.com";
      String password = "secure123";
      User user = userService.register(email, password, "en");

      TokenResponse tokenResponse = authService.login(email, password);

      // when
      TokenResponse response = authService.reissueToken(tokenResponse.refreshToken());

      // then
      assertThat(response.accessToken()).isNotNull();
      assertThat(response.refreshToken()).isNotEqualTo(tokenResponse.refreshToken()); // 새로운 토큰 발급됨
    }

    @Test
    @DisplayName("실패: 잘못된 형식의 토큰")
    void 실패_형식불일치() {
      // given
      String malformedToken = "invalid.token.here";

      // when & then
      assertThatThrownBy(() -> authService.reissueToken(malformedToken))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid Refresh Token");
    }

    @Test
    @DisplayName("실패: 저장된 토큰과 일치하지 않음")
    void 실패_불일치() {
      // given
      String email = "test2@invalid.com";
      String password = "secretsecret";
      User user = userService.register(email, password, "en");

      TokenResponse tokenResponse = authService.login(email, password);

      String fakeToken = jwtTokenProvider.generateRefreshToken(user.getId());

      // when & then
      assertThatThrownBy(() -> authService.reissueToken(fakeToken))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Refresh Token not found");
    }
  }
}
