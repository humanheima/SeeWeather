package util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by dumingwei on 2016/4/13.
 * SnackBar统一管理类
 */
public class SnackUtil {

    private SnackUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * short snack
     *
     * @param view
     * @param message
     */
    public static void SnackShort(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * short snack
     *
     * @param view
     * @param message
     */
    public static void SnackShort(View view, int message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * short snack
     *
     * @param view
     * @param message
     */
    public static void SnackLong(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * short snack
     *
     * @param view
     * @param message
     */
    public static void SnackLong(View view, int message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
