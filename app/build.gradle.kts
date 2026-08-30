plugins {
    id("com.android.application")
}

android {
    namespace = "dev.angur0.musicmode"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.angur0.musicmode"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "0.1-alpha"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
}
