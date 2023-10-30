import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.2.0-RC1"
  id("io.spring.dependency-management") version "1.1.3"
  kotlin("jvm") version "1.9.20-RC"
  kotlin("plugin.spring") version "1.9.20-RC"
}

group = "dev.axgr"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")

  implementation("org.xhtmlrenderer:flying-saucer-pdf-openpdf:9.3.1")
  implementation("org.jsoup:jsoup:1.16.2")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
