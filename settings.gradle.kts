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

//include(":flutter")
//project(":flutter").projectDir = file("${rootDir}${File.separator}flutter")
