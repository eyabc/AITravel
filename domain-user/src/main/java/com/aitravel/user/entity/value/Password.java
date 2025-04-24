package com.aitravel.user.entity.value;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Password {

  private String hashed;

  private Password(String hashed) {
    this.hashed = hashed;
  }

  public static Password fromRaw(String raw) {
    // 실제 구현 시 BCryptPasswordEncoder 등으로 암호화
    if (raw.length() < 8) {
      throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
    }
    return new Password("ENC(" + raw + ")"); // TODO: 암호화 로직 교체
  }

  public boolean matches(String raw) {
    return this.hashed.equals("ENC(" + raw + ")"); // TODO: 실제 매칭 로직
  }

}
