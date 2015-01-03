package tbs.jumpsnew.objects;

import tbs.jumpsnew.GameValues;
import tbs.jumpsnew.Screen;
import tbs.jumpsnew.utility.GameObject;
import tbs.jumpsnew.utility.Utility;

public class SpeedParticle extends GameObject {

    public void update() {
        yPos += GameValues.SPEED_FACTOR * 2.25f;
        if (yPos > Screen.height)
            yPos = Utility.randInt(-(GameValues.SPEED_PARTICLE_HEIGHT * 2),
                    -GameValues.SPEED_PARTICLE_HEIGHT);
    }
}
