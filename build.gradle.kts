/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/6.7/samples
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {

    val bootVer = "2.4.0"
    val kotlinVer = "1.4.20"
    val springMgmtVer = "1.0.10.RELEASE"

    id("org.springframework.boot") version bootVer
    id("io.spring.dependency-management") version springMgmtVer
    kotlin("jvm") version kotlinVer
    kotlin("plugin.spring") version kotlinVer
    kotlin("plugin.jpa") version kotlinVer
    kotlin("kapt") version kotlinVer
    kotlin("plugin.allopen") version kotlinVer
    kotlin("plugin.noarg") version kotlinVer
}

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

}

subprojects {
    val queryDslVer = "4.4.0"
    val loggerVer = "1.7.10"
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "kotlin-spring")

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    group = "com.clnk.livecommerce.api"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_15

    repositories {
        mavenCentral()
    }

    allOpen {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.Embeddable")
        annotation("javax.persistence.MappedSuperclass")
    }

    noArg {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.Embeddable")
        annotation("javax.persistence.MappedSuperclass")
    }

    dependencies {
        implementation("io.github.rybalkinsd:kohttp:0.12.0")
        implementation("com.google.firebase:firebase-admin:7.1.0")
        implementation("com.amazonaws:aws-java-sdk-s3:1.11.862")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("com.auth0:java-jwt:3.11.0")
        implementation("org.modelmapper:modelmapper:2.3.9")

        api("com.querydsl:querydsl-jpa:$queryDslVer")
        kapt("com.querydsl:querydsl-apt:$queryDslVer:jpa")
        kapt("org.springframework.boot:spring-boot-configuration-processor")
        implementation("io.github.microutils:kotlin-logging:$loggerVer")
        implementation("org.apache.commons:commons-lang3")
        implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.6.2")

        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        runtimeOnly("mysql:mysql-connector-java")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        testImplementation("io.projectreactor:reactor-test")
        testImplementation("org.springframework.security:spring-security-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "15"
        }
    }
}


project(":application") {
    dependencies {
        api(project(":library"))
        implementation("org.springframework.boot:spring-boot-starter-security")
        testImplementation("org.springframework.security:spring-security-test")
    }
    tasks.getByName<BootJar>("bootJar") {
        enabled = true
        launchScript()
    }
}

project(":admin") {
    dependencies {
        api(project(":library"))
        implementation("org.springframework.boot:spring-boot-starter-security")

        testImplementation("org.springframework.security:spring-security-test")
    }
    tasks.getByName<BootJar>("bootJar") {
        enabled = true
        launchScript()
    }
}

project(":library") {
    dependencies {
    }
    tasks.getByName<Jar>("jar") { enabled = true }
    tasks.getByName<BootJar>("bootJar") { enabled = false }
}