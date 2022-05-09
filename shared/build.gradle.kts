plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = Versions.library_version

kotlin {
    android{
//        publishLibraryVariants("release", "debug")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = Versions.deploymentTarget
//        specRepos {
//            url("https://github.com/Kotlin/kotlin-cocoapods-spec.git")
//        }
        specRepos {
            url("https://github.com/CocoaPods/Specs.git")
        }
        framework {
            baseName = "shared"
            isStatic = false
            // Bitcode embedding
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE)
        }
        pod("GYSDK"){
            //BUG,因为Pod名字与实际library名字不一样,目前需要找到真实的Framework名字才能使用
            //类似于 https://github.com/CocoaPods/Specs/XXXXXXXX/GYSDK/2.2.0.0/GYSDK.podspec.json
            version = Versions.gysdk_pod
            moduleName = "GeYanSdk"
        }
        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    }
    /*sourceSets.all {
        languageSettings.apply {
            languageVersion = "1.9" // possible values: '1.0', '1.1', '1.2', '1.3', '1.4', '1.5', '1.6', '1.7', '1.8', '1.9'
            apiVersion = "1.7" // possible values: '1.0', '1.1', '1.2', '1.3', '1.4', '1.5', '1.6', '1.7'
//            enableLanguageFeature("InlineClasses") // language feature name
//            optIn("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
            progressiveMode = true // false by default
        }
    }*/
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        val commonMain by getting{
            dependencies {
                // Koin for Kotlin apps
//                implementation("io.insert-koin:koin-core:${Versions.koin}")
                implementation("org.kodein.di:kodein-di:${Versions.kodein_di}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
            }
        }

        val androidMain by getting{
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodel}")
                //个验sdk
                implementation("com.getui:gysdk:${Versions.gysdk}")
                //个推公共库，如已接其他个推sdk则保留一个最高版本即可
                implementation("com.getui:gtc:${Versions.gysdk_gtc}")
//                implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        configure(listOf(iosArm64Main, iosX64Main)) {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.coroutines_native}")
            }
        }

    }
}

android {
    compileSdk = Versions.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
}

/*
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)*/
