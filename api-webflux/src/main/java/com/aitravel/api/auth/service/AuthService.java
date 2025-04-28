package com.aitravel.api.auth.service;

import com.aitravel.api.auth.dto.RefreshTokenRequest;
import com.aitravel.api.auth.dto.TokenResponse;
import com.aitravel.api.controller.jwt.JwtTokenProvider;
import com.aitravel.auth.entity.RefreshToken;
import com.aitravel.auth.service.RefreshTokenService;
import com.aitravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;

  public TokenResponse reissue(RefreshTokenRequest request) {
    String refreshToken = request.refreshToken();

    // 1. Refresh Token 검증
    if (!jwtTokenProvider.isValidToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid Refresh Token");
    }

    // 2. Refresh Token으로 User 찾기
    Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
    RefreshToken savedToken = refreshTokenService.findByUserId(userId)
      .orElseThrow(() -> new IllegalArgumentException("Refresh Token not found"));

    if (!savedToken.getToken().equals(refreshToken)) {
      throw new IllegalArgumentException("Refresh Token mismatch");
    }

    // 3. Access Token 재발급
    String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

    // (선택) 4. Refresh Token이 만료 임박이면, 새로 발급 (생략 가능)

    return new TokenResponse(newAccessToken, refreshToken);
  }
}
