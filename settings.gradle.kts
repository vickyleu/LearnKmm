pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url ="https://kotlin.bintray.com/kotlinx")
    }
}
rootProject.name = "GeyanLib"

include(":shared")

include(":flutter")
project(":flutter").projectDir = file("${rootProject.projectDir}${File.separator}flutter")

include(":Test_KMM_Plugin")
project(":Test_KMM_Plugin").projectDir = file("${rootProject.projectDir}${File.separator}Test_KMM_Plugin")
