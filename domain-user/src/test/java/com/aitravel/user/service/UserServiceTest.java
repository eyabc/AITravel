package com.aitravel.user.service;


import com.aitravel.user.entity.User;
import com.aitravel.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void 사용자_등록_테스트() {
    UserService userService = new UserService(userRepository);

    User user = userService.register("test@example.com", "password123", "en");

    assertThat(user.getId()).isNotNull();
    assertThat(user.getEmail().getValue()).isEqualTo("test@example.com");
    assertThat(user.getPreferredLanguage().toString()).isEqualTo("en");
  }
}
