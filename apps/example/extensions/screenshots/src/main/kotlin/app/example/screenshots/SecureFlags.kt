@file:JvmName("SecureFlags")

package app.example.screenshots

import android.view.Window
import android.view.WindowManager
import app.reseam.runtime.Export

@Export
fun addFlags(window: Window, flags: Int) {
    window.addFlags(flags and WindowManager.LayoutParams.FLAG_SECURE.inv())
}

@Export
fun setFlags(window: Window, flags: Int, mask: Int) {
    window.setFlags(flags and WindowManager.LayoutParams.FLAG_SECURE.inv(), mask)
}
