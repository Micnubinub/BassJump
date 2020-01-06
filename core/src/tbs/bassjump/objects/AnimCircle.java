package tbs.bassjump.objects;


import tbs.bassjump.Game;
import tbs.bassjump.utility.GameObject;

public class AnimCircle extends GameObject {

    private static final float MAX_ANIM_TIME = 450f;
    // EXTRA
    public boolean active;
    public float a; // ALPHA
    private float animTime = 0;

    public void update() {
        if (active) {
            animTime -= Game.delta;
            a = animTime / MAX_ANIM_TIME;
            scale = Math.round(Game.w * (1 - a) * 0.35f);
//            a = (a * 0.75f);
            if (animTime <= 0) {
                active = false;
            }
        }
    }

    public void activate(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        this.active = true;
        scale = 0;
        animTime = MAX_ANIM_TIME;
    }
}
