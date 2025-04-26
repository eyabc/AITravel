package com.aitravel.api.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Base64;

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

  public String generateToken(Long userId) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + jwtProperties.getExpiration());

    return Jwts.builder()
      .claims()
      .subject(String.valueOf(userId))
      .issuedAt(now)
      .expiration(expiry)
      .and() // <- 다시 빌더 본체로 돌아감
      .signWith(secretKey)
      .compact();
  }
}
