package com.aitravel.api.auth.service;

import com.aitravel.api.auth.dto.TokenResponse;
import com.aitravel.api.exception.InvalidEmailException;
import com.aitravel.api.exception.InvalidPasswordException;
import com.aitravel.api.jwt.JwtTokenProvider;
import com.aitravel.auth.entity.RefreshToken;
import com.aitravel.auth.service.RefreshTokenService;
import com.aitravel.user.entity.User;
import com.aitravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  public TokenResponse login(String email, String rawPassword) {
    User user = userService.findByEmail(email)
      .orElseThrow(() -> new InvalidEmailException("User not found: " + email));

    if (!user.authenticate(rawPassword)) {
      throw new InvalidPasswordException("Invalid password");
    }

    // ✅ AccessToken + RefreshToken 발급
    String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
    String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

    // ✅ RefreshToken 저장 (DB)
    refreshTokenService.saveOrUpdate(
      user.getId(),
      refreshToken,
      jwtTokenProvider.getRefreshTokenExpiration()
    );

    return new TokenResponse(accessToken, refreshToken);

  }

  public TokenResponse reissueToken(String oldRefreshToken) {
    // 1. 토큰 유효성 검증
    if (!jwtTokenProvider.isValidToken(oldRefreshToken)) {
      throw new IllegalArgumentException("Invalid Refresh Token");
    }

    // 2. 토큰 기반 유저 식별
    Long userId = jwtTokenProvider.getUserIdFromToken(oldRefreshToken);

    // 3. 저장된 토큰과 일치 여부 확인
    RefreshToken saved = refreshTokenService.findByUserId(userId)
      .orElseThrow(() -> new IllegalArgumentException("Refresh Token not found"));

    if (!saved.getToken().equals(oldRefreshToken)) {
      throw new IllegalArgumentException("Token mismatch");
    }

    // 4. 새로운 토큰 발급 및 저장 (핵심)
    String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
    LocalDateTime expiry = jwtTokenProvider.getRefreshTokenExpiration();

    refreshTokenService.rotateToken(userId, newRefreshToken, expiry);

    return new TokenResponse(newAccessToken, newRefreshToken);
  }}
