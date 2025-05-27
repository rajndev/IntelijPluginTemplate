package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import java.io.File

fun createDomainDir(moduleData: ModuleTemplateData) {
    val domainDir = File(moduleData.projectTemplateData.rootDir, "Domain")

    domainDir.mkdirs()
}