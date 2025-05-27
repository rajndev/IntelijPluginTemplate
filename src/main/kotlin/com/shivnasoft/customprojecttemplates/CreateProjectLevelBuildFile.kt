package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import java.io.File

fun createProjectBuildFile(moduleData: ModuleTemplateData) {
    val buildFileContent = """
       buildscript {
           val agp_version by extra("8.2.2")
           val hiltVersion by extra { "2.50" }
           val navigationVersion by extra { Versions.navigation_version }
           //extra["hilt_version"] = "2.50"
           //extra["navigation_version"] = Versions.navigation_version // Make sure to define Versions.navigation_version elsewhere in your project

           dependencies {
               classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}{Versions.kotlinGradlePlugin}")
               classpath("com.google.dagger:hilt-android-gradle-plugin:${'$'}hiltVersion")
               classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${'$'}navigationVersion")
           }
       }
       
       plugins {
           id("com.android.application") version "8.7.3" apply false
           id("com.android.library") version "8.7.3" apply false
           id("org.jetbrains.kotlin.android") version "1.9.24" apply false
           id("org.jetbrains.kotlin.jvm") version "1.9.24" apply false

       }

       tasks.register("clean", Delete::class) {
           delete(rootProject.layout.buildDirectory)
       }

    """.trimIndent()
    val buildFile = File(moduleData.projectTemplateData.rootDir, "build.gradle.kts")
    buildFile.writeText(buildFileContent)
}