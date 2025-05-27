package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import com.shivnasoft.customprojecttemplates.emptyactivity.projectTemplate

class MyProjectTemplatesProvider : WizardTemplateProvider() {

    override fun getTemplates(): List<Template> {
        return listOf(projectTemplate)
    }
}