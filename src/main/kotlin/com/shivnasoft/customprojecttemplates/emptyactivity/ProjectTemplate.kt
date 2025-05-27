package com.shivnasoft.customprojecttemplates.emptyactivity

import com.android.tools.idea.wizard.template.Category
import com.android.tools.idea.wizard.template.CheckBoxWidget
import com.android.tools.idea.wizard.template.FormFactor
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.PackageNameWidget
import com.android.tools.idea.wizard.template.TemplateConstraint
import com.android.tools.idea.wizard.template.TemplateData
import com.android.tools.idea.wizard.template.TextFieldWidget
import com.android.tools.idea.wizard.template.WizardUiContext
import com.android.tools.idea.wizard.template.booleanParameter
import com.android.tools.idea.wizard.template.impl.defaultPackageNameParameter
import com.android.tools.idea.wizard.template.stringParameter
import com.android.tools.idea.wizard.template.template
import java.io.File

val projectTemplate
    get() = template {
        name = "Custom Project Template"
        description = "Custom Project Template"
        minApi = 27
        constraints = listOf(
            TemplateConstraint.AndroidX,
            TemplateConstraint.Kotlin,
            TemplateConstraint.Material3,
            TemplateConstraint.Compose,
        )
        category = Category.Application
        formFactor = FormFactor.Mobile
        screens = listOf(WizardUiContext.NewProject, WizardUiContext.NewProjectExtraDetail)

        val activityName = stringParameter {
            name = "Activity name"
            default = "MainActivity"
        }

        val addComposeDependencies = booleanParameter {
            name = "Add Compose Dependencies"
            default = false
        }

        val packageName = defaultPackageNameParameter

        widgets(
            TextFieldWidget(activityName),
            CheckBoxWidget(addComposeDependencies),
            PackageNameWidget(packageName)
        )

        // I am reusing the thumbnail provided by Android Studio, but
        // replace it with your own
        thumb { File("compose-activity-material3").resolve("template_compose_empty_activity_material3.png") }

        recipe = { data: TemplateData ->
            projectRecipe(
                moduleData = data as ModuleTemplateData,
                packageName = packageName.value,
                activityName = activityName.value,
                canAddComposeDependencies = addComposeDependencies.value
            )
        }
    }