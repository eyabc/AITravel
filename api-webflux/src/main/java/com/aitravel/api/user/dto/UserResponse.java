package com.aitravel.api.user.dto;

import com.aitravel.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
  private Long id;
  private String email;
  private String language;

  public static UserResponse from(User user) {

    return UserResponse.builder()
      .id(user.getId())
      .email(user.getEmail().getValue())
      .language(user.getPreferredLanguage().toString())
      .build();
  }
}
