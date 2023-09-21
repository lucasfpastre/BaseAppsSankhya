plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-android")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "br.com.generic.base"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.generic.base"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0.1"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    bundle {
        storeArchive {
            enable = false
        }
    }
}

dependencies {

    // Implantações padrões
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ( "androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ( "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ( "androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation ( "androidx.fragment:fragment-ktx:1.6.1")
    implementation ( "androidx.navigation:navigation-ui-ktx:2.7.2")
    implementation ( "androidx.activity:activity-ktx:1.7.2")
    implementation ( "androidx.annotation:annotation:1.7.0")
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")

    //https://developer.android.com/topic/libraries/architecture/lifecycle
    // Usado no modelo MVVM para realizar processos com o ViewModel
    implementation ( "androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ( "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ( "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ( "androidx.lifecycle:lifecycle-common-java8:2.6.2")

    //https://square.github.io/retrofit/
    // Retrofit é utilizado para realizar as conexões de API, GSON e JSOUP são utilizados para ler o JSON de retorno
    implementation ( "com.squareup.retrofit2:retrofit:2.9.0")
    implementation ( "com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ( "com.squareup.retrofit2:retrofit:2.9.0")
    implementation ( "com.google.code.gson:gson:2.10.1")
    implementation ( "com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation ( "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    //https://developer.android.com/kotlin/coroutines?hl=pt-br
    // Utilizado para manipulações no ROOM de forma assíncrona
    implementation ( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ( "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //https://dagger.dev/hilt/gradle-setup
    // Hilt é o injetor de dependências, facilita a comunicação entre bancos, consultas e telas
    implementation ( "com.google.dagger:hilt-android:2.47")
    kapt ("com.google.dagger:hilt-compiler:2.47")

    //https://developer.android.com/training/data-storage/room
    // Banco de dados interno
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    // To use Kotlin annotation processing tool
    ksp ("androidx.room:room-compiler:2.5.2")

    //https://github.com/square/picasso
    // Usado para decodificar e exibir imagens
    implementation("com.squareup.picasso:picasso:2.8")

    hilt {
        enableAggregatingTask = true
    }
}
