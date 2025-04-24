
## ✅ 멀티모듈 설계 개요
```
AITravel/
├── build.gradle.kts (root)
├── settings.gradle.kts
├── common/               (공통 유틸/DTO/exception) 유틸, 공통 예외, DTO, Response Wrapper 등
├── domain-user/          (회원 도메인) 
├── domain-museum/        (박물관 도메인)
├── domain-translation/   (번역 도메인)
├── api-webflux/          (외부에 노출되는 WebFlux API)
├── batch-task/           (필요시 배치 처리)
└── external-ai-client/   (Python AI 서버 호출 클라이언트)
```

### 🔹 [3] common 모듈
유틸, 공통 예외, DTO, Response Wrapper 등

### 🔹 [4] domain-* 모듈
각 도메인별로 Entity, Repository, Service 등을 나눔
예: domain-user, domain-museum, domain-translation

### 🔹 [5] api-webflux 모듈
Controller와 외부 API 레이어만 존재


### 🔹 [6] external-ai-client 모듈
Python 서버로 OCR/번역 요청을 보내는 클라이언트

📌 보너스: 확장 고려
- module-api-admin (관리자 전용 API 분리)
- module-integration-kafka (Kafka 이벤트 연동용 모듈)
- module-core-llm (향후 LLM Prompt/Tuning 공통화)

