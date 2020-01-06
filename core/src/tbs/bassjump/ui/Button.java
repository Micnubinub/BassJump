package tbs.bassjump.ui;

import com.badlogic.gdx.math.Rectangle;

import tbs.bassjump.utility.GameObject;


public class Button extends GameObject {
    private static final Rectangle rect = new Rectangle();

    public boolean click(int clickX, int clickY) {
        clickY += height;
        rect.set(xPos, yPos, width, height);
        return rect.contains(clickX, clickY);
    }
}
