plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.example.grpc.android"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.example.grpc.android"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(project(":proto"))

  implementation(libs.grpc.core)
  implementation(libs.grpc.stub)
  implementation(libs.grpc.kotlin.stub)
  implementation(libs.grpc.okhttp)
  implementation(libs.grpc.protobuf.lite)

  implementation(libs.core.ktx)
  implementation(libs.lifecycle.runtime.ktx)

  implementation(libs.compose.activity)
  implementation(platform(libs.compose.bom))
  implementation(libs.compose.material3)
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.graphics)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.compose.ui.tooling)
}