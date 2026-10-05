package app.example.patches

import app.reseam.patch.ExtClass
import app.reseam.patch.Type
import app.reseam.patch.patch

private const val WINDOW = "android.view.Window"

private object SecureFlags : ExtClass("app.example.screenshots.SecureFlags") {
    val addFlags by static(WINDOW, Type.Int)
    val setFlags by static(WINDOW, Type.Int, Type.Int)
}

val allowScreenshots = patch("Allow screenshots") {
    description("Allows screenshots on windows the app marks as secure.")

    execute {
        val redirected = listOf(SecureFlags.addFlags, SecureFlags.setFlags).sumOf { bytecode.redirectCalls(WINDOW, it.name, it) }
        log.info("Cleared the secure flag at $redirected call sites")
    }
}
