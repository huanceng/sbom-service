plugins {
    id("java")
}

group = "org.openeuler.sbom"
version = "1.0-SNAPSHOT"

val retrofitVersion: String by project
val okhttpVersion: String by project
val commonsLang3Version: String by project
val commonsCompressVersion: String by project
val jacksonVersion: String by project
val slf4jVersion: String by project
val log4jVersion: String by project
val commonsIoVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("oss-review-toolkit:model")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("org.apache.commons:commons-compress:$commonsCompressVersion")
    implementation("commons-io:commons-io:$commonsIoVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}