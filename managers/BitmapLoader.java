package tbs.jumpsnew.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import tbs.jumpsnew.GameValues;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.R;
import tbs.jumpsnew.utility.Utility;

public class BitmapLoader {

    // PLAYER:
    public static Bitmap player;

    // LOADING
    public static Bitmap leader;
    public static Bitmap sound;
    public static Bitmap soundO;
    public static Bitmap achiv;
    public static Bitmap store;
    public static Bitmap achievm;
    public static Bitmap share;

    // MODES:
    public static Bitmap modeArcade;
    public static Bitmap modeRecruit;
    public static Bitmap modeUltra;
    public static Bitmap modeSingular;

    // COIN:
    public static Bitmap coin;

    public BitmapLoader() {
        Utility.log("BitmapLoader Initialized");

        // PLAYER:
        player = Utility
                .getResizedBitmap(
                        BitmapFactory.decodeResource(
                                MainActivity.context.getResources(),
                                R.drawable.player), GameValues.PLAYER_SCALE,
                        GameValues.PLAYER_SCALE);

        leader = Utility
                .getResizedBitmap(
                        BitmapFactory.decodeResource(
                                MainActivity.context.getResources(),
                                R.drawable.leader), GameValues.BUTTON_SCALE,
                        GameValues.BUTTON_SCALE);
        sound = Utility.getResizedBitmap(
                BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.sound),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);
        soundO = Utility.getResizedBitmap(BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.soundoff),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);

        achiv = Utility.getResizedBitmap(
                BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.achiv),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);
        store = Utility.getResizedBitmap(
                BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.store),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);
        achievm = Utility
                .getResizedBitmap(
                        BitmapFactory.decodeResource(
                                MainActivity.context.getResources(),
                                R.drawable.achiv2), GameValues.BUTTON_SCALE,
                        GameValues.BUTTON_SCALE);
        share = Utility.getResizedBitmap(
                BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.share),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);

        // MODE:
        modeArcade = Utility.getResizedBitmap(BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.modearcade),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);
        modeRecruit = Utility.getResizedBitmap(BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.moderecruit),
                GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);
//		modeUltra = Utility.getResizedBitmap(BitmapFactory.decodeResource(
//				MainActivity.context.getResources(), R.drawable.modeultra),
//				GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);
//		modeSingular = Utility.getResizedBitmap(BitmapFactory.decodeResource(
//				MainActivity.context.getResources(), R.drawable.modesingul),
//				GameValues.BUTTON_SCALE, GameValues.BUTTON_SCALE);

        // COIN:
        coin = Utility.getResizedBitmap(
                BitmapFactory.decodeResource(
                        MainActivity.context.getResources(), R.drawable.coin),
                GameValues.COIN_SCALE, GameValues.COIN_SCALE);
    }
}
