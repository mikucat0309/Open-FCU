buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43")
    }
}

plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.21.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
