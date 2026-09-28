package app.example.screenshots;

import android.view.Window;
import android.view.WindowManager;

public final class SecureFlags {
    private SecureFlags() {}

    public static void addFlags(Window window, int flags) {
        window.addFlags(flags & ~WindowManager.LayoutParams.FLAG_SECURE);
    }

    public static void setFlags(Window window, int flags, int mask) {
        window.setFlags(flags & ~WindowManager.LayoutParams.FLAG_SECURE, mask);
    }
}
