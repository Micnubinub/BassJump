package tbs.bassjump;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class GameController {
    private static final GestureDetector.GestureListener gestureListener = new GestureDetector.GestureListener() {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
//            GameController.click((int) x, (int) y);
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            GameController.fling(velocityX, velocityY);
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    };
    private static int startX, startY;
    private static int lastTouchX, lastTouchY;
    private static final InputProcessor inputProcessor = new InputProcessor() {
        @Override
        public boolean keyDown(int keycode) {
            keyPress(keycode);
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
            click(x, y);
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            Game.shop.drag(startX, startY, screenX - lastTouchX, screenY - lastTouchY);
            lastTouchX = screenX;
            lastTouchY = screenY;
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            scroll(amount * (Game.h / 200));
            return false;
        }
    };
    private static InputMultiplexer multiplexer = new InputMultiplexer();
    private static GestureDetector gestureDetector;

    private static void click(int x, int y) {
        startX = x;
        startY = y;

        lastTouchX = x;
        lastTouchY = y;

        if (!Game.introShowing) {
            if (Game.shop.isShowing()) {
                Game.shop.click(x, y);
            } else if (Game.state == GameState.Menu) {
                if (x >= Game.soundBtn.xPos
                        && x <= Game.soundBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.soundBtn.yPos
                        && y <= Game.soundBtn.yPos + GameValues.BUTTON_SCALE) {

                    // SOUND:
                    if (Game.isMusicEnabled) {
                        // TURN OFF
                        Game.isMusicEnabled = false;
                        try {
                            Game.pauseMusic();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tbs.bassjump.utility.Utility.saveString("musicOn", "off");
                    } else {
                        // TURN ON
                        Game.isMusicEnabled = true;
                        try {
                            Game.playMusic();
                        } catch (Exception r) {
                            r.printStackTrace();
                        }
                        tbs.bassjump.utility.Utility.saveString("musicOn", "on");
                    }
                } else if (x >= Game.rateBtn.xPos
                        && x <= Game.rateBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.rateBtn.yPos
                        && y <= Game.rateBtn.yPos + GameValues.BUTTON_SCALE) {
                    // FB PAGE:
                    tbs.bassjump.utility.Utility.openLink("https://www.facebook.com/AndroidHackerApp");


                } else if (x >= Game.modeBtn.xPos
                        && x <= Game.modeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.modeBtn.yPos
                        && y <= Game.modeBtn.yPos + GameValues.BUTTON_SCALE) {

                    // STORE:
                    if (Game.mode == GameMode.Recruit) {
                        Game.mode = GameMode.Arcade;
                        tbs.bassjump.utility.Utility.saveString("gMode", "arcade");
                    } else if (Game.mode == GameMode.Arcade) {
                        Game.mode = GameMode.Ultra;
                        tbs.bassjump.utility.Utility.saveString("gMode", "ultra");
                    } else if (Game.mode == GameMode.Ultra) {
                        Game.mode = GameMode.Singularity;
                        tbs.bassjump.utility.Utility.saveString("gMode", "singul");
                    } else if (Game.mode == GameMode.Singularity) {
                        Game.mode = GameMode.SpeedRunner;
                        GameValues.PLAYER_JUMP_SPEED_MULT = 8;
                        tbs.bassjump.utility.Utility.saveString("gMode", "speed");
                    } else if (Game.mode == GameMode.SpeedRunner) {
                        Game.mode = GameMode.Recruit;
                        tbs.bassjump.utility.Utility.saveString("gMode", "recruit");
                        GameValues.PLAYER_JUMP_SPEED_MULT = 3;
                    }
                } else if (x >= Game.shareBtn.xPos
                        && x <= Game.shareBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.shareBtn.yPos
                        && y <= Game.shareBtn.yPos + GameValues.BUTTON_SCALE) {

                    // SHARE:
                    tbs.bassjump.utility.Utility.openLink("http://thebigshots.net");
                    // tbs.bassjump.utility.Utility.showToast("Share Coming Soon!", tbs.bassjump.reference.);
                } else if (x >= Game.storeBtn.xPos
                        && x <= Game.storeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.storeBtn.yPos
                        && y <= Game.storeBtn.yPos + GameValues.BUTTON_SCALE) {
                    // STORE:

                    Game.shop.show();
                } else {

                    // PLAY:
                    Game.state = GameState.Playing;
                    Game.player.jump();
                    Game.showAnimatedText("GO!", (Game.w / 2),
                            (Game.h / 2) + ((Game.h) / 6),
                            (Game.h) / 150, 9, 255, 0);
                }
            } else if (Game.state == GameState.Playing) {
                Game.player.jump();
            }
        } else {
            //tbs.bassjump.reference.Game.introShowing = false; // SKIP
        }
    }

    private static void scroll(float dy) {
        Game.shop.drag(Game.w / 2, Game.h / 2, 0, dy);
    }

    private static void fling(float vx, float vy) {
        Game.shop.fling(vx, vy);
    }

    private static void zoom(float scale) {

    }

    private static void longClick(float scale) {

    }

    private static void keyPress(int keyCode) {
        switch (keyCode) {
            case Input.Keys.SPACE:
                click(0, 0);
                break;
        }
    }

    private static void keyRelease(int keyCode) {

    }

    public static void init() {
        //Todo call this in every resume
        multiplexer.clear();
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(gestureListener);
        }
        multiplexer.addProcessor(gestureDetector);
        multiplexer.addProcessor(inputProcessor);

        Gdx.input.setInputProcessor(multiplexer);
    }

}
