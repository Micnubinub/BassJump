package tbs.bassjump.ui;

/**
 * Created by linde on 04-Dec-15.
 */
public abstract class Adapter {

    public abstract int getCount();

    public abstract View getView(int position);

    public abstract boolean click(int x, int y);

}
