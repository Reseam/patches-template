package app.example.patches

import app.example.screenshots.bindings.SecureFlags
import app.reseam.patch.patch

private const val WINDOW = "android.view.Window"

val allowScreenshots = patch("Allow screenshots") {
    description("Allows screenshots on windows the app marks as secure.")

    execute {
        val redirected = listOf(SecureFlags.addFlags, SecureFlags.setFlags).sumOf { bytecode.redirectCalls(WINDOW, it.name, it) }
        log.info("Cleared the secure flag at $redirected call sites")
    }
}
