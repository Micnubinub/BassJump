package tbs.bassjump.ui;

import java.util.ArrayList;

/**
 * Created by linde on 07-Dec-15.
 */
public abstract class ViewGroup extends View {
    protected final ArrayList<View> views = new ArrayList<View>();

    @Override
    public boolean click(int xPos, int yPos) {
        for (int i = (views.size() - 1); i >= 0; i--) {
            final View view = views.get(i);
            if (view.click(xPos, yPos)) {
                return true;
            }
        }
        return super.click(xPos, yPos);
    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {
        onDraw(relX, relY, parentRight, parentTop);
        for (int i = (views.size() - 1); i >= 0; i--) {
            final View view = views.get(i);
            if (cullView(view)) {
                view.draw(relX, relY, parentRight, parentTop);
            }
        }
    }

    public abstract void onDraw(float relX, float relY, float parentRight, float parentTop);

    public boolean cullView(final View v) {
        rect2.set(lastRelX + x, lastRelY + y, w, h);
        return rect2.contains(v.getViewBounds()) || rect2.overlaps(v.getViewBounds());
    }

    public void addView(View view) {
        if (view == null || views.contains(view))
            return;
        views.add(view);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean fling(float vx, float vy) {
        //Todo
        return false;
    }
}
