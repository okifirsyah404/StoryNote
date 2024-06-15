plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kspKotlinAndroid)
}

android {
    namespace = "com.okifirsyah.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
        buildConfigField("String", "DB_NAME", "\"story-note-db\"")
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.espresso.idling.resource)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //  Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    //Paging
    implementation(libs.androidx.paging.runtime.ktx)

    //  Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    //  okHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //  Retrofit
    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    //  Timber
    implementation(libs.timber)
}