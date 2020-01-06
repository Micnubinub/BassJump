package tbs.bassjump.ui;

/**
 * Created by Michael on 3/10/2015.
 */
public class ListView extends ScrollView {
    //Todo get methods from Scrollview
    private OnItemClickListener onItemClickListener;
    private Adapter adapter;
    private float lastParentTop, lastParentRight;

    public ListView() {
        super(true);
    }

    public ListView(Adapter adapter) {
        super(true);
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean drag(float startX, float startY, float dx, float dy) {
        rect.set(lastRelX + x, lastRelY + y, w, h);
        if (rect.contains(startX, startY)) {
            //Todo pan animator
            setScrollX(scrollX + (int) dx);
            setScrollY(scrollY + (int) dy);
            return true;
        }
        return false;
    }

    @Override
    public boolean click(int xPos, int yPos) {
        return adapter.click(xPos, yPos);
    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {
        lastRelX = relX;
        lastRelY = relY;
        lastParentRight = parentTop;
        lastParentTop = parentTop;

        updatePanAnimator();
        if (adapter == null || adapter.getCount() < 1) {
            return;
        }

        scrollY = scrollY > 0 ? 0 : scrollY;
        if (lastMeasuredHeight != 0) {
            if (Math.abs(scrollY) + h > lastMeasuredHeight)
                scrollY = -((int) (lastMeasuredHeight - h));
        }

        int cumulative = 0;
        final float viewTop = relY + y + h;
        //Todo Utility.print("viewTop: " + viewTop);

        for (int i = 0; i < adapter.getCount(); i++) {
            final View v = adapter.getView(i);
            if (resizeChildrenWhenParentResized)
                v.w = v.w > w ? w : v.w;

            cumulative += v.h;
            v.setLastRelX(relX + x + scrollX);
            v.setLastRelY(viewTop - cumulative - scrollY);
            //Todo not meant to be here
            v.setWidth(w);

//            if (cullView(v))
            v.draw(relX + x, viewTop - cumulative - scrollY, Math.min(relX + x + scrollX + w, parentRight), Math.min(viewTop - cumulative - scrollY + h, parentTop));

        }
        lastMeasuredHeight = cumulative;
//        print("listView tic toc > " + (System.nanoTime() - tic));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void addView(View view) {
        print("listview can't add views");
    }

    @Override
    public void removeAllViews() {
        print("listview can't remove views");
    }

    @Override
    public void removeView(View view) {
        print("listview can't remove views");
    }

    @Override
    public void dispose() {
        for (View view : views) {
            view.dispose();
        }

        if (adapter != null)
            for (int i = 0; i < adapter.getCount(); i++) {
                adapter.getView(i).dispose();
            }

        if (adapter instanceof ColorAdapter) {
            ColorAdapter.dispose();
        } else if (adapter instanceof ShapesAdapter) {
            ((ShapesAdapter) adapter).dispose();
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

}
