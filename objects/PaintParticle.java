package tbs.jumpsnew.objects;

import tbs.jumpsnew.Screen;
import tbs.jumpsnew.utility.GameObject;

public class PaintParticle extends GameObject {

    // EXTRA
    public boolean active;

    public boolean isRight() {
        return xPos > Screen.getCenterX();

    }
}
