package tbs.jumpsnew;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.games.Games;

import tbs.jumpsnew.utility.BaseGameActivity;
import tbs.jumpsnew.utility.Utility;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static SurfaceHolder surfaceHolder;
    private final Context context;
    private GameThread displayThread;

    public GameView(Context context) {
        super(context);
        this.context = context;
        InitView();
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
                        final String leadID = Game.mode == GameMode.Arcade ? "CgkIvYbi1pMMEAIQBg" : "CgkIvYbi1pMMEAIQBw";
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