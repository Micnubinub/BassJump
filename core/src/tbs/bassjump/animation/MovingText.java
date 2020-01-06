package tbs.bassjump.animation;


import tbs.bassjump.utility.GameObject;

public class MovingText extends GameObject {
    public String text;
    public boolean active;
    public int alpha;
    public int alphaMin;
    public int speed;
    public int scaleInc;

    public MovingText() {
        this.text = "";
    }

    public void update() {
        if (active) {
            yPos -= speed;
            alpha -= alphaMin;
            scale += scaleInc;
            if (alpha <= 0) {
                alpha = 0;
                active = false;
            }
        }
    }

    public void activate(String text, int x, int y, int spd, int am, int a, int growth) {
        alphaMin = am;
        alpha = a;
        xPos = x;
        yPos = y;
        speed = spd;
        this.text = text;
        scaleInc = growth;
        active = true;
    }
}
