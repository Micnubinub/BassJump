package tbs.jumpsnew.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.GameMode;
import tbs.jumpsnew.GameValues;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.Screen;
import tbs.jumpsnew.levels.Level;
import tbs.jumpsnew.levels.Platform;
import tbs.jumpsnew.utility.GameObject;
import tbs.jumpsnew.utility.Utility;

public class Player extends GameObject {
    //Todo setPlayer playerShape
    //Todo set defaults half done
    //Todo polish player drawing *rotate player, when not a quarter half done
    //Todo add a tab for getting coins in store >> video ads, fullscreenAds, IAP if possible
    //Todo loading screen for the store + convert loading to ASync
    //Todo fix store shapes, *kinda* easy fix
    //Todo fix played games not incrementing sometimes
    //Todo Fix the  buy button after purchase
    //Todo fix the store being out of view at the top
    //Todo consider moving the pausing into ondestroy
    //Todo fix rectangle drawing *easy fix
    //Todo maybe make things more expensive, seems a little easy atm

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int color = 0xffe5e475;
    //Color
    private static final int scoreBackground = 0xff404040;
    public static int paintIndex;
    public static Player.PlayerShape playerShape;

    private static int[] points;
    //Michael's quick fix
    private static int cx, cy, l, angleOffSet, initRotation, rotationStep;
    private static double playerJumpDistance, playerJumpPercentage;
    // PARTICLES
    public final ArrayList<Particle> splashParticles1;
    public final ArrayList<Particle> splashParticles2;
    // MOVEMENT
    public boolean moving;
    public boolean canMove; // MAKE LEVEL MOVE INSTEAD
    public boolean goingRight; // 0 = LEFT, 1 = RIGHT
    // STATE
    public PlayerState state;
    // SCORE
    public int score;
    public int highScoreA; // ARCADE
    public int highScoreR; // RECRUIT
    public int gamesPlayed; // GAMES PLAYED
    // SPECIAL ACHIEVEMENTS
    public int gamesHund; // Games over 100;
    // PAINT TRAIL
    public ArrayList<PaintParticle> paintTrail;

    public Player() {
        Utility.log("Player Initialized");
        splashParticles1 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            splashParticles1.add(new Particle());
        }
        splashParticles2 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            splashParticles2.add(new Particle());
        }
        l = (GameValues.PLAYER_SCALE / 2) - GameValues.PAINT_THICKNESS;
        playerJumpDistance = Screen.width - (GameValues.PLATFORM_WIDTH * 2);
    }

    public static void setPlayerShape(PlayerShape playerShape) {
        Player.playerShape = playerShape;
        switch (playerShape) {
            case TRIANGLE:
                initTriangle();
                break;
            case PENTAGON:
                initPentagon();
                break;
            case HEXAGON:
                initHexagon();
                break;
        }
    }

    private static void initTriangle() {
        points = new int[6];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
    }

    private static void initPentagon() {
        points = new int[10];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
    }

    private static void initHexagon() {
        points = new int[12];
        initRotation = 30;
        rotationStep = 60;
        angleOffSet = 0;
    }

    public static void drawRectangle(Canvas canvas, int w, int h) {
        paint.setColor(color);
        paint.setAlpha(255);
        canvas.drawRoundRect(new RectF(0, 0, w, w), 12, 12, paint);
        paint.setColor(scoreBackground);
        canvas.drawRoundRect(new RectF(12, 12, w - 12, w - 12), 12, 12, paint);
        // canvas.drawRoundRect(new RectF(GameValues.PAINT_THICKNESS, GameValues.PAINT_THICKNESS, w - GameValues.PAINT_THICKNESS, w - GameValues.PAINT_THICKNESS), 12, 12, paint);
    }

    public static void drawTriangle(Canvas canvas, int w, int h) {
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color);
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    public static void drawCircle(Canvas canvas, int w, int h) {
        paint.setColor(color);
        canvas.drawCircle(w / 2, h / 2, Math.min(w, h) / 2, paint);
        paint.setColor(scoreBackground);
        canvas.drawCircle(w / 2, h / 2, (Math.min(w, h) / 2) - 12, paint);
    }

    public static void drawPentagon(Canvas canvas, int w, int h) {
        paint.setColor(color);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    public static void drawHexagon(Canvas canvas, int w, int h) {
        paint.setColor(color);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    @Override
    public void setup() {
        super.setup();

        // MOVEMENT
        moving = false; // Initiated by Controller to make true
        canMove = true;
        goingRight = true; // GOING RIGHT

        // LOCATION & SCALE
        scale = GameValues.PLAYER_SCALE;
        yPos = Screen.height; // START OFF SCREEN
        xPos = GameValues.PLATFORM_WIDTH;

        // OTHER
        state = PlayerState.ON_GROUND;
        score = 0;

        // PAINT:
        paintIndex = 0;
        paintTrail = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            paintTrail.add(new PaintParticle());
        }
        activatePaint(true);
    }

    public void update() {
        // Utility.log(String.valueOf(state));

        // STARTING ANIMATION
        if (yPos > GameValues.PLAYER_POSITION) {
            yPos -= GameValues.SPEED_FACTOR / 3;
            if (yPos < GameValues.PLAYER_POSITION)
                yPos = GameValues.PLAYER_POSITION;
        }

        // PAINT
        for (int i = 0; i < paintTrail.size(); ++i) {
            if (paintTrail.get(i).active) {
                paintTrail.get(i).yPos += GameValues.SPEED_FACTOR;
            }
        }

        // PARTICLES:
        for (int i = 0; i < splashParticles1.size(); i++) {
            splashParticles1.get(i).update();
        }
        for (int i = 0; i < splashParticles2.size(); i++) {
            splashParticles2.get(i).update();
        }

        switch (state) {
            case ON_GROUND:
                if (!isAlive(false)) {
                    startDying();
                } else if (canPaint()) {
                    int speed = GameValues.SPEED_FACTOR;
                    if (Game.mode == GameMode.Recruit)
                        speed /= 1.5f;
                    if (speed < 1)
                        speed = 1;
                    paintTrail.get(paintIndex).height += speed;
                    paintTrail.get(paintIndex).yPos = yPos;
                }

                break;
            case DYING:
                if (goingRight) {
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos + scale < -GameValues.DEATH_GAP) {
                        if (Game.mode == GameMode.Arcade) {
                            if (highScoreA < score) {
                                MainActivity.preferences.put("hScore",
                                        String.valueOf(score));
                                highScoreA = score;
                            }
                        } else {
                            if (highScoreR < score) {
                                MainActivity.preferences.put("hScoreR",
                                        String.valueOf(score));
                                highScoreR = score;
                            }
                        }

                        // PLAYED AND DEATHS
                        if (MainActivity.preferences.getString("gPlayed") != null) {
                            gamesPlayed = Integer.parseInt(MainActivity.preferences
                                    .getString("gPlayed")) + 1;
                        } else {
                            gamesPlayed = 0;
                        }
                        MainActivity.preferences.put("gPlayed",
                                String.valueOf(gamesPlayed));

                        if (score > 100 && Game.mode == GameMode.Arcade) {
                            gamesHund += 1;
                            if (gamesHund >= 10) {
                                MainActivity
                                        .unlockAchievement("CgkIvYbi1pMMEAIQDg");
                            }
                        } else if (Game.mode == GameMode.Arcade) {
                            gamesHund = 0;
                        }
                        Game.setupGame();
                    }
                } else {
                    xPos += GameValues.PLAYER_JUMP_SPEED;
                    if (xPos > Screen.width + GameValues.DEATH_GAP) {
                        if (Game.mode == GameMode.Arcade) {
                            if (highScoreA < score) {
                                MainActivity.preferences.put("hScore",
                                        String.valueOf(score));
                                highScoreA = score;
                            }
                        } else {
                            if (highScoreR < score) {
                                MainActivity.preferences.put("hScoreR",
                                        String.valueOf(score));
                                highScoreR = score;
                            }
                        }
                        Game.setupGame();
                    }
                }

                //Convert score to coins and show ad
                Utility.saveCoins(Game.context, Utility.getCoins(Game.context) + score / 5);

                if (gamesPlayed % 10 == 0 && gamesPlayed > 0) {
                    MainActivity.getView().post(new Runnable() {
                        @Override
                        public void run() {
                            final InterstitialAd ad = Game.adManager.getFullscreenAd();
                            if (ad.isLoaded()) ad.show();
                        }
                    });

                } else if (gamesPlayed % 7 == 0 && gamesPlayed > 0) {
                    MainActivity.getView().post(new Runnable() {
                        @Override
                        public void run() {
                            Game.adManager.loadFullscreenAd();
                        }
                    });

                }

                break;
            case JUMPING:
                if (goingRight) { // RIGHT
                    xPos += GameValues.PLAYER_JUMP_SPEED;
                    if ((xPos + scale) >= (Screen.width - GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Screen.width / 2));
                        if (isAlive(true))
                            land(true);
                        else
                            startDying();
                    }
                } else { // LEFT
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos <= (GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Screen.width / 2));
                        if (isAlive(true))
                            land(false);
                        else
                            startDying();
                    }
                }
                break;
        }

        playerJumpPercentage = (xPos - GameValues.PLATFORM_WIDTH) / playerJumpDistance;

    }

    public void startDying() {
        state = PlayerState.DYING;
    }

    public boolean isAlive(boolean j) {
        boolean fine = false;
        if (goingRight) {
            for (Platform p : Game.level.platformsRight) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
            }
        } else {
            for (Platform p : Game.level.platformsLeft) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
            }
        }
        return fine;
    }

    public boolean canPaint() {
        boolean fine = false;
        if (goingRight) {
            for (Platform p : Game.level.platformsRight) {
                if (yPos >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                }
            }
        } else {
            for (Platform p : Game.level.platformsLeft) {
                if (yPos >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                }
            }
        }
        return fine;
    }

    public void jump() {
        if (state == PlayerState.ON_GROUND) {
            state = PlayerState.JUMPING;
        }

        activatePaint(false);
    }

    public void activatePaint(boolean start) {
        // PAINT ACTIVATE:
        int moveIndex = 0;
        if (!(paintIndex == paintTrail.size() - 1)) {
            moveIndex = paintIndex + 1;
        }
        paintTrail.get(moveIndex).active = true;
        paintTrail.get(moveIndex).height = 0;
        paintTrail.get(moveIndex).yPos = yPos;
        if (!start) {
            if (!goingRight) {
                paintTrail.get(moveIndex).xPos = GameValues.PLATFORM_WIDTH
                        - GameValues.PAINT_THICKNESS;
            } else {
                paintTrail.get(moveIndex).xPos = (Screen.width - GameValues.PLATFORM_WIDTH);
            }
        } else {
            paintTrail.get(moveIndex).xPos = GameValues.PLATFORM_WIDTH
                    - GameValues.PAINT_THICKNESS;
        }

        paintIndex = moveIndex;
    }

    public void land(boolean right) {
        Game.lowBeat(310);
        Game.alphaM = 255; // BLITZ
        score += 1;
        // if (score % 5 == 0)
        Game.color = Game.colors[Utility.randInt(0, Game.colors.length - 1)];
        state = PlayerState.ON_GROUND;
        if (right) {
            xPos = ((Screen.width - GameValues.PLATFORM_WIDTH) - scale);
        } else {
            xPos = (GameValues.PLATFORM_WIDTH);
        }
        if (score % 2 == 0)
            GameValues.SPEED_BONUS += .005f;

        if (score % 18 == 0) {
            Level.powerCountdown = 4;
        }
        // / PARTICLES:
        showParticles();
        Game.showCircle(scale, getXCenter(), getYCenter(), 160, false);
    }

    public int getXCenter() {
        return xPos + (scale / 2);
    }

    public int getYCenter() {
        return yPos + (scale / 2);
    }

    public boolean IsInBox(int x1, int y1, int width1, int height1, int x2,
                           int y2, int width2, int height2) {
        int right1 = x1 + width1;
        int right2 = x2 + width2;
        int bottom1 = y1 + height1;
        int bottom2 = y2 + height2;

        // Check if top-left point is in box chexk && y2 >= y2
        if (x2 >= x1 && x2 <= right1 && y2 <= bottom1)
            return true;

        // Check if bottom-right point is in box

        return (right2 >= x1 && right2 <= right1 && bottom2 >= y2 && bottom2 <= bottom1);
    }

    public void showParticles() {
        if (goingRight) {
            for (int i = 0; i < splashParticles1.size(); i++) {
                splashParticles1.get(i).setup(xPos, getYCenter(), true);
            }
        } else {
            for (int i = 0; i < splashParticles2.size(); i++) {
                splashParticles2.get(i).setup(xPos + scale, getYCenter(), false);
            }
        }
    }

    public void draw(Canvas canvas, Paint paint, int x, int y, int w, int h, double rotation) {
        setShapeRotation(rotation);

        switch (playerShape) {
            case RECT:
                drawRectangle(canvas, w, h);
                break;
            case CIRCLE:
                drawCircle(canvas, w, h);
                break;
            case TRIANGLE:
                drawTriangle(canvas, w, h);
                break;
            case PENTAGON:
                drawPentagon(canvas, w, h);
                break;
            case HEXAGON:
                drawHexagon(canvas, w, h);
                break;
        }
    }

    public void setShapeRotation(double rotation) {
        rotation = (rotation % 180) + angleOffSet;
        cx = getXCenter();
        cy = getYCenter();

        for (int i = 0; i < points.length; i += 2) {
            points[i] = cx + (int) (l * Math.cos(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
            points[i + 1] = cy + (int) (l * Math.sin(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
        }
    }

    public enum PlayerShape {
        RECT, CIRCLE, TRIANGLE, HEXAGON, PENTAGON
    }
}
