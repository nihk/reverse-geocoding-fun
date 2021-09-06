buildscript {
    repositories.addProjectDefaults()
    dependencies {
        classpath(Dependencies.androidGradlePlugin)
        classpath(Dependencies.Kotlin.plugin)
        classpath(Dependencies.Dagger.Hilt.plugin)
        classpath(Dependencies.GoogleMaps.secretsGradlePlugin)
    }
}

plugins {
    `ben-manes-versions`
    `ktlint-gradle`
}

allprojects {
    repositories.addProjectDefaults()
    apply(plugin = Plugins.ktlintGradle)
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
