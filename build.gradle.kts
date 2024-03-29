buildscript {
    val agp_version by extra("8.1.3")
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$agp_version")  // Updated version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")  // Updated version
        classpath("com.google.gms:google-services:4.4.0")  // Your version is fine
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.45")  // Your version is fine
    }
}

plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

