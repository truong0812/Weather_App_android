plugins {
    id("com.android.application")
}

android {
    namespace = "com.hmt.weather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hmt.weather"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.android.gms:play-services-location:19.0.0")
    implementation ("com.google.android.libraries.places:places:2.4.0")




}