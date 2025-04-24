package com.aitravel.user.entity.value;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Locale;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PreferredLanguage {

  private String languageTag; // 'en', 'ko', 'fr-FR' 등

  private PreferredLanguage(String languageTag) {
    this.languageTag = languageTag;
  }

  public static PreferredLanguage from(String input) {
    try {
      Locale locale = Locale.forLanguageTag(input);
      if (locale.getLanguage().isEmpty()) {
        throw new IllegalArgumentException("Invalid language tag: " + input);
      }
      return new PreferredLanguage(locale.toLanguageTag());
    } catch (Exception e) {
      throw new IllegalArgumentException("잘못된 언어 코드입니다: " + input);
    }
  }

  public Locale toLocale() {
    return Locale.forLanguageTag(languageTag);
  }

  @Override
  public String toString() {
    return languageTag;
  }
}
