plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("java")
    id("war")
}

group = "org.openeuler.sbom"
version = "1.0-SNAPSHOT"

val commonsIoVersion: String by project
val commonsLang3Version: String by project
val packageUrlJavaVersion: String by project

dependencies {
    implementation(project(":analyzer"))
    implementation(project(":utils"))

    implementation("oss-review-toolkit:model")
    implementation("oss-review-toolkit:utils:spdx-utils")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("commons-io:commons-io:$commonsIoVersion")
    implementation("com.github.package-url:packageurl-java:$packageUrlJavaVersion")
    testImplementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
    runtimeOnly("org.postgresql:postgresql")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
    mainClass.set("org.openeuler.sbom.manager.SbomManagerApplication")
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

allprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.logging.log4j:log4j-api")
        implementation("org.apache.logging.log4j:log4j-core")
        implementation("org.apache.logging.log4j:log4j-slf4j-impl")
        implementation("org.slf4j:slf4j-api")
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}