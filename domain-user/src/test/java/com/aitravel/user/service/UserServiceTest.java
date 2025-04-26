package com.aitravel.user.service;


import com.aitravel.user.entity.User;
import com.aitravel.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.assertj.core.api.Assertions.assertThat;

@EntityScan(basePackages = "com.aitravel.user.entity")
@SpringBootTest
@EnableJpaRepositories(basePackages = "com.aitravel.user.repository")
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
