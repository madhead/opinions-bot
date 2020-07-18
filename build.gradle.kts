import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.3.71")
    id("com.github.johnrengelman.shadow").version("5.2.0")
}

repositories {
    jcenter()
}

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val intTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.13.1"))
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.10.3"))
    implementation(platform("software.amazon.awssdk:bom:2.11.9"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.amazonaws:aws-lambda-java-events:2.2.7")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.0")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.1.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.3")
    implementation("org.koin:koin-core:2.1.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("software.amazon.awssdk:dynamodb")
    implementation("com.google.api-client:google-api-client:1.23.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
    implementation("com.github.insanusmokrassar:TelegramBotAPI-all:0.27.6")

    testImplementation(platform("org.junit:junit-bom:5.6.0"))
    testRuntimeOnly(platform("org.junit:junit-bom:5.6.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core")
    intTestImplementation("by.dev.madhead.aws-junit5:dynamo-v2:5.0.4")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    val shadowJar by getting(ShadowJar::class) {
        transform(Log4j2PluginsCacheFileTransformer::class.java)
    }
    test {
        useJUnitPlatform()
    }
}
