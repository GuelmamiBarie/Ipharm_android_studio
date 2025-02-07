plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ipharm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ipharm"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Material Components (replacement for com.android.support:design)
    implementation("com.google.android.material:material:1.9.0")

    // Universal Image Loader
    implementation("com.nostra13.universalimageloader:universal-image-loader:1.9.5")

    // Circle Image View Library
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // CardView Library (AndroidX version)
    implementation("androidx.cardview:cardview:1.0.0")

}