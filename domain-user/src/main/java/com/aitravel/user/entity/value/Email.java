package com.aitravel.user.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Email {

  @Column(name = "email")
  private String value;

  private Email(String value) {
    this.value = value;
  }

  public static Email of(String email) {
    if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
      throw new IllegalArgumentException("Invalid email format");
    }
    return new Email(email.toLowerCase());
  }

  @Override
  public String toString() {
    return value;
  }
}
