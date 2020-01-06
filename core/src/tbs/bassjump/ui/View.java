package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Michael on 2/9/2015.
 */
public abstract class View {
    public static final Color color = new Color();
    public static final Rectangle scissors = new Rectangle(), clipBounds = new Rectangle();
    protected static final Rectangle rect = new Rectangle();
    protected static final Rectangle rect2 = new Rectangle();
    public float x, y, w, h;
    public Object tag;
    public boolean debugDraw;
    public long tic;
    protected float lastRelX, lastRelY;
    protected int id;

    public boolean click(int xPos, int yPos) {
        rect.set(lastRelX + x, lastRelY + y, w, h);
        return rect.contains(xPos, yPos);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }

    public Rectangle getViewBounds() {
        rect.set(lastRelX + x, lastRelY + y, w, h);
        return rect;
    }

    public boolean drag(float startX, float startY, float dx, float dy) {
        return false;
    }

    public float getWidth() {
        return w;
    }

    public void setWidth(float w) {
        this.w = w;
    }

    public float getHeight() {
        return h;
    }

    public void setHeight(float h) {
        this.h = h;
    }

    public abstract void draw(float relX, float relY, float parentRight, float parentTop);

    public abstract void dispose();

    public void print(String str) {
        System.out.println(str);
    }

    public void setLastRelX(float lastRelX) {
        this.lastRelX = lastRelX;
    }

    public void setLastRelY(float lastRelY) {
        this.lastRelY = lastRelY;
    }

    @Override
    public String toString() {
        rect.set(lastRelX + x, y + lastRelY, w, h);
        return rect.toString();
    }

    public abstract boolean fling(float vx, float vy);

}
