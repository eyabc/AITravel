package com.aitravel.api.auth.service;

import com.aitravel.user.entity.User;
import com.aitravel.user.entity.value.Email;
import com.aitravel.user.entity.value.Password;
import com.aitravel.user.entity.value.PreferredLanguage;
import com.aitravel.user.repository.UserRepository;
import com.aitravel.user.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {


  private final UserRepository userRepository = mock(UserRepository.class);
  private final UserService userService = new UserService(userRepository);
  private final AuthService authService = new AuthService(userService);

  @Nested
  class 로그인_성공_케이스 {
    @Test
    void 로그인_성공_토큰_반환() {
      // given
      Email email = Email.of("test@example.com");
      Password password = Password.fromRaw("password123");
      PreferredLanguage language = PreferredLanguage.from("en");
      User mockUser = User.register(email, password, language);

      when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

      // when
      String token = authService.login("test@example.com", "password123");

      // then
      assertThat(token).startsWith("dummy-jwt-token");
    }

  }

  @Nested
  class 로그인_실패_케이스 {
    @Test
    void 로그인_실패_이메일없음() {
      when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

      assertThatThrownBy(() -> authService.login("no-user@example.com", "password123")).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("User not found");
    }

    @Test
    void 로그인_실패_비밀번호불일치() {
      Email email = Email.of("test@example.com");
      Password password = Password.fromRaw("correct-password");
      PreferredLanguage lang = PreferredLanguage.from("en");
      User user = User.register(email, password, lang);

      when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

      assertThatThrownBy(() -> authService.login("test@example.com", "wrong-password")).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Invalid password");
    }


  }

}
