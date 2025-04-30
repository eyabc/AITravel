package com.aitravel.auth.service;

import com.aitravel.auth.entity.RefreshToken;
import com.aitravel.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public void save(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

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

  public Optional<RefreshToken> findByUserId(Long userId) {
    return refreshTokenRepository.findByUserId(userId);
  }

  public void deleteByUserId(Long userId) {
    refreshTokenRepository.deleteByUserId(userId);
  }

  @Transactional
  public RefreshToken rotateToken(Long userId, String newToken, LocalDateTime expiresAt) {
    RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
      .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

    refreshToken.updateToken(newToken, expiresAt);
    return refreshToken;
  }

}
