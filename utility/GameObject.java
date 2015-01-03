package tbs.jumpsnew.utility;


public class GameObject {
    // VARIABLES:
    public int height;
    public int width;
    public int xPos;
    public int yPos;
    public int scale;
    public int scale2;

    // JUMPING:
    public boolean bouncing; // IS JUMPING?
    public int bounceSpeed; // Jump Speed

    public GameObject() {

    }

    public void setup() {
        bouncing = false;
        bounceSpeed = 0;
    }
}
