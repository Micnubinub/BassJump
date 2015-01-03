package tbs.jumpsnew;

import android.content.Intent;
import android.net.Uri;

import tbs.jumpsnew.managers.StoreManager;

public class GameController {

    public static void pressed(int x, int y, int index) {
        if (!Game.introShowing) {
            if (Game.state == GameState.Menu) {
                if (x >= Game.soundBtn.xPos
                        && x <= Game.soundBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.soundBtn.yPos
                        && y <= Game.soundBtn.yPos + GameValues.BUTTON_SCALE) {
                    // SOUND:
                    if (Game.isPlaying) {
                        // TURN OFF
                        Game.mpSong.pause();
                        MainActivity.preferences.put("musicOn", "off");
                    } else {
                        // TURN ON
                        Game.mpSong.start();
                        MainActivity.preferences.put("musicOn", "on");
                    }
                    Game.isPlaying = !Game.isPlaying;
                } else if (x >= Game.rateBtn.xPos
                        && x <= Game.rateBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.rateBtn.yPos
                        && y <= Game.rateBtn.yPos + GameValues.BUTTON_SCALE) {
                    // ACHIEVE:
                    final String appPackageName = MainActivity.context
                            .getPackageName();
                    MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQBQ");
                    try {
                        MainActivity.context.startActivity(new Intent(
                                Intent.ACTION_VIEW, Uri
                                .parse("market://details?id="
                                        + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        MainActivity.context
                                .startActivity(new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id="
                                                + appPackageName)));
                    }
                } else if (x >= Game.modeBtn.xPos
                        && x <= Game.modeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.modeBtn.yPos
                        && y <= Game.modeBtn.yPos + GameValues.BUTTON_SCALE) {
                    // STORE:
                    if (Game.mode == GameMode.Arcade) {
                        Game.mode = GameMode.Recruit;
                        MainActivity.preferences.put("gMode", "recruit");
                    } else {
                        Game.mode = GameMode.Arcade;
                        MainActivity.preferences.put("gMode", "arcade");
                    }
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
            Game.introShowing = false; // SKIP
        }
    }

    public static void released(int x, int y, int index) {

    }
}
