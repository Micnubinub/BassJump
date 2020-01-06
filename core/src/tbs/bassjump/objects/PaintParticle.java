package tbs.bassjump.objects;


import tbs.bassjump.Game;
import tbs.bassjump.utility.GameObject;

public class PaintParticle extends GameObject {

    // EXTRA
    public boolean active;
    public boolean goingRight;

    public boolean isRight() {
        return xPos > (Game.w / 2);

    }
}
