package tbs.bassjump.levels;


import tbs.bassjump.utility.GameObject;

public class Platform extends GameObject {

    // EXTRA VARIABLES:
    public int imageIndex; // WHICH IMAGE ITS USING
    public boolean landedOn;

    // OTHER PLATFROMS:
    public boolean hasNext;
    public boolean hasPrevious;
    public boolean hasSpikes;

    public Platform() {

    }

    public void setup() {

    }

    public void reset(int previousIndex) { // MOVED TO BACK
        // xPos = tbs.bassjump.reference.Game.level.platforms.get(previousIndex).xPos
        // + GameValues.PLATFORM_WIDTH;
    }
}
