package com.shivnasoft.customprojecttemplates

import java.io.File
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor

fun createAppStringsXmlFile(moduleData: ModuleTemplateData) {
    val valuesDir = File(moduleData.projectTemplateData.rootDir, "app/src/main/res/values")
    if (!valuesDir.exists()) valuesDir.mkdirs()

    val stringsXml = File(valuesDir, "strings.xml")

    val appName = moduleData.projectTemplateData.rootDir.name // Project name from wizard

    val content = """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <string name="app_name">$appName</string>
        </resources>
    """.trimIndent()

    stringsXml.writeText(content)
}