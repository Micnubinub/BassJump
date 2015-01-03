package tbs.jumpsnew.objects;

import tbs.jumpsnew.Screen;
import tbs.jumpsnew.utility.GameObject;

public class AnimCircle extends GameObject {

    // EXTRA
    public boolean active;
    public int a; // ALPHA
    public int scaleUp;
    public boolean special;

    public void update() {
        if (active) {
            this.scale += Screen.width / 38;
            this.a -= 10;
            if (a <= 0) {
                active = false;
            }
        }
    }

    public void activate(int scale, int x, int y, int a, boolean special) {
        this.scale = scale;
        this.xPos = x;
        this.yPos = y;
        this.a = a;
        this.active = true;
        this.special = special;
    }
}
