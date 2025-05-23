package com.aitravel.api.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;
  private SecretKey secretKey;

  public JwtTokenProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  @PostConstruct
  protected void init() {
    this.secretKey = Keys.hmacShaKeyFor(
      Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8))
        .getBytes(StandardCharsets.UTF_8)
    );
  }

  public String generateAccessToken(Long userId) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + jwtProperties.getExpiration());

    return Jwts.builder()
      .claim("nonce", UUID.randomUUID().toString())
      .claims()
      .subject(String.valueOf(userId))
      .issuedAt(now)
      .expiration(expiry)
      .and() // <- 다시 빌더 본체로 돌아감
      .signWith(secretKey)
      .compact();
  }

  public Long getUserIdFromToken(String token) {
    return Long.valueOf(
      Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject()
    );
  }

  public String generateRefreshToken(Long userId) {
    LocalDateTime expiry = LocalDateTime.now().plusDays(7);

    return Jwts.builder()
      .claim("nonce", UUID.randomUUID().toString())
      .subject(userId.toString())
      .issuedAt(new Date())
      .expiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
      .signWith(secretKey)
      .compact();
  }

  public boolean isValidToken(String token) {
    try {
      Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token); // 파싱이 성공하면 유효
      return true;
    } catch (Exception e) {
      return false; // 파싱 실패시 유효하지 않음
    }
  }

  public LocalDateTime getRefreshTokenExpiration() {
    return LocalDateTime.now().plusDays(7);
  }

}
