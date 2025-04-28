package com.aitravel.api.controller.service;

import com.aitravel.api.controller.exception.InvalidEmailException;
import com.aitravel.api.controller.exception.InvalidPasswordException;
import com.aitravel.api.controller.jwt.JwtTokenProvider;
import com.aitravel.auth.service.RefreshTokenService;
import com.aitravel.user.entity.User;
import com.aitravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;


  public String login(String email, String rawPassword) {
    User user = userService.findByEmail(email)
      .orElseThrow(() -> new InvalidEmailException("User not found: " + email));

    if (!user.authenticate(rawPassword)) {
      throw new InvalidPasswordException("Invalid password");
    }

    // ✅ AccessToken + RefreshToken 발급
    String accessToken = jwtTokenProvider.generateToken(user.getId());
    String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

    // ✅ RefreshToken 저장 (DB)
    refreshTokenService.saveOrUpdate(
      user.getId(),
      refreshToken,
      jwtTokenProvider.getRefreshTokenExpiration()
    );

    return accessToken;

  }

}
