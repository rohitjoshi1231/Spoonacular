plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "1.4.0"
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.spoonacular"
    compileSdk = 34


    viewBinding {
        enable = true
    }
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
        applicationId = "com.example.spoonacular"
        minSdk = 24
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }


}


apply(plugin = "kotlin-kapt")
dependencies {

    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.play.services.ads.lite)
    implementation(libs.play.services.ads)
    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.picasso)
    implementation(libs.androidx.room.paging)
    implementation(libs.logging.interceptor)
    implementation(libs.play.services.auth)
    implementation(libs.retrofit)
    implementation(libs.converter.gson.v290)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.coroutines.core.v151)
    implementation(libs.kotlinx.coroutines.android.v151)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.ktx4)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.firebase.components)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
