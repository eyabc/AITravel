package com.aitravel.api.auth.service;

import com.aitravel.api.controller.dto.LoginRequest;
import com.aitravel.api.controller.exception.InvalidEmailException;
import com.aitravel.api.controller.exception.InvalidPasswordException;
import com.aitravel.api.controller.jwt.JwtTokenProvider;
import com.aitravel.api.controller.service.AuthService;
import com.aitravel.auth.service.RefreshTokenService;
import com.aitravel.user.entity.User;
import com.aitravel.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthControllerTest {

  private final UserService userService = mock(UserService.class);
  private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
  private final RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);
  private final AuthService authService = new AuthService(userService, jwtTokenProvider, refreshTokenService);


  @Nested
  @DisplayName("로그인 테스트")
  class LoginTest {

    @Test
    @DisplayName("성공: 올바른 이메일과 비밀번호로 로그인")
    void 로그인_성공() {
      // given
      String mail = "test@example.com";
      String password123 = "password123";
      User user = mock(User.class);

      when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
      when(user.authenticate(anyString())).thenReturn(true);
      when(jwtTokenProvider.generateAccessToken(anyLong())).thenReturn("fake-jwt-token");

      // when
      String token = authService.login(mail, password123);

      // then
      assertThat(token).isEqualTo("fake-jwt-token");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 이메일")
    void 로그인_이메일_없음() {
      // given
      String mail = "wrong@example.com";
      String password123 = "password123";

      when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> authService.login(mail, password123))
        .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    @DisplayName("실패: 비밀번호 불일치")
    void 로그인_비밀번호_틀림() {
      // given
      String mail = "test@example.com";
      String wrongpassword = "wrongpassword";
      LoginRequest loginRequest = new LoginRequest(mail, wrongpassword);
      User user = mock(User.class);

      when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
      when(user.authenticate(anyString())).thenReturn(false);

      // when & then
      assertThatThrownBy(() -> authService.login(mail, wrongpassword))
        .isInstanceOf(InvalidPasswordException.class);
    }
  }
}
