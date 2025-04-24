package com.aitravel.user.service;

import com.aitravel.user.entity.User;
import com.aitravel.user.entity.value.Email;
import com.aitravel.user.entity.value.Password;
import com.aitravel.user.entity.value.PreferredLanguage;
import com.aitravel.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // ✅ 사용자 조회 (by 이메일)
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(Email.of(email));
  }

  // ✅ 사용자 등록 (회원가입)
  @Transactional
  public User register(String email, String rawPassword, String languageCode) {
    Email emailVO = Email.of(email);
    Password passwordVO = Password.fromRaw(rawPassword);
    PreferredLanguage langVO = PreferredLanguage.from(languageCode);

    if (userRepository.findByEmail(emailVO).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    User user = User.register(emailVO, passwordVO, langVO);
    return userRepository.save(user);
  }
}
