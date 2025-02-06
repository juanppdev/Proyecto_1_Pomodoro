plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialize)
    alias(libs.plugins.ktlint.jlleitschuh)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.mundocode.pomodoro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mundocode.pomodoro"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        android.buildFeatures.buildConfig = true
    }

    buildTypes {

        forEach { buildType ->
            buildType.buildConfigField(
                "String",
                "WEB_CLIENT_ID",
                "\"${providers.gradleProperty("web_client_id").get()}\"",
            )
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.core)

    // Android
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidBundle)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebaseBundle)

    // Google
    implementation(libs.bundles.googleBundle)

    // Kiwi
    implementation(libs.core)
    // Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

ktlint {
    version.set("1.5.0")
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
//    baseline.set(file("ktlint-baseline.xml"))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

kotlin {
    sourceSets.configureEach {
        languageSettings.enableLanguageFeature("ExplicitBackingFields")
    }
}
