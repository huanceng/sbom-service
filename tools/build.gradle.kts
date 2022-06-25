val commonsLang3Version: String by project

dependencies {
    implementation(project(":utils"))

    implementation("oss-review-toolkit:model")

    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}