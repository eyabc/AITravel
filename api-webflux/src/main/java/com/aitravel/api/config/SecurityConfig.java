package com.aitravel.api.config;

import com.aitravel.api.jwt.JwtAuthenticationFilter;
import com.aitravel.api.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .authorizeExchange(exchange -> exchange
        .pathMatchers("/api/v1/auth/login").permitAll()
        .anyExchange().authenticated()
      )
      .addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider),
        SecurityWebFiltersOrder.AUTHENTICATION)
      .build();
  }
}
