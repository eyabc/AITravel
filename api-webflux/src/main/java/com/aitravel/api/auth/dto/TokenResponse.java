package com.aitravel.api.auth.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
  String accessToken,
  String refreshToken
) {
}
