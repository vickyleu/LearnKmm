buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url ="https://kotlin.bintray.com/kotlinx")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.android.tools.build:gradle:7.1.3")
    }
}
allprojects {
    afterEvaluate {
        // Remove log pollution until Android support in KMP improves.
        project.extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()?.let { kmpExt ->
            kmpExt.sourceSets.removeAll {
                it.name == "androidAndroidTestRelease" ||
                it.name == "androidTestFixtures" ||
                it.name == "androidTestFixturesDebug" ||
                it.name == "androidTestFixturesRelease" ||
                "Test" in it.name
            }
        }
        @Suppress("DEPRECATION") // Alternative is to do it for each android plugin id.
        plugins.withType(com.android.build.gradle.BasePlugin::class.java).configureEach {
            project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
                lintOptions.isAbortOnError = false
            }
        }

        tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile>().configureEach {
            kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            kotlinOptions.jvmTarget = "1.8"
        }

        tasks.whenTaskAdded {
            if ("DebugUnitTest" in name || "ReleaseUnitTest" in name) {
                enabled = false
                // MPP + Android unit testing is so broken we just disable it altogether,
                // (discussion here https://kotlinlang.slack.com/archives/C3PQML5NU/p1572168720226200)
            }
        }

    }
    repositories {
        google()
        mavenCentral()
        maven(url ="https://kotlin.bintray.com/kotlinx")
        maven(url ="https://jitpack.io")
        maven {
            setUrl("http://download.flutter.io")
            isAllowInsecureProtocol = true
        }
        maven { setUrl("https://mvn.getui.com/nexus/content/repositories/releases")  }
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}