package edu.usm.cs.csc414.pocketfinances;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

public class ReplaceFont {

    public static void replaceDefaultFont(Context context,
                                          String nameOfOldDefaultFont,
                                          String fileNameOfNewFont) {

        Typeface font = Typeface.createFromAsset(context.getAssets(), fileNameOfNewFont);
        replaceFont(nameOfOldDefaultFont, font);
    }

    private static void replaceFont(String nameOfOldDefaultFont, Typeface font) {

        try {
            Field field = Typeface.class.getDeclaredField(nameOfOldDefaultFont);
            field.setAccessible(true);
            field.set(null, font);
        } catch (Exception e) {
            Log.e("ReplaceFont", "Failed to set font globally.", e);
        }
    }
}
