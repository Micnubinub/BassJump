package tbs.jumpsnew;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static SurfaceHolder surfaceHolder;
    final long DELAY = 1;
    long sleepTime;
    long lastUpdate = System.currentTimeMillis();
    boolean isRunning;
    int delta;
    Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, Context context) {
        GameThread.surfaceHolder = surfaceHolder;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            delta = (int) (System.currentTimeMillis() - lastUpdate);
            GameValues.SPEED_FACTOR = (int) ((GameValues.SPEED_FACTOR_ORIGINAL * GameValues.SPEED_BONUS) * delta);
            if (GameValues.SPEED_FACTOR < 1)
                GameValues.SPEED_FACTOR = 1;
            GameValues.PLAYER_JUMP_SPEED = (GameValues.SPEED_FACTOR * GameValues.PLAYER_JUMP_SPEED_MULT);
            lastUpdate = System.currentTimeMillis();
            Game.update();

            canvas = surfaceHolder.lockCanvas(null);
            if (canvas != null) {
                synchronized (surfaceHolder) {

                    Game.draw(canvas);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean IsRunning() {
        return isRunning;
    }

    public void SetIsRunning(boolean state) {
        isRunning = state;
    }
}