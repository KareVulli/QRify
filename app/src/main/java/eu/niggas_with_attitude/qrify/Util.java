package eu.niggas_with_attitude.qrify;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

class Util {
    @ColorInt
    static int resolveColorAttr(Context context, @AttrRes int colorAttr) {
        TypedValue resolvedAttr = resolveThemeAttr(context, colorAttr);
        // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
        int colorRes = resolvedAttr.resourceId != 0 ? resolvedAttr.resourceId : resolvedAttr.data;
        return ContextCompat.getColor(context, colorRes);
    }

    private static TypedValue resolveThemeAttr(Context context, @AttrRes int attrRes) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attrRes, typedValue, true);
        return typedValue;
    }
}
