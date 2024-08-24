import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    kotlin("kapt")
    id("kotlin-parcelize")
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.wtw.whattheweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wtw.whattheweather"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_MAP_KEY", getApiKey("KAKAO_MAP_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "KAKAO_MAP_KEY", getApiKey("KAKAO_MAP_KEY"))
        }
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "KAKAO_MAP_KEY", getApiKey("KAKAO_MAP_KEY"))

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir,providers).getProperty(propertyKey)
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.coil)
    implementation(libs.android)
    implementation(libs.v2.all)
    implementation(libs.v2.user)

    implementation(libs.lottie)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.gson)

}