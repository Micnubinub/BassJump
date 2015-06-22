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
						try {
							Game.pauseSong();
						} catch (Exception e) {
							e.printStackTrace();
						}
						MainActivity.preferences.put("musicOn", "off");
					} else {
						// TURN ON
						try {
							Game.playSong();
						} catch (Exception r) {
							r.printStackTrace();
						}
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
			Game.introShowing = false; // SKIP
		}
	}

	public static void released(int x, int y, int index) {

	}

	public static void share() {
//		Calendar c = Calendar.getInstance();
//		Date d = c.getTime();
//
//		Game.update();
//		Config conf = Config.RGB_565;
//		Bitmap image = Bitmap.createBitmap(Screen.width, Screen.height, conf);
////		Canvas canvas = GameThread.surfaceHolder.lockCanvas(null);
////		canvas.setBitmap(image);
////		Paint backgroundPaint = new Paint();
////		backgroundPaint.setColor(0xff292929);
////		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
////				backgroundPaint);
////		Game.draw(canvas);
////		Bitmap screen = Bitmap.createBitmap(image, 0, 0, Screen.width,
////				Screen.height);
////		canvas.setBitmap(null);
////		GameThread.surfaceHolder.unlockCanvasAndPost(canvas);
//		String path = Images.Media.insertImage(
//				Game.context.getContentResolver(), screen, "screenShotBJ" + d
//						+ ".png", null);
//		System.out.println(path + " PATH");
//		Intent shareIntent = new Intent(Intent.ACTION_SEND);
//		Uri screenshotUri = Uri.parse(path);
//		shareIntent.setType("*/*");
//		shareIntent
//				.putExtra(
//						Intent.EXTRA_TEXT,
//						"Check out my High Score on Bass Jump: \nhttps://play.google.com/store/apps/details?id=tbs.jumpsnew");
//		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//		Game.context.startActivity(Intent.createChooser(shareIntent,
//				"Share High Score:"));
	}
}
