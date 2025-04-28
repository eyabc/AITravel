package com.aitravel.api.auth.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthenticationFilter implements WebFilter {

  private static final String TOKEN_PREFIX = "Bearer ";
  private final JwtTokenProvider jwtTokenProvider;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = resolveToken(exchange);

    if (token != null) {
      if (jwtTokenProvider.isValidToken(token)) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, List.of());
        return chain.filter(exchange)
          .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
      } else {
        // 토큰이 있긴 하지만, 유효하지 않으면 → 그냥 "인증 실패" 처리
        return unauthorized(exchange);
      }
    }

    // 토큰이 아예 없는 경우도 그냥 다음 필터로 넘기자 (익명 접근 허용)
    return chain.filter(exchange);
  }

  // 401 Unauthorized 처리
  private Mono<Void> unauthorized(ServerWebExchange exchange) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  private String resolveToken(ServerWebExchange exchange) {
    String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // "Bearer " 이후만 자름
    }
    return null;
  }
}
