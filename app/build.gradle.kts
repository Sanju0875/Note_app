plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.notesharingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesharingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
                getDefaultProguardFile("proguard-android-optimize.txt");
                "proguard-rules.pro"

        }
    }
    buildFeatures{
        viewBinding
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation( "androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.3.0")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")

}