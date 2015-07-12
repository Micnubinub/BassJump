package tbs.jumpsnew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.games.Games;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

import tbs.jumpsnew.utility.BaseGameActivity;

public class GameView extends View {
    public static int background;
    public static long lastUpdate = System.currentTimeMillis();
    public static ShareButtonClicked shareButtonClicked;
    static int delta, sw, sh;
    private final Context context;

    public GameView(Context context) {
        super(context);
        this.context = context;
//        background = Utility.getColor(Utility.getEquippedBackground(context));
        background = 0xff222222;

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        background = Utility.getColor(Utility.getEquippedBackground(context));
        background = 0xff222222;
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        background = Utility.getColor(Utility.getEquippedBackground(context));
        background = 0xff222222;
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

        return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
    }

    public static void drawGame(Canvas canvas) {
        delta = (int) (System.currentTimeMillis() - lastUpdate);
        GameValues.SPEED_FACTOR = (int) ((GameValues.SPEED_FACTOR_ORIGINAL * GameValues.SPEED_BONUS) * delta);
        if (GameValues.SPEED_FACTOR < 1)
            GameValues.SPEED_FACTOR = 1;
        GameValues.PLAYER_JUMP_SPEED = (GameValues.SPEED_FACTOR * GameValues.PLAYER_JUMP_SPEED_MULT);
        lastUpdate = System.currentTimeMillis();
        Game.update();
        Game.paint.setStyle(Paint.Style.FILL);
        Game.paint.setColor(background);
        canvas.drawRect(0, 0, sw, sh, Game.paint);
        Game.paint.setStyle(Paint.Style.STROKE);
        Game.draw(canvas);

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
                    Log.e("learderBoardClick > ", "registered");
                    if (BaseGameActivity.getApiClient().isConnected()) {
                        String leadID = "";
                        if (Game.mode == GameMode.Arcade) {
                            leadID = "CgkIvYbi1pMMEAIQBg";
                        } else if (Game.mode == GameMode.Recruit) {
                            leadID = "CgkIvYbi1pMMEAIQBw";
                        } else if (Game.mode == GameMode.Ultra) {
                            leadID = "CgkIvYbi1pMMEAIQEQ";
                        } else if (Game.mode == GameMode.SpeedRunner) {
                            leadID = "CgkIvYbi1pMMEAIQFA";
                        } else { // Singular
                            leadID = "CgkIvYbi1pMMEAIQEg";
                        }
                        Log.e("learderBoardStaring > ", new String(leadID));
                        ((FragmentActivity) context)
                                .startActivityForResult(
                                        Games.Leaderboards
                                                .getLeaderboardIntent(
                                                        MainActivity.getApiClient(),
                                                        leadID),
                                        10101);
                    } else {
                        Log.e("learderBoardStaring > ", "not connected");
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
    protected void onDraw(Canvas canvas) {
        drawGame(canvas);
        invalidate();
        if (shareButtonClicked != null) {
            shareButtonClicked.onShareButtonClicked();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sw = w;
        sh = h;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        invalidate();
    }
}