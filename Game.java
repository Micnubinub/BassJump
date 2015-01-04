package tbs.jumpsnew;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tbs.jumpsnew.animation.MovingText;
import tbs.jumpsnew.levels.Level;
import tbs.jumpsnew.managers.BitmapLoader;
import tbs.jumpsnew.objects.AnimCircle;
import tbs.jumpsnew.objects.Player;
import tbs.jumpsnew.ui.Button;
import tbs.jumpsnew.utility.AdManager;
import tbs.jumpsnew.utility.BeatDetectorByFrequency;
import tbs.jumpsnew.utility.GameObject;
import tbs.jumpsnew.utility.Utility;

public class Game {
    //Todo Red and blue default color, Square default shape
    private static final Paint paintText = new Paint();
    //todo necessary to have 2?
    private static final Paint paintVisualizer = new Paint();
    private static final Rect result = new Rect();
    private static final RectF paintTrailRect = new RectF();
    // PAINTER:
    private static final Paint paint = new Paint();
    public static int[] colors = new int[]{0xff32e532, 0xff327ae5, 0xffe532cd, 0xffe57e32, 0xffd54040};
    // CONTEXT
    public static Context context;
    // LEVEL AND PLAYER:
    public static Player player; // PLAYER CONTAINS PLAYER INFO
    public static Level level; // LEVEL CONTAINS LEVEL OBJECTS
    public static int textSize;
    // MUSIC
    public static int alphaM;
    public static boolean isPlaying;
    // STATE:
    public static GameState state;
    public static Button leaderBtn;
    public static Button rateBtn;
    public static Button modeBtn;
    public static Button exitButton;
    public static Button homeButton;
    public static Button storeBtn;
    public static Button soundBtn;
    public static Button achievBtn;
    // SOUND && VISUALIZER:
    public static MediaPlayer mpSong;
    // COLORS:
    public static int color; // CHANGE TO INT
    // STORE
    public static Typeface font;
    public static boolean introShowing;
    // MODE
    public static GameMode mode;
    public static AdManager adManager;
    // MOVING TEXTS:
    private static ArrayList<MovingText> animatedTexts; // ANIMATED TEXT LIST
    private static int animatedTextIndex; // WHICH TEXT TO USE
    private static Matrix rotator;
    private static BitmapLoader bitmaps;
    private static boolean alphaMInc;
    // INTERFACE:
    private static GameObject scoreDisplay;
    // GLOBAL PARTICLES:
    private static ArrayList<AnimCircle> circles;
    private static int circleIndex;
    // ANIMATION
    private static int menuTextAlpha;
    private static BeatDetectorByFrequency beatDetector;
    private static String songName;
    // FREQUENCY:
    private static int LOW_FREQUENCY = 0;
    private static int LOW_F_HEIGHT = 0;
    private static int MID_FREQUENCY = 0;
    private static int MID_F_HEIGHT = 0;
    private static int HIGH_FREQUENCY = 0;
    private static int HIGH_F_HEIGHT = 0;
    private static float prcnt;
    // INTRO
    private static int introDelay;
    private static String introText;
    private static int loadProg;
    private static int loadWidth;
    public Typeface tf;

    public static void init(Context cont) {
        // CONST
        context = cont;

        font = Typeface.createFromAsset(
                MainActivity.context.getAssets(), "Chunkfive.otf");
        paintText.setTypeface(font);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(GameValues.STROKE_WIDTH);
        paintText.setColor(Color.WHITE);
        paintVisualizer.setColor(0xffe5e4a0);
        paintVisualizer.setAlpha(255);
        paintVisualizer.setStrokeWidth(GameValues.STROKE_WIDTH / 6);

        circles = new ArrayList<>();
        circleIndex = 0;
        for (int i = 0; i < 4; ++i) {
            circles.add(new AnimCircle());
        }

        // MUSIC
        isPlaying = true;
        beatDetector = new BeatDetectorByFrequency();
        Utility.equipSong(context, Utility.getEquippedSong(context));

        // LOAD IMAGES ONCE
        bitmaps = new BitmapLoader();

        introDelay = 150;
        loadProg = 0;
        loadWidth = 0;
        introText = "The Big Shots";
        introShowing = true;
        adManager = new AdManager(context);
        Utility.addGameColors();
    }

    public static void setup() {
        Utility.log("Game Initialized");

        // MUSIC
        alphaM = 0;
        alphaMInc = true;
        LOW_FREQUENCY = 0;
        LOW_F_HEIGHT = 0;
        MID_FREQUENCY = 0;
        MID_F_HEIGHT = 0;
        HIGH_FREQUENCY = 0;
        HIGH_F_HEIGHT = 0;
        prcnt = 0;


        // MENU
        menuTextAlpha = 255;

        // SPEED CALC
        GameValues.SPEED_FACTOR_ORIGINAL = ((float) Screen.height / 600);

        Utility.log("SPEED: " + GameValues.SPEED_FACTOR_ORIGINAL);

        rotator = new Matrix();
        player = new Player();

        String shape = Utility.getPrefs(context).getString(Utility.EQUIPPED_SHAPE);
        if (shape == null || shape.length() < 2)
            shape = Utility.SHAPE_RECTANGLE;
        Player.setPlayerShape(Utility.getShapeType(shape));

        level = new Level();
        setupGame();

        setupInterface();
    }

    public static void update() {
        if (introShowing) {
            introDelay -= 1;
            loadProg += 1;
            loadProg(loadProg);
            if (introDelay <= 0) {
                introShowing = false;
            }
        } else {
            introShowing = false;
        }

        player.update();
        level.update();
        for (int i = 0; i < animatedTexts.size(); ++i) {
            animatedTexts.get(i).update();
        }
        for (int i = 0; i < circles.size(); ++i) {
            circles.get(i).update();
        }

        // M:
        if (alphaM > 0) {
            alphaM -= 15;
            if (alphaM < 0)
                alphaM = 0;
        }

        // FREQ:
        HIGH_FREQUENCY -= 10;
        updateHighFreq();
        if (HIGH_FREQUENCY < 0) {
            HIGH_FREQUENCY = 0;
        }
        MID_FREQUENCY -= 10;
        updateMidFreq();
        if (MID_FREQUENCY < 0) {
            MID_FREQUENCY = 0;
        }
        LOW_FREQUENCY -= 10;
        updatelowFreq();
        if (LOW_FREQUENCY < 0) {
            LOW_FREQUENCY = 0;
        }
    }

    public static void draw(Canvas canvas) {
        // DRAW EVERYTHING IN ORDER:
        //paint.setColor(0x000000); // DEFAULT

        paint.setColor(0xffe5e4a0);
        paint.setAlpha(25);
        canvas.drawCircle(player.getXCenter(), player.getYCenter(),
                HIGH_F_HEIGHT * 1.15f, paint);
        // canvas.drawCircle(player.getXCenter(), player.getYCenter(),
        // MID_F_HEIGHT * 1.15f, paint);
        canvas.drawCircle(player.getXCenter(), player.getYCenter(),
                LOW_F_HEIGHT * 1.15f, paint);

        // PLATFORMS:
        for (int i = 0; i < level.platformsRight.size(); ++i) {
            paint.setColor(0xff5b5b5b);
            paint.setAlpha(255);
            boolean drawTop = true;
            boolean drawBottom = true;
            if (level.platformsRight.get(i).hasNext
                    || Game.state != GameState.Playing) {
                drawTop = false;
            }
            if (level.platformsRight.get(i).hasPrevious
                    || Game.state != GameState.Playing) {
                drawBottom = false;
            }

            Game.drawRectangle(canvas, level.platformsRight.get(i).xPos,
                    level.platformsRight.get(i).yPos,
                    GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                    false, true, drawTop, drawBottom);

            if (player.goingRight && alphaM > 0) {
                paint.setColor(color);
                paint.setAlpha(alphaM);
                Game.drawRectangle(canvas, level.platformsRight.get(i).xPos,
                        level.platformsRight.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        false, true, drawTop, drawBottom);
            }
        }
        for (int i = 0; i < level.platformsLeft.size(); ++i) {
            paint.setColor(0xff5b5b5b);
            paint.setAlpha(255);
            boolean drawTop = true;
            boolean drawBottom = true;
            if (level.platformsLeft.get(i).hasNext
                    || Game.state != GameState.Playing) {
                drawTop = false;
            }
            if (level.platformsLeft.get(i).hasPrevious
                    || Game.state != GameState.Playing) {
                drawBottom = false;
            }
            Game.drawRectangle(canvas, level.platformsLeft.get(i).xPos,
                    level.platformsLeft.get(i).yPos, GameValues.PLATFORM_WIDTH,
                    GameValues.PLATFORM_HEIGHT, true, false, drawTop,
                    drawBottom);
            if (!player.goingRight && alphaM > 0) {
                paint.setColor(color);
                paint.setAlpha(alphaM);
                Game.drawRectangle(canvas, level.platformsLeft.get(i).xPos,
                        level.platformsLeft.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        true, false, drawTop, drawBottom);
            }
        }

        // PAINT
        for (int i = 0; i < player.paintTrail.size(); ++i) {
            paint.setColor(color);
            paint.setAlpha(255);
            if (player.paintTrail.get(i).active) {
                paintTrailRect.set(player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        player.paintTrail.get(i).xPos
                                + GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).yPos
                                + player.paintTrail.get(i).height);
                canvas.drawRoundRect(
                        paintTrailRect, 8,
                        8, paint);

            }
            if (player.paintTrail.get(i).isRight() != player.goingRight
                    && alphaM > 0) {
                paint.setColor(0xffe5e475);
                paint.setAlpha(alphaM);
                paintTrailRect.set(player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        player.paintTrail.get(i).xPos
                                + GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).yPos
                                + player.paintTrail.get(i).height);
                canvas.drawRoundRect(
                        paintTrailRect, 8,
                        8, paint);
            }
        }

        // SPLASH
        for (int i = 0; i < player.splashParticles1.size(); i++) {
            player.splashParticles1.get(i).draw(canvas);
        }
        for (int i = 0; i < player.splashParticles2.size(); i++) {
            player.splashParticles2.get(i).draw(canvas);
        }

        // PLAYER:

        player.draw(canvas, paint, player.xPos, player.yPos, GameValues.PLAYER_SCALE, GameValues.PLAYER_SCALE, 0);

        // // PARTICLES && TEXTS:
        paint.setColor(Color.WHITE);
        paint.setAlpha(8);
        for (int i = 0; i < level.speedParticles.size(); ++i) {
            canvas.drawRect(level.speedParticles.get(i).xPos,
                    level.speedParticles.get(i).yPos,
                    level.speedParticles.get(i).xPos
                            + GameValues.SPEED_PARTICLE_WIDTH,
                    level.speedParticles.get(i).yPos
                            + GameValues.SPEED_PARTICLE_HEIGHT, paint);
        }

        paintText.setTextAlign(Align.CENTER);
        paintText.setTextSize(Screen.width / 6);
        for (int i = 0; i < animatedTexts.size(); ++i) {
            if (animatedTexts.get(i).active) {
                paintText.setAlpha(animatedTexts.get(i).alpha);
                canvas.drawText(animatedTexts.get(i).text,
                        animatedTexts.get(i).xPos, animatedTexts.get(i).yPos,
                        paintText);
            }
        }

        // CIRCLES
        for (int i = 0; i < circles.size(); ++i) {
            if (circles.get(i).active) {
                paint.setColor(0xffe5e4a0);
                paint.setAlpha(circles.get(i).a);
                canvas.drawCircle(circles.get(i).xPos, circles.get(i).yPos,
                        circles.get(i).scale, paint);
            }
        }

        paint.setAlpha(255);
        if (state == GameState.Menu) {
            paint.setColor(Color.WHITE);
            canvas.drawBitmap(BitmapLoader.leader, leaderBtn.xPos,
                    leaderBtn.yPos, paint);
            canvas.drawBitmap(BitmapLoader.achiv, rateBtn.xPos, rateBtn.yPos,
                    paint);
            canvas.drawBitmap(BitmapLoader.store, storeBtn.xPos, storeBtn.yPos,
                    paint);
            canvas.drawBitmap(BitmapLoader.achievm, achievBtn.xPos,
                    achievBtn.yPos, paint);
            if (isPlaying)
                canvas.drawBitmap(BitmapLoader.sound, soundBtn.xPos,
                        soundBtn.yPos, paint);
            else
                canvas.drawBitmap(BitmapLoader.soundO, soundBtn.xPos,
                        soundBtn.yPos, paint);
            if (mode == GameMode.Arcade)
                canvas.drawBitmap(BitmapLoader.modeArcade, modeBtn.xPos,
                        modeBtn.yPos, paint);
            else
                canvas.drawBitmap(BitmapLoader.modeRecruit, modeBtn.xPos,
                        modeBtn.yPos, paint);

            // MODE TEXT:
            paintText.setColor(0xffe5e4a0);
            paintText.setAlpha(255);
            paintText.setTextSize(Screen.width / 24);
            paintText.setTextAlign(Align.RIGHT);
            if (mode == GameMode.Arcade)
                canvas.drawText("Arcade", modeBtn.xPos
                                - GameValues.BUTTON_PADDING,
                        (modeBtn.yPos + GameValues.BUTTON_SCALE)
                                - (GameValues.BUTTON_PADDING / 4), paintText);
            else
                canvas.drawText("Recruit", modeBtn.xPos
                                - GameValues.BUTTON_PADDING,
                        (modeBtn.yPos + GameValues.BUTTON_SCALE)
                                - (GameValues.BUTTON_PADDING / 4), paintText);

            // TEXT
            paintText.setColor(0xffe5e4a0);
            paintText.setTextAlign(Align.RIGHT);
            paintText.setTextSize(Screen.width / 4.85f);
            paintText.getTextBounds("BASS", 0, "JUMP".length(), result);
            canvas.drawText("BASS", leaderBtn.xPos - GameValues.BUTTON_PADDING,
                    (result.height() * 1.25f), paintText);
            canvas.drawText("JUMP", leaderBtn.xPos - GameValues.BUTTON_PADDING,
                    (result.height() * 2.25f), paintText);
            paintText.setTextSize(Screen.width / 19.25f);
            canvas.drawText("Tap anywhere", leaderBtn.xPos
                            - GameValues.BUTTON_PADDING, (result.height() * 2.75f),
                    paintText);
            canvas.drawText("to Start", leaderBtn.xPos
                            - GameValues.BUTTON_PADDING, (result.height() * 3.1f),
                    paintText);

            // SONG NAME:
            paintText.setColor(0xffe5e4a0);
            paintText.setAlpha(255);
            paintText.setTextSize(Screen.width / 24);
            paintText.setTextAlign(Align.RIGHT);
            paintText.getTextBounds(songName, 0, songName.length(), result);
            if (isPlaying)
                canvas.drawText(songName, soundBtn.xPos
                        - GameValues.BUTTON_PADDING, Screen.height
                        - GameValues.BUTTON_PADDING, paintText);
            else
                canvas.drawText("Music Off", soundBtn.xPos
                        - GameValues.BUTTON_PADDING, Screen.height
                        - GameValues.BUTTON_PADDING, paintText);

            // SCORE & STATS:
            String txt = ("Played: " + player.gamesPlayed);
            paintText.setColor(0xffe5e4a0);
            paintText.setTextAlign(Align.LEFT);
            paintText.setTextSize(Screen.width / 19.25f);
            paintText.getTextBounds(txt, 0, txt.length(), result);
            canvas.drawText(
                    txt,
                    (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                    (achievBtn.yPos + GameValues.BUTTON_SCALE), paintText);
            if (mode == GameMode.Arcade)
                canvas.drawText(
                        "Best: " + player.highScoreA,
                        (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                        (achievBtn.yPos + GameValues.BUTTON_SCALE)
                                - result.height(), paintText);
            else
                canvas.drawText(
                        "Best: " + player.highScoreR,
                        (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                        (achievBtn.yPos + GameValues.BUTTON_SCALE)
                                - result.height(), paintText);
        } else if (state == GameState.Playing) {
            // SCORE
            paintText.setColor(0xffe5e4a0);
            paintText.setTextSize(Screen.width / 4.1f);
            paintText.setTextAlign(Align.CENTER);
            paintText.getTextBounds(String.valueOf(player.score), 0, String
                    .valueOf(player.score).length(), result);
            paintText.setAlpha(255);
            if (player.score > 0) {
                canvas.drawText(String.valueOf(player.score),
                        Screen.getCenterX(),
                        scoreDisplay.yPos + (result.height() / 2), paintText);
            } else {
                canvas.drawText(String.valueOf(1), Screen.getCenterX(),
                        scoreDisplay.yPos + (result.height() / 2), paintText);
            }
            paintText.getTextBounds("0", 0, "0".length(), result);
            paintText.setTextSize(Screen.width / 15.5f);
            String txt = "";
            if (mode == GameMode.Arcade) {
                txt = ("BEST: " + String.valueOf(player.highScoreA));
                if (player.score > player.highScoreA)
                    txt = ("NEW BEST!");
            } else {
                txt = ("BEST: " + String.valueOf(player.highScoreR));
                if (player.score > player.highScoreR)
                    txt = ("NEW BEST!");
            }
            paintText.setColor(0xffe5e4a0);
            paintText.setAlpha(255);
            canvas.drawText(txt, Screen.getCenterX(), scoreDisplay.yPos
                    + (result.height()), paintText);
        }

        // INTRO
        if (introShowing) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xff3e3e3e);
            canvas.drawRect(0, 0, Screen.width, Screen.height, paint);
            paintText.setColor(0xffe5e4a0);
            paintText.setTextSize(Screen.width / 9);
            paintText.setTextAlign(Align.CENTER);
            canvas.drawText("The Big Shots", Screen.getCenterX(),
                    Screen.getCenterY(), paintText);
            paintText.setTextSize(Screen.width / 25);
            paintText.setTextAlign(Align.CENTER);
            canvas.drawText("Plays best with Headphones", Screen.getCenterX(),
                    Screen.height - GameValues.BUTTON_PADDING, paintText);
            paint.setColor(0xffe532cd);
            canvas.drawRect(Screen.getCenterX()
                            - (GameValues.LOADING_BAR_WIDTH / 2), Screen.height
                            - GameValues.LOADING_BAR_WIDTH / 2f,
                    (Screen.getCenterX() - (GameValues.LOADING_BAR_WIDTH / 2))
                            + loadWidth,
                    (Screen.height - GameValues.LOADING_BAR_WIDTH / 2f)
                            + GameValues.LOADING_BAR_WIDTH / 10, paint);
            paint.setStyle(Paint.Style.STROKE);
        }
    }

    public static void setupGame() {
        // SETUP NEW GAME
        Utility.saveCoins(Game.context, Utility.getCoins(Game.context) + (player.score / 8));
        if (player.gamesPlayed % 10 == 0 && player.gamesPlayed > 0) {
            MainActivity.getView().post(new Runnable() {
                @Override
                public void run() {
                    final InterstitialAd ad = Game.adManager.getFullscreenAd();
                    if (ad.isLoaded()) ad.show();
                }
            });

        } else if (player.gamesPlayed % 7 == 0 && player.gamesPlayed > 0) {
            MainActivity.getView().post(new Runnable() {
                @Override
                public void run() {
                    Game.adManager.loadFullscreenAd();
                }
            });

        }
        Utility.log("Game Setup Initialized");
        GameValues.SPEED_BONUS = 1;

        // COLORS:
        color = colors[Utility.randInt(0, colors.length - 1)];

        // SAVE SCORE TO LB
        if (MainActivity.getApiClient().isConnected()) {
            Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                    "CgkIvYbi1pMMEAIQBg", player.highScoreA);
            Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                    "CgkIvYbi1pMMEAIQBw", player.highScoreR);

        }

        // PLAYER & LEVEL
        state = GameState.Menu;
        level.setup(); // Setup Before Player: Position Reasons
        player.setup();

        // TEXT
        animatedTexts = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            animatedTexts.add(new MovingText());
        }

        // CHECK ACHIEVEMENTS:
        checkAchievements();
    }

    private static void drawRectangle(Canvas canvas, int x, int y, int w, int h,
                                      boolean drawLeft, boolean drawRight, boolean drawTop,
                                      boolean drawBottom) {
        if (drawLeft)
            canvas.drawLine(x, y, x, y + h, paint);

        if (drawRight)
            canvas.drawLine(x + w, y, x + w, y + h, paint);

        if (drawTop)
            canvas.drawLine(x, y, x + w, y, paint);

        if (drawBottom)
            canvas.drawLine(x, y + h, x + w, y + h, paint);

    }

    private static void checkAchievements() {
        // SCORE RELATED:
        if (MainActivity.getApiClient().isConnected()) {
            if (player.highScoreA >= 10)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAQ");
            if (player.highScoreA >= 50)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCA");
            if (player.highScoreA >= 100)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCg");
            if (player.highScoreA >= 200)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDQ");
            if (player.highScoreR >= 10)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAg");
            if (player.highScoreR >= 50)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCQ");

            // DEATH & GAMES RELATED:
            if (player.gamesPlayed >= 1000)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAw");
            if (player.gamesPlayed >= 500)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQBA");

            // OTHER
            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDA");
        }
    }

    public static void showAnimatedText(String text, int x, int y, int spd,
                                        int am, int a, int grwth) {
        try {
            animatedTexts.get(animatedTextIndex).activate(text, x, y, spd, am,
                    a, grwth);
            animatedTextIndex += 1;
            if (animatedTextIndex == 3)
                animatedTextIndex = 0;
        } catch (IndexOutOfBoundsException e) {
            animatedTexts.get(0).activate(text, x, y, spd, am, a, grwth);
        }
    }

    private static void setupInterface() {
        // BUTTONS && IMAGES:
        scoreDisplay = new GameObject();
        scoreDisplay.xPos = Screen.getCenterX();
        scoreDisplay.scale = Screen.height / 13;
        scoreDisplay.scale2 = (int) (scoreDisplay.scale * 1.25f);
        scoreDisplay.yPos = (int) (scoreDisplay.scale * 1.35f);

        // BUTTONS:
        leaderBtn = new Button();
        leaderBtn.scale = GameValues.BUTTON_SCALE;
        leaderBtn.xPos = (Screen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        leaderBtn.yPos = GameValues.BUTTON_PADDING;

        rateBtn = new Button();
        rateBtn.scale = GameValues.BUTTON_SCALE;
        rateBtn.xPos = (Screen.width - GameValues.BUTTON_SCALE) - GameValues.BUTTON_PADDING;
        rateBtn.yPos = GameValues.BUTTON_SCALE + (GameValues.BUTTON_PADDING * 2);

        modeBtn = new Button();
        modeBtn.scale = GameValues.BUTTON_SCALE;
        modeBtn.xPos = (Screen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        modeBtn.yPos = (GameValues.BUTTON_SCALE * 2)
                + (GameValues.BUTTON_PADDING * 3);

        achievBtn = new Button();
        achievBtn.scale = GameValues.BUTTON_SCALE;
        achievBtn.xPos = GameValues.BUTTON_PADDING;
        achievBtn.yPos = modeBtn.yPos;

        soundBtn = new Button();
        soundBtn.scale = GameValues.BUTTON_SCALE;
        soundBtn.xPos = (Screen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        soundBtn.yPos = (Screen.height - GameValues.BUTTON_SCALE)
                - (GameValues.BUTTON_PADDING);

        storeBtn = new Button();
        storeBtn.scale = GameValues.BUTTON_SCALE;
        storeBtn.xPos = (Screen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        storeBtn.yPos = (soundBtn.yPos - GameValues.BUTTON_SCALE)
                - (GameValues.BUTTON_PADDING);

        // MOVING TEXT:
        animatedTextIndex = 0;
        animatedTexts = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            animatedTexts.add(new MovingText());
        }
    }

    public static void showCircle(int scale, int x, int y, int a,
                                  boolean special) {
        circleIndex += 1;
        if (circleIndex == circles.size())
            circleIndex = 0;
        circles.get(circleIndex).activate(scale, x, y, a, special);
    }

    // BEAT LISTENERS:
    public static void highBeat(double power) {
        HIGH_FREQUENCY += (power * 1000); // ORIGINAL: * 1000
        if (HIGH_FREQUENCY > GameValues.FREQ_MAX) {
            HIGH_FREQUENCY = GameValues.FREQ_MAX;
        }
        updateHighFreq();
    }

    private static void updateHighFreq() {
        prcnt = HIGH_FREQUENCY * 100 / GameValues.FREQ_MAX;
        if (prcnt < 0)
            prcnt = 0;
        HIGH_F_HEIGHT = (int) (GameValues.FREQ_MAX_HEIGHT * (prcnt / 100));
    }

    public static void lowBeat(double power) {
        LOW_FREQUENCY += power;
        if (LOW_FREQUENCY > GameValues.FREQ_MAX) {
            LOW_FREQUENCY = GameValues.FREQ_MAX;
        }
        updatelowFreq();
    }

    private static void updatelowFreq() {
        prcnt = LOW_FREQUENCY * 100 / GameValues.FREQ_MAX;
        if (prcnt < 0)
            prcnt = 0;
        LOW_F_HEIGHT = (int) (GameValues.FREQ_MAX_HEIGHT * (prcnt / 100));
    }

    public static void mediumBeat(double power) {
        MID_FREQUENCY += (power * 100);
        if (MID_FREQUENCY > GameValues.FREQ_MAX) {
            MID_FREQUENCY = GameValues.FREQ_MAX;
        }
        updateMidFreq();
    }

    private static void updateMidFreq() {
        prcnt = MID_FREQUENCY * 100 / GameValues.FREQ_MAX;
        if (prcnt < 0)
            prcnt = 0;
        MID_F_HEIGHT = (int) (GameValues.FREQ_MAX_HEIGHT * (prcnt / 100));
    }

    // FAKE LOADER:
    private static void loadProg(int loadProg) {
        float pcrn = loadProg * 100 / 150;
        if (pcrn < 0)
            pcrn = 0;
        loadWidth = (int) ((GameValues.LOADING_BAR_WIDTH * (pcrn / 100)));
    }

    public static void playSong(String file) {
        playSong(new File(file));
    }

    private static void playSong(File file) {
        if (mpSong != null) {
            if (mpSong.isLooping())
                mpSong.stop();
            mpSong.release();
        }

        mpSong = MediaPlayer.create(MainActivity.context, Uri.fromFile(file));
        setUpSong();
    }

    public static void playDefaultSong() {
        if (mpSong != null) {
            if (mpSong.isLooping())
                mpSong.stop();
            mpSong.release();
        }

        mpSong = MediaPlayer.create(MainActivity.context, R.raw.song1);
        setUpSong();
    }


    private static void setUpSong() {
        mpSong.setLooping(true);
        mpSong.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (!isPlaying) {
            try {
                mpSong.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        final String equipped = Utility.getEquippedSong(context);
        if (equipped.equals(Uri.parse("android.resource://" + context.getApplicationInfo().packageName + "/raw/song1").toString()))
            songName = "SmaXa - Fall Back";
        else {
            final String[] songDetails = Utility.getSongTitle(equipped).split(Utility.SEP);

            if (songDetails == null || (songDetails[0] + songDetails[1]).length() < 2)
                songName = "SmaXa - Fall Back";
            else
                songName = songDetails[1] + " - " + songDetails[0];
        }

        try {
            mpSong.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mpSong.start();
        beatDetector.link(mpSong);
    }


}
