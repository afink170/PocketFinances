package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;

public class AppClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String newFont = getString(R.string.font_walkway_black);
        ReplaceFont.replaceDefaultFont(this, "DEFAULT", newFont);
        ReplaceFont.replaceDefaultFont(this, "MONOSPACE", newFont);
        ReplaceFont.replaceDefaultFont(this, "SERIF", newFont);
        ReplaceFont.replaceDefaultFont(this, "SANS_SERIF", newFont);

    }
}
