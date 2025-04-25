package com.aitravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
  "com.aitravel.api",
  "com.aitravel.user",
  "com.aitravel.common",
})
public class ApiWebfluxApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiWebfluxApplication.class, args);
  }
}

