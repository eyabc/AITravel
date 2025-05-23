package com.aitravel.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
  @NotBlank(message = "Refresh Token은 필수입니다.")
  String refreshToken
) {
}
