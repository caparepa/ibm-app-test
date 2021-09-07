plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
}

android {

    defaultConfig {
        compileSdk = 31
        applicationId = "com.example.international.business.men"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://quiet-stone-2094.herokuapp.com\"")
        }

        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"https://quiet-stone-2094.herokuapp.com\"")
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

dependencies {
    //Core dependencies
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

    //livedat / lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    //navigation components
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

}

dependencies {
    val mockitoVersion by extra("3.12.4")
    val mockitoDexVersion by extra("2.12.1")
    val coroutinesTestVersion by extra("1.5.1")
    val mockWebServerVersion by extra("4.9.1")

    //Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    //mockito test
    testImplementation ("org.mockito:mockito-core:$mockitoVersion")
    testImplementation ("org.mockito:mockito-inline:$mockitoVersion")
    testImplementation ("com.linkedin.dexmaker:dexmaker-mockito:$mockitoDexVersion")

    // Mock retrofit for test
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockWebServerVersion")
    // coroutine test library
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")
}

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")//Third-party dependencies

    //Version define
    val koinVersion by extra("3.1.0")
    val retrofitVersion by extra("2.9.0")
    val okhttpVersion by extra("4.5.0")

    //OKHTTP
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    //Dependency injection
    // Koin for Kotlin apps core
    implementation("io.insert-koin:koin-core:$koinVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")

    // Koin for android
    implementation("io.insert-koin:koin-android:$koinVersion")

    //spinkit
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
}