package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import java.io.File

fun RecipeExecutor.createGradleWrapperProperties(moduleData: ModuleTemplateData) {
    val wrapperDir = File(moduleData.projectTemplateData.rootDir, "gradle/wrapper")

    if (!wrapperDir.exists()) {
        wrapperDir.mkdirs()
    }

    val wrapperPropertiesFile = File(wrapperDir, "gradle-wrapper.properties")

    val wrapperPropertiesContent = """
        distributionBase=GRADLE_USER_HOME
        distributionPath=wrapper/dists
        distributionUrl=https\://services.gradle.org/distributions/gradle-8.12.1-bin.zip
        zipStoreBase=GRADLE_USER_HOME
        zipStorePath=wrapper/dists
    """.trimIndent()

    wrapperPropertiesFile.writeText(wrapperPropertiesContent)
}