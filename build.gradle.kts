// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath ("com.squareup:javapoet:1.13.0")
    }
}
plugins {
    id ("com.android.application") version "8.9.0" apply false
    id ("com.android.library") version "8.9.0" apply false
    id ("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id ("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}

