package tbs.bassjump.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import tbs.bassjump.Game;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        final int h = (int) (com.google.gwt.user.client.Window.getClientHeight() * 0.92);
        return new GwtApplicationConfiguration((h * 9) / 16, h);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new Game();
    }
}
