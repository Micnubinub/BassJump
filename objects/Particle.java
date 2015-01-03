package tbs.jumpsnew.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.GameValues;
import tbs.jumpsnew.Screen;
import tbs.jumpsnew.utility.Utility;

public class Particle {
    public static final int STATE_ALIVE = 0; // particle is alive
    public static final int STATE_DEAD = 1; // particle is dead

    public static final int DEFAULT_LIFETIME = 200; // play with this
    public static final int MAX_DIMENSION = 5; // the maximum width or height
    public static final int MAX_SPEED = 10; // maximum speed (per update)
    private final Paint paint; // internal use to avoid instantiation
    private int state; // particle is alive or dead
    private float width; // width of the particle
    private float height; // height of the particle
    private float x, y; // horizontal and vertical position
    private double xv, yv; // vertical and horizontal velocity
    private int age; // current age of the particle
    private int lifetime; // particle dies when it reaches this value
    private int color; // the color of the particle

    public Particle() {
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(GameValues.STROKE_WIDTH / 3);
    }

    public void setup(int x, int y, boolean right) {
        this.x = x;
        this.y = y;
        this.state = Particle.STATE_ALIVE;
        this.width = Utility.randFloat(GameValues.SPLASH_MIN_SCALE,
                GameValues.SPLASH_MAX_SCALE);
        this.height = this.width;
        this.lifetime = DEFAULT_LIFETIME;
        this.age = 0;
        this.xv = (double) (Utility.randFloat(0, GameValues.SPEED_FACTOR));
        if (!right)
            this.xv *= -1;

        this.yv = (Utility.randInt(-GameValues.SPEED_FACTOR,
                GameValues.SPEED_FACTOR));
        // smoothing out the diagonal speed
        xv *= 0.85;
        yv *= 0.85;

        // RESET COLOR:
        this.color = Color.argb(255, 229, 228, 160);
    }

    public void update() {
        if (this.state != STATE_DEAD) {
            this.x += this.xv;
            if (xv > 0) { // GOING RIGHT
                if (x + width >= Screen.width - GameValues.PLATFORM_WIDTH) {
                    xv *= -1;
                }
            } else { // GOING LEFT
                if (x <= GameValues.PLATFORM_WIDTH) {
                    xv *= -1;
                }
            }
            this.y += this.yv;
            this.yv += 2;

            // extract alpha
            int a = this.color >>> 24;
            a -= 7; // fade by 2
            if (a <= 0) { // if reached transparency kill the particle
                this.state = STATE_DEAD;
            } else {
                this.color = (this.color & 0x00ffffff) + (a << 24); // set the
                // new alpha
                this.paint.setAlpha(Game.alphaM);
                this.age++; // increase the age of the particle
            }
            if (this.age >= this.lifetime) { // reached the end if its life
                this.state = STATE_DEAD;
            }
        }
    }

    public void draw(Canvas canvas) {
        if (this.state == STATE_ALIVE) {
            paint.setColor(this.color);
            canvas.drawRect(this.x, this.y, this.x + this.width, this.y
                    + this.height, paint);
        }
    }
}