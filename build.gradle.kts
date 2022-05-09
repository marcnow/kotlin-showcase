import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"

    id("org.sonarqube") version "3.3"
    id("com.palantir.docker") version "0.33.0"

    jacoco
}

group = "de.adesso"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-property:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Spring Boot Admin
    implementation("de.codecentric:spring-boot-admin-starter-client:2.6.0")
    implementation("de.codecentric:spring-boot-admin-starter-server:2.6.0")

    // SpringDoc / Swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.7")

    // Actuator / Prometheus
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation ("org.springframework.boot:spring-boot-starter-actuator")

    //Dependency, so it runs flawless on my M1 Mac
    implementation("io.netty:netty-all:4.1.68.Final")
}

docker {
    name = "${project.name}:${project.version}"
    copySpec.from("build/libs").into("build/libs")
    tag("DockerHub", "marcnow/kotlin-showcase:${project.version}")
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.isEnabled = true
    }
}
