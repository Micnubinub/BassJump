package tbs.jumpsnew;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import tbs.jumpsnew.utility.Utility;

public class Screen {
    // VARIABLES
    public static int width;
    public static int height;

    public static void setup(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        Utility.log("Screen Initialized. Width: " + width + " : Height: "
                + height);
    }

    public static int getCenterX() {
        return width / 2;
    }

    public static int getCenterY() {
        return height / 2;
    }

}
