package tbs.jumpsnew;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.google.android.gms.games.Games;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

import tbs.jumpsnew.utility.BaseGameActivity;
import tbs.jumpsnew.utility.Utility;

public class GameView extends GLSurfaceView implements SurfaceHolder.Callback {
    private final Context context;
    private GameThread displayThread;

    public GameView(Context context) {
        super(context);
        this.context = context;
        InitView();
    }

    public static Bitmap SavePixels(int x, int y, int w, int h) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
        int b[] = new int[w * (y + h)];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, 0, w, y + h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
        for (int i = 0, k = 0; i < h; i++, k++) {
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - k - 1) * w + j] = pix1;
            }
        }

        Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }

    void InitView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        displayThread = new GameThread(holder, context);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // DO NOTHING
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int p = e.getPointerCount();

        switch (e.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                int x = (int) e.getX();
                int y = (int) e.getY();
                if (x >= Game.leaderBtn.xPos
                        && x <= Game.leaderBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.leaderBtn.yPos
                        && y <= Game.leaderBtn.yPos + GameValues.BUTTON_SCALE) {
                    // LEADER:
                    if (BaseGameActivity.getApiClient().isConnected()) {
                        String leadID = "";
                        if (Game.mode == GameMode.Arcade) {
                            leadID = "CgkIvYbi1pMMEAIQBg";
                        } else if (Game.mode == GameMode.Recruit) {
                            leadID = "CgkIvYbi1pMMEAIQBw";
                        } else if (Game.mode == GameMode.Ultra) {
                            leadID = "CgkIvYbi1pMMEAIQEQ";
                        } else { // Singular
                            leadID = "CgkIvYbi1pMMEAIQEg";
                        }
                        ((FragmentActivity) context)
                                .startActivityForResult(
                                        Games.Leaderboards
                                                .getLeaderboardIntent(
                                                        MainActivity.getApiClient(),
                                                        leadID),
                                        10101);
                    } else {
                        ((BaseGameActivity) context).getGameHelper()
                                .beginUserInitiatedSignIn();
                    }
                } else if (x >= Game.achievBtn.xPos
                        && x <= Game.achievBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.achievBtn.yPos
                        && y <= Game.achievBtn.yPos + GameValues.BUTTON_SCALE) {
                    if (BaseGameActivity.getApiClient().isConnected()) {
                        try {
                            ((FragmentActivity) context).startActivityForResult(
                                    Games.Achievements
                                            .getAchievementsIntent(MainActivity
                                                    .getApiClient()), 10101);
                        } catch (Exception ex) { // BAD PRATICE :P
                            System.out.println(e);
                        }
                    } else {
                        ((BaseGameActivity) context).getGameHelper()
                                .beginUserInitiatedSignIn();
                    }
                } else
                    GameController.pressed((int) (e.getX()), (int) (e.getY()), p);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                GameController.pressed((int) (e.getX(p - 1)),
                        (int) (e.getY(p - 1)), p);
                break;

            case MotionEvent.ACTION_UP:
                GameController.released((int) (e.getX()), (int) (e.getY()), p);
                break;
        }
        return true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        displayThread.SetIsRunning(false);
        Utility.StopThread(displayThread);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (!displayThread.IsRunning()) {
            displayThread = new GameThread(getHolder(), context);
            displayThread.start();
        } else {
            displayThread.start();
        }
    }
}