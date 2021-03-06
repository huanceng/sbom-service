val retrofitVersion: String by project
val okhttpVersion: String by project
val commonsLang3Version: String by project
val commonsIoVersion: String by project

dependencies {
    implementation(project(":utils"))

    implementation("oss-review-toolkit:model")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("commons-io:commons-io:$commonsIoVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}