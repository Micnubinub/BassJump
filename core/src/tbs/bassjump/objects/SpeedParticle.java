package tbs.bassjump.objects;


import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.utility.GameObject;
import tbs.bassjump.utility.Utility;

public class SpeedParticle extends GameObject {

    public void update() {
        yPos += GameValues.SPEED_FACTOR * 1.9f;
        if (yPos > Game.h)
            yPos = Utility.randInt(-(GameValues.SPEED_PARTICLE_HEIGHT * 2),
                    -GameValues.SPEED_PARTICLE_HEIGHT);
    }
}
