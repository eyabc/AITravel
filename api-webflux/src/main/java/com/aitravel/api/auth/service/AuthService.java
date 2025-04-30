package com.aitravel.api.auth.service;

import com.aitravel.api.auth.dto.RefreshTokenRequest;
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


  public String login(String email, String rawPassword) {
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

    return accessToken;

  }

  public TokenResponse reissue(RefreshTokenRequest request) {
    String refreshToken = request.refreshToken();

    if (!jwtTokenProvider.isValidToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid Refresh Token");
    }

    Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

    RefreshToken savedToken = refreshTokenService.findByUserId(userId)
      .orElseThrow(() -> new IllegalArgumentException("Refresh Token not found"));

    if (!savedToken.getToken().equals(refreshToken)) {
      throw new IllegalArgumentException("Refresh Token mismatch");
    }

    // 🔄 새 RefreshToken 생성 및 기존 토큰 교체
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
    LocalDateTime expiresAt = jwtTokenProvider.getRefreshTokenExpiration();
    refreshTokenService.rotateToken(userId, newRefreshToken, expiresAt);

    // 🔐 새 AccessToken 발급
    String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

    return new TokenResponse(newAccessToken, newRefreshToken);
  }

}
