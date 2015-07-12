package tbs.jumpsnew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.games.Games;

import org.json.JSONException;
import org.json.JSONObject;

import tbs.jumpsnew.fragments.GetCoinsFragment;
import tbs.jumpsnew.ui.CustomDialog;
import tbs.jumpsnew.ui.OtherAppsAd;
import tbs.jumpsnew.utility.AdManager;
import tbs.jumpsnew.utility.BaseGameActivity;
import tbs.jumpsnew.utility.GPurchaseManager;
import tbs.jumpsnew.utility.SecurePreferences;
import tbs.jumpsnew.utility.Utility;

public class MainActivity extends BaseGameActivity {

    // TAG & ACTIVITY:
    public static final String TAG = "Mini_RPG";
    public static Context context;
//    public static View view;

    // SAVE DATA:
    public static SecurePreferences preferences;
    public static AdManager adManager;
    public static MainActivity mainActivity;
    public static GetCoinsFragment getCoinsFragment = new GetCoinsFragment();

    // Other apps ad
    public static OtherAppsAd otherAppsAd;
    public static int adsWatched;

    // PURCHASES:
    public static GPurchaseManager purchases;


    // ADS:
    public static boolean showAds;

    //Game container
    public static RelativeLayout gameContainer;

    public static void unlockAchievement(String id) {
        if (getApiClient().isConnected()) {
            Games.Achievements.unlock(getApiClient(), id);
        }
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static View getView() {
        return gameContainer;
    }

    private static void log(String step) {
        Log.e("msg", step);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gameContainer = (RelativeLayout) findViewById(R.id.game);
        adManager = new AdManager(this);

        mainActivity = this;
        // ACHIEVEMENT:
        unlockAchievement("CgkIvYbi1pMMEAIQDA");

        // SETUP:
        context = this;
        Utility.setupRandom();
        Screen.setup(context);
        Game.init(context);
        Game.setup();
        // LOAD AD:
        Game.adManager.loadFullscreenAd();

        // LOAD DATA:
        preferences = new SecurePreferences(context, "prefs_tbs_n",
                "X5TBSSDVSHYGF", true);
        if (preferences.getString("nerUds") != null) {
            showAds = false;
        } else {
            showAds = true;
        }

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
        if (preferences.getString("hScoreU") != null) {
            Game.player.highScoreU = Integer.parseInt(preferences
                    .getString("hScoreU"));
        } else {
            Game.player.highScoreR = 0;
        }
        if (preferences.getString("hScoreS") != null) {
            Game.player.highScoreS = Integer.parseInt(preferences
                    .getString("hScoreS"));
        } else {
            Game.player.highScoreS = 0;
        }
        if (preferences.getString("hScoreS2") != null) {
            Game.player.highScoreS2 = Integer.parseInt(preferences
                    .getString("hScoreS2"));
        } else {
            Game.player.highScoreS2 = 0;
        }

        if (preferences.getString("musicOn") != null) {
            if (preferences.getString("musicOn").equals("off")) {
                Game.isMusicEnabled = false;
                Game.pauseSong();
            } else {
                Game.isMusicEnabled = true;
            }
        } else {
            Game.isMusicEnabled = true;
        }

        if (preferences.getString("gMode") != null) {
            if (preferences.getString("gMode").equals("arcade")) {
                Game.mode = GameMode.Arcade;
            } else if (preferences.getString("gMode").equals("recruit")) {
                Game.mode = GameMode.Recruit;
            } else if (preferences.getString("gMode").equals("ultra")) {
                Game.mode = GameMode.Ultra;
            } else if (preferences.getString("gMode").equals("singul")) {
                Game.mode = GameMode.Singularity;
            } else if (preferences.getString("gMode").equals("speed")) {
                Game.mode = GameMode.SpeedRunner;
                GameValues.PLAYER_JUMP_SPEED_MULT = 8;
            }
        } else {
            Game.mode = GameMode.Arcade;
        }
        if (MainActivity.preferences.getString("gPlayed") != null) {
            Game.player.gamesPlayed = Integer.parseInt(MainActivity.preferences
                    .getString("gPlayed")) + 1;
        } else {
            Game.player.gamesPlayed = 0;
        }

        // Set up other apps adView and add gameView
        try {
//            final RelativeLayout gameContainer = ;
//            gameContainer.addView(view);
            otherAppsAd = new OtherAppsAd(this, gameContainer);
            otherAppsAd.show(5000);
        } catch (Exception e) {
            Log.e("Exception: ", "ERROR! DIALOG");
        }
        // Load songs in a thread
        Utility.refreshSongs();
        // Utility.saveCoins(this, 212424124);
        // PURCHASES: (PUT IN LATER)
        purchases = new GPurchaseManager();
        mHelper.reconnectClient();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Game.pauseSong();
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
        Game.playSong();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroyCalled");
        try {
            if (purchases.mService != null) {
                unbindService(purchases.mServiceConn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    // COMPLETE PURCHASE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    // HANDLE PURCHASE:
                    if (sku.equals(GameValues.IAP_1_ID)) {
                        Utility.saveCoins(context,
                                Utility.getCoins(context) + 12000);
                        CustomDialog.setNumCoins(Utility.getCoins(context));
                    } else if (sku.equals(GameValues.IAP_2_ID)) {
                        Utility.saveCoins(context,
                                Utility.getCoins(context) + 25000);
                        CustomDialog.setNumCoins(Utility.getCoins(context));
                    } else if (sku.equals(GameValues.IAP_3_ID)) {
                        Utility.saveCoins(context,
                                Utility.getCoins(context) + 100000);
                        CustomDialog.setNumCoins(Utility.getCoins(context));
                    } else if (sku.equals(GameValues.IAP_4_ID)) {
                        // REMOVE ADS:
                        preferences.put("nerUds", "xxxxx");
                        showAds = false;
                    } else if (sku.equals(GameValues.IAP_5_ID)) {
                        // DOUBLE COINS:
                        Utility.saveCoins(context,
                                Utility.getCoins(context) * 2);
                        CustomDialog.setNumCoins(Utility.getCoins(context));
                    }
                    Utility.showToast("Purchase Complete!", context);
                } catch (JSONException e) {
                    Utility.showToast("Purchase Failed!", context);
                    e.printStackTrace();
                }
            }
        }
    }
}