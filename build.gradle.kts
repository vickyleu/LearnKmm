buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io")  }
        maven { setUrl("https://kotlin.bintray.com/kotlinx")  }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io")  }
        maven { setUrl("https://mvn.getui.com/nexus/content/repositories/releases")  }
    }
}




//subprojects {
//    afterEvaluate {
////        project.extensions.findByType<KotlinMultiplatformExtension>()?.let { ext ->
////            ext.sourceSets.removeAll { sourceSet ->
////                setOf(
////                    "androidAndroidTestRelease",
////                    "androidTestFixtures",
////                    "androidTestFixturesDebug",
////                    "androidTestFixturesRelease",
////                ).contains(sourceSet.name)
////            }
////        }
//    }
//}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}