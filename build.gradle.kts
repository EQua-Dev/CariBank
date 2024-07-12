// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    /*extensions() {
        var compose_version = "1.2.1"
        var hilt_plugin_version = "2.43.2"
        var kotlin_version = "1.5.31"
    }*/
    repositories {
        google()
        mavenCentral()
        //maven { url("https://jitpack.io") }

    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("com.android.tools.build:gradle:8.0.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.5.31")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")

//        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.33-beta'


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}