package com.aitravel.auth.service;

import com.aitravel.auth.entity.RefreshToken;
import com.aitravel.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public void saveOrUpdate(Long userId, String token, LocalDateTime expiration) {
    refreshTokenRepository.findByUserId(userId)
      .ifPresentOrElse(
        refreshToken -> refreshToken.updateToken(token, expiration),
        () -> refreshTokenRepository.save(new RefreshToken(userId, token, expiration))
      );
  }

  public RefreshToken findByToken(String token) {
    return refreshTokenRepository.findByToken(token)
      .orElseThrow(() -> new IllegalArgumentException("Refresh Token not found"));
  }

  public void deleteByUserId(Long userId) {
    refreshTokenRepository.deleteByUserId(userId);
  }
}
