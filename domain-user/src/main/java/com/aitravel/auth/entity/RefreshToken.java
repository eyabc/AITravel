package com.aitravel.auth.entity;

import com.aitravel.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long userId;

  @Column(nullable = false, length = 512)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiration;

  public RefreshToken(Long userId, String token, LocalDateTime expiration) {
    this.userId = userId;
    this.token = token;
    this.expiration = expiration;
  }

  public boolean isExpired() {
    return expiration.isBefore(LocalDateTime.now());
  }

  public void updateToken(String newToken, LocalDateTime newExpiration) {
    this.token = newToken;
    this.expiration = newExpiration;
  }
}
