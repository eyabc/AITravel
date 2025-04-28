package com.aitravel.api.auth.service;

import com.aitravel.api.auth.exception.InvalidEmailException;
import com.aitravel.api.auth.exception.InvalidPasswordException;
import com.aitravel.api.auth.jwt.JwtTokenProvider;
import com.aitravel.user.entity.User;
import com.aitravel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  public String login(String email, String rawPassword) {
    User user = userService.findByEmail(email)
      .orElseThrow(() -> new InvalidEmailException("User not found: " + email));

    if (!user.authenticate(rawPassword)) {
      throw new InvalidPasswordException("Invalid password");
    }

    return jwtTokenProvider.generateToken(user.getId());
  }

}
