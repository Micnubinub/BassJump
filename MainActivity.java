package tbs.jumpsnew;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.games.Games;

import tbs.jumpsnew.ui.OtherAppsAd;
import tbs.jumpsnew.utility.AdManager;
import tbs.jumpsnew.utility.BaseGameActivity;
import tbs.jumpsnew.utility.SecurePreferences;
import tbs.jumpsnew.utility.Utility;

public class MainActivity extends BaseGameActivity {


    // TAG & ACTIVITY:
    public static final String TAG = "Mini_RPG";
    public static Context context;
    public static SurfaceView view;

    // SAVE DATA:
    public static SecurePreferences preferences;
    public static String LEADERBOARD_ID = "CgkIvYbi1pMMEAIQBg";
    public static AdManager adManager;
    public static MainActivity mainActivity;

    //Other apps ad
    public static OtherAppsAd otherAppsAd;

    public static void unlockAchievement(String id) {
        if (getApiClient().isConnected()) {
            Games.Achievements.unlock(getApiClient(), id);
        }
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static SurfaceView getView() {
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Utility.log("MainActivity Initialized");
        adManager = new AdManager(this);
        view = new GameView(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        mainActivity = this;

        // ACHIEVEMENT:
        unlockAchievement("CgkIvYbi1pMMEAIQDA");

        // SETUP:
        context = this;
        Screen.setup(context);
        Game.init(context);
        Game.setup();

        // LOAD DATA:
        preferences = new SecurePreferences(context, "prefs_tbs_n",
                "X5TBSSDVSHYGF", true);
        if (preferences.getString("hScore") != null) {
            Game.player.highScoreA = Integer.parseInt(preferences
                    .getString("hScore"));
        } else {
            Game.player.highScoreA = 0;
        }
        if (preferences.getString("hScoreR") != null) {
            Game.player.highScoreR = Integer.parseInt(preferences
                    .getString("hScoreR"));
        } else {
            Game.player.highScoreR = 0;
        }
        if (preferences.getString("musicOn") != null) {
            if (preferences.getString("musicOn").equals("off")) {
                Game.isPlaying = false;
                Game.mpSong.pause();
            } else {
                Game.isPlaying = true;
            }
        } else {
            Game.isPlaying = true;
        }
        if (preferences.getString("gMode") != null) {
            if (preferences.getString("gMode").equals("arcade")) {
                Game.mode = GameMode.Arcade;
            } else if (preferences.getString("gMode").equals("recruit")) {
                Game.mode = GameMode.Recruit;
            }
        } else {
            Game.mode = GameMode.Arcade;
        }


        //Set up other apps adView and add gameView
        final RelativeLayout gameContainer = (RelativeLayout) findViewById(R.id.game);
        gameContainer.addView(view);
        otherAppsAd = new OtherAppsAd(this, gameContainer);
        //Todo sidney
        MainActivity.otherAppsAd.show();

        //Load songs in a thread
        Utility.refreshSongs();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Game.isPlaying) {
            Game.mpSong.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Game.isPlaying) {
            Game.mpSong.start();
        }
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
}