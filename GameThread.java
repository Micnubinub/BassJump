package tbs.jumpsnew;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static SurfaceHolder surfaceHolder;
    final Paint backgroundPaint;
    final long DELAY = 1;
    long sleepTime;
    long lastUpdate = System.currentTimeMillis();
    boolean isRunning;
    int delta;
    Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(0xff292929);
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
                    canvas.drawRect(0, 0, canvas.getWidth(),
                            canvas.getHeight(), backgroundPaint);
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