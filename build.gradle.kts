import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "com.github.wenslo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}
val springVersion = "5.0.20.RELEASE"
val luceneVersion = "8.4.1"

dependencies {
    implementation(group = "redis.clients", name = "jedis", version = "3.4.1")
    implementation(group = "cglib", name = "cglib", version = "3.3.0")
    // spring依赖
    implementation(group = "org.springframework", name = "spring-core", version = springVersion)
    implementation(group = "org.springframework", name = "spring-beans", version = springVersion)
    implementation(group = "org.springframework", name = "spring-context", version = springVersion)

    // lucene 相关依赖
    implementation(group = "org.apache.lucene", name = "lucene-core", version = luceneVersion)
    implementation(group = "org.apache.lucene", name = "lucene-queryparser", version = luceneVersion)
    implementation(group = "org.apache.lucene", name = "lucene-analyzers-common", version = luceneVersion)
    implementation(group = "org.apache.lucene", name = "lucene-queries", version = luceneVersion)
    implementation(group = "org.apache.lucene", name = "lucene-highlighter", version = luceneVersion)
    implementation(group = "org.apache.lucene", name = "lucene-memory", version = luceneVersion)
    implementation(group = "org.apache.lucene", name = "lucene-analyzers-smartcn", version = luceneVersion)
    // ik分词器依赖
    implementation(group = "org.wltea.ik-analyzer", name = "ik-analyzer", version = "8.4.0")

    // tika 依赖
    implementation(group = "org.apache.tika", name = "tika-app", version = "1.26")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}