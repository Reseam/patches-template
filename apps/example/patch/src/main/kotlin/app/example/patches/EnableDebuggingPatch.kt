package app.example.patches

import app.reseam.patch.patch

val enableDebugging = patch(ExamplePatchStrings.enableDebugging) {
    description(ExamplePatchStrings.enableDebuggingDescription)

    execute {
        manifest.edit {
            val application = findByTag("application").firstOrNull() ?: error("manifest has no <application> element")
            application["android:debuggable"] = "true"
        }
    }
}
