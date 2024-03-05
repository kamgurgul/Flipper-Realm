plugins {
    id("com.android.library")
    kotlin("android")
    id("io.realm.kotlin")
}

android {
    namespace = "com.kgurgul.flipper.realm.kotlin"

    defaultConfig {
        minSdk = 19
        compileSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly("io.realm.kotlin:library-base:1.11.0")
    compileOnly("com.facebook.flipper:flipper:0.247.0")
}