@file:Suppress("UnstableApiUsage")
pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
  }
}
rootProject.name = "XY-MST-Doctors"
include(":app")
include(":core-model")
include(":core-network")
include(":core-preferences")
include(":core-data")
include(":core-designsystem")
include(":core-navigation")
include(":feature-chat")
include(":feature-login")
include(":benchmark")