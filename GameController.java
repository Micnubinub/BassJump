package tbs.jumpsnew;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.Calendar;
import java.util.Date;

import tbs.jumpsnew.managers.StoreManager;

public class GameController {

    private static final ShareButtonClicked share = new ShareButtonClicked() {
        @Override
        public void onShareButtonClicked() {
            final Date d = Calendar.getInstance().getTime();
            Game.update();
            final Bitmap image = Bitmap.createBitmap(Screen.width, Screen.height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(image);
            GameView.drawGame(canvas);
            final String path = MediaStore.Images.Media.insertImage(
                    Game.context.getContentResolver(), image, "screenShotBassJump_" + d
                            + ".png", null);
            System.out.println(path + " PATH");
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse(path);
            shareIntent.setType("image/*");
            shareIntent
                    .putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out my High Score on Bass Jump: \nhttps://play.google.com/store/apps/details?id=tbs.jumpsnew");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            Game.context.startActivity(Intent.createChooser(shareIntent,
                    "Share High Score:"));
            GameView.shareButtonClicked = null;
        }
    };

    public static void pressed(int x, int y, int index) {
        if (!Game.introShowing) {
            if (Game.state == GameState.Menu) {
                if (x >= Game.soundBtn.xPos
                        && x <= Game.soundBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.soundBtn.yPos
                        && y <= Game.soundBtn.yPos + GameValues.BUTTON_SCALE) {
                    // SOUND:
                    if (Game.isMusicEnabled) {
                        // TURN OFF
                        Game.isMusicEnabled = false;
                        try {
                            Game.pauseSong();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        MainActivity.preferences.put("musicOn", "off");
                    } else {
                        // TURN ON
                        Game.isMusicEnabled = true;
                        try {
                            Game.playSong();
                        } catch (Exception r) {
                            r.printStackTrace();
                        }
                        MainActivity.preferences.put("musicOn", "on");
                    }
                } else if (x >= Game.rateBtn.xPos
                        && x <= Game.rateBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.rateBtn.yPos
                        && y <= Game.rateBtn.yPos + GameValues.BUTTON_SCALE) {
                    // FB PAGE:
                    try {
                        Game.context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        Game.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/337927283025915")));
                    } catch (Exception e) {
                        Game.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AndroidHackerApp")));
                    }
                } else if (x >= Game.modeBtn.xPos
                        && x <= Game.modeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.modeBtn.yPos
                        && y <= Game.modeBtn.yPos + GameValues.BUTTON_SCALE) {
                    // STORE:
                    if (Game.mode == GameMode.Recruit) {
                        Game.mode = GameMode.Arcade;
                        MainActivity.preferences.put("gMode", "arcade");
                    } else if (Game.mode == GameMode.Arcade) {
                        Game.mode = GameMode.Ultra;
                        MainActivity.preferences.put("gMode", "ultra");
                    } else if (Game.mode == GameMode.Ultra) {
                        Game.mode = GameMode.Singularity;
                        MainActivity.preferences.put("gMode", "singul");
                    } else if (Game.mode == GameMode.Singularity) {
                        Game.mode = GameMode.SpeedRunner;
                        GameValues.PLAYER_JUMP_SPEED_MULT = 8;
                        MainActivity.preferences.put("gMode", "speed");
                    } else if (Game.mode == GameMode.SpeedRunner) {
                        Game.mode = GameMode.Recruit;
                        MainActivity.preferences.put("gMode", "recruit");
                        GameValues.PLAYER_JUMP_SPEED_MULT = 3;
                    }
                } else if (x >= Game.shareBtn.xPos
                        && x <= Game.shareBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.shareBtn.yPos
                        && y <= Game.shareBtn.yPos + GameValues.BUTTON_SCALE) {
                    // SHARE:
                    share();
                    // Utility.showToast("Share Coming Soon!", Game.context);
                } else if (x >= Game.storeBtn.xPos
                        && x <= Game.storeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.storeBtn.yPos
                        && y <= Game.storeBtn.yPos + GameValues.BUTTON_SCALE) {
                    // STORE:
                    final StoreManager manager = new StoreManager(Game.context);
                    manager.showStore();
                } else {
                    // PLAY:
                    Game.state = GameState.Playing;
                    Game.player.jump();
                    Game.showAnimatedText("GO!", Screen.getCenterX(),
                            Screen.getCenterY() + (Screen.height / 6),
                            Screen.height / 150, 9, 255, 0);
                }
            } else if (Game.state == GameState.Playing) {
                Game.player.jump();
            }
        } else {
            //Game.introShowing = false; // SKIP
        }
    }

    public static void released(int x, int y, int index) {

    }

    public static void share() {
        GameView.shareButtonClicked = share;
    }
}
