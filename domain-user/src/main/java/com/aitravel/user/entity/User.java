package com.aitravel.user.entity;

import com.aitravel.common.domain.BaseTimeEntity;
import com.aitravel.user.entity.value.Email;
import com.aitravel.user.entity.value.Password;
import com.aitravel.user.entity.value.PreferredLanguage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 생성자
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private Email email;

  @Embedded
  private Password password;

  @Embedded
  private PreferredLanguage preferredLanguage;

  // ✅ 도메인 생성 메서드 (정적 팩토리)
  public static User register(Email email, Password password, PreferredLanguage language) {
    User user = new User();
    user.email = email;
    user.password = password;
    user.preferredLanguage = language;
    user.createdAt = LocalDateTime.now();
    user.updatedAt = user.createdAt;
    return user;
  }

  // ✅ 도메인 행위: 비밀번호 검증
  public boolean authenticate(String rawPassword) {
    return this.password.matches(rawPassword);
  }

  // ✅ 도메인 행위: 언어 변경
  public void changeLanguage(PreferredLanguage language) {
    this.preferredLanguage = language;
    this.updatedAt = LocalDateTime.now();
  }

}
