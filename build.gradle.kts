plugins {
    `java-library`
}

group = "com.mobile"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.appium:java-client:9.3.0")
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    testImplementation("org.testng:testng:7.10.2")
}

val platform = (findProperty("platform") as String?) ?: "android"

tasks.test {
    useTestNG {
        excludeGroups(if (platform == "ios") "android" else "ios")
    }
    systemProperty("platform", platform)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
