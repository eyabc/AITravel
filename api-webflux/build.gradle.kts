plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain-user"))
    implementation(project(":domain-museum"))
    implementation(project(":domain-translation"))
    implementation(project(":external-ai-client"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security") // JWT 등 보안 구성 대비
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
