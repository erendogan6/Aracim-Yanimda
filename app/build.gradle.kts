plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.erendogan6.aracimyanimda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.erendogan6.aracimyanimda"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (libs.rxjava)
    implementation (libs.rxandroid)
    implementation (libs.adapter.rxjava3)
    implementation (libs.converter.scalars)
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)
    implementation (libs.security.crypto)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.play.services.maps)
    implementation(libs.annotation)
}