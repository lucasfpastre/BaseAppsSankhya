// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.1.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.1")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath ("com.squareup:javapoet:1.13.0")
    }
}
plugins {
    id ("com.android.application") version "8.1.1" apply false
    id ("com.android.library") version "8.1.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
}

