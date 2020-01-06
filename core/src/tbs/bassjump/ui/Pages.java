package tbs.bassjump.ui;

import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import java.util.ArrayList;

import tbs.bassjump.Game;


/**
 * Created by mike on 3/7/16.
 */
public class Pages extends ViewPager {
    public static ListView colors = new ListView();
    public static ListView cars = new ListView();

    public void init() {
        if (colors.getAdapter() == null || cars.getAdapter() == null) {
            colors.setAdapter(new ColorAdapter());
            cars.setAdapter(new ShapesAdapter());
        }
    }

    @Override
    public ArrayList getTitles() {
        return null;
    }

    @Override
    public void onDraw(float relX, float relY, float parentWidth, float parentHeight) {
        h = Math.min(h, parentHeight - relY);
        clipBounds.set(relX, relY, w, h);
        Game.spriteBatch.flush();
        ScissorStack.calculateScissors(Game.camera, Game.spriteBatch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);
        //Todo Utility.print("setSze: " + w + ", " + h);
        final ListView curr = getCurrentListView();

        curr.setSize((int) w, (int) h);
        curr.draw(relX, relY, parentWidth, h);

        Game.spriteBatch.flush();
        ScissorStack.popScissors();
    }

    public ListView getCurrentListView() {
        switch (currentPage) {
            case 0:
                return colors;
            case 1:
                Game.spriteBatch.setShader(Game.shaderProgram);
                return cars;
        }
        return null;
    }

    @Override
    public void dispose() {
        colors.dispose();
        cars.dispose();
        super.dispose();
    }

    @Override
    public boolean click(int xPos, int yPos) {
        return getCurrentListView().click(xPos, yPos);
    }

    @Override
    public boolean drag(float startX, float startY, float dx, float dy) {
        return getCurrentListView().drag(startX, startY, dx, dy);
    }

    @Override
    public boolean fling(float vx, float vy) {
        return getCurrentListView().fling(vx, vy);
    }
}
