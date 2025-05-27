package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import java.io.File

fun createFeaturesDir(moduleData: ModuleTemplateData) {
    val featuresDir = File(moduleData.projectTemplateData.rootDir, "Features")

    featuresDir.mkdirs()
}