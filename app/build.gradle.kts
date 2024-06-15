plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kspKotlinAndroid)
//    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("androidx.navigation.safeargs")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.okifirsyah.storynote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.okifirsyah.storynote"
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
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    secrets {
        // Optionally specify a different file name containing your secrets.
        // The plugin defaults to "local.properties"
        propertiesFileName = "secrets.properties"

        // A properties file containing default secret values. This file can be
        // checked in version control.
        defaultPropertiesFileName = "local.defaults.properties"

        // Configure which keys should be ignored by the plugin by providing regular expressions.
        // "sdk.dir" is ignored by default.
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        animationsDisabled = true
        unitTests.isReturnDefaultValues = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.intents)

    // Self dependency
    implementation(project(":customview"))
    implementation(project(":domain"))
    implementation(project(":data"))

    //  Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //  Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //  Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Coil
    implementation(libs.coil)

    //  Timber
    implementation(libs.timber)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Image View
    implementation(libs.circleimageview)
    implementation(libs.roundedimageview)

    // Swipe Refresh Layout
    implementation(libs.androidx.swiperefreshlayout)

    // Maps
    implementation(libs.play.services.maps)

    //Paging
    implementation(libs.androidx.paging.runtime.ktx)

    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.androidx.fragment.testing.manifest)
    debugImplementation(libs.androidx.fragment.testing)
    testImplementation(libs.androidx.core.testing)

    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // MockWebServer
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp.tls)


}