plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.bookapp"
version = "0.0.1-SNAPSHOT"
description = "Backend usando Spring boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

	//WebFlux
	implementation("org.springframework.boot:spring-boot-starter-webflux") // WebClient


	//Logging
	implementation("net.logstash.logback:logstash-logback-encoder:7.4") // estable y compatible

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	
	// Jackson para XML/JSON (versiones consistentes)
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
	implementation("com.fasterxml.woodstox:woodstox-core")
	//h2 y postgresql
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")
	//Flywaydb
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
